package com.kakaopay.sprinkle.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SprinkleGetDto {
    private final long amount;
    private final long userId;
}
