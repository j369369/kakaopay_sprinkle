package com.kakaopay.sprinkle.service;

import com.kakaopay.sprinkle.domain.sprinkle.Sprinkle;
import com.kakaopay.sprinkle.dto.SprinkleResponseDto;

public interface SprinkleService {

	/**
	 * 뿌리기
	 *
	 * @param amount     뿌릴 금액
	 * @param totalCount 받을 인원수
	 * @param userId     사용자 아이디
	 * @param roomId     대화방 아이디
	 * @return {@link Sprinkle} 뿌리기 엔티티
	 */
	String sprinkle(long amount, int totalCount, long userId, String roomId);

	/**
	 * 조회
	 *
	 * @param token  뿌리기 토큰
	 * @param userId 사용자 아이디
	 * @param roomId 대화방 아이디
	 * @return {@link SprinkleResponseDto} 리스폰스 DTO
	 */
	SprinkleResponseDto selectSprinkle(String token, long userId, String roomId);
}
