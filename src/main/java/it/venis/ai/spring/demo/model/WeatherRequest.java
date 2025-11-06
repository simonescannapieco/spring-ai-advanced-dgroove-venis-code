package it.venis.ai.spring.demo.model;

import it.venis.ai.spring.demo.data.Unit;

public record WeatherRequest(String location, Unit unit) {}