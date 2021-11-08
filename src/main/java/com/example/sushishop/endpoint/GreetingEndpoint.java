package com.example.sushishop.endpoint;

import com.example.sushishop.config.WebServiceConfig;
import com.example.sushishop.service.GreetingService;
import com.example.sushishop.ws.greeting.GetGreetingRequest;
import com.example.sushishop.ws.greeting.GetGreetingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;

@Endpoint
public class GreetingEndpoint {

	public static final String NAMESPACE_URL = "http://example.com/sushishop/ws/greeting";

	private GreetingService greetingService;

	@Autowired
	public GreetingEndpoint(GreetingService greetingService) {
		this.greetingService = greetingService;
	}

	@PayloadRoot(namespace = NAMESPACE_URL, localPart = "getGreetingRequest")
	@ResponsePayload
	public GetGreetingResponse getGreeting(@RequestPayload GetGreetingRequest request)
			throws DatatypeConfigurationException {
		GetGreetingResponse response = new GetGreetingResponse();

		response.setGreeting(greetingService.generateGreeting(request.getName()));

		return response;
	}
}