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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.weather.exception.BadGatewayException;
import com.weather.exception.GenericException;
import com.weather.model.OpenWeather;
import com.weather.model.OpenWeather.City;
import com.weather.model.OpenWeather.OpenWeatherDetail;
import com.weather.request.ApiRequest;
import com.weather.service.ApiClientServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApiClientTest {

	@InjectMocks
	private ApiClientServiceImpl apiClientService;

	@MockBean
	private RestTemplate restTemplate;

	@SuppressWarnings("unchecked")
	@Test
	public void testExecuteRestApi_Success() {

		ResponseEntity<OpenWeather> response = new ResponseEntity<OpenWeather>(mockOpenWeather(), HttpStatus.OK);

		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class))).thenReturn(response);

		ResponseEntity<?> apiResponse = apiClientService.executeRestApi(buildApiRequest(), OpenWeather.class);
		Assertions.assertNotNull(apiResponse);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExecuteRestApi_NullResponse() {

		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class))).thenReturn(null);

		ResponseEntity<?> apiResponse = apiClientService.executeRestApi(buildApiRequest(), OpenWeather.class);
		Assertions.assertNull(apiResponse);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExecuteRestApi_BadGateway() {

		ResponseEntity<OpenWeather> response = new ResponseEntity<OpenWeather>(mockOpenWeather(),
				HttpStatus.BAD_GATEWAY);

		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class))).thenReturn(response);
		try {
			apiClientService.executeRestApi(buildApiRequest(), OpenWeather.class);
			Assertions.fail();
		} catch (BadGatewayException e) {

		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExecuteRestApi_BadRequestException() {

		ResponseEntity<OpenWeather> response = new ResponseEntity<OpenWeather>(mockOpenWeather(),
				HttpStatus.BAD_GATEWAY);

		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class))).thenReturn(response);

		try {
			apiClientService.executeRestApi(buildApiRequest(), OpenWeather.class);
			Assertions.fail();
		} catch (BadGatewayException e) {
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExecuteRestApi_GenericException() {

		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class),Mockito
				.any(Object.class))).thenThrow(new NullPointerException());

		try {
			apiClientService.executeRestApi(buildApiRequest(), OpenWeather.class);
			Assertions.fail();
		} catch (GenericException e) {
		}
	}
	
	private <T> ApiRequest<T> buildApiRequest() {
		ApiRequest<T> apiRequest = new ApiRequest<>();
		String url = UriComponentsBuilder.fromUriString("http://www.google.com").build().toString();

		apiRequest.setApiUrl(url);
		apiRequest.setHttpMethod(HttpMethod.GET);
		return apiRequest;

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
