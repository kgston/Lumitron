package com.lumitron.ledevents;

import java.util.HashMap;

import com.lumitron.led.LedDeviceManager;

public class LedEvent implements Comparable<LedEvent>, Runnable {
    /**
     * Execution time in milliseconds
     */
    private Long time;

    /**
     * Device name
     */
    private String deviceName;

    /**
     * Name of the command to be executed by the controller
     */
    private String command;

    /**
     * Parameters for the command
     */
    private HashMap<String, String> params;

    /**
     * Constructor
     * @param executionTime The execution time in milliseconds
     * @param deviceName The device name
     * @param command The name of the command to be executed by the device
     * @param commandParameters The parameters for the command
     */
    public LedEvent(Long executionTime, String deviceName, String command, HashMap<String, String> commandParameters) {
        this.time = executionTime;
        this.deviceName = deviceName;
        this.command = command;
        this.params = commandParameters;
    }

    /**
     * Compare the execution time of the event in the ascending order
     * @param e the other event to compare to
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(LedEvent other) {
        Long comparison = this.time - other.time;

        if (comparison == 0) {
            return 0;
        }

        return (comparison < 0) ? -1 : 1;
    }

    @Override
    public String toString() {
        return "{time: " + time +
               "; device: " + deviceName +
               "; command: " + command +
               "; parameters: " + ((params == null) ? "{}" : params.toString()) + "}";
    }

    /**
     * Create a new thread and execute the event inside
     */
    public void execute() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        LedDeviceManager.sendCommand(deviceName, command, params);
    }

    /**
     * @return the execution time in milliseconds
     */
    public Long getExecutionTime() {
        return time;
    }

    /**
     * @return the device name
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return the command parameters
     */
    public HashMap<String, String> getCommandParameters() {
        return params;
    }
}
