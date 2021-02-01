package com.kakaopay.sprinkle.controller;


import com.kakaopay.sprinkle.constants.Headers;
import com.kakaopay.sprinkle.constants.ResponseCodes;
import com.kakaopay.sprinkle.domain.sprinkle.Sprinkle;
import com.kakaopay.sprinkle.dto.ApiResponseDto;
import com.kakaopay.sprinkle.dto.SprinkleGetDto;
import com.kakaopay.sprinkle.dto.SprinkleRequestDto;
import com.kakaopay.sprinkle.dto.SprinkleResponseDto;
import com.kakaopay.sprinkle.service.SprinkleGetService;
import com.kakaopay.sprinkle.service.SprinkleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@Api(tags = "뿌리기 API")
@RequestMapping("/v1/sprinkle")
@RequiredArgsConstructor
@RestController
public class SprinkleApiController {

    private final SprinkleService sprinkleService;
    private final SprinkleGetService sprinkleGetService;

    @ApiOperation(value = "생성 API", notes = "뿌릴 금액과 뿌릴 인원을 요청값으로 받아 뿌립니다.")
    @PostMapping
    public ResponseEntity<ApiResponseDto> saveSprinkle(
            @RequestHeader(Headers.USER_ID) long userId,
            @RequestHeader(Headers.ROOM_ID) String roomId,
            @Valid @RequestBody SprinkleRequestDto requestDto) {

        String token = sprinkleService.sprinkle(requestDto.getAmount(), requestDto.getTotalCount(), userId, roomId);

        return new ResponseEntity<>(ApiResponseDto.get(ResponseCodes.SPRINKLE_SUCCESS,token), HttpStatus.CREATED);
    }


    @ApiOperation(value = "받기 API", notes = "토큰을 요청값으로 받아 뿌려진 돈을 받습니다.")
    @PutMapping("/{token}")
    public ResponseEntity<ApiResponseDto> getSprinkle(
            @RequestHeader(Headers.USER_ID) long userId,
            @RequestHeader(Headers.ROOM_ID) String roomId,
            @PathVariable String token) {

        long amount = sprinkleGetService.sprinkleGet(token,userId,roomId);

        return new ResponseEntity<>(ApiResponseDto.get(ResponseCodes.GET_SUCCESS,amount), HttpStatus.OK);
    }

    @ApiOperation(value = "조회 API", notes = "토큰을 요청값으로 받아 뿌리기 정보를 조회 합니다.")
    @GetMapping(value = "/{token}")
    ResponseEntity<ApiResponseDto> selectSprinkle(
            @RequestHeader(Headers.USER_ID) long userId,
            @PathVariable String token) {

        SprinkleResponseDto sprinkle = sprinkleService.selectSprinkle(token,userId);

        return new ResponseEntity<>(ApiResponseDto.get(ResponseCodes.SUCCESS,sprinkle), HttpStatus.OK);
    }


}
