package com.lumitron.led;

import com.lumitron.util.AppSystem;

public class LaguteLedController extends GenericLedController {
    
    private final String HEADER = "7E";
    private final String FOOTER = "00EF";
    
    private final String COMMAND_ON = "040401";
    private final String COMMAND_OFF = "040400";
    
    private final String COLOUR_WHITE = "FFFFFF";
    
    public LaguteLedController(String deviceName, String ipAddress) throws LedException {
        super(deviceName, ipAddress, 5000);
    }
    
    @Override
    public boolean queryStatus() throws LedException {
        on();
        setColour("0D0D0D");
        return true;
    }
    
    @Override
    public void on() throws LedException {
        AppSystem.log(this.getClass(), "Turning " + deviceName + " on");
        send(HEADER + COMMAND_ON + COLOUR_WHITE + FOOTER, false);
    }

    @Override
    public void off() throws LedException {
        AppSystem.log(this.getClass(), "Turning " + deviceName + " off");
        send(HEADER + COMMAND_OFF + COLOUR_WHITE + FOOTER, false);
    }

    @Override
    public void setColour(String hexColourString) throws LedException {
        if(hexColourString == null || hexColourString.length() != 6) {
            throw new LedException(this.getClass().getSimpleName(), "0010", "Hex colour not valid");
        }
        AppSystem.log(this.getClass(), "Setting " + deviceName + " colour to: " + hexColourString);
        send(HEADER + "070503" + hexColourString + FOOTER, false);
        currentHexColour = hexColourString;
    }
    
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
