package com.kakaopay.sprinkle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.sprinkle.constants.CommonConstants;
import com.kakaopay.sprinkle.constants.Headers;
import com.kakaopay.sprinkle.constants.ResponseCodes;
import com.kakaopay.sprinkle.dto.SprinkleRequestDto;
import com.kakaopay.sprinkle.service.SprinkleGetService;
import com.kakaopay.sprinkle.service.SprinkleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("API 테스트")@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SprinkleApiControllerTest {

	@Autowired
	private SprinkleService sprinkleService;

	@Autowired
	private SprinkleGetService sprinkleGetService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	long amount;
	int totalCount;
	long userId;
	String roomId;

	@BeforeEach
	void init() {
		amount = 10000;
		totalCount = 4;
		userId = 1001;
		roomId = "AAABQWE";
	}

	@Test
	@DisplayName("뿌리기 API")
	public void saveSprinkle() throws Exception {

		SprinkleRequestDto requestDto = new SprinkleRequestDto(amount, totalCount);

		ResultActions actions = mockMvc.perform(
				post("/v1/sprinkle")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(Headers.USER_ID, userId)
						.header(Headers.ROOM_ID, roomId)
						.content(objectMapper.writeValueAsBytes(requestDto))
		);

		actions
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("code", is(ResponseCodes.SPRINKLE_SUCCESS.code)))
				.andExpect(jsonPath("message", is(ResponseCodes.SPRINKLE_SUCCESS.description)))
				.andExpect(jsonPath("data").isNotEmpty());
	}

	@Test
	@DisplayName("받기 API")
	public void getSprinkle() throws Exception {

		String token = sprinkleService.sprinkle(amount, totalCount, userId, roomId);

		long otherId = 1002;

		ResultActions actions = mockMvc.perform(
				put("/v1/sprinkle/{token}", token)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(Headers.USER_ID, otherId)
						.header(Headers.ROOM_ID, roomId)
		);

		actions
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("code", is(ResponseCodes.GET_SUCCESS.code)))
				.andExpect(jsonPath("message", is(ResponseCodes.GET_SUCCESS.description)))
				.andExpect(jsonPath("data").isNotEmpty());
	}

	@Test
	@DisplayName("조회 API")
	public void selectSprinkle() throws Exception {

		String token = sprinkleService.sprinkle(amount, totalCount, userId, roomId);

		long otherId = 1002;
		long getAmt = sprinkleGetService.sprinkleGet(token, otherId, roomId);


		ResultActions actions = mockMvc.perform(
				get("/v1/sprinkle/{token}", token)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(Headers.USER_ID, userId)
		);

		actions
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("code", is(ResponseCodes.SUCCESS.code)))
				.andExpect(jsonPath("message", is(ResponseCodes.SUCCESS.description)))
				.andExpect(jsonPath("data").isNotEmpty())
				.andExpect(jsonPath("data.amount").value(amount))
				.andExpect(jsonPath("data.gottenAmount").value(getAmt))
				.andExpect(jsonPath("data.gottenList[0].amount").value(getAmt))
				.andExpect(jsonPath("data.gottenList[0].userId").value(otherId));
	}
}