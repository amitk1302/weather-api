package com.weather.exception;

import lombok.Getter;

@Getter
public class BadGatewayException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8690429914764246588L;

	private String errorCode;
	
	private  String errorMesage;
	
	public BadGatewayException(String errorCode, String errorMessage) {
		super();

		this.errorCode = errorCode;
		this.errorMesage = errorMessage;

	}
}
