package com.kakaopay.sprinkle.exception;

import com.kakaopay.sprinkle.constants.ResponseCodes;

public class OwnerCannotGetException extends SprinkleException{
    public OwnerCannotGetException(ResponseCodes codes) {
        super(codes);
    }
}
