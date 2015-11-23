package com.lumitron.sheet;

import java.util.HashMap;

import com.lumitron.network.LumitronService;
import com.lumitron.network.RequestHandler;

public class SheetService implements LumitronService {
    private HashMap<String, String> serviceRoute;
    private HashMap<String, String> params;

    @Override
    public void setRequestData(HashMap<String, HashMap<String, String>> requestData) {
        this.serviceRoute = requestData.get("serviceRoute");
        this.params = requestData.get("params");
    }

    public void save() {
        RequestHandler.send(serviceRoute.get("uuid"), SheetHandler.save(params));
    }

    public void load() {
        RequestHandler.send(serviceRoute.get("uuid"), SheetHandler.load(params));
    }

    public void read() {
        RequestHandler.send(serviceRoute.get("uuid"), SheetHandler.read(params));
    }
}
