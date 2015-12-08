package com.lumitron.led;

import java.util.HashMap;

/**
 * Defines the interface of a LED controller
 * @author Kingston Chan
 *
 */
public interface LedController {
    /**
     * Returns the model name of the device
     * @return The model name
     */
    public String getModel();
    
    /**
     * Returns the device name
     * @return The device name
     */
    public String getName();
    
    /**
     * Returns the IP address of the device
     * @return the IP address
     */
    public String getIpAddress();
    
    /**
     * Returns the last known state of the device
     * @return the IP address
     */
    public HashMap<String, Object> getState() throws LedException;
    
    /**
     * Turns the lights on
     * @throws LedException
     */
    public void on() throws LedException;
    
    /**
     * Turns the lights off
     * @throws LedException
     */
    public void off() throws LedException;
    
    /**
     * Connects to the device and opens a long-lived connection
     * @throws LedException
     */
    public void connect() throws LedException;
    
    /**
     * Disconnects from the device and closes the connection to the device
     * @throws LedException
     */
    public void disconnect() throws LedException;
    
    /**
     * Sets the colour to the specified RGB values immediately
     * @param red The amount of red from 0-255
     * @param green The amount of green from 0-255
     * @param blue The amount of blue from 0-255
     * @throws LedException
     */
    public void setColour(int red, int green, int blue) throws LedException;
    
    /**
     * Sets the colour to the specified hex colour immediately
     * @param hexString The RGB colour in hex format (without the initial #). E.g FF0000 for red and 0000FF for green.
     * @throws LedException
     */
    public void setColour(String hexString) throws LedException;
    
    /**
     * Algorithmic implementation to transit to the specified colour smoothly from the current colour
     * From on the current colour, it will increase/decrease the RGB value towards the target colour 
     * in steps based on the increment interval, followed by a pause interval
     * @param pauseInterval The interval in ms to pause after 1 step
     * @param incrementInterval The size of the step
     * @param hexColourString The target colour in hex
     * @throws LedException
     */
    public void transitionToColour(String pauseInterval, String incrementInterval, String hexColourString) throws LedException;
    
    /**
     * Sets max brightness level
     * @param brightnessLevel The brightness level from 0-100 in steps of 10
     * @throws LedException
     */
    public void setBrightness(String brightnessLevel) throws LedException;
}
