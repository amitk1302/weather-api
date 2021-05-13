package com.weather.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.weather.handler.ZipCodeValidator;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ZipCodeValidator.class)
public @interface ZipCodeValidation {
	String message() default "Invalid ZipCode";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
