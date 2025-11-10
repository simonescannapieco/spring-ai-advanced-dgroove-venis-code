package it.venis.ai.spring.demo.services;

import java.util.function.Function;

import it.venis.ai.spring.demo.data.Unit;
import it.venis.ai.spring.demo.model.WeatherRequest;
import it.venis.ai.spring.demo.model.TemperatureResponse;

public class TemperatureService implements Function<WeatherRequest, TemperatureResponse> {
    
    public TemperatureResponse apply(WeatherRequest request) {
        return new TemperatureResponse(30.0, Unit.C);
    }
    
}