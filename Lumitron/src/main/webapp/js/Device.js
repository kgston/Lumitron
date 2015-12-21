var lumitron = lumitron || {};

lumitron.device = $.extend(true, lumitron.device || {}, (function() {
    var deviceControllers = [{
            name: "Undefined",
            options: "disabled selected"
        }
    ];
    var availableDevices = [];
    var registeredDevices = [];
    
    var registeredDevicesStencil, availableDevicesStencil;
    
    //Only works within the last octet
    var search = function(fromIP, toIP) {
        availableDevices = [];
        availableDevicesStencil.clear();
        
        var searchSettings = lumitron.opts.device.search;
        var sequencialSearchCount = searchSettings.sequencialSearchCount;
        var searchTimeout = searchSettings.timeout;
        var fromIPOctets = toIntArray(fromIP.split("."));
        var toIPOctets = toIntArray(toIP.split("."));
        
        do {
            var currentIP = toIPAddress(fromIPOctets);
            checkIpReachable(currentIP).done(setDevice);
        } while(++fromIPOctets[3] <= toIPOctets[3])
        
        function checkIpReachable(ipAddress) {
            var delayedExecution = $.Deferred();
            if(sequencialSearchCount > 0) {
                sequencialSearchCount--;
                var params = {
                    "ipAddress": ipAddress,
                    "timeout": searchTimeout
                };
                return lumitron.request.send("network", "isIpReachable", params)
                    .done(function(response) {
                        sequencialSearchCount++;
                    });
            } else {
                delayedExecution.timeoutId = setTimeout(function() {
                    checkIpReachable(ipAddress).done(function(response) {
                        delayedExecution.resolve(response);
                    });
                }, searchTimeout + 250);
                return delayedExecution.promise();
            }
        }
        
        function setDevice(response) {
            if(response.isAlive) {
                var isRegistered = !registeredDevices.every(function(registeredDevice) {
                    if(registeredDevice.ipAddress === response.ipAddress) {
                        return false;
                    } else {
                        return true;
                    }
                });
                if(!isRegistered) {
                    getMacAddress(response.ipAddress).done(function(response) {
                        //Register the device if is known, else show as avaliable
                        if(response) {
                            var devices = lumitron.project.devices;
                            var isFound = !devices.every(function(device) {
                                if(device.macAddress === response.macAddress) {
                                    response.deviceName = device.deviceName;
                                    response.controller = device.controller;
                                    registerDevice(response)
                                    return false;
                                }
                                return true;
                            });
                            if(!isFound) {
                                displayAvaliableDevice(response);
                            }
                        }
                    });
                }
            }
        }
        
        function getMacAddress(ipAddress) {
            var params = {"ipAddress": ipAddress};
            return lumitron.request.send("network", "getMacAddress", params);
        }
        
        function displayAvaliableDevice(deviceDetails) {
            availableDevices.push(deviceDetails);
            deviceDetails.deviceControllers = deviceControllers;
            var $renderedPanel = availableDevicesStencil.render(deviceDetails, "fragment");
            
            //Set click bindings for the device
            $renderedPanel.find(".registerDeviceBtn").iconClick(function() {
                (registerDevice.bind(this))(deviceDetails);
            });
            $("#avaliableDevices").append($renderedPanel);
            
            lumitron.ui.inlineSVG();
        }
        
        function toIntArray(stringArray) {
            var intArray = [];
            if(Array.isArray(stringArray)) {
                stringArray.forEach(function(item) {
                    intArray.push(parseInt(item));
                });
            }
            return intArray;
        }
        
        function toIPAddress(ipOctets) {
            return ipOctets[0] + "." + ipOctets[1] + "." + ipOctets[2] + "." + ipOctets[3];
        }
    };
    
    var getDeviceControllers = function() {
        return lumitron.request.send("led", "getDeviceControllers").done(function(response) {
            deviceControllers = deviceControllers.concat(response.deviceControllers);
        });
    };
    
    var getAvaliableDevices = function() {
        var searchSettings = lumitron.opts.device.search;
        availableDevicesStencil.clear();
        search(searchSettings.fromIP, searchSettings.toIP);
    };
    
    var registerDevice = function(deviceDetails) {
        var name, model;
        if(this instanceof jQuery) {
            var $avaliableDevicePanel = this.closest(".device");
            name = $avaliableDevicePanel.find(".deviceName").val();
            model = $avaliableDevicePanel.find(".deviceModel").val();
        } else {
            name = deviceDetails.deviceName;
            model =  deviceDetails.controller;
        }
        
        var params = {
            "name": name,
            "model": model,
            "ipAddress": deviceDetails.ipAddress
        };
        return lumitron.request.send("led", "registerDevice", params)
            .done(function(response) {
                if(response.isRegistered) {
                    deviceDetails.name = name;
                    if($avaliableDevicePanel) {
                        $avaliableDevicePanel.remove(); 
                    }
                    
                    var registeredDevice = newRegisteredDevice(
                        deviceDetails.ipAddress, 
                        name, 
                        model
                    );
                    registeredDevice.update();
                    registeredDevices.push(registeredDevice);
                }
            });
    };
    
    var getRegisteredDevices = function() {
        lumitron.request.send("led", "getRegisteredDevices")
            .done(function(response) {
                registeredDevices.forEach(function(registeredDevice) {
                    registeredDevice.clearHeartbeat();
                });
                registeredDevices = [];
                registeredDevicesStencil.clear();
                response.registeredDevices.forEach(function(deviceDetails) {
                    var registeredDevice = newRegisteredDevice(
                        deviceDetails.ipAddress, 
                        deviceDetails.name, 
                        deviceDetails.model
                    );
                    registeredDevice.setColour("0505050505")
                    registeredDevice.update();
                    registeredDevices.push(registeredDevice);
                });
            });
    };
    
    function newRegisteredDevice(ipAddress, name, model) {
        return {
            ipAddress: ipAddress,
            name: name,
            model: model,
            draw: function() {
                var $devicePanel = registeredDevicesStencil.render(this, "fragment");
                var isFirstDraw = this.panel == null;
                this.panel = $devicePanel;
                this.applyPanelBindings();
                if(isFirstDraw) { //First draw
                    $("#registeredDevices").append($devicePanel);
                    lumitron.ui.inlineSVG();
                    this.heartbeat(); //Start heartbeat listener
                } else {
                    this.panel.replaceWith($devicePanel);
                }
                return this;
            },
            setColour: function(hexColour) {
                var uiObj = this.panel.find(".colour");
                var params = {
                    "device": name,
                    "command": "setColour",
                    "colour": hexColour
                };
                return lumitron.request.send("led", "sendCommand", params)
                    .done(function() {
                        uiObj.removeClass("error");
                        this.colour = hexColour;
                    }.bind(this))
                    .fail(function() {
                        uiObj.addClass("error");
                    }.bind(this));
            },
            setBrightness: function(brightnessValue) {
                var uiObj = this.panel.find(".brightness");
                var params = {
                    "device": name,
                    "command": "setBrightness",
                    "brightness": brightnessValue
                };
                return lumitron.request.send("led", "sendCommand", params)
                    .done(function() {
                        uiObj.removeClass("error");
                        this.brightness = brightnessValue;
                    }.bind(this))
                    .fail(function() {
                        uiObj.addClass("error");
                    }.bind(this));
            },
            setState: function(state) {
                var uiObj = this.panel.find(".state");
                var params = {
                    "device": name,
                    "command": state
                };
                return lumitron.request.send("led", "sendCommand", params)
                    .done(function() {
                        uiObj.removeClass("error");
                        this.state = state;
                    }.bind(this))
                    .fail(function() {
                        uiObj.addClass("error");
                    }.bind(this));
            },
            update: function() {
                var params = {
                    "device": this.name
                };
                return lumitron.request.send("led", "getState", params)
                    .done(function(response) {
                        this.state = response.state;
                        this.brightness = response.brightness;
                        this.colour = response.colour;
                        this.draw();
                    }.bind(this));
            },
            applyPanelBindings: function() {
                var deviceObject = this;
                var name = this.name;
                var panel = this.panel;
                var clearHeartbeat = this.clearHeartbeat.bind(this);
                panel.find(".deregister").iconClick(function() {
                    var params = {
                        "name": name
                    };
                    lumitron.request.send("led", "deregisterDevice", params)
                        .done(function(response) {
                            if(!response.isRegistered) {
                                clearHeartbeat();
                                panel.remove();
                                registeredDevices.every(function(registeredDevice, index) {
                                    if(registeredDevice.name == name) {
                                        registeredDevices.splice(index, 1);
                                        return false;
                                    } else {
                                        return true;
                                    }
                                });
                            }
                        });
                });
                panel.find(".state").change(function() {
                    deviceObject.setState($(this).val());
                });
                panel.find(".brightness").inputComplete(function() {
                    deviceObject.setBrightness($(this).val());
                });
                panel.find(".colour").inputComplete(function() {
                    deviceObject.setColour($(this).val());
                });
            },
            heartbeat: function() {
                if(this.heartbeatIntervalId || lumitron.opts.debug) {
                    return;
                }
                
                var searchSettings = lumitron.opts.device.search;
                var heartbeatSettings = lumitron.opts.device.heartbeat;
                this.heartbeatIntervalId = setInterval(function() {
                    var params = {
                        "ipAddress": this.ipAddress,
                        "timeout": searchSettings.timeout
                    };
                    lumitron.request.send("network", "isIpReachable", params)
                        .done(function(response) {
                            var statusSrc = {
                                3: "css/icons/Entypo/progress-full.svg",
                                2: "css/icons/Entypo/progress-two.svg",
                                1: "css/icons/Entypo/progress-one.svg",
                                0: "css/icons/Entypo/progress-empty.svg"
                            };
                            if(response.isAlive) {
                                if(this.connectionStrength != null && this.connectionStrength < 3) {
                                    var params = {
                                        "device": name,
                                        "command": "connect"
                                    };
                                    lumitron.request.send("led", "sendCommand", params);
                                }
                                this.connectionStrength = 3;
                                this.panel.find(".connectionStrength").changeSVGSrc(statusSrc[this.connectionStrength.toString()]);
                            } else {
                                if(this.connectionStrength && this.connectionStrength === 3) {
                                    var params = {
                                        "device": name,
                                        "command": "disconnect"
                                    };
                                    lumitron.request.send("led", "sendCommand", params);
                                }
                                this.connectionStrength--;
                                this.panel.find(".connectionStrength").changeSVGSrc(
                                    statusSrc[this.connectionStrength.toString()] || statusSrc["0"]);
                            }
                        }.bind(this));
                }.bind(this), heartbeatSettings.intervalLength);
            },
            clearHeartbeat: function() {
                clearInterval(this.heartbeatIntervalId);
            }
        };
    }
    
    var init = function() {
        //Static bindings
        $("#refreshAvaliableBtn").iconClick(getAvaliableDevices);
        $("#refreshRegisteredBtn").iconClick(getRegisteredDevices);
        
        //Preload the avaliable device controllers
        getDeviceControllers().done(function() {
            getAvaliableDevices();
        });
    };
    
    $(document).ready(function() {
        registeredDevicesStencil = stencil.define("registeredDevicesStencil", "#registeredDevices");
        availableDevicesStencil = stencil.define("avaliableDevicesStencil", "#avaliableDevices");
    });
    
    return {
        init: init,
        search: search,
        getDeviceControllers: getDeviceControllers,
        getAvaliableDevices: getAvaliableDevices,
        registerDevice: registerDevice,
        getRegisteredDevices: getRegisteredDevices
    };
})());