package com.kakaopay.sprinkle.dto;

import com.kakaopay.sprinkle.constants.ResponseCodes;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class ApiResponseDto {
	@NonNull
	private String code;
	@NonNull
	private String message;
	@Setter
	private Object data;

	public static ApiResponseDto get(ResponseCodes code) {
		return new ApiResponseDto(code.code, code.description);
	}

	public static ApiResponseDto get(ResponseCodes code, Object data) {
		ApiResponseDto response = get(code);
		response.setData(data);
		return response;
	}
}
