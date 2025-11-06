package it.venis.ai.spring.demo.services;

import java.util.function.Function;

import it.venis.ai.spring.demo.data.Unit;
import it.venis.ai.spring.demo.model.WeatherRequest;
import it.venis.ai.spring.demo.model.WeatherResponse;

public class WeatherService implements Function<WeatherRequest, WeatherResponse> {
    
    public WeatherResponse apply(WeatherRequest request) {
        return new WeatherResponse(30.0, Unit.C);
    }
    
}