package com.weather.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WeatherResponse {

	private String city;
	
	private String country;
	
	private List<Weather> weatherList = new ArrayList<>();
	
	
	@Getter
	@Setter
	public static class Weather {
		private String temprature;
		
		private String minTemprature;
		
		private String maxTemprature;
		
		private String dateTime;
		
		
	}
}
