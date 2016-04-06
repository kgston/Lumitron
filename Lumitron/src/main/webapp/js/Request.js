var lumitron = lumitron || {};

lumitron.request = $.extend(true, lumitron.request || {}, (function() {
    //Private vars
    var serverURL = "ws://localhost:8080/Lumitron/ws/request";
    var socket = null;

    //UI vars
    var serverStateUI;

    //Buffer to hold requests while socket is not yet open
    var pendingRequests = [];

    //Container to map a request to a response and all related info
    var pendingResponses = {};

    //First run code
    $(document).ready(function() {
        serverStateUI = stencil.define("serverStatusStencil");
    });

    //Private functions
    var setServerState = function(state) {
        switch (state) {
            case "loading":
                serverStateUI.render({
                    class: "iconDisabled",
                    icon: "css/icons/Entypo/dots-three-horizontal.svg"
                });
                break;
            case "connected":
                serverStateUI.render({
                    class: "iconActive",
                    icon: "css/icons/Entypo/bar-graph.svg"
                });
                $("#serverState").off().click(lumitron.request.close);
                break;
            case "disconnected":
                serverStateUI.render({
                    class: "iconError",
                    icon: "css/icons/Entypo/warning.svg"
                });
                $("#serverState").off().click(lumitron.request.open);
                break;
        }
        lumitron.ui.inlineSVG();
    };

    //Starts a new connection to the server. 
    //Also checks if there are existing cached response and retrieves them in case of disconnection.
    var open = function() {
        if (socket == null) {
            socket = new WebSocket(serverURL);
            socket.onopen = function(event) {
                setServerState("connected");
                //Checks for caches responses on server
                Object.keys(pendingResponses).forEach(function(responseUUID) {
                    send("request", "resend", { uuid: responseUUID }, false);
                });
                //Sends out any waiting requests
                pendingRequests.forEach(function(request) {
                    //Send request
                    socket.send(JSON.stringify(request));
                });
                pendingRequests = [];
            };
            socket.onclose = function(event) {
                setServerState("disconnected");
                socket = null;
            };
            socket.onmessage = function(event) {
                var response = JSON.parse(event.data);
                var uuid = response.uuid;
                var type = response.type;
                var request = pendingResponses[uuid];

                if (request == null) {
                    if (uuid == null) {
                        console.log("A response was received without a matching callback mapping, the response will be lost");
                    }
                    return;
                }

                if (type === "receipt") {
                    request.isAcknowledged = true;
                } else if (response.type === "response") {
                    var responseData = response.response;
                    if (response.keepAlive) {
                        request.deferred.notify(responseData);
                    } else {
                        request.deferred.resolve(responseData);
                        delete pendingResponses[uuid];
                    }
                } else if (type === "error") {
                    responseData = response.error;
                    request.deferred.reject(responseData);
                    delete pendingResponses[uuid];
                } else {
                    console.log("Unknown response type!");
                }
            };
        } else {
            console.log("Connection is already started");
        }
    };

    //Closes the connection and clears the response mapping
    var close = function() {
        if (socket != null) {
            socket.close();
            socket = null;
        } else {
            console.log("Connection is already closed");
        }
    };

    //Sends a request to the server
    //  domainName - [String] Name of the domain the service resides in
    //  serviceName - [String] Name of the of the service requesting
    //  params - [Object] A flat object of key value parameters
    //
    //  Returns promise [function] - Use promise.done(callback) for success cases, promise.fail(callback) for error cases
    //								 promise.always(callback) for all cases, promise.progress(callback) for multiple responses
    var send = function(domainName, serviceName, params) {
        params = params || null;
        //Check if params is of right format
        if (typeof params !== "object") {
            console.log("Unable to send request with an invalid params format");
            return;
        }

        //Create the request object
        var request = {
            serviceRoute: {
                uuid: lumitron.getUUID(),
                domain: domainName,
                service: serviceName
            },
            params: params
        };

        //Create the promise for the callback
        var deferred = jQuery.Deferred();

        //FOR TESTING USE
        deferred.promise().always(function(response) {
            $("#pushedItems").text(JSON.stringify(response));
        });

        //Store request
        pendingResponses[request.serviceRoute.uuid] = {
            request: request,
            deferred: deferred,
            isAcknowledged: false
        };

        if (socket == null || socket.readyState != 1) {
            pendingRequests.push(request);
        } else {
            //Send request
            socket.send(JSON.stringify(request));
        }

        //Return a promise to the caller
        return deferred.promise();
    };

    var init = function() {
        setServerState("loading");
        open();
    };

    //Public APIs
    return {
        init: init,
        open: open,
        close: close,
        send: send
    };
})());