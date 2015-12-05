var lumitron = lumitron || {};

lumitron.music = $.extend(true, lumitron.music || {}, (function() {
    var totalPlaybackInMicro;
    
    var load = function(path) {
        var params = lumitron.project.music;
        lumitron.request.send("music", "load", params)
            .done(function(response) {
                totalPlaybackInMicro = response.totalPlaybackInMicro;
                $("#playbackTimeline").width("0%");
                $("#totalPlaybackTime").text(response.totalPlaybackTime);
                
                $("#play").iconOff().iconClick(play).iconRemoveClass("iconDisabled");
            });
    };
    
    var play = function() {
        lumitron.request.send("music", "play")
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
        $("#play").iconOff().iconAddClass("iconActive");
        $("#pause").iconOff().iconClick(pause).iconRemoveClass("iconDisabled iconActive");
        $("#stop").iconOff().iconClick(stop).iconRemoveClass("iconDisabled iconActive");
    };
    
    var pause = function() {
        lumitron.request.send("music", "pause");
        $("#pause").iconOff().iconAddClass("iconActive");
        $("#play").iconOff().iconClick(play).iconRemoveClass("iconActive");
    };
    
    var stop = function(event, silent) {
        if(!silent) {
            lumitron.request.send("music", "stop");
        } else {
            $("#stop").iconOff().iconAddClass("iconActive");
            $("#pause").iconOff().iconRemoveClass("iconActive").iconAddClass("iconDisabled");
            $("#play").iconOff().iconClick(play).iconRemoveClass("iconActive");
            load();
        }
    };
    
    $(document).ready(function() {
        load();
    });
    
    //Public APIs
    return {
        load: load,
        play: play,
        pause: pause,
        stop: stop
    };
})());