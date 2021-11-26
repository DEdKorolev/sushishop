package com.example.sushishop.service;

import com.example.sushishop.ws.greeting.Greeting;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.GregorianCalendar;

@Component
public class GreetingService {
	public Greeting generateGreeting(String name) throws DatatypeConfigurationException {
		System.out.println("Вызван метод generateGreeting");
		Greeting greeting = new Greeting();
		greeting.setText("Hello, " + name);
		greeting.setDate(DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(new GregorianCalendar()));
		return greeting;
	}
}