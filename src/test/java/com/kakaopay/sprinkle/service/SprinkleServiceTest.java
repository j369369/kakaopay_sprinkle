package com.kakaopay.sprinkle.service;

import com.kakaopay.sprinkle.constants.CommonConstants;
import com.kakaopay.sprinkle.constants.ResponseCodes;
import com.kakaopay.sprinkle.domain.sprinkle.Sprinkle;
import com.kakaopay.sprinkle.domain.sprinkle.SprinkleRepository;
import com.kakaopay.sprinkle.domain.sprinkleGet.SprinkleGet;
import com.kakaopay.sprinkle.dto.SprinkleRequestDto;
import com.kakaopay.sprinkle.dto.SprinkleResponseDto;
import com.kakaopay.sprinkle.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("뿌리기 & 조회 서비스 테스트")
@SpringBootTest
class SprinkleServiceTest {

	@Autowired
	private SprinkleService sprinkleService;

	@Autowired
	private SprinkleRepository sprinkleRepository;

	long amount;
	int totalCount;
	long userId;
	String roomId;
	LocalDateTime createdAt;

	@BeforeEach
	void init() {
		amount = 10000;
		totalCount = 4;
		userId = 1001;
		roomId = "AAABQWE";
		createdAt = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).minusDays(CommonConstants.EXPIRE_READ_DAYS);

	}

	@Test
	@DisplayName("금액, 인원을 요청값으로 받고 금액은 인원수 이상이어야 합니다.")
	void sprinkleTest001() {

		amount = 2;
		totalCount = 3;

		assertThatThrownBy(() -> sprinkleService.sprinkle(amount, totalCount, userId, roomId))
				.isInstanceOf(ValidateException.class);
	}


	@Test
	@DisplayName("뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다.")
	void sprinkleTest002() {

		String token = sprinkleService.sprinkle(amount, totalCount, userId, roomId);


		Sprinkle sprinkle = sprinkleRepository.findByTokenAndCreatedAtGreaterThan(token, createdAt)
				.orElseThrow(() -> new AssertionError("sprinkleTest failed"));

		assertThat(token).isNotEmpty().hasSize(CommonConstants.TOKEN_LENGTH);
		assertThat(sprinkle.getAmount()).isEqualTo(amount);
		assertThat(sprinkle.getTotalCount()).isEqualTo(totalCount);
		assertThat(sprinkle.getUserId()).isEqualTo(userId);
		assertThat(sprinkle.getRoomId()).isEqualTo(roomId);
	}

	@Test
	@DisplayName("뿌릴 금액을 인원수에 맞게 분배하여 저장합니다.")
	@Transactional
	void sprinkleTest003() {

		String token = sprinkleService.sprinkle(amount, totalCount, userId, roomId);


		Sprinkle sprinkle = sprinkleRepository.findByTokenAndCreatedAtGreaterThan(token, createdAt)
				.orElseThrow(() -> new AssertionError("sprinkleTest failed"));

		assertThat(sprinkle.getSprinkleGets().size()).isEqualTo(totalCount);
		assertThat(sprinkle.getSprinkleGets().stream().map(SprinkleGet::getAmount).reduce(0L, Long::sum))
				.isEqualTo(amount);
	}

	@Test
	@DisplayName("조회를 요청하고 token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다.")
	void sprinkleTest004() {

		String token = sprinkleService.sprinkle(amount, totalCount, userId, roomId);

		SprinkleResponseDto sprinkle = sprinkleService.selectSprinkle(token, userId, roomId);

		assertThat(sprinkle.getSprinkleAt()).isNotNull();
		assertThat(sprinkle.getAmount()).isEqualTo(amount);
		assertThat(sprinkle.getGottenAmount()).isEqualTo(0);
		assertThat(sprinkle.getGottenList()).hasSize(0);
	}

	@Test
	@DisplayName("뿌린 사람 자신만 조회를 할 수 있습니다.")
	void sprinkleTest005() {
		long other = 1002;
		String token = sprinkleService.sprinkle(amount, totalCount, userId, roomId);

		assertThatThrownBy(() -> sprinkleService.selectSprinkle(token, other, roomId))
				.isInstanceOf(PermissionDeniedException.class);
	}

	@Test
	@DisplayName("뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.")
	@Transactional
	void sprinkleTest006() {
		String token = sprinkleService.sprinkle(amount, totalCount, userId, roomId);

		Sprinkle sprinkle = sprinkleRepository.findByTokenAndCreatedAtGreaterThan(token, createdAt)
				.orElseThrow(() -> new AssertionError("sprinkleTest failed"));

		sprinkle.setCreatedAt(LocalDateTime.now().minusDays(CommonConstants.EXPIRE_READ_DAYS + 1));

		assertThatThrownBy(() -> sprinkleService.selectSprinkle(token, userId, roomId))
				.isInstanceOf(SprinkleNotFoundException.class);
	}

}