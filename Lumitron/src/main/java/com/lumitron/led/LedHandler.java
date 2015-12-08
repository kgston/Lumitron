package com.lumitron.led;

import java.util.ArrayList;
import java.util.HashMap;

public class LedHandler {
    public static HashMap<String, Object> getDeviceControllers() {
        // Get the available device models
        ArrayList<HashMap<String, String>> deviceControllers = LedDeviceManager.getDeviceControllers();

        // Return the formatted response
        HashMap<String, Object> response = new HashMap<>();
        response.put("deviceControllers", deviceControllers);

        return response;
    }

    public static HashMap<String, Object> getRegisteredDevices() {
        // Get the registered devices
        ArrayList<HashMap<String, String>> registeredDevices = LedDeviceManager.getRegisteredDevices();

        // Return the formatted response
        HashMap<String, Object> response = new HashMap<>();
        response.put("registeredDevices", registeredDevices);

        return response;
    }

    public static HashMap<String, Object> registerDevice(HashMap<String, String> params) {
        // Get the parameters
        String model = params.get("model");
        String name = params.get("name");
        String ipAddress = params.get("ipAddress");

        // Register the controller
        LedDeviceManager.registerDevice(model, name, ipAddress);
        boolean isDeviceRegistered = LedDeviceManager.isDeviceRegistered(name);

        // Return the formatted response
        HashMap<String, Object> response = new HashMap<>();
        response.put("name", name);
        response.put("isRegistered", isDeviceRegistered);

        return response;
    }

    public static HashMap<String, Object> deregisterDevice(HashMap<String, String> params) {
        // Get the parameters
        String name = params.get("name");

        // Deregister the device
        LedDeviceManager.deregisterDevice(name);
        boolean isDeviceRegistered = LedDeviceManager.isDeviceRegistered(name);

        // Return the formatted response
        HashMap<String, Object> response = new HashMap<>();
        response.put("name", name);
        response.put("isRegistered", isDeviceRegistered);

        return response;
    }
    
    public static HashMap<String, Object> getState(HashMap<String, String> params) {
        // Get the parameters
        String name = params.get("device");

        // Gets the state of the device
        return LedDeviceManager.getState(name);
    }

    public static HashMap<String, Object> sendCommand(HashMap<String, String> params) {
        // Get the parameters
        String device = (String) params.get("device");
        String command = (String) params.get("command");
        
        @SuppressWarnings("unchecked")
        HashMap<String, String> commandParams = (HashMap<String, String>) params.clone();
        commandParams.remove("device");
        commandParams.remove("command");

        // Send the command to the device
        boolean isCommandSent = LedDeviceManager.sendCommand(device, command, commandParams);

        // Return the formatted response
        HashMap<String, Object> response = new HashMap<>();
        response.put("device", device);
        response.put("command", command);
        response.put("isCommandSent", isCommandSent);

        return response;
    }
}
