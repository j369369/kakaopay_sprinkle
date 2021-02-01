package com.kakaopay.sprinkle.service;

import com.kakaopay.sprinkle.constants.CommonConstants;
import com.kakaopay.sprinkle.constants.ResponseCodes;
import com.kakaopay.sprinkle.domain.sprinkle.Sprinkle;
import com.kakaopay.sprinkle.domain.sprinkle.SprinkleRepository;
import com.kakaopay.sprinkle.domain.sprinkleGet.SprinkleGet;
import com.kakaopay.sprinkle.dto.SprinkleGetDto;
import com.kakaopay.sprinkle.dto.SprinkleResponseDto;
import com.kakaopay.sprinkle.exception.SprinkleException;
import com.kakaopay.sprinkle.exception.SprinkleNotFoundException;
import com.kakaopay.sprinkle.exception.ValidateException;
import com.kakaopay.sprinkle.util.CommonUtils;
import com.kakaopay.sprinkle.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SprinkleServiceImpl implements SprinkleService {

    private final SprinkleRepository sprinkleRepository;
    private final CommonUtils utils;
	private final ValidateUtils validateUtils;


    @Transactional
    @Override
    public String sprinkle(long amount, int totalCount, long userId, String roomId) {
        if (amount < totalCount) {
            throw new ValidateException(ResponseCodes.AMT_OVER_THEN_COUNT);
        }

        Sprinkle sprinkle = Sprinkle.builder()
                .token(generateToken())
                .userId(userId)
                .roomId(roomId)
                .amount(amount)
                .totalCount(totalCount)
                .build();


        long[] list = utils.dvdAmt(amount, totalCount, 1);

        for (long amt : list) {
            sprinkle.addSprinkleGet(SprinkleGet.builder().amount(amt).build());
        }

        return sprinkleRepository.save(sprinkle).getToken();
    }

    @Transactional(readOnly = true)
    @Override
    public SprinkleResponseDto selectSprinkle(String token, long userId) {
	    validateUtils.validateToken(token);
	    LocalDateTime createdAt = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).minusDays(CommonConstants.EXPIRE_READ_DAYS);

	    Sprinkle sprinkle = sprinkleRepository.findByTokenAndCreatedAtGreaterThan(token,createdAt)
                .orElseThrow(() -> new SprinkleNotFoundException(ResponseCodes.SELECT_NOT_FOUND));

	    validateUtils.validateSelect(sprinkle,userId);

        List<SprinkleGet> list = sprinkle.getSprinkleGets().stream().filter(SprinkleGet::isAlreadyGet).collect(Collectors.toList());

        return new SprinkleResponseDto(
                sprinkle.getCreatedAt(),
                sprinkle.getAmount(),
                list.stream().mapToLong(SprinkleGet::getAmount).sum(),
                list.stream().map(el -> new SprinkleGetDto(el.getAmount(), el.getUserId()))
                        .collect(Collectors.toList())
        );
    }


    // 조회기간이 지난 토큰 재사용
    private String generateToken() {
        String token;
        LocalDateTime createdAt = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).minusDays(CommonConstants.EXPIRE_READ_DAYS);
        try {
            while (true) {
                token = utils.generateToken();
                if (!sprinkleRepository.findByTokenAndCreatedAtGreaterThan(token, createdAt).isPresent()) break;
            }
        }catch (Exception e){
            throw new SprinkleException(ResponseCodes.TOKEN_GENERATE_ERROR);
        }
        return token;
    }
}
