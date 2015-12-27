package com.lumitron.ledevents;

import java.util.HashMap;

import com.lumitron.network.LumitronService;
import com.lumitron.network.RequestHandler;

public class LedEventsService implements LumitronService {
    private HashMap<String, String> serviceRoute;
    private HashMap<String, String> params;

    @Override
    public void setRequestData(HashMap<String, HashMap<String, String>> requestData) {
        this.serviceRoute = requestData.get("serviceRoute");
        this.params = requestData.get("params");
    }

    public void play() {
        RequestHandler.send(serviceRoute.get("uuid"), LedEventsHandler.play(params));
    }

    public void stop() {
        RequestHandler.send(serviceRoute.get("uuid"), LedEventsHandler.stop());
    }
    
    public void seek() {
        RequestHandler.send(serviceRoute.get("uuid"), LedEventsHandler.seek(params));
    }
}
