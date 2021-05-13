package com.weather.handler;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.weather.constant.ErrorConstant;
import com.weather.constant.WeatherConstant;
import com.weather.exception.BadGatewayException;
import com.weather.exception.BadRequestException;
import com.weather.exception.GenericException;
import com.weather.response.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BadGatewayException.class)
	@ResponseStatus(value = HttpStatus.BAD_GATEWAY)
	public ErrorResponse handleBadGatwayExeption(BadGatewayException exception, WebRequest request) {

		return buildErrorResponse(exception.getErrorCode(), exception.getErrorMesage());
	}

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorResponse handleBadRequestExeption(BadRequestException exception, WebRequest request) {

		return buildErrorResponse(exception.getErrorCode(), exception.getErrorMesage());
	}

	@ExceptionHandler(GenericException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleGenericExeption(GenericException exception, WebRequest request) {

		return buildErrorResponse(ErrorConstant.GENERICEXCEPTON, WeatherConstant.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorResponse handleConstraintViolationExeption(Exception exception, WebRequest request) {

		String message = exception.getMessage().substring(exception.getMessage().indexOf(": ") + 1);
		return buildErrorResponse(ErrorConstant.API_BAD_REQUST, message.trim());
	}

	private ErrorResponse buildErrorResponse(String errorCode, String errorMessage) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(errorCode);
		errorResponse.setErrorMessage(errorMessage);

		return errorResponse;

	}

}
