package com.kakaopay.sprinkle.constants;

public enum ResponseCodes {

	// 200
	SUCCESS("SUCCESS", "조회 완료 되었습니다."),
	SPRINKLE_SUCCESS("SPRINKLE_SUCCESS", "뿌리기가 완료 되었습니다."),
	GET_SUCCESS("GET_SUCCESS", "받기가 완료 되었습니다."),

	// 500
	INVALID_ERROR("INVALID_ERROR", "예상치 못한 오류가 발생하였습니다."),
	TOKEN_GENERATE_ERROR("TOKEN_GENERATE_ERROR", "TOKEN 발급에 실패하였습니다."),

	// 400
	DUPLICATE_GET("DUPLICATE_GET", "이미 받으셨습니다. 받기는 한번씩만 가능합니다."),
	SELECT_NOT_FOUND("SELECT_NOT_FOUND", "조회 기간이 지났거나 데이터가 존재하지 않습니다."),
	OWNER_CANNOT_GET("OWNER_CANNOT_GET", "본인이 뿌린 것은 본인이 받을 수 없습니다."),
	DIFFERENT_ROOM("OWNER_CANNOT_GET", "대화방에 참여한 인원만 받을수 있습니다."),
	AMT_OVER_THEN_COUNT("AMT_OVER_THEN_COUNT", "뿌릴 금액은 뿌릴 인원 이상이어야 합니다."),
	EXPIRED_GET_TIME("EXPIRED_GET_TIME", "받을수 있는 시간이 마감되었습니다. 다음 기회에!"),
	NOT_AVAILABLE_GET("NOT_AVAILABLE_GET", "받을수 있는 갯수가 마감되었습니다. 다음 기회에!"),
	INVALID_REQUEST("INVALID_REQUEST", "요청값 형식이 잘못되었습니다."),
	INVALID_TOKEN("INVALID_TOKEN", "토큰 형식이 잘못되었습니다."),
	INVALID_HEADER("INVALID_HEADER", "헤더 형식이 잘못되었습니다."),

	// 403
	SELECT_PERMISSION_DENIED("SELECT_PERMISSION_DENIED", "내역 조회 권한 없음");

	public final String code;
	public final String description;

	ResponseCodes(String code, String description) {
		this.code = code;
		this.description = description;
	}
}
