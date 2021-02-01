package com.kakaopay.sprinkle.domain.sprinkle;

import com.kakaopay.sprinkle.constants.CommonConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SprinkleRepositoryTest {

    @Autowired
    SprinkleRepository sprinkleRepository;

	long amount;
	int totalCount;
	long userId;
	String roomId;
	String token;
	LocalDateTime createdAt;

	@BeforeEach
	void init(){
		amount = 10000;
		totalCount = 4;
		userId = 1001;
		roomId = "AAABQWE";
		token ="Xab";
		createdAt = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).minusDays(CommonConstants.EXPIRE_READ_DAYS);

	}
	@AfterEach
	public void cleanUp(){
		sprinkleRepository.deleteAll();
	}


	@DisplayName("뿌리기 저장 테스트")
    @Test
    public void sprinkleSaveTest(){

        sprinkleRepository.save(Sprinkle.builder()
                .token(token)
                .userId(userId)
                .roomId(roomId)
                .amount(amount)
                .totalCount(totalCount)
                .build());


        Sprinkle sprinkle = sprinkleRepository.findByTokenAndCreatedAtGreaterThan(token,createdAt).get();
        assertThat(sprinkle.getToken()).isEqualTo(token);
        assertThat(sprinkle.getUserId()).isEqualTo(userId);
        assertThat(sprinkle.getRoomId()).isEqualTo(roomId);

    }

}