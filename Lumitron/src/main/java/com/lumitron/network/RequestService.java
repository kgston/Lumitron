package com.lumitron.network;

import java.util.HashMap;


public class RequestService implements LumitronService {
    private HashMap<String, HashMap<String, String>> requestData;
    
    public void resendResponse() {
        RequestHandler.resend(getParams().get("uuid"), getServiceRoute().get("uuid"));
    }
    
    @Override
    public void setRequestData(HashMap<String, HashMap<String, String>> requestData) {
        this.requestData = requestData;
    }
    
    private HashMap<String, String> getParams() {
        return requestData.get("params");
    }
    
    private HashMap<String, String> getServiceRoute() {
        return requestData.get("serviceRoute");
    }
}
