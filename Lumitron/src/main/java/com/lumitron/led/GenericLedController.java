package com.lumitron.led;

import com.lumitron.util.AppSystem;

public abstract class GenericLedController implements LedController {
    
    protected String deviceName;
    protected String ipAddress;
    protected final int port;
    
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

}
