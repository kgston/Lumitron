lumitron.request = (function() {
	//Private vars
	var serverURL = "ws://localhost:8080/Lumitron/ws/request";
	var socket = null;
	
	//Container to map a request to a response and all related info
	var pendingResponses = {};
	
	//Private functions
	//Starts a new connection to the server. 
	//Also checks if there are existing cached response and retrieves them in case of disconnection.
	var open = function() {
		if(socket == null) {
			socket = new WebSocket(serverURL);
			socket.onopen = function(event) {
				$("#state").html("Open");
				//Checks for caches responses on server
				Object.keys(pendingResponses).forEach(function(responseUUID) {
					send("request", "resend", {uuid: responseUUID}, false);
				});
			}
			socket.onclose = function(event) {
				$("#state").html("Closed");
				socket = null;
			}
			socket.onmessage = function(event) {
				var responseData = JSON.parse(event.data);
				var uuid = responseData.uuid;
				var type = responseData.type;
				var request = pendingResponses[uuid];
				
				if(request == null) {
					if(uuid == null) {
						console.log("A response was received without a matching callback mapping, the response will be lost");
					}
					return;
				}
				
				if(type === "receipt") {
					request.isAcknowledged = true;
				} else if (responseData.type === "response") {
					responseData = responseData.response;
					if(request.keepAlive) {
						request.deferred.notify(responseData);
					} else {
						request.deferred.resolve(responseData);
						delete pendingResponses[uuid];
					}
				} else if(type === "error") {
					responseData = responseData.error;
					request.deferred.reject(responseData);
					delete pendingResponses[uuid];
				} else {
					console.log("Unknown response type!");
				}
			}
		} else {
			console.log("Connection is already started");
		}
	};
	
	//Closes the connection and clears the response mapping
	var close = function() {
		if(socket != null) {
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
	// keepAlive - [boolean] false if only one response is expected, true if responses will be streaming
	//
	//  Returns promise [function] - Use promise.done(callback) for success cases, promise.fail(callback) for error cases
	//								 promise.always(callback) for all cases, promise.progress(callback) for multiple responses
	var send = function(domainName, serviceName, params, keepAlive) {
		params = params || null;
		if(socket != null) {
			//Check if params is of right format
			if(typeof params !== "object") {
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
			}
			
			//Create the promise for the callback
			var deferred = jQuery.Deferred();
			
			//FOR TESTING USE
			deferred.promise().always(function() {
				$("#pushedItems").append(event.data + "</br>");
			});
			
			//Store request
			pendingResponses[request.serviceRoute.uuid] = {
				request: request,
				deferred: deferred,
				keepAlive: keepAlive,
				isAcknowledged: false
			}
			//Send request
			socket.send(JSON.stringify(request));
			
			//Return a promise to the caller
			return deferred.promise();
		} else {
			console.log("Connection isn't started");
		}
	}
	
	//Public APIs
	return {
		open: open,
		close: close,
		send: send
	}
})();