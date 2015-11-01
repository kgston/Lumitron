package com.lumitron.led;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import com.lumitron.network.RequestData;
import com.lumitron.network.RequestHandler;

public class LedService implements RequestData {
    
    private HashMap<String, HashMap<String, String>> requestData;
    
    public void getAvaliableControllers() {
        TreeSet<String> avaliableControllers = LedMaster.getAvaliableControllers();
        RequestHandler.send((String) requestData.get("serviceRoute").get("uuid"), avaliableControllers);
    }
    
    public void getRegisteredControllers() {
        TreeSet<String> registeredControllers = LedMaster.getRegisteredControllers();
        RequestHandler.send((String) requestData.get("serviceRoute").get("uuid"), registeredControllers);
    }
    
    public void addController() {
        try {
            LedMaster.addController((String) getParams().get("deviceName"));
        } catch (LedException e) {
            RequestHandler.sendError(
                            (String) requestData.get("serviceRoute").get("uuid"), 
                            this.getClass().getSimpleName(),
                            e.getErrorCode(),
                            e.getMessage());
        }
    }
    
    public void sendCommand() {
        try {
            HashMap<String, String> params = getParams();
            ArrayList<String> commandParameters = new ArrayList<>();
            switch(params.get("command")) {
                case "transitionToColour": 
                    commandParameters.add(params.get("pauseInterval"));
                    commandParameters.add(params.get("incrementInterval"));
                case "setColour": 
                    commandParameters.add(params.get("colour"));
                    break;
                case "setBrightness": 
                    commandParameters.add(params.get("brightness"));
                    break;   
            }
            String[] commandParametersAry = new String[commandParameters.size()];
            LedMaster.sendCommand(
                            params.get("device"),
                            params.get("command"),
                            commandParameters.toArray(commandParametersAry)
                            );
        } catch (LedException e) {
            RequestHandler.sendError(
                            (String) requestData.get("serviceRoute").get("uuid"), 
                            this.getClass().getSimpleName(),
                            e.getErrorCode(),
                            e.getMessage());
        }
    }
    
    @Override
    public void setRequestData(HashMap<String, HashMap<String, String>> requestData) {
        this.requestData = requestData;
    }
    
    private HashMap<String, String> getParams() {
        return (HashMap<String, String>) requestData.get("params");
    }

}
