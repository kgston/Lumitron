var lumitron = lumitron || {};

lumitron = (function() {
	//Private vars
	
	//Private methods
	var init = function() {
		this.request.open();
	};
	
	var getUUID = function() {
	    var d = Date.now();
	    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
	        var r = (d + Math.random()*16)%16 | 0;
	        d = Math.floor(d/16);
	        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
	    });
	    return uuid;
	};
	
	//Public API
	return {
		init: init,
		getUUID: getUUID
	}
})();

$(document).ready(function() {
	lumitron.init();
});