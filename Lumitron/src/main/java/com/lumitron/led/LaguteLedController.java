package com.lumitron.led;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;

import javax.xml.bind.DatatypeConverter;

import com.lumitron.util.AppSystem;

public class LaguteLedController extends GenericLedController {
    
    private DatagramSocket connection;
    
    private final String HEADER = "7E";
    private final String FOOTER = "00EF";
    
    private final String COMMAND_ON = "040401";
    private final String COMMAND_OFF = "040400";
    
    private final String COLOUR_WHITE = "FFFFFF";
    
    public LaguteLedController(String deviceName, String ipAddress) throws LedException {
        super(deviceName, ipAddress, 5000);
    }
    
    @Override
    public boolean queryStaus() throws LedException {
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
    
    private String send(String hexCommand, boolean hasResponse) throws LedException {
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
