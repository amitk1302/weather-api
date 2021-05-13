package com.weather.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8046617191712822415L;

	private String errorCode;

	private String errorMesage;

	public BadRequestException(String errorCode, String errorMessage) {
		super();

		this.errorCode = errorCode;
		this.errorMesage = errorMessage;

	}
}
