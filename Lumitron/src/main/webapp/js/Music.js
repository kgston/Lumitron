var lumitron = lumitron || {};

lumitron.music = $.extend(true, lumitron.music || {}, (function() {
    var totalPlaybackInMicro;
    
    var load = function(path) {
        var params = lumitron.project.music;
        return lumitron.request.send("music", "load", params)
            .done(function(response) {
                totalPlaybackInMicro = response.totalPlaybackInMicro;
                $("#playbackTimeline").width("0%");
                $("#totalPlaybackTime").text(response.totalPlaybackTime);
                
                $("#play").iconOff().iconClick(play).iconRemoveClass("iconDisabled");
                $("#musicTimeline").off().click(seekEvent);
                
                //Register the timing events after loading music
                lumitron.events.registerEvents();
            });
    };
    
    var play = function() {
        $("#play").iconOff().iconAddClass("iconActive");
        $("#pause").iconOff().iconClick(pause).iconRemoveClass("iconDisabled iconActive");
        $("#stop").iconOff().iconClick(stop).iconRemoveClass("iconDisabled iconActive");
        return lumitron.request.send("music", "play")
            .progress(function(response) {
                var width = (response.currentPlaybackInMicro / totalPlaybackInMicro * 100) + "%";
                $("#playbackTimeline").width(width);
                $("#currentPlaybackTime").text(response.currentPlaybackTime);
            })
            .done(function(response) {
                var width = (response.currentPlaybackInMicro / totalPlaybackInMicro * 100) + "%";
                $("#playbackTimeline").width(width);
                $("#currentPlaybackTime").text(response.currentPlaybackTime);
                if(response.hasCompleted) {
                    stop(null, true);
                }
            });
    };
    
    var seek = function(timeInMicro) {
        return lumitron.request.send("ledevents", "seek", {seekTo: Math.floor(timeInMicro)})
            .done(function() {
                lumitron.request.send("music", "seek", {seekTo: Math.floor(timeInMicro)});
            });
    };
    
    var seekEvent = function(event) {
        var timelineWidth = $(this).width();
        var offsetX = event.offsetX;
        var targetTime = totalPlaybackInMicro * offsetX / timelineWidth;
        seek(targetTime);
    };
    
    var pause = function() {
        $("#pause").iconOff().iconAddClass("iconActive");
        $("#play").iconOff().iconClick(play).iconRemoveClass("iconActive");
        return lumitron.request.send("music", "pause");
    };
    
    var stop = function(event, silent) {
        if(!silent) {
            return lumitron.request.send("music", "stop").done(function() {
                onStop();
            });
        } else {
            onStop();
            return load();
        }
        function onStop() {
            $("#stop").iconOff().iconAddClass("iconActive");
            $("#pause").iconOff().iconRemoveClass("iconActive").iconAddClass("iconDisabled");
            $("#play").iconOff().iconClick(play).iconRemoveClass("iconActive");
        }
    };
    
    $(document).ready(function() {
    });
    
    var init = function() {
        load();
    };
    
    //Public APIs
    return {
        init: init,
        load: load,
        play: play,
        seek: seek,
        pause: pause,
        stop: stop
    };
})());