package com.lumitron.led;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

public class LedDeviceManager {
    private static final HashMap<String, Class<?>> DEVICE_MODELS_CLASS_MAP = new HashMap<>();
    static {
        DEVICE_MODELS_CLASS_MAP.put(LaguteLedController.MODEL, LaguteLedController.class);
    }

    private static HashMap<String, LedController> registeredDevices = new HashMap<>();

    /**
     * Get all available device models that can be registered
     * @return A list of the device model names
     */
    public static ArrayList<HashMap<String, String>> getDeviceControllers() {
        TreeSet<String> models = new TreeSet<>();
        models.addAll(DEVICE_MODELS_CLASS_MAP.keySet());
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        for(String model: models) {
            HashMap<String, String> modelMap = new HashMap<>();
            modelMap.put("name", model);
            list.add(modelMap);
        }
        return list;
    }

    /**
     * Get all registered device information
     * @return A list of the registered devices information ("name": {"model", "ipAddress"})
     */
    public static ArrayList<HashMap<String, String>> getRegisteredDevices() {
        ArrayList<HashMap<String, String>> devicesInfo = new ArrayList<>();

        for (Entry<String, LedController> element : registeredDevices.entrySet()) {
            HashMap<String, String> deviceInfo = new HashMap<>();
            deviceInfo.put("model", element.getValue().getModel());
            deviceInfo.put("name", element.getValue().getName());
            deviceInfo.put("ipAddress", element.getValue().getIpAddress());

            devicesInfo.add(deviceInfo);
        }

        return devicesInfo;
    }

    /**
     * Check whether a device is registered
     * @param name The registered name of the device
     * @return True if the device is registered, false otherwise
     */
    public static boolean isDeviceRegistered(String name) {
        return registeredDevices.get(name) != null;
    }

    /**
     * Register a device. If the device is already registered, it is deregistered and registered again
     * with the given information
     * @param model A valid device model (see getAvailableControllerModels())
     * @param name A name for the device to be registered
     * @param ipAddress The IP address of the device: where the commands are sent
     * @throws LedException
     */
    public static void registerDevice(String model, String name, String ipAddress) throws LedException {
        // Check the parameters
        if (DEVICE_MODELS_CLASS_MAP.containsKey(model) == false) {
            throw new LedException(LedDeviceManager.class.getSimpleName(), "0001", "Unknown device model '" + model + "'");
        }
        if (name == null || name.isEmpty()) {
            throw new LedException(LedDeviceManager.class.getSimpleName(), "0002", "The device name must be specified");
        }
        if (ipAddress == null || ipAddress.isEmpty()) {
            throw new LedException(LedDeviceManager.class.getSimpleName(), "0003", "The IP address must be specified");
        }

        // Deregister the device if exists
        deregisterDevice(name);

        // Create a new controller
        LedController ledController = null;

        try {
            Class<?> ledControllerClass = DEVICE_MODELS_CLASS_MAP.get(model);
            Constructor<?> ledControllerConstructor = ledControllerClass.getConstructor(String.class, String.class);
            ledController = (LedController) ledControllerConstructor.newInstance(name, ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LedException(LedDeviceManager.class.getSimpleName(), "0004", "Led device registration failed (unknown error occurred)");
        }

        // Register the new controller
        if (ledController == null) {
            throw new LedException(LedDeviceManager.class.getSimpleName(), "0005", "Led device registration failed");
        }

        registeredDevices.put(name, ledController);
    }

    /**
     * Deregister a device
     * @param deviceName The name of the device to deregister
     */
    public static void deregisterDevice(String deviceName) {
        registeredDevices.remove(deviceName);
    }
    
    /**
     * Gets the last known state of the device
     * @param deviceName The name of the device to get the state of
     */
    public static HashMap<String, Object> getState(String deviceName) {
        // Get the registered device controller
        LedController controller = registeredDevices.get(deviceName);
        
        if (controller == null) {
            return null;
        } else {
            return controller.getState();
        }
    }

    /**
     * Send a command to the device given in parameter
     * @param controllerName The device name
     * @param command The command name
     * @param params The parameters for the command, null if no parameters are necessary
     * @return True if the command has been sent, false otherwise (device not registered)
     * @throws LedException
     */
    public static boolean sendCommand(String deviceName, String command, HashMap<String, String> params) throws LedException {
        // Check the parameters
        if (deviceName == null || deviceName.isEmpty()) {
            throw new LedException(LedDeviceManager.class.getSimpleName(), "0006", "The device name must be specified");
        }
        if (command == null || command.isEmpty()) {
            throw new LedException(LedDeviceManager.class.getSimpleName(), "0007", "The command must be specified");
        }

        // Get the registered device controller
        LedController controller = registeredDevices.get(deviceName);

        if (controller == null) {
            return false;
        }

        // Execute the command
        switch (command) {
            case "on":
                controller.on();
                break;
            case "off":
                controller.off();
                break;
            case "setColour":
                controller.setColour(params.get("colour"));
                break;
            case "setBrightness":
                controller.setBrightness(params.get("brightness"));
                break;
            case "transitionToColour":
                controller.transitionToColour(params.get("pauseInterval"), params.get("incrementInterval"), params.get("colour"));
                break;
            default:
                throw new LedException(LedDeviceManager.class.getSimpleName(), "0008", "Unknown command '" + command + "'");
        }

        return true;
    }
}
