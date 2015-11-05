package com.lumitron.led;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;

import javax.xml.bind.DatatypeConverter;

import com.lumitron.util.AppSystem;

public abstract class GenericLedController implements LedController {
    
    protected String deviceName;
    
    protected String ipAddress;
    protected final int port;
    protected DatagramSocket connection;
    
    protected String currentHexColour = "";
    
    public GenericLedController(String deviceName, String ipAddress, int port) throws LedException {
        this.deviceName = deviceName;
        this.ipAddress = ipAddress;
        this.port = port;
        
        connect();
        if(queryStatus()) {
            AppSystem.log(this.getClass(), "Connected!");
        } else {
            AppSystem.log(this.getClass(), "No response from device");
        }
    }
    
    @Override
    public void connect() throws LedException {
        AppSystem.log(this.getClass(), "Connecting to " + deviceName);
        try {
            if(connection == null) {
                connection = new DatagramSocket();
                AppSystem.log(this.getClass(), "Connected!");
            } else {
                AppSystem.log(this.getClass(), "Already connected!");
            }
        } catch (SocketException e) {
            AppSystem.log(this.getClass(), "Failed to connect! " + e.getMessage());
            throw new LedException(this.getClass().getSimpleName(), "0012", "Unable to connect to device");
        }
    }
    
    @Override
    public void disconnect() throws LedException {
        off();
        AppSystem.log(this.getClass(), "Disconnecting from " + deviceName);
        if(connection != null) {
            connection.close();
            connection = null;
        }
    }
    
    @Override
    public void setColour(int red, int green, int blue) throws LedException {
        String redHex = toColourHex(red);
        String greenHex = toColourHex(green);
        String blueHex = toColourHex(blue);
        
        setColour(redHex + greenHex + blueHex);
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
        
        while(fromRed != toRed && fromGreen != toGreen && fromBlue != toBlue) {
            fromRed = incrementColour(fromRed, toRed, increment);
            fromGreen = incrementColour(fromGreen, toGreen, increment);
            fromBlue = incrementColour(fromBlue, toBlue, increment);
            setColour(fromRed, fromGreen, fromBlue);
            
            try {
                Thread.sleep(Integer.parseInt(pauseInterval));
            } catch (InterruptedException e) {
                AppSystem.log(this.getClass(), "Got interrupted while sleeping during transition");
            }
        }
    }
    
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
    
    private int incrementColour(int from, int to, int increment) {
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
    
    private String[] splitByLength(String string, int lengthToSplitBy) {
        return string.split("(?<=\\G.{" + lengthToSplitBy + "})");
    }
    
    protected String send(String hexCommand, boolean hasResponse) throws LedException {
        try {
            //Convert the command to bytes
            byte[] sendData = DatatypeConverter.parseHexBinary(hexCommand);
            //Create the UDP packet with the byte data along with destination information
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, Inet4Address.getByName(ipAddress) , port);
            //Fire away
            connection.send(sendPacket);
            
            //This controller never returns a response
            // byte[] response = new byte[1024];
            // DatagramPacket receivePacket = new DatagramPacket(response, response.length);
            // client.receive(receivePacket);
            // response = receivePacket.getData();
            
            // String responseString = DatatypeConverter.printHexBinary(response);
            // System.out.println("Server says " + responseString);
        }catch (IOException e) {
            AppSystem.log(this.getClass(), "Error sending data " + hexCommand + " to " + deviceName);
            AppSystem.log(this.getClass(), "Failed with error: " + e.getMessage());
            e.printStackTrace();
            throw new LedException(this.getClass().getSimpleName(), "0011", "Unable to send data to device");
        }
        return null;
    }
}
