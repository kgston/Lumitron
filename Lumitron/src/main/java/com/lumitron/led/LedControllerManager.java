package com.lumitron.led;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Set;

public class LedControllerManager {
    private static final HashMap<String, String> CONTROLLER_MODELS_CLASS_MAP = new HashMap<>();
    static {
        CONTROLLER_MODELS_CLASS_MAP.put("Lagute", "com.lumitron.led.LaguteLedController");
    }

    private static HashMap<String, LedController> registeredControllers = new HashMap<>();
    private static HashMap<String, HashMap<String, String>> registeredControllersInfo = new HashMap<>();

    /**
     * Get all available controller models that can be registered
     * @return A list of the controller model names
     */
    public static Set<String> getAvailableControllerModels() {
        return CONTROLLER_MODELS_CLASS_MAP.keySet();
    }

    /**
     * Get all registered controllers information
     * @return A list of the registered controller information ("name": {"model", "ipAddress"})
     */
    public static HashMap<String, HashMap<String, String>> getRegisteredControllers() {
        return registeredControllersInfo;
    }

    /**
     * Check whether a controller is registered
     * @param name The registered name of the controller
     * @return True if the controller is registered, false otherwise
     */
    public static boolean isControllerRegistered(String name) {
        return registeredControllers.get(name) != null && registeredControllersInfo.containsKey(name);
    }

    /**
     * Register a controller. If the controller is already registered, it is deregistered and registered again
     * with the given information
     * @param model A valid controller model (see getAvailableControllerModels())
     * @param name A name for the controller to be registered
     * @param ipAddress The IP address of the controller: where the commands are sent
     * @throws LedException
     */
    public static void registerController(String model, String name, String ipAddress) throws LedException {
        // Check the parameters
        if (CONTROLLER_MODELS_CLASS_MAP.containsKey(model) == false) {
            throw new LedException(LedControllerManager.class.getSimpleName(), "0001", "Unknown controller model '" + model + "'");
        }
        if (name == null || name.isEmpty()) {
            throw new LedException(LedControllerManager.class.getSimpleName(), "0002", "The controller name must be specified");
        }
        if (ipAddress == null || ipAddress.isEmpty()) {
            throw new LedException(LedControllerManager.class.getSimpleName(), "0003", "The IP address must be specified");
        }

        // Deregister the controller if exists
        deregisterController(name);

        // Create a new controller
        LedController ledController = null;

        try {
            Class<?> ledControllerClass = Class.forName(CONTROLLER_MODELS_CLASS_MAP.get(model));
            Constructor<?> ledControllerConstructor = ledControllerClass.getConstructor(String.class, String.class);
            ledController = (LedController) ledControllerConstructor.newInstance(name, ipAddress);
        } catch (ClassNotFoundException e) {
            throw new LedException(LedControllerManager.class.getSimpleName(), "0004", "Led controller class not found '" + CONTROLLER_MODELS_CLASS_MAP.get(model) + "'");
        } catch (Exception e) {
            e.printStackTrace();
            throw new LedException(LedControllerManager.class.getSimpleName(), "0005", "Led controller registration failed (unknown error occurred)");
        }

        // Register the new controller
        if (ledController == null) {
            throw new LedException(LedControllerManager.class.getSimpleName(), "0006", "Led controller registration failed");
        }

        HashMap<String, String> controllerInfo = new HashMap<>();
        controllerInfo.put("model", model);
        controllerInfo.put("ipAddress", ipAddress);

        registeredControllers.put(name, ledController);
        registeredControllersInfo.put(name, controllerInfo);
    }

    /**
     * Deregister a controller
     * @param name The name of the controller to deregister
     */
    public static void deregisterController(String name) {
        registeredControllers.remove(name);
        registeredControllersInfo.remove(name);
    }

    /**
     * Send a command to the controller given in parameter
     * @param controllerName The controller name
     * @param command The command name
     * @param params The parameters for the command, null if no parameters are necessary
     * @return True if the command has been sent, false otherwise (controller not registered)
     * @throws LedException
     */
    public static boolean sendCommand(String controllerName, String command, HashMap<String, String> params) throws LedException {
        // Check the parameters
        if (controllerName == null || controllerName.isEmpty()) {
            throw new LedException(LedControllerManager.class.getSimpleName(), "0007", "The controller name must be specified");
        }
        if (command == null || command.isEmpty()) {
            throw new LedException(LedControllerManager.class.getSimpleName(), "0008", "The command must be specified");
        }

        // Get the registered controller
        LedController controller = registeredControllers.get(controllerName);

        if (controller == null) {
            return false;
        }

        // Execute the command
        switch (command) {
            case "queryStatus":
                controller.queryStatus();
                break;
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
                throw new LedException(LedControllerManager.class.getSimpleName(), "0009", "Unknown command '" + command + "'");
        }

        return true;
    }
}
