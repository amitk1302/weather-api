package com.weather.weatherapi.integrationtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherApiIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@Test
	public void weatherTest_Success() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/weather/forcast/nextday").param("zipcode", "90011"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Los Angeles"));
	}
	
	@Test
	public void weatherTest_InvalidZipCode() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/weather/forcast/nextday").param("zipcode", "90#@011"))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Invalid ZipCode"));
	}

}
