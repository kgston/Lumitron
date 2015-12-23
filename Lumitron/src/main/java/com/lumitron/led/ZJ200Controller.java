/**
 * 
 */
package com.lumitron.led;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.lumitron.util.AppSystem;

/**
 * @author Kingston Chan
 *
 */
public class ZJ200Controller extends GenericLedController {
    static final String MODEL = "ZJ-200";

    private static final String ON_CMD = "71230FA3";
    private static final String OFF_CMD = "71240FA4";
    
    private static final String TRANSISTION_CMD_HEADER = "31";
    private static final String STROBE_CMD_HEADER = "51";
    private static final String RGB_CMD_SIGNAL = "F00F";
    private static final String WWCW_CMD_SIGNAL = "0F0F";
    private static final String ALL_CMD_SIGNAL = "0000";
    
    private static final String STROBE_END_PADDING = "00010203000102030001020300010203000102030001020300010203000102030001020300010203000102030001020300010203000102030001020300013BFF0F";
    
    private String currentHexWWCW = "0000";
    private double brightnessMultipler = Double.parseDouble(currentBrightness) / 100;
    
    public ZJ200Controller(String deviceName, String ipAddress) throws LedException {
        super(deviceName, ipAddress, 5577);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#getModel()
     */
    @Override
    public String getModel() {
        return MODEL;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<String, Object> getState() {
        HashMap<String, Object> state = new HashMap<>();
        state.put("state", currentState);
        state.put("brightness", currentBrightness);
        state.put("colour", currentHexColour + currentHexWWCW);
        return state;
    }

    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#connect()
     */
    @Override
    public void connect() throws LedException {
        AppSystem.log(this.getClass(), "Connecting to " + deviceName);
        try {
            if(tcpConnection == null) {
                tcpConnection = new Socket (ipAddress, port);
                AppSystem.log(this.getClass(), "Connected!");
                on();
            } else {
                AppSystem.log(this.getClass(), "Already connected!");
            }
        } catch (SocketException e) {
            
        } catch (UnknownHostException e) {
            AppSystem.log(this.getClass(), "Unable to locate device! " + e.getMessage());
            throw new LedException(this.getClass().getSimpleName(), "0012", "Unable to locate device");
        } catch (IOException e) {
            AppSystem.log(this.getClass(), "Failed to connect! " + e.getMessage());
            throw new LedException(this.getClass().getSimpleName(), "0013", "Unable to connect to device");
        }
    }
    
    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#disconnect()
     */
    @Override
    public void disconnect() throws LedException {
        AppSystem.log(this.getClass(), "Disconnecting from " + deviceName);
        if(tcpConnection != null) {
            off();
            try {
                tcpConnection.close();
            } catch (IOException e) {
                AppSystem.log(this.getClass(), "Error disconnecting from " + deviceName);
                throw new LedException(this.getClass().getSimpleName(), "0014", "Unable to disconnect from device");
            }
            tcpConnection = null;
        }
    }
    
    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#on()
     */
    @Override
    public void on() throws LedException {
        currentState = "on";
        sendTCP(ON_CMD, false);
    }

    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#off()
     */
    @Override
    public void off() throws LedException {
        currentState = "off";
        sendTCP(OFF_CMD, false);
    }
    
    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#setColour(java.lang.String)
     */
    @Override
    public void setColour(String hexColourString) throws LedException {
        if(hexColourString == null) {
            throw new LedException(this.getClass().getSimpleName(), "0010", "Hex colour not valid");
        }
        
        if(hexColourString.length() == 6) {
            AppSystem.log(this.getClass(), "Setting " + deviceName + " colour to: " + hexColourString);
            sendTCP(TRANSISTION_CMD_HEADER + adjustBrightness(hexColourString) + adjustBrightness(currentHexWWCW) + RGB_CMD_SIGNAL, false);
            currentHexColour = hexColourString;
            
        } else if(hexColourString.length() == 4) {
            AppSystem.log(this.getClass(), "Setting " + deviceName + " colour to: " + currentHexColour + " " + hexColourString);
            sendTCP(TRANSISTION_CMD_HEADER + adjustBrightness(currentHexColour) + adjustBrightness(hexColourString) + WWCW_CMD_SIGNAL, false);
            currentHexWWCW = hexColourString;
            
        } else if(hexColourString.length() == 10) {
            String rgbHex = hexColourString.substring(0, 6);
            String wwcwHex = hexColourString.substring(6);
            AppSystem.log(this.getClass(), "Setting " + deviceName + " colour to: " + rgbHex + " " + wwcwHex);
            sendTCP(TRANSISTION_CMD_HEADER + adjustBrightness(rgbHex) + adjustBrightness(wwcwHex) + ALL_CMD_SIGNAL, false);
            currentHexColour = rgbHex;
            currentHexWWCW = wwcwHex;
        } else {
            throw new LedException(this.getClass().getSimpleName(), "0010", "Hex colour has invalid length");
        }
    }
    
    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#setStrobe(java.lang.String)
     */
    @Override
    public void setStrobe(String hexColourString) throws LedException {
        if(hexColourString == null) {
            throw new LedException(this.getClass().getSimpleName(), "0010", "Hex colour not valid");
        }
        
        if(hexColourString.length() == 6) {
            AppSystem.log(this.getClass(), "Setting " + deviceName + " colour to: " + hexColourString);
            sendTCP(STROBE_CMD_HEADER + adjustBrightness(hexColourString) + STROBE_END_PADDING, false);
            currentHexColour = hexColourString;
            
        } else {
            throw new LedException(this.getClass().getSimpleName(), "0010", "Hex colour has invalid length");
        }
    }
    
    public String adjustBrightness(String hexColour) throws LedException {
        System.out.println(hexColour.length());
        System.out.println(hexColour.length() % 2);
        if(hexColour.length() % 2 != 0) {
            throw new LedException(this.getClass().getSimpleName(), "0011", "Unable to adjust brightness, hex colour has invalid length");
        }
        
        String[] individualColours = hexColour.split("(?<=\\G.{2})");
        StringBuilder finalHexColour = new StringBuilder();
        for(String individualColour: individualColours) {
            int colourValue = toColourInt(individualColour);
            colourValue = (int) Math.floor(colourValue * brightnessMultipler);
            finalHexColour.append(toColourHex(colourValue));
        }
        return finalHexColour.toString();
    }

    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#setBrightness(java.lang.String)
     */
    @Override
    public void setBrightness(String brightnessLevel) throws LedException {
        currentBrightness = brightnessLevel;
        brightnessMultipler = Double.parseDouble(brightnessLevel) / 100;
        AppSystem.log(this.getClass(), "Setting " + deviceName + " brightness to " + brightnessLevel + "%");
        setColour(currentHexColour + currentHexWWCW);
    }

    @Override
    protected String sendTCP(String hexCommand, boolean hasResponse) throws LedException {
        return super.sendTCP(appendChecksum(hexCommand), hasResponse);
    }
    
    private static String appendChecksum(String hexCommand) {
        int checksum = 0;
        String[] hexBits = hexCommand.split("(?<=\\G.{2})");
        for(String hexBit: hexBits) {
            checksum += Integer.parseInt(hexBit, 16);
        }
        checksum %= 256;
        String checksumStr = Integer.toHexString(checksum);
        if(checksumStr.length() == 1) {
            checksumStr = "0" + checksumStr;
        }
        return hexCommand + checksumStr;
    }
}
