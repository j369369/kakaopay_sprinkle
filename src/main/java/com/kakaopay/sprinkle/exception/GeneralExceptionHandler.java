package com.kakaopay.sprinkle.exception;


import com.kakaopay.sprinkle.constants.ResponseCodes;
import com.kakaopay.sprinkle.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler({MissingRequestHeaderException.class,MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<ApiResponseDto> handleHeaderExceptions(Exception e) {
        return new ResponseEntity<>(ApiResponseDto.get(ResponseCodes.INVALID_HEADER,e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponseDto> handleParameterExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return new ResponseEntity<>(ApiResponseDto.get(ResponseCodes.INVALID_REQUEST,errors), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(SprinkleNotFoundException.class)
    protected ResponseEntity<ApiResponseDto> handleExceptions(SprinkleNotFoundException e) {
        return new ResponseEntity<>(ApiResponseDto.get(e.codes), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SprinkleException.class)
    protected ResponseEntity<ApiResponseDto> handleExceptions(SprinkleException e) {
        return new ResponseEntity<>(ApiResponseDto.get(e.codes), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponseDto> handle(Exception e) {
        return new ResponseEntity<>(ApiResponseDto.get(ResponseCodes.INVALID_ERROR,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

    }


}