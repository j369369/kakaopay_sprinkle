package com.kakaopay.sprinkle.exception;

import com.kakaopay.sprinkle.constants.ResponseCodes;

public class SprinkleException extends RuntimeException{
    ResponseCodes codes ;

    public SprinkleException(ResponseCodes codes) {
        this.codes = codes;
    }
}
