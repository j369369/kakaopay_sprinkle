package com.kakaopay.sprinkle.util;

import com.kakaopay.sprinkle.constants.CommonConstants;
import com.kakaopay.sprinkle.constants.ResponseCodes;
import com.kakaopay.sprinkle.domain.sprinkle.Sprinkle;
import com.kakaopay.sprinkle.domain.sprinkleGet.SprinkleGet;
import com.kakaopay.sprinkle.exception.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Component
public class ValidateUtils {


	/**
	 * 토큰 검증 로직
	 */
	public void validateToken(String token) {
		String pattern = "[a-zA-Z]{3}";
		if (!Pattern.matches(pattern, token)) throw new ValidateException(ResponseCodes.INVALID_TOKEN);
	}


	/**
	 * 받기시 검증 로직
	 */
	public void validateGet(Sprinkle sprinkle, long userId, String roomId) {
		if (sprinkle.getUserId().equals(userId)) throw new OwnerCannotGetException(ResponseCodes.OWNER_CANNOT_GET);
		if (!sprinkle.getRoomId().equals(roomId)) throw new DifferentRoomException(ResponseCodes.DIFFERENT_ROOM);
		if (sprinkle.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(CommonConstants.EXPIRE_GET_MINUTE)))
			throw new ExpiredGetTimeException(ResponseCodes.EXPIRED_GET_TIME);
		if (sprinkle.getSprinkleGets().stream()
				.filter(SprinkleGet::isAlreadyGet)
				.anyMatch(get -> get.getUserId().equals(userId)))
			throw new DuplicateGetException(ResponseCodes.DUPLICATE_GET);
	}

	/**
	 * 조회시 검증 로직
	 */
	public void validateSelect(Sprinkle sprinkle, long userId, String roomId) {
		if (!sprinkle.getUserId().equals(userId))
			throw new PermissionDeniedException(ResponseCodes.SELECT_PERMISSION_DENIED);
		if (!sprinkle.getRoomId().equals(roomId)) throw new DifferentRoomException(ResponseCodes.DIFFERENT_ROOM);
		if (sprinkle.getCreatedAt().isBefore(LocalDateTime.now().minusDays(CommonConstants.EXPIRE_READ_DAYS)))
			throw new SprinkleException(ResponseCodes.SELECT_NOT_FOUND);

	}
}
