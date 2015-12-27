package com.lumitron.ledevents;

import java.util.HashMap;

import com.lumitron.music.MusicHandler;

public class LedEventsHandler {
    public static HashMap<String, Object> play(HashMap<String, String> params) {
        // Get the parameters
        String events = params.get("events");

        // Load and play the events
        LedEventsManager.load(events);
        LedEventsManager.play();

        // Return the formatted response
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "Ready to play the events when the music starts");

        return response;
    }

    public static HashMap<String, Object> stop() {
        // Stop playing the events
        LedEventsManager.stop();

        // Return the formatted response
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "Event reading has been stopped");

        return response;
    }
    
    public static HashMap<String, Object> seek(HashMap<String, String> params) {
        // Get the parameters
        Long seekTo = Long.parseLong(params.get("seekTo"));
        
        LedEventsManager.seek(seekTo, MusicHandler.getCurrentPlaybackTime());
        
        // Return the formatted response
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "Events has been seeked to " + seekTo / 1000 + " ms");

        return response;
    }
}
