package com.kakaopay.sprinkle.service;

import com.kakaopay.sprinkle.constants.CommonConstants;
import com.kakaopay.sprinkle.domain.sprinkle.Sprinkle;
import com.kakaopay.sprinkle.domain.sprinkle.SprinkleRepository;
import com.kakaopay.sprinkle.domain.sprinkleGet.SprinkleGet;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("받기 서비스 테스트")
@SpringBootTest
class SprinkleGetServiceTest {

	@Autowired
	private SprinkleService sprinkleService;

	@Autowired
	private SprinkleGetService sprinkleGetService;

	@Autowired
	private SprinkleRepository sprinkleRepository;

	long amount;
	int totalCount;
	long userId;
	String roomId;
	LocalDateTime createdAt;
	String token;

	@BeforeEach
	void init() {
		amount = 10000;
		totalCount = 4;
		userId = 1001;
		roomId = "AAABQWE";
		createdAt = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).minusDays(CommonConstants.EXPIRE_READ_DAYS);
		token = sprinkleService.sprinkle(amount, totalCount, userId, roomId);

	}


	@Test
	@DisplayName("할당되지 않은 분배건 하나를 API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.")
	@Transactional
	void sprinkleGetTest001() {
		long other = 1002;

		long amt = sprinkleGetService.sprinkleGet(token, other, roomId);

		Sprinkle sprinkle = sprinkleRepository.findByTokenAndCreatedAtGreaterThan(token, createdAt)
				.orElseThrow(() -> new AssertionError("sprinkleTest failed"));

		assertThat(sprinkle.getSprinkleGets().stream().filter(SprinkleGet::isAlreadyGet).mapToLong(SprinkleGet::getAmount).sum()).isEqualTo(amt);
	}

	@Test
	@DisplayName("뿌리기 당 한 사용자는 한번만 받을 수 있습니다.")
	void sprinkleGetTest002() {
		long other = 1002;

		sprinkleGetService.sprinkleGet(token, other, roomId);

		assertThatThrownBy(() -> sprinkleGetService.sprinkleGet(token, other, roomId))
				.isInstanceOf(DuplicateGetException.class);

	}

	@Test
	@DisplayName("자신이 뿌리기한 건은 자신이 받을 수 없습니다.")
	void sprinkleGetTest003() {
		assertThatThrownBy(() -> sprinkleGetService.sprinkleGet(token, userId, roomId))
				.isInstanceOf(OwnerCannotGetException.class);
	}

	@Test
	@DisplayName("대화방에 속한 사용자만이 받을 수 있습니다.")
	void sprinkleGetTest004() {
		long other = 1002;
		String otherRoom = "VVzxCA";
		assertThatThrownBy(() -> sprinkleGetService.sprinkleGet(token, other, otherRoom))
				.isInstanceOf(DifferentRoomException.class);
	}

	@Test
	@DisplayName("뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다.")
	@Transactional
	void sprinkleGetTest005() {
		long other = 1002;
		Sprinkle sprinkle = sprinkleRepository.findByTokenAndCreatedAtGreaterThan(token, createdAt)
				.orElseThrow(() -> new AssertionError("sprinkleTest failed"));

		sprinkle.setCreatedAt(LocalDateTime.now().minusMinutes(CommonConstants.EXPIRE_GET_MINUTE + 1));

		assertThatThrownBy(() -> sprinkleGetService.sprinkleGet(token, other, roomId))
				.isInstanceOf(ExpiredGetTimeException.class);
	}

	@Test
	@DisplayName("받을수 있는 건이 없으면 실패 응답")
	@Transactional
	void sprinkleGetTest006() {
		long other = 1002;

		sprinkleGetService.sprinkleGet(token, 111, roomId);
		sprinkleGetService.sprinkleGet(token, 222, roomId);
		sprinkleGetService.sprinkleGet(token, 333, roomId);
		sprinkleGetService.sprinkleGet(token, 444, roomId);


		assertThatThrownBy(() -> sprinkleGetService.sprinkleGet(token, other, roomId))
				.isInstanceOf(NotAvailableGetException.class);
	}


}