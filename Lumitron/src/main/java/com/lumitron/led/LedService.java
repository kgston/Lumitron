package com.lumitron.led;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import com.lumitron.network.LumitronService;
import com.lumitron.network.RequestHandler;

public class LedService implements LumitronService {
    private HashMap<String, String> serviceRoute;
    private HashMap<String, String> params;
    
    public void getAvaliableControllers() {
        TreeSet<String> avaliableControllers = LedHandler.getAvailableControllers();
        RequestHandler.send(serviceRoute.get("uuid"), avaliableControllers);
    }
    
    public void getRegisteredControllers() {
        TreeSet<String> registeredControllers = LedHandler.getRegisteredControllers();
        RequestHandler.send(serviceRoute.get("uuid"), registeredControllers);
    }
    
    public void addController() {
        try {
            LedHandler.addController(params.get("deviceName"));
        } catch (LedException e) {
            RequestHandler.sendError(
                            serviceRoute.get("uuid"), 
                            this.getClass().getSimpleName(),
                            e.getErrorCode(),
                            e.getMessage());
        }
    }
    
    public void sendCommand() {
        try {
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
            LedHandler.sendCommand(
                            params.get("device"),
                            params.get("command"),
                            commandParameters.toArray(commandParametersAry)
                            );
        } catch (LedException e) {
            RequestHandler.sendError(
                            serviceRoute.get("uuid"), 
                            this.getClass().getSimpleName(),
                            e.getErrorCode(),
                            e.getMessage());
        }
    }
    
    @Override
    public void setRequestData(HashMap<String, HashMap<String, String>> requestData) {
        this.serviceRoute = requestData.get("serviceRoute");
        this.params = requestData.get("params");
    }
}
