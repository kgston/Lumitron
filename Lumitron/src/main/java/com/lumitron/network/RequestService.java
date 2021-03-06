package com.lumitron.network;

import java.util.HashMap;


public class RequestService implements LumitronService {
    private HashMap<String, String> serviceRoute;
    private HashMap<String, String> params;
    
    public void resendResponse() {
        RequestHandler.resend(params.get("uuid"), serviceRoute.get("uuid"));
    }
    
    @Override
    public void setRequestData(HashMap<String, HashMap<String, String>> requestData) {
        this.serviceRoute = requestData.get("serviceRoute");
        this.params = requestData.get("params");
    }
}
