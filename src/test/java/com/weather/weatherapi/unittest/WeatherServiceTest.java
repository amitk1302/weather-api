package com.weather.weatherapi.unittest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.weather.exception.GenericException;
import com.weather.model.OpenWeather;
import com.weather.model.OpenWeather.City;
import com.weather.model.OpenWeather.OpenWeatherDetail;
import com.weather.request.ApiRequest;
import com.weather.response.WeatherResponse;
import com.weather.service.IApiClientService;
import com.weather.service.WeatherServiceimpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherServiceTest {

	@InjectMocks
	private WeatherServiceimpl weatherService;
	
	@MockBean
	private IApiClientService apiClientService;
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetNextDayForcast_Success() {
		weatherService.setApiUrl("http://api.openweathermap.org/data/2.5/forecast");
		weatherService.setApiKey("34223423424234");
		ResponseEntity<OpenWeather> response =  new ResponseEntity<OpenWeather>(mockOpenWeather(), HttpStatus.OK);
		
		Mockito.when(apiClientService.executeRestApi(Mockito.any(ApiRequest.class), Mockito.any(Class.class))).thenReturn(response);
		
		WeatherResponse weatherResponse = weatherService.getNextDayForcast("44224");
		Assertions.assertNotNull(weatherResponse);
		Assertions.assertEquals(1, weatherResponse.getWeatherList().size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetNextDayForcast_GenericExeption() {
		ResponseEntity<OpenWeather> response = new ResponseEntity<OpenWeather>(mockOpenWeather(), HttpStatus.OK);

		Mockito.when(apiClientService.executeRestApi(Mockito.any(ApiRequest.class), Mockito.any(Class.class)))
				.thenReturn(response);
		try {
			 weatherService.getNextDayForcast("44224");
			Assertions.fail();
		} catch (GenericException e) {

		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetNextDayForcast_NullResponse() {
		weatherService.setApiUrl("http://api.openweathermap.org/data/2.5/forecast");
		weatherService.setApiKey("34223423424234");

		Mockito.when(apiClientService.executeRestApi(Mockito.any(ApiRequest.class), Mockito.any(Class.class)))
				.thenReturn(null);
		
		WeatherResponse weatherResponse = weatherService.getNextDayForcast("44224");
		Assertions.assertNull(weatherResponse);
	}
	
	private OpenWeather mockOpenWeather() {
		OpenWeather openWeather = new OpenWeather();
		openWeather.setCity(new City());
		OpenWeatherDetail weatherDetail = new OpenWeatherDetail();
		weatherDetail.setDt_txt("2020-04-11");
		openWeather.getList().add(weatherDetail);
		return openWeather;
	}
}
