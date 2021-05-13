package com.weather.weatherapi.unittest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.weather.controller.WeatherController;
import com.weather.exception.GenericException;
import com.weather.response.WeatherResponse;
import com.weather.service.IWeatherService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@InjectMocks
	private WeatherController controller;
	
	@MockBean
	private IWeatherService weatherService;
	
	@Test
	public void testGetNextDayForcast_Success() throws Exception {
		Mockito.when(weatherService.getNextDayForcast(Mockito.anyString())).thenReturn(mockWeatherResponse());
		
		mockMvc.perform(MockMvcRequestBuilders.get("/weather/forcast/nextday").param("zipcode", "90011"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.city").value("California"));
		
	}
	
	@Test
	public void testGetNextDayForcast_InvalidZipcode() throws Exception {
		Mockito.when(weatherService.getNextDayForcast(Mockito.anyString())).thenReturn(mockWeatherResponse());
		
		mockMvc.perform(MockMvcRequestBuilders.get("/weather/forcast/nextday").param("zipcode", "90#@011"))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Invalid ZipCode"));
		
	}
	
	@Test
	public void testGetNextDayForcast_NoContent() throws Exception {
		Mockito.when(weatherService.getNextDayForcast(Mockito.anyString())).thenReturn(null);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/weather/forcast/nextday").param("zipcode", "90011"))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
		
		
	}

	@Test
	public void testGetNextDayForcast_InternalServerError() throws Exception {
		Mockito.when(weatherService.getNextDayForcast(Mockito.anyString())).thenThrow(new GenericException());
		
		mockMvc.perform(MockMvcRequestBuilders.get("/weather/forcast/nextday").param("zipcode", "90011"))
		.andExpect(MockMvcResultMatchers.status().isInternalServerError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Internal Server Error"));
		
		
	}

	private WeatherResponse mockWeatherResponse() {
		WeatherResponse weatherResponse = new WeatherResponse();
		
		weatherResponse.setCity("California");
		
		return weatherResponse;
	}
}
