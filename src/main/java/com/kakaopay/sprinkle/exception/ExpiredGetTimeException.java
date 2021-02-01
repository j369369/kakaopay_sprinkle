package com.kakaopay.sprinkle.exception;

import com.kakaopay.sprinkle.constants.ResponseCodes;

public class ExpiredGetTimeException extends SprinkleException{
    public ExpiredGetTimeException(ResponseCodes codes) {
        super(codes);
    }
}
