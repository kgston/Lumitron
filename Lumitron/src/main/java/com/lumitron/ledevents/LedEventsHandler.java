package com.lumitron.ledevents;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LedEventsHandler {
    public static HashMap<String, Object> play(HashMap<String, String> params) {
        // Get the parameters
        String events = params.get("events");

        // Load and play the events
        LedEventsManager.load(deserializeLedEvents(events));
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

    private static ArrayList<LedEvent> deserializeLedEvents(String ledEventsJson) {
        Type eventListType = new TypeToken<ArrayList<LedEvent>>(){}.getType();
        ArrayList<LedEvent> ledEvents = new Gson().fromJson(ledEventsJson, eventListType);
        return ledEvents;
    }
}
