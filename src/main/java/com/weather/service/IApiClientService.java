package com.weather.service;

import org.springframework.http.ResponseEntity;

import com.weather.request.ApiRequest;

public interface IApiClientService {

	<T> ResponseEntity<?> executeRestApi(ApiRequest<T> apiRequest, Class<?> responseType);
}
