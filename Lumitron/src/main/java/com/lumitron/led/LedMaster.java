package com.lumitron.led;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.TreeSet;

import com.lumitron.util.AppSystem;

public class LedMaster {
    
    private static HashMap<String, Class<?>[]> commandParameterTypes = new HashMap<>();
    private static HashMap<String, String> deviceControllerClassMap = new HashMap<>();
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
        
        //Define the available LED controllers
        deviceControllerClassMap.put("Lagute", "com.lumitron.led.LaguteLedController");
        //Search for all devices on the network
        //Populate all found devices to the deviceMap
    }
    
    public static TreeSet<String> getAvaliableControllers() {
        refreshAvaliableControllers();
        return new TreeSet<String>(avaliableControllers.keySet());
    }
    
    private static void refreshAvaliableControllers() {
        //TODO Implement search for controllers on WiFi network
        HashMap<String, String> deviceInfo = new HashMap<>();
        deviceInfo.put("ipAddress", "10.10.10.10");
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
            throw new LedException("0007", "Device name not specified");
        }
        Class<?> deviceControllerClass = null;
        LedController ledController = null;
        try {
            System.out.println(avaliableControllers);
            deviceControllerClass = Class.forName(deviceControllerClassMap.get(avaliableControllers.get(deviceName).get("deviceModel")));
            Constructor<?> deviceControllerConstructor = deviceControllerClass.getConstructor(String.class, String.class);
            ledController = (LedController) deviceControllerConstructor.newInstance(deviceName, avaliableControllers.get(deviceName).get("ipAddress"));
        } catch (Exception e) {
            AppSystem.log(LedMaster.class, "Unable to connect to " + deviceName + "@" + avaliableControllers.get(deviceName).get("ipAddress") + ": " + e.getMessage());
            throw new LedException("0001", "Connection failed");
        }
        if(ledController != null) {
            registeredControllers.put(deviceName, ledController);
        } else {
            AppSystem.log(LedMaster.class, deviceName + " was not added to the device map");
        }
    }
    
    public static void sendCommand(String deviceName, String commandName, String... commandParameters) throws LedException {
        if(deviceName == null || deviceName.length() == 0) {
            throw new LedException("0007", "Device name not specified");
        }
        LedController ledController = registeredControllers.get(deviceName);
        try {
            Method method = ledController.getClass().getMethod(commandName, commandParameterTypes.get(commandName));
            method.invoke(ledController, (Object[]) commandParameters);
        } catch (NoSuchMethodException e) {
            throw new LedException("0002", "Command not found");
        } catch (SecurityException e) {
            throw new LedException("0003", "Security violation");
        } catch (IllegalAccessException e) {
            throw new LedException("0004", "Unable to access the command");
        } catch (IllegalArgumentException e) {
            throw new LedException("0005", "Invalid command parameters");
        } catch (InvocationTargetException e) {
            throw new LedException("0006", "Error running command");
        }
    }
    
}
