package com.lumitron.music;

import com.lumitron.util.LumitronException;

public class MusicException extends LumitronException {

    private static final long serialVersionUID = 6582051926821011253L;

    public MusicException(String originClass, String errorCode, String message) {
        super(originClass, errorCode, message);
    }
    
}
