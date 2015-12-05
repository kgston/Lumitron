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

    public void getDeviceControllers() {
        RequestHandler.send(serviceRoute.get("uuid"), LedHandler.getDeviceControllers());
    }

    public void getRegisteredDevices() {
        RequestHandler.send(serviceRoute.get("uuid"), LedHandler.getRegisteredDevices());
    }

    public void registerDevice() {
        RequestHandler.send(serviceRoute.get("uuid"), LedHandler.registerDevice(params));
    }

    public void deregisterDevice() {
        RequestHandler.send(serviceRoute.get("uuid"), LedHandler.deregisterDevice(params));
    }

    public void sendCommand() {
        RequestHandler.send(serviceRoute.get("uuid"), LedHandler.sendCommand(params));
    }
}
