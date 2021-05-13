/**
 * 
 */
package com.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.weather.constant.ErrorConstant;
import com.weather.exception.BadGatewayException;
import com.weather.exception.BadRequestException;
import com.weather.exception.GenericException;
import com.weather.request.ApiRequest;

/**
 * @author User
 *
 */
@Service
public class ApiClientServiceImpl implements IApiClientService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * method to call rest api
	 * @param ApiRequest
	 * @param Class<?> responsetype
	 * @return ResponseEntity<?>
	 */
	@Override
	public <T> ResponseEntity<?> executeRestApi(ApiRequest<T> apiRequest, Class<?> responseType) {

		ResponseEntity<?> response = null;
		HttpEntity<T> request = new HttpEntity<T>(apiRequest.getRequest());

		try {
			response = restTemplate.exchange(apiRequest.getApiUrl(), apiRequest.getHttpMethod(), request, responseType);

			if (null == response) {
				logger.debug("Api response is null");
				return null;
			}

			handleError(response.getStatusCode());

		} catch (BadGatewayException | BadRequestException be) {

			throw be;

		} catch (Exception e) {

			logger.error("Generic Exception in api calling", e);
			throw new GenericException();
		}

		return response;

	}

	private void handleError(HttpStatus httpStatus) {
		logger.info("http staus :" + httpStatus.name());
		
		switch (httpStatus) {
		case BAD_GATEWAY:
			logger.error("Bad gateway Exception in api calling");
			throw new BadGatewayException(ErrorConstant.API_BAD_GATEWAY, "Bad Gateway in weather api");

		case BAD_REQUEST:
			logger.error("Bad request Exception in api calling");
			throw new BadRequestException(ErrorConstant.API_BAD_REQUST, "Bad Gateway in weather api");
		default:
			break;
		}
	}

}
