var lumitron = lumitron || {};

lumitron = (function() {
	//Private vars
	
	//Private methods
	//Converts all external SVGs into inline SVGs so they can be manipulated by CSS  
	var inlineSVG = function() {
		//http://stackoverflow.com/questions/11978995/how-to-change-color-of-svg-image-using-css-jquery-svg-image-replacement
		jQuery('img.svg').each(function(){
            var $img = jQuery(this);
            var imgID = $img.attr('id');
            var imgClass = $img.attr('class');
            var imgURL = $img.attr('src');

            jQuery.get(imgURL, function(data) {
                // Get the SVG tag, ignore the rest
                var $svg = jQuery(data).find('svg');
                // Add replaced image's ID to the new SVG
                if(typeof imgID !== 'undefined') {
                    $svg = $svg.attr('id', imgID);
                }
                // Add replaced image's classes to the new SVG
                if(typeof imgClass !== 'undefined') {
                    $svg = $svg.attr('class', imgClass+' replaced-svg');
                }
                // Remove any invalid XML tags as per http://validator.w3.org
                $svg = $svg.removeAttr('xmlns:a');
                // Replace image with new SVG
                $img.replaceWith($svg);
            }, 'xml');
        });
	};
	
	//Run all button bindings
	var bindButtons = function() {
		
	};
	
	//Initializes the application and run all startup scripts
	var init = function() {
		inlineSVG();
		bindButtons();
		this.request.open();
	};
	
	//Util function to get a random UUID
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