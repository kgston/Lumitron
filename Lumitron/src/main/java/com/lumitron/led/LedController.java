package com.lumitron.led;


public interface LedController {
    public boolean queryStaus() throws LedException;
    
    public void on() throws LedException;
    
    public void off() throws LedException;
    
    public void connect() throws LedException;
    
    public void disconnect() throws LedException;
    
    public void setColour(int red, int green, int blue) throws LedException;
    
    public void setColour(String hexString) throws LedException;
    
    public void transitionToColour(String pauseInterval, String incrementInterval, String hexColourString) throws LedException;
    
    public void setBrightness(String brightnessLevel) throws LedException;
}
