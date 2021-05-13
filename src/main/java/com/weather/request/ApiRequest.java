package com.weather.request;

import java.util.Map;

import org.springframework.http.HttpMethod;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ApiRequest<T> {

	private String apiUrl;
	
	private T request;
	
	private Map<String, String> pathParam;
	
	private HttpMethod httpMethod;
	
	
}
