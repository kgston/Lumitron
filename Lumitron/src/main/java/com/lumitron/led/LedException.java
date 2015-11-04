package com.lumitron.led;

import com.lumitron.util.LumitronException;

public class LedException extends LumitronException {
    
    private static final long serialVersionUID = -1816339581698138905L;
    
    public LedException(String originClass, String errorCode, String message) {
        super(originClass, errorCode, message);
    }
    
}
