package com.kakaopay.sprinkle.exception;

import com.kakaopay.sprinkle.constants.ResponseCodes;

public class DifferentRoomException extends SprinkleException{
    public DifferentRoomException(ResponseCodes codes) {
        super(codes);
    }
}
