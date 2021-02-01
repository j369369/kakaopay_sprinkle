package com.kakaopay.sprinkle.exception;

import com.kakaopay.sprinkle.constants.ResponseCodes;

public class NotAvailableGetException extends SprinkleException{
    public NotAvailableGetException(ResponseCodes codes) {
        super(codes);
    }
}
