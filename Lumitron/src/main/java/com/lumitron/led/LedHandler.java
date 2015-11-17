package com.lumitron.led;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.TreeSet;

import com.lumitron.util.AppSystem;

public class LedHandler {
    
    private static final HashMap<String, Class<?>[]> commandParameterTypes = new HashMap<>();
    private static final HashMap<String, String> deviceControllerClassMap = new HashMap<>();
    private static HashMap<String, HashMap<String, String>> avaliableControllers = new HashMap<>();
    private static HashMap<String, LedController> registeredControllers = new HashMap<>();
    
    static {
        //Define command parameter types
        commandParameterTypes.put("on", null);
        commandParameterTypes.put("off", null);
        commandParameterTypes.put("connect", null);
        commandParameterTypes.put("disconnect", null);
        commandParameterTypes.put("setColour", new Class<?>[] {String.class});
        commandParameterTypes.put("setBrightness", new Class<?>[] {String.class});
        commandParameterTypes.put("transitionToColour", new Class<?>[] {String.class, String.class, String.class});
        
        //Define the available LED controllers
        deviceControllerClassMap.put("Lagute", "com.lumitron.led.LaguteLedController");
        //Search for all devices on the network
        //Populate all found devices to the deviceMap
    }
    
    public static TreeSet<String> getAvailableControllers() {
        refreshAvailableControllers();
        return new TreeSet<String>(avaliableControllers.keySet());
    }
    
    private static void refreshAvailableControllers() {
        //TODO Implement search for controllers on WiFi network
        HashMap<String, String> deviceInfo = new HashMap<>();
        deviceInfo.put("ipAddress", "10.10.1.1");
        deviceInfo.put("deviceModel", "Lagute");
        if(registeredControllers.containsKey("Lagute-0001")) {
            avaliableControllers.remove("Lagute-0001");
        } else {
            avaliableControllers.put("Lagute-0001", deviceInfo);
        }
    }
    
    public static TreeSet<String> getRegisteredControllers() {
        return new TreeSet<String>(registeredControllers.keySet());
    }
    
    public static void addController(String deviceName) throws LedException {
        if(deviceName == null || deviceName.length() == 0) {
            throw new LedException(LedHandler.class.getSimpleName(), "0007", "Device name not specified");
        }
        Class<?> deviceControllerClass = null;
        LedController ledController = null;
        try {
            deviceControllerClass = Class.forName(deviceControllerClassMap.get(avaliableControllers.get(deviceName).get("deviceModel")));
            Constructor<?> deviceControllerConstructor = deviceControllerClass.getConstructor(String.class, String.class);
            ledController = (LedController) deviceControllerConstructor.newInstance(deviceName, avaliableControllers.get(deviceName).get("ipAddress"));
        } catch (Exception e) {
            AppSystem.log(LedHandler.class, "Unable to connect to " + deviceName + "@" + avaliableControllers.get(deviceName).get("ipAddress") + ": " + e.getMessage());
            e.printStackTrace();
            throw new LedException(LedHandler.class.getSimpleName(), "0001", "Connection failed");
        }
        if(ledController != null) {
            registeredControllers.put(deviceName, ledController);
        } else {
            AppSystem.log(LedHandler.class, deviceName + " was not added to the device map");
        }
    }
    
    public static void sendCommand(String deviceName, String commandName, String... commandParameters) throws LedException {
        //Error checking
        if(deviceName == null || deviceName.length() == 0) {
            throw new LedException(LedHandler.class.getSimpleName(), "0007", "Device name not specified");
        }
        if(commandName == null || commandName.length() == 0) {
            throw new LedException(LedHandler.class.getSimpleName(), "0008", "Command name not specified");
        }
        
        //Get the controller from the list of registered devices
        LedController ledController = registeredControllers.get(deviceName);
        
        //OMFG, you mean it was not registered?
        if(ledController == null) {
            //Quickly! See if its floating around somewhere!
            AppSystem.log(LedHandler.class, deviceName + " was not registered, attempting to add from avaliable list");
            TreeSet<String> avaliableControllers =  getAvailableControllers();
            //Phew! Found it~! Let's register it and get on with it
            if(avaliableControllers.contains(deviceName)) {
                AppSystem.log(LedHandler.class, deviceName + " was automatically registered");
                addController(deviceName);
                ledController = registeredControllers.get(deviceName);
            } else {
                //Crap! It's no where to be found
                AppSystem.log(LedHandler.class, deviceName + " was not found on the avaliable list");
                throw new LedException(LedHandler.class.getSimpleName(), "0009", "Controller not found/registered");
            }
        }
        
        //Invoke the method
        try {
            Method method = ledController.getClass().getMethod(commandName, commandParameterTypes.get(commandName));
            method.invoke(ledController, (Object[]) commandParameters);
        } catch (NoSuchMethodException e) {
            throw new LedException(LedHandler.class.getSimpleName(), "0002", "Command not found");
        } catch (SecurityException e) {
            throw new LedException(LedHandler.class.getSimpleName(), "0003", "Security violation");
        } catch (IllegalAccessException e) {
            throw new LedException(LedHandler.class.getSimpleName(), "0004", "Unable to access the command");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new LedException(LedHandler.class.getSimpleName(), "0005", "Invalid command parameters");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new LedException(LedHandler.class.getSimpleName(), "0006", "Error running command");
        }
    }
    
}
