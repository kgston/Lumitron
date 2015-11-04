package com.lumitron.util;

public class LumitronException extends RuntimeException {
    
    private static final long serialVersionUID = -1816339581698138905L;
    
    private String originClass;
    private String errorCode;
    
    public LumitronException(String originClass, String errorCode, String message) {
        super(message);
        this.originClass = originClass;
        this.errorCode = errorCode;
    }
    
    public String getOriginClass() {
        return originClass;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
