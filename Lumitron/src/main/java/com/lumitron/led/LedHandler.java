package com.lumitron.led;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LedHandler {
    public static HashMap<String, Set<String>> getAvailableControllerModels() {
        // Get the available controller models
        Set<String> availableControllerModels = LedControllerManager.getAvailableControllerModels();

        // Return the formatted response
        HashMap<String, Set<String>> response = new HashMap<>();
        response.put("availableControllerModels", availableControllerModels);

        return response;
    }

    public static HashMap<String, ArrayList<HashMap<String, String>>> getRegisteredControllers() {
        // Get the registered controllers
        ArrayList<HashMap<String, String>> registeredControllers = LedControllerManager.getRegisteredControllers();

        // Return the formatted response
        HashMap<String, ArrayList<HashMap<String, String>>> response = new HashMap<>();
        response.put("registeredControllers", registeredControllers);

        return response;
    }

    public static HashMap<String, String> registerController(HashMap<String, String> params) {
        // Get the parameters
        String model = params.get("model");
        String name = params.get("name");
        String ipAddress = params.get("ipAddress");

        // Register the controller
        LedControllerManager.registerController(model, name, ipAddress);
        boolean isControllerRegistered = LedControllerManager.isControllerRegistered(name);

        // Return the formatted response
        HashMap<String, String> response = new HashMap<>();
        response.put("name", name);
        response.put("isRegistered", isControllerRegistered ? "true" : "false");

        return response;
    }

    public static HashMap<String, String> deregisterController(HashMap<String, String> params) {
        // Get the parameters
        String name = params.get("name");

        // Deregister the controller
        LedControllerManager.deregisterController(name);
        boolean isControllerRegistered = LedControllerManager.isControllerRegistered(name);

        // Return the formatted response
        HashMap<String, String> response = new HashMap<>();
        response.put("name", name);
        response.put("isRegistered", isControllerRegistered ? "true" : "false");

        return response;
    }

    public static HashMap<String, String> sendCommand(HashMap<String, String> params) {
        // Get the parameters
        String device = (String) params.get("device");
        String command = (String) params.get("command");
        
        @SuppressWarnings("unchecked")
        HashMap<String, String> commandParams = (HashMap<String, String>) params.clone();
        commandParams.remove("device");
        commandParams.remove("command");

        // Send the command to the controller
        boolean isCommandSent = LedControllerManager.sendCommand(device, command, commandParams);

        // Return the formatted response
        HashMap<String, String> response = new HashMap<>();
        response.put("device", device);
        response.put("command", command);
        response.put("isCommandSent", isCommandSent ? "true" : "false");

        return response;
    }
}
