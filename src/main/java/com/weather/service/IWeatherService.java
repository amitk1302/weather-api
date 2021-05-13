package com.weather.service;

import com.weather.response.WeatherResponse;

public interface IWeatherService {

	public WeatherResponse getNextDayForcast(String zipCode);
}
