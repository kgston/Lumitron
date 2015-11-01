package com.lumitron.util;

public class LumitronException extends Exception {
    
    private static final long serialVersionUID = -1816339581698138905L;
    
    private String errorCode;
    
    public LumitronException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
