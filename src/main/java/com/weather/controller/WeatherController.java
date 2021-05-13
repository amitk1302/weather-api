/**
 * 
 */
package com.weather.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.annotation.ZipCodeValidation;
import com.weather.response.WeatherResponse;
import com.weather.service.IWeatherService;

/**
 * @author User
 *
 */
@RestController
@Validated
public class WeatherController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IWeatherService weatherService;
	 
	/**
	 * Controller for fetch tomorrow's weather
	 * @param zipCode
	 * @return
	 */
	@GetMapping("/weather/forcast/nextday")
	public ResponseEntity<WeatherResponse> getNextDayWeatherForcast(@ZipCodeValidation @RequestParam("zipcode") String zipCode) {
		logger.debug("Executing getNextDayWeatherForcast method");
		WeatherResponse response = weatherService.getNextDayForcast(zipCode);
		if (null == response) {
			logger.debug("response is null, so returning 204 Statuss");

			return new ResponseEntity<WeatherResponse>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<WeatherResponse>(response, HttpStatus.OK);
	}
	
}
