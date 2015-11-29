var lumitron = lumitron || {};

lumitron.music = $.extend(lumitron.music || {}, (function() {
    var totalPlaybackInMicro;
    
    var load = function(path) {
        var params = {
            "file": "C:\\piano.mp3"
        }
        lumitron.request.send("music", "load", params)
            .done(function(response) {
                totalPlaybackInMicro = response.totalPlaybackInMicro;
                $("#playbackTimeline").width("0%");
                $("#totalPlaybackTime").text(response.totalPlaybackTime);
                
                $("#play").parent().click(play);
                $("#play")[0].classList.remove("iconDisabled");
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
                    stop(true);
                }
            });
        $("#play").parent().off();
        $("#play")[0].classList.add("iconActive");
        $("#pause").parent().click(pause)
        $("#pause")[0].classList.remove("iconDisabled");
        $("#pause")[0].classList.remove("iconActive");
        $("#stop").parent().click(stop);
        $("#stop")[0].classList.remove("iconDisabled");
        $("#stop")[0].classList.remove("iconActive");
    };
    
    var pause = function() {
        lumitron.request.send("music", "pause");
        $("#pause").parent().off();
        $("#pause")[0].classList.add("iconActive");
        $("#play").parent().click(play);
        $("#play")[0].classList.remove("iconActive");
    };
    
    var stop = function(silent) {
        if(!silent) {
            lumitron.request.send("music", "stop");
        }
        $("#stop").parent().off();
        $("#stop")[0].classList.add("iconActive");
        $("#pause").parent().off();
        $("#pause")[0].classList.remove("iconActive");
        $("#pause")[0].classList.add("iconDisabled");
        $("#play").parent().click(play);
        $("#play")[0].classList.remove("iconActive");
        load();
    };
    
    $(document).ready(function() {
        
    });
    
    //Public APIs
    return {
        load: load,
        play: play,
        pause: pause,
        stop: stop
    };
})());