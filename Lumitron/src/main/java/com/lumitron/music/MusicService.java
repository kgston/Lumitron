package com.lumitron.music;

import java.util.HashMap;

import com.lumitron.network.LumitronService;


public class MusicService implements LumitronService {
    private HashMap<String, String> serviceRoute;
    private HashMap<String, String> params;
    
    @Override
    public void setRequestData(HashMap<String, HashMap<String, String>> requestData) {
        this.serviceRoute = requestData.get("serviceRoute");
        this.params = requestData.get("params");
    }
    
}
