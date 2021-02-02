package com.kakaopay.sprinkle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.sprinkle.constants.Headers;
import com.kakaopay.sprinkle.constants.ResponseCodes;
import com.kakaopay.sprinkle.dto.SprinkleRequestDto;
import com.kakaopay.sprinkle.service.SprinkleGetService;
import com.kakaopay.sprinkle.service.SprinkleService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("검증 테스트")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ValidateTest {

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
	@DisplayName("헤더 검증 (회원 아이디 누락)")
	public void HeaderValidate001() throws Exception {

		SprinkleRequestDto requestDto = new SprinkleRequestDto(amount, totalCount);

		ResultActions actions = mockMvc.perform(
				post("/v1/sprinkle")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(Headers.ROOM_ID, roomId)
						.content(objectMapper.writeValueAsBytes(requestDto))
		);

		actions
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("code", is(ResponseCodes.INVALID_HEADER.code)))
				.andExpect(jsonPath("message", is(ResponseCodes.INVALID_HEADER.description)))
				.andExpect(jsonPath("data", Matchers.containsString(Headers.USER_ID)));
	}

	@Test
	@DisplayName("헤더 검증 (대화방 아이디 누락)")
	public void HeaderValidate002() throws Exception {

		SprinkleRequestDto requestDto = new SprinkleRequestDto(amount, totalCount);

		ResultActions actions = mockMvc.perform(
				post("/v1/sprinkle")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(Headers.USER_ID, userId)
						.content(objectMapper.writeValueAsBytes(requestDto))
		);

		actions
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("code", is(ResponseCodes.INVALID_HEADER.code)))
				.andExpect(jsonPath("message", is(ResponseCodes.INVALID_HEADER.description)))
				.andExpect(jsonPath("data", Matchers.containsString(Headers.ROOM_ID)));
	}

	@Test
	@DisplayName("헤더 값 검증")
	public void HeaderValidate003() throws Exception {

		SprinkleRequestDto requestDto = new SprinkleRequestDto(amount, totalCount);

		ResultActions actions = mockMvc.perform(
				post("/v1/sprinkle")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(Headers.USER_ID, "a")
						.header(Headers.ROOM_ID, 1)
						.content(objectMapper.writeValueAsBytes(requestDto))
		);

		actions
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("code", is(ResponseCodes.INVALID_HEADER.code)))
				.andExpect(jsonPath("message", is(ResponseCodes.INVALID_HEADER.description)));
	}

	@Test
	@DisplayName("요청 값 검증")
	public void paramValidate001() throws Exception {

		SprinkleRequestDto requestDto = new SprinkleRequestDto(-1, 0);

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
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("code", is(ResponseCodes.INVALID_REQUEST.code)))
				.andExpect(jsonPath("message", is(ResponseCodes.INVALID_REQUEST.description)))
				.andExpect(jsonPath("data", Matchers.hasKey("amount")))
				.andExpect(jsonPath("data", Matchers.hasKey("totalCount")));
	}
/*
	@Test
	@DisplayName("받기 API")
	public void getSprinkle() throws Exception {

		String token = sprinkleService.sprinkle(amount,totalCount,userId,roomId);

		long otherId = 1002;

		ResultActions actions = mockMvc.perform(
				put("/v1/sprinkle/{token}",token)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(Headers.USER_ID, otherId)
						.header(Headers.ROOM_ID, roomId)
		);

		actions
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("code",is(ResponseCodes.GET_SUCCESS.code)))
				.andExpect(jsonPath("message",is(ResponseCodes.GET_SUCCESS.description)))
				.andExpect(jsonPath("data").isNotEmpty());
	}
	@Test
	@DisplayName("조회 API")
	public void selectSprinkle() throws Exception {

		String token = sprinkleService.sprinkle(amount,totalCount,userId,roomId);

		long otherId = 1002;
		long getAmt = sprinkleGetService.sprinkleGet(token,otherId,roomId);


		ResultActions actions = mockMvc.perform(
				get("/v1/sprinkle/{token}",token)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(Headers.USER_ID, userId)
		);

		actions
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("code",is(ResponseCodes.SUCCESS.code)))
				.andExpect(jsonPath("message",is(ResponseCodes.SUCCESS.description)))
				.andExpect(jsonPath("data").isNotEmpty())
				.andExpect(jsonPath("data.amount").value(amount))
				.andExpect(jsonPath("data.gottenAmount").value(getAmt))
				.andExpect(jsonPath("data.gottenList[0].amount").value(getAmt))
				.andExpect(jsonPath("data.gottenList[0].userId").value(otherId));
	}*/
}