/**
 * 
 */
package com.weather.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author User
 *
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeather {

	private String cnt;

	private List<OpenWeatherDetail> list = new ArrayList<OpenWeather.OpenWeatherDetail>();
	
	private City city;
	

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class OpenWeatherDetail {
		private String dt;
		
		private String dt_txt;
		
		private MainData main;

	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class MainData {
		private String temp;

		private String feels_like;

		private String temp_min;

		private String temp_max;
	}
	
	@Getter
	@Setter
	public static class City {
		private String name;
		
		private String country;
		
	}
}
