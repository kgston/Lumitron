var lumitron = lumitron || {};

lumitron.opts = $.extend(true, lumitron.opts || {}, (function() {
    return {
        debug: false,
        device: {
            search: {
                sequentialSearchCount: 10,
                timeout: 5000,
                fromIP: "10.10.1.1",
                toIP: "10.10.1.1"
            },
            heartbeat: {
                intervalLength: 3000
            }
        }
    };
})());

lumitron = $.extend(true, lumitron || {}, (function() {
	//Private vars
	
	//Private methods
	
	//Initializes the application and run all startup scripts
	var init = function() {
		return lumitron.ui.inlineSVG().done(function() {
		    lumitron.request.init();
            lumitron.device.init();
            lumitron.music.init();
            lumitron.events.init();
        });
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
	};
})());

lumitron.ui = $.extend(true, lumitron.ui || {}, (function() {
    //Converts all external SVGs into inline SVGs so they can be manipulated by CSS  
	var inlineSVG = function() {
        //http://stackoverflow.com/questions/11978995/how-to-change-color-of-svg-image-using-css-jquery-svg-image-replacement
        var inlining = $.Deferred();
		$('img.svg').each(function(){
            var $img = jQuery(this);
            var imgID = $img.attr('id');
            var imgClass = $img.attr('class');
            var imgURL = $img.attr('src');

            $.get(imgURL, function(data) {
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
                //And wrap it with an outer div
                $svg.wrap(document.createElement("div"));
            }, 'xml').done(function() {
                if($('img.svg').length === 0) {
                    inlining.resolve();
                }
            });
        });
        return inlining.promise();
	};
    
    //Set the changeSVGSrc method
    $.fn.changeSVGSrc = function(newSrc) {
        $.get(newSrc, function(data) {
                // Get the SVG tag, ignore the rest
                var $svg = jQuery(data).find('svg');
                //Replace contents of the existing svg with the new svg contents
                this.empty().append($svg.children());
            }.bind(this), 'xml');
        return this;
    };
    //Set the SVG icon click method
    $.fn.iconClick = function(callback) {
        return this.each(function(index, element) {
            var icon = $(this);
            var parentDiv = icon.parent();
            parentDiv.click(callback.bind(parentDiv));
        });
    };
    //Set the SVG icon off method
    $.fn.iconOff = function(eventName) {
        return this.each(function(index, element) {
            var icon = $(this);
            var parentDiv = icon.parent();
            parentDiv.off(eventName);
        });
    };
    //Set the SVG icon addClass method
    $.fn.iconAddClass = function(classnames) {
        return this.each(function(index, element) {
            var classnameAry = classnames.split(" ");
            classnameAry.forEach(function(classname) {
                this.classList.add(classname);
            }.bind(this));
        });
    };
    //Set the SVG icon removeClass method
    $.fn.iconRemoveClass = function(classnames) {
        return this.each(function(index, element) {
            var classnameAry = classnames.split(" ");
            classnameAry.forEach(function(classname) {
                this.classList.remove(classname);
            }.bind(this));
        });
    };
    //Set the inputComplete method
    $.fn.inputComplete = function(callback) {
        return this.each(function(index, element) {
            var input = $(this);
            input.bind('blur keyup',function(event) {  
                if(event.type == 'blur' || event.keyCode == 13) {
                    callback.bind(this)(event);
                }
            }); 
        });
    };
    
    return {
        inlineSVG: inlineSVG
    };
})());

$(document).ready(function() {
	lumitron.init();
});