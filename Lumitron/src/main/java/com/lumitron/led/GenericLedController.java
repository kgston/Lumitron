package com.lumitron.led;

import com.lumitron.util.AppSystem;

public abstract class GenericLedController implements LedController {
    
    protected String deviceName;
    protected String ipAddress;
    protected final int port;
    
    protected String currentHexColour = "";
    
    public GenericLedController(String deviceName, String ipAddress, int port) throws LedException {
        this.deviceName = deviceName;
        this.ipAddress = ipAddress;
        this.port = port;
        
        connect();
        if(queryStaus()) {
            AppSystem.log(this.getClass(), "Connected!");
        } else {
            AppSystem.log(this.getClass(), "No response from device");
        }
    }

    public void transitionToColour(String pauseInterval, String incrementInterval, String hexColourString) throws LedException {
        String[] fromHexColour = splitByLength(currentHexColour, 2);
        String[] toHexColour = splitByLength(hexColourString, 2);
        int fromRed = Integer.parseInt(fromHexColour[0], 16);
        int fromGreen = Integer.parseInt(fromHexColour[1], 16);
        int fromBlue = Integer.parseInt(fromHexColour[2], 16);
        int toRed = Integer.parseInt(toHexColour[0], 16);
        int toGreen = Integer.parseInt(toHexColour[1], 16);
        int toBlue = Integer.parseInt(toHexColour[2], 16);
        
        int increment = Integer.parseInt(incrementInterval);
        int diff = 0;
        
        boolean transitioning = true;
        while(transitioning) {
            transitioning = false;
            if(fromRed < toRed) {
                diff = toRed - fromRed;
                if(diff < increment) {
                    fromRed+= diff;
                } else {
                    fromRed+= increment;
                }
                transitioning = true;
            }
            if(fromGreen < toGreen) {
                diff = toGreen - fromGreen;
                if(diff < increment) {
                    fromGreen+= diff;
                } else {
                    fromGreen+= increment;
                }
                transitioning = true;
            }
            if(fromBlue < toBlue) {
                diff = toBlue - fromBlue;
                if(diff < increment) {
                    fromBlue+= diff;
                } else {
                    fromBlue+= increment;
                }
                transitioning = true;
            }
            if(fromRed > toRed) {
                diff = fromRed - toRed;
                if(diff < increment) {
                    fromRed-= diff;
                } else {
                    fromRed-= increment;
                }
                transitioning = true;
            }
            if(fromGreen > toGreen) {
                diff = fromGreen - toGreen;
                if(diff < increment) {
                    fromGreen-= diff;
                } else {
                    fromGreen-= increment;
                }
                transitioning = true;
            }
            if(fromBlue > toBlue) {
                diff = fromBlue - toBlue;
                if(diff < increment) {
                    fromBlue-= diff;
                } else {
                    fromBlue-= increment;
                }
                transitioning = true;
            }
            setColour(fromRed, fromGreen, fromBlue);
            try {
                Thread.sleep(Integer.parseInt(pauseInterval));
            } catch (InterruptedException e) {
                AppSystem.log(this.getClass(), "Got interrupted during transitioning");
            }
        }
    }
    
    private String[] splitByLength(String string, int lengthToSplitBy) {
        return string.split("(?<=\\G.{" + lengthToSplitBy + "})");
    }
}
