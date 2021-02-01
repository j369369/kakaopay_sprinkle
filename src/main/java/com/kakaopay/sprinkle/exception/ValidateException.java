package com.kakaopay.sprinkle.exception;

import com.kakaopay.sprinkle.constants.ResponseCodes;

public class ValidateException extends SprinkleException{
    public ValidateException(ResponseCodes codes) {
        super(codes);
    }
}
