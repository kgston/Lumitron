var webSocket = {
	socket: null,
	start: function() {
		if(this.socket == null) {
			this.socket = new WebSocket("ws://localhost:8080/Lumitron/ws/request");
			this.socket.onopen = function(event) {
				$("#state").html("Open");
			}
			this.socket.onclose = function(event) {
				$("#state").html("Closed");
			}
			this.socket.onmessage = function(event) {
				$("#pushedItems").append(event.data + "</br>");
			}
			
		} else {
			console.log("Websocket is already started");
		}
	},
	stop: function() {
		if(this.socket != null) {
			this.socket.close();
			this.socket = null;
		} else {
			console.log("Websocket is already closed");
		}
	},
	send: function(message) {
		if(this.socket != null) {
			if(message instanceof Object) {
				message = JSON.stringify(message);
			}
			this.socket.send(message);
		} else {
			console.log("Websocket isn't started");
		}
	}
};