/**
 * 
 */
package com.weather.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.util.UriComponentsBuilder;

import com.weather.exception.GenericException;
import com.weather.model.OpenWeather;
import com.weather.model.OpenWeather.City;
import com.weather.model.OpenWeather.MainData;
import com.weather.model.OpenWeather.OpenWeatherDetail;
import com.weather.request.ApiRequest;
import com.weather.response.WeatherResponse;
import com.weather.response.WeatherResponse.Weather;

import lombok.Getter;
import lombok.Setter;

/**
 * @author User
 *
 */
@Getter
@Setter
@Service
public class WeatherServiceimpl implements IWeatherService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IApiClientService apiClientService;

	@Value("${api.url}")
	private String apiUrl;

	@Value("${api.key}")
	private String apiKey;

	@Override
	public WeatherResponse getNextDayForcast(String zipCode) {
		logger.debug("Calling open weather api");
		ApiRequest<?> apiRequest = buildApiRequest(zipCode);
		StopWatch stopWatch = new StopWatch("Open Weather api");
		stopWatch.start();
		@SuppressWarnings("unchecked")
		ResponseEntity<OpenWeather> openWeather = (ResponseEntity<OpenWeather>) apiClientService
				.executeRestApi(apiRequest, OpenWeather.class);

		stopWatch.stop();
		logger.debug("Open Weather api response time = {} ms", stopWatch.getTotalTimeMillis());

		if (null == openWeather || null == openWeather.getBody())
			return null;

		return buildWeatherResponse(openWeather.getBody(), zipCode);
	}

	private WeatherResponse buildWeatherResponse(OpenWeather openWeather, String zipCode) {
		logger.debug("building open weather response");

		List<OpenWeatherDetail> openWeatherDetails = openWeather.getList();

		WeatherResponse weatherResponse = new WeatherResponse();
		Optional<City> city = Optional.ofNullable(openWeather.getCity());
		weatherResponse.setCity(city.isPresent() ? city.get().getName() : null);
		weatherResponse.setCountry(city.isPresent() ? city.get().getCountry() : null);

		openWeatherDetails.stream().forEach(e -> {
			if (isTomorrowsDate(e.getDt_txt())) {

				Weather weather = new Weather();
				weather.setDateTime(e.getDt_txt());
				Optional<MainData> mainDataOptional = Optional.ofNullable(e.getMain());

				if (mainDataOptional.isPresent()) {
					MainData mainData = mainDataOptional.get();
					weather.setMaxTemprature(mainData.getTemp_max());
					weather.setMinTemprature(mainData.getTemp_min());
					weather.setTemprature(mainData.getTemp());
				}
				weatherResponse.getWeatherList().add(weather);
			}
		});

		return weatherResponse;
	}

	private boolean isTomorrowsDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDate inputDate = LocalDate.parse(date, formatter);

		LocalDate localDate = LocalDate.now(ZoneId.of("GMT-5"));
		localDate = localDate.plusDays(1);
		return localDate.compareTo(inputDate) == 0 ? true : false;
	}

	private <T> ApiRequest<T> buildApiRequest(String zipCode) {
		try {
			ApiRequest<T> apiRequest = new ApiRequest<>();
			String url = UriComponentsBuilder.fromUriString(apiUrl).queryParam("zip", zipCode)
					.queryParam("appid", apiKey).queryParam("units", "metric").build().toString();

			apiRequest.setApiUrl(url);
			apiRequest.setHttpMethod(HttpMethod.GET);
			return apiRequest;
		} catch (Exception e) {
			logger.error("Exception while building open weather api response", e);
			throw new GenericException();
		}
	}

}
