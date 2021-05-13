package com.weather.handler;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.weather.annotation.ZipCodeValidation;
import com.weather.constant.WeatherConstant;

public class ZipCodeValidator implements ConstraintValidator<ZipCodeValidation, String> {

	@Override
	public boolean isValid(String zipcode, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(zipcode))
			return false;

		return Pattern.matches(WeatherConstant.USZIPCODE_REGEX, zipcode);
	}

}
