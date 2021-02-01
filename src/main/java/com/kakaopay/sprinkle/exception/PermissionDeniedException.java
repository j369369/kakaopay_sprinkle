package com.kakaopay.sprinkle.exception;

import com.kakaopay.sprinkle.constants.ResponseCodes;

public class PermissionDeniedException extends SprinkleException{
    public PermissionDeniedException(ResponseCodes codes) {
        super(codes);
    }
}
