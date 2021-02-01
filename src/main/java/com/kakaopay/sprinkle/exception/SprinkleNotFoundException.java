package com.kakaopay.sprinkle.exception;

import com.kakaopay.sprinkle.constants.ResponseCodes;

public class SprinkleNotFoundException extends SprinkleException{
    public SprinkleNotFoundException(ResponseCodes codes) {
        super(codes);
    }
}
