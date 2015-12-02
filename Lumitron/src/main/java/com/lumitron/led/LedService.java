package com.lumitron.led;

import java.util.HashMap;

import com.lumitron.network.LumitronService;
import com.lumitron.network.RequestHandler;

public class LedService implements LumitronService {
    private HashMap<String, String> serviceRoute;
    private HashMap<String, String> params;

    @Override
    public void setRequestData(HashMap<String, HashMap<String, String>> requestData) {
        this.serviceRoute = requestData.get("serviceRoute");
        this.params = requestData.get("params");
    }

    public void getAvailableControllerModels() {
        RequestHandler.send(serviceRoute.get("uuid"), LedHandler.getAvailableControllerModels());
    }

    public void getRegisteredControllers() {
        RequestHandler.send(serviceRoute.get("uuid"), LedHandler.getRegisteredControllers());
    }

    public void registerController() {
        RequestHandler.send(serviceRoute.get("uuid"), LedHandler.registerController(params));
    }

    public void deregisterController() {
        RequestHandler.send(serviceRoute.get("uuid"), LedHandler.deregisterController(params));
    }

    public void sendCommand() {
        RequestHandler.send(serviceRoute.get("uuid"), LedHandler.sendCommand(params));
    }
}
