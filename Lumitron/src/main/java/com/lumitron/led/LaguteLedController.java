package com.lumitron.led;

import com.lumitron.util.AppSystem;

/**
 * A concrete implementation of an LED controller called Lagute.
 * @author Kingston Chan
 *
 */
public class LaguteLedController extends GenericLedController {
    
    private final String HEADER = "7E";
    private final String FOOTER = "00EF";
    
    private final String COMMAND_ON = "040401";
    private final String COMMAND_OFF = "040400";
    
    private final String COLOUR_WHITE = "FFFFFF";
    
    /**
     * Creates a new instance of the Lagute LED controller
     * @param deviceName A user defined name for the controller. Should be unique
     * @param ipAddress The IP address where the controller is reachable
     * @throws LedException
     */
    public LaguteLedController(String deviceName, String ipAddress) throws LedException {
        super(deviceName, ipAddress, 5000);
    }
    
    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#queryStatus()
     */
    @Override
    public boolean queryStatus() throws LedException {
        on();
        setColour("0D0D0D");
        return true;
    }
    
    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#on()
     */
    @Override
    public void on() throws LedException {
        AppSystem.log(this.getClass(), "Turning " + deviceName + " on");
        send(HEADER + COMMAND_ON + COLOUR_WHITE + FOOTER, false);
    }

    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#off()
     */
    @Override
    public void off() throws LedException {
        AppSystem.log(this.getClass(), "Turning " + deviceName + " off");
        send(HEADER + COMMAND_OFF + COLOUR_WHITE + FOOTER, false);
    }

    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#setColour(java.lang.String)
     */
    @Override
    public void setColour(String hexColourString) throws LedException {
        if(hexColourString == null || hexColourString.length() != 6) {
            throw new LedException(this.getClass().getSimpleName(), "0010", "Hex colour not valid");
        }
        AppSystem.log(this.getClass(), "Setting " + deviceName + " colour to: " + hexColourString);
        send(HEADER + "070503" + hexColourString + FOOTER, false);
        currentHexColour = hexColourString;
    }
    
    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#setBrightness(java.lang.String)
     */
    @Override
    public void setBrightness(String brightnessLevel) throws LedException {
        if(brightnessLevel == null || brightnessLevel.length() == 0) {
            throw new LedException(this.getClass().getSimpleName(), "0009", "Device name not specified");
        }
        AppSystem.log(this.getClass(), "Setting " + deviceName + " brightness to: " + brightnessLevel + "%");
        String levelHex = Integer.toHexString(Integer.parseInt(brightnessLevel));
        if(levelHex.length() == 1) {
            levelHex = "0" + levelHex;
        }
        send(HEADER + "0401" + levelHex + COLOUR_WHITE + FOOTER, false);
    }
}
