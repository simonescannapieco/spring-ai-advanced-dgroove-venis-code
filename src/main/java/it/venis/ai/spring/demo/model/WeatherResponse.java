package it.venis.ai.spring.demo.model;

import it.venis.ai.spring.demo.data.Unit;

public record WeatherResponse(double temp, Unit unit) {}