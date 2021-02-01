package com.kakaopay.sprinkle.exception;

import com.kakaopay.sprinkle.constants.ResponseCodes;

public class DuplicateGetException extends SprinkleException{
    public DuplicateGetException(ResponseCodes codes) {
        super(codes);
    }
}
