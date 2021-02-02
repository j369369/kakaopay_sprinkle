package com.kakaopay.sprinkle.service;

import com.kakaopay.sprinkle.constants.CommonConstants;
import com.kakaopay.sprinkle.constants.ResponseCodes;
import com.kakaopay.sprinkle.domain.sprinkle.Sprinkle;
import com.kakaopay.sprinkle.domain.sprinkle.SprinkleRepository;
import com.kakaopay.sprinkle.domain.sprinkleGet.SprinkleGet;
import com.kakaopay.sprinkle.exception.NotAvailableGetException;
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
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SprinkleGetServiceImpl implements SprinkleGetService {

	private final SprinkleRepository sprinkleRepository;
	private final ValidateUtils validateUtils;

	@Override
	@Transactional
	public long sprinkleGet(String token, long userId, String roomId) {
		validateUtils.validateToken(token);

		LocalDateTime createdAt = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).minusDays(CommonConstants.EXPIRE_READ_DAYS);
		Sprinkle sprinkle = sprinkleRepository.findByTokenAndCreatedAtGreaterThan(token, createdAt)
				.orElseThrow(() -> new SprinkleNotFoundException(ResponseCodes.SELECT_NOT_FOUND));

		validateUtils.validateGet(sprinkle, userId, roomId);

		List<SprinkleGet> list = sprinkle.getSprinkleGets().stream().filter(SprinkleGet::isNotGet).collect(Collectors.toList());

		if (list.size() == 0) throw new NotAvailableGetException(ResponseCodes.NOT_AVAILABLE_GET);
		Random random = new Random();
		SprinkleGet get = list.get(random.nextInt(list.size()));

		get.setUserId(userId);

		return get.getAmount();
	}


}
