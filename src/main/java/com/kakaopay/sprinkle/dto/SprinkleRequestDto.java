package com.kakaopay.sprinkle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SprinkleRequestDto {

	@DecimalMin(value = "1", message = "뿌릴 금액이 1원 이상 이어야 합니다.")
	private long amount;

	@DecimalMin(value = "1", message = "받을 사람이 1명 이상 이어야 합니다.")
	private int totalCount;

}
