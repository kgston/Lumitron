package com.lumitron.network;

import java.util.HashMap;

import com.lumitron.util.LumitronException;

public class NetworkService implements LumitronService {
    private HashMap<String, String> serviceRoute;
    private HashMap<String, String> params;

    @Override
    public void setRequestData(HashMap<String, HashMap<String, String>> requestData) {
        this.serviceRoute = requestData.get("serviceRoute");
        this.params = requestData.get("params");
    }

    public void isIpReachable() throws LumitronException {
        RequestHandler.send(serviceRoute.get("uuid"), NetworkHandler.isIpReachable(params));
    }

    public void getMacAddress() throws LumitronException {
        RequestHandler.send(serviceRoute.get("uuid"), NetworkHandler.getMacAddress(params));
    }
}
