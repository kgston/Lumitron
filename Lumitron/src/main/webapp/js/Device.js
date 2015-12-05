var lumitron = lumitron || {};

lumitron.device = $.extend(true, lumitron.device || {}, (function() {
    var deviceControllers = [{
            name: "Undefined",
            options: "disabled selected"
        }
    ];
    var avaliableDevices = [];
    var registeredDevices = [];
    
    var registeredDevicesStencil, avaliableDevicesStencil;
    
    //Only works within the last octet
    var search = function(fromIP, toIP) {
        avaliableDevices = [];
        avaliableDevicesStencil.clear();
        
        var searchSettings = lumitron.opts.device.search;
        var sequencialSearchCount = searchSettings.sequencialSearchCount;
        var searchTimeout = searchSettings.timeout;
        var fromIPOctets = toIntArray(fromIP.split("."));
        var toIPOctets = toIntArray(toIP.split("."));
        
        do {
            var currentIP = toIPAddress(fromIPOctets);
            checkIpReachable(currentIP).done(function(response) {
                if(response.isAlive) {
                    getMacAddress(response.ipAddress).done(function(response) {
                        //Add device to avaliable list
                        if(response) {
                            displayAvaliableDevice(response);
                        }
                    });
                }
            });
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
        
        function getMacAddress(ipAddress) {
            var params = {"ipAddress": ipAddress};
            return lumitron.request.send("network", "getMacAddress", params);
        }
        
        function displayAvaliableDevice(deviceDetails) {
            avaliableDevices.push(deviceDetails);
            deviceDetails.deviceControllers = deviceControllers;
            avaliableDevicesStencil.render(deviceDetails, "append");
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
    }
    
    var getDeviceControllers = function() {
        return lumitron.request.send("led", "getDeviceControllers").done(function(response) {
            deviceControllers = deviceControllers.concat(response.deviceControllers);
        });
    }
    
    var panelClickBindings = function() {
        
    };
    
    $(document).ready(function() {
        registeredDevicesStencil = stencil.define("registeredDevicesStencil", "#registeredDevices");
        avaliableDevicesStencil = stencil.define("avaliableDevicesStencil", "#avaliableDevices");
        
        //Preload the avaliable device controllers
        getDeviceControllers().done(function() {
            var searchSettings = lumitron.opts.device.search;
            search(searchSettings.fromIP, searchSettings.toIP);
        });
        panelClickBindings();
    });
    
    return {
        search: search,
        getDeviceControllers: getDeviceControllers,
        // getAvaliableDevices: getAvaliableControllers,
        // registerDevice: registerDevice,
        // unregisterDevice: unregisterDevice,
        // getRegisteredDevices: getRegisteredDevices,
        // setStethoscope: setStethoscope,
        // sendCommand: sendCommand
    };
})());