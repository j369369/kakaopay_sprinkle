package com.kakaopay.sprinkle.domain.sprinkleGet;

import com.kakaopay.sprinkle.constants.CommonConstants;
import com.kakaopay.sprinkle.domain.sprinkle.Sprinkle;
import com.kakaopay.sprinkle.domain.sprinkle.SprinkleRepository;
import com.kakaopay.sprinkle.util.CommonUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
class SprinkleGetRepositoryTest {


    @Autowired
    SprinkleRepository sprinkleRepository;

    @Autowired
    SprinkleGetRepository getRepository;

    @Autowired
    CommonUtils utils;


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
        getRepository.deleteAll();
    }


    @DisplayName("뿌린금액 리스트 저장 테스트")
    @Test
    @Transactional
    public void sprinkleGetSaveTest(){

        Sprinkle sprinkle = sprinkleRepository.save(Sprinkle.builder()
                .token(token)
                .userId(userId)
                .roomId(roomId)
                .amount(amount)
                .totalCount(totalCount)
                .build());


        long[] dvdList = utils.dvdAmt(amount,totalCount,1);

        for (long l : dvdList){
            sprinkle.addSprinkleGet(SprinkleGet.builder().amount(l).build());
        }


	    sprinkle = sprinkleRepository.findByTokenAndCreatedAtGreaterThan(token,createdAt).get();

        List<SprinkleGet> getList =  sprinkle.getSprinkleGets();

        assertThat(getRepository.findAll()).isEqualTo(getList);
        assertThat(getList.size()).isEqualTo(totalCount);
        assertThat(getList.stream().map(SprinkleGet::getAmount).reduce(0L, Long::sum))
                .isEqualTo(sprinkle.getAmount());

    }
}