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
        if(red < 0) {
            red = 0;
        }
        if(red > 255) {
            red = 255;
        }
        if(green < 0) {
            green = 0;
        }
        if(green > 255) {
            green = 255;
        }
        if(blue < 0) {
            blue = 0;
        }
        if(blue > 255) {
            blue = 255;
        }
        String redHex = Integer.toHexString(red);
        String greenHex = Integer.toHexString(green);
        String blueHex = Integer.toHexString(blue);
        if(redHex.length() == 1) {
            redHex = "0" + redHex;
        }
        if(greenHex.length() == 1) {
            greenHex = "0" + greenHex;
        }
        if(blueHex.length() == 1) {
            blueHex = "0" + blueHex;
        }
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
