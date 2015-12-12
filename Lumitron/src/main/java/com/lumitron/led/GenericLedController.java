package com.lumitron.led;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.bind.DatatypeConverter;

import com.lumitron.util.AppSystem;

/**
 * Implements an abstract LED controller that implements all generic functions across LED controllers
 * Uses UDP as a connection protocol
 * @author Kingston Chan
 *
 */
public abstract class GenericLedController implements LedController {
    protected String deviceName;

    protected DatagramSocket udpConnection;
    protected Socket tcpConnection;
    
    protected String ipAddress;
    protected final int port;
    
    protected String currentState = "off";
    protected String currentBrightness = "0";
    protected String currentHexColour = "000000";
    
    /**
     * Abstracted implementation of a LED controller for use with subclasses
     * @param deviceName Name of the LED controller
     * @param ipAddress IP address of the device in IPv4 syntax
     * @param port The listening port of the LED controller
     * @throws LedException Throws an exception if there is an error when connecting to the LED controller
     */
    public GenericLedController(String deviceName, String ipAddress, int port) throws LedException {
        this.deviceName = deviceName;
        this.ipAddress = ipAddress;
        this.port = port;
        
        connect();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return deviceName;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getIpAddress() {
        return ipAddress;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<String, Object> getState() {
        HashMap<String, Object> state = new HashMap<>();
        state.put("state", currentState);
        state.put("brightness", currentBrightness);
        state.put("colour", currentHexColour);
        return state;
    }
    
    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#setColour(int, int, int)
     */
    @Override
    public void setColour(int red, int green, int blue) throws LedException {
        String redHex = toColourHex(red);
        String greenHex = toColourHex(green);
        String blueHex = toColourHex(blue);
        
        setColour(redHex + greenHex + blueHex);
    }
    
    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#setStrobe(int, int, int)
     */
    @Override
    public void setStrobe(int red, int green, int blue) throws LedException {
        String redHex = toColourHex(red);
        String greenHex = toColourHex(green);
        String blueHex = toColourHex(blue);
        
        setStrobe(redHex + greenHex + blueHex);
    }

    /* (non-Javadoc)
     * @see com.lumitron.led.LedController#transitionToColour(java.lang.String, java.lang.String, java.lang.String)
     */
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
        
        while(fromRed != toRed || fromGreen != toGreen || fromBlue != toBlue) {
            fromRed = incrementColour(fromRed, toRed, increment);
            fromGreen = incrementColour(fromGreen, toGreen, increment);
            fromBlue = incrementColour(fromBlue, toBlue, increment);
            setStrobe(fromRed, fromGreen, fromBlue);
            
            try {
                Thread.sleep(Integer.parseInt(pauseInterval));
            } catch (InterruptedException e) {
                AppSystem.log(this.getClass(), "Got interrupted while sleeping during transition");
            }
        }
    }
    
    /**
     * Converts from a 0-255 colour value to a 2 character hex string
     * @param colourValue Integer in the range of 0-255
     * @return Returns a hex representation of the colour in 2 characters
     */
    private String toColourHex(int colourValue) {
        if(colourValue < 0) {
            colourValue = 0;
        } else if(colourValue > 255) {
            colourValue = 255;
        }
        String colourHex = Integer.toHexString(colourValue);
        if(colourHex.length() == 1) {
            colourHex = "0" + colourHex;
        }
        return colourHex;
    }
    
    /**
     * Increments the from digit towards the to digit by the increment specified. Will not increment/decrease when it is the same
     * @param from The starting point to increment
     * @param to The end point of increment
     * @param increment The amount to increment/decrease by, depending on the direction of the to digit. Defaults to 1 if less than 1
     * @return
     */
    private int incrementColour(int from, int to, int increment) {
        if(increment < 1) {
            increment = 1;
        }
        int diff = 0;
        if(from < to) {
            diff = to - from;
            if(diff < increment) {
                from+= diff;
            } else {
                from+= increment;
            }
        } else if(from > to) {
            diff = from - to;
            if(diff < increment) {
                from-= diff;
            } else {
                from-= increment;
            }
        }
        return from;
    }
    
    /**
     * Util method to split any String by length
     * @param string String to split
     * @param lengthToSplitBy The length to split by
     * @return An array of Strings split by the specified length
     */
    private String[] splitByLength(String string, int lengthToSplitBy) {
        return string.split("(?<=\\G.{" + lengthToSplitBy + "})");
    }
    
    /**
     * Sends the command to the controller. Implemented in UDP
     * @param hexCommand The command to send to the controller, in hex encoding
     * @param hasResponse Whether the controller will return a response. 
     * If the code expects a response but no response is given, it will continue to block the thread
     * @return Returns the response from the controller. Returns null if hasResponse is false
     * @throws LedException
     */
    protected String sendUDP(String hexCommand, boolean hasResponse) throws LedException {
        try {
            //Convert the command to bytes
            byte[] sendData = DatatypeConverter.parseHexBinary(hexCommand);
            //Create the UDP packet with the byte data along with destination information
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, Inet4Address.getByName(ipAddress) , port);
            //Fire away
            udpConnection.send(sendPacket);
            
            //Depending on whether the controller returns a response
            if(hasResponse) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                udpConnection.receive(packet);
                buffer = packet.getData();
                
                byte[] data = Arrays.copyOf(buffer, packet.getLength()-1);
                
                
                String responseString = DatatypeConverter.printHexBinary(data);
                AppSystem.log(this.getClass(), deviceName + " responded with " + responseString);
                return responseString;
            }
        }catch (IOException e) {
            AppSystem.log(this.getClass(), "Error sending data " + hexCommand + " to " + deviceName);
            AppSystem.log(this.getClass(), "Failed with error: " + e.getMessage());
            e.printStackTrace();
            throw new LedException(this.getClass().getSimpleName(), "0011", "Unable to send data to device");
        }
        return null;
    }
    
    protected String sendTCP(String hexCommand, boolean hasResponse) throws LedException {
        InputStream in = null;
        OutputStream out = null;
        try {
            //Convert the command to bytes
            byte[] sendData = DatatypeConverter.parseHexBinary(hexCommand);
            //Create the TCP stream with the byte data along with destination information
            in = tcpConnection.getInputStream();
            out = tcpConnection.getOutputStream();
            //Fire away
            out.write(sendData);
            
            if(hasResponse) {
                byte[] data = new byte[1024];
                in.read(data);
                data = trim(data);
                
                String responseString = DatatypeConverter.printHexBinary(data);
                AppSystem.log(this.getClass(), deviceName + " responded with " + responseString);
                return responseString;
            }
        }catch (IOException e) {
            AppSystem.log(this.getClass(), "Error sending data " + hexCommand + " to " + deviceName);
            AppSystem.log(this.getClass(), "Failed with error: " + e.getMessage());
            e.printStackTrace();
            throw new LedException(this.getClass().getSimpleName(), "0011", "Unable to send data to device");
        } finally {
//            try {
//                out.close();
//                in.close();
//                connection.close();
//            } catch (IOException e) {
//                AppSystem.log(this.getClass(), "Unable to close TCP connection for " + deviceName);
//            }
        }
        return null;
    }
    
    private byte[] trim(byte[] bytes){
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0){
            --i;
        }

        return Arrays.copyOf(bytes, i + 1);
    }
}
