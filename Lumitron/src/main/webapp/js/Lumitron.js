var lumitron = lumitron || {};

lumitron.opts = $.extend(true, lumitron.opts || {}, (function() {
    return {
        device: {
            search: {
                sequencialSearchCount: 10,
                timeout: 2000,
                fromIP: "10.10.1.1",
                toIP: "10.10.1.20"
            }
        }
    };
})());

lumitron.project = $.extend(true, lumitron.project || {}, (function() {
    return {
        projectName: "Noukai 2015",
        music: {
            file: "C:\\Fresh.mp3"
        },
        controllers: [{
            deviceName: "",
            macAddress: "",
            controller: ""
        }],
        events: [{
            time: "", //ms
            deviceName: "",
            command: "",
            params: {
                
            }
        }]
    };
})());

lumitron = $.extend(true, lumitron || {}, (function() {
	//Private vars
	
	//Private methods
	
	//Run all button bindings
	var bindButtons = function() {
		
	};
	
	//Initializes the application and run all startup scripts
	var init = function() {
		lumitron.ui.inlineSVG();
		bindButtons();
		this.request.open();
	};
	
	//Util function to get a random UUID
	var getUUID = function() {
	    //Retrieved from http://stackoverflow.com/questions/105034/how-to-create-a-guid-uuid-in-javascript
        var d = new Date().getTime();
        if(window.performance && typeof window.performance.now === "function"){
            d += performance.now(); //use high-precision timer if available
        }
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
})());

lumitron.ui = $.extend(true, lumitron.ui || {}, (function() {
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
                $svg = $svg.removeAttr('xmlns:a')
                // Replace image with new SVG
                $img.replaceWith($svg)
                //And wrap it with an outer div
                $svg.wrap(document.createElement("div"));
            }, 'xml');
        });
	};
    
    //Set the SVG icon click method
    $.fn.iconClick = function(callback) {
        return this.each(function(index, element) {
            var icon = $(this);
            var parentDiv = icon.parent();
            parentDiv.click(callback);
        });
    }
    //Set the SVG icon off method
    $.fn.iconOff = function(eventName) {
        return this.each(function(index, element) {
            var icon = $(this);
            var parentDiv = icon.parent();
            parentDiv.off(eventName);
        });
    }
    //Set the SVG icon addClass method
    $.fn.iconAddClass = function(classnames) {
        return this.each(function(index, element) {
            var classnameAry = classnames.split(" ");
            classnameAry.forEach(function(classname) {
                this.classList.add(classname);
            }.bind(this));
        });
    }
    //Set the SVG icon removeClass method
    $.fn.iconRemoveClass = function(classnames) {
        return this.each(function(index, element) {
            var classnameAry = classnames.split(" ");
            classnameAry.forEach(function(classname) {
                this.classList.remove(classname);
            }.bind(this));
        });
    }
    
    return {
        inlineSVG: inlineSVG
    }
})());

$(document).ready(function() {
	lumitron.init();
});