package com.kakaopay.sprinkle.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class SprinkleResponseDto {

	private final LocalDateTime sprinkleAt;
	private final long amount;
	private final long gottenAmount;
	private final List<SprinkleGetDto> gottenList;
}
