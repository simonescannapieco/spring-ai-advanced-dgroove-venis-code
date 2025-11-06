package it.venis.ai.spring.demo.tools;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;

import it.venis.ai.spring.demo.model.WeatherRequest;
import it.venis.ai.spring.demo.services.WeatherService;

public class WeatherTools {
    
    public static final String GET_WEATHER_IN_LOCATION_FUNCTION_NAME = "getWeatherInLocation";

    ToolCallback toolCallback = FunctionToolCallback
            .builder(GET_WEATHER_IN_LOCATION_FUNCTION_NAME, new WeatherService())
            .description("Ottieni la temperatura corrente nella località specificata.")
            .inputType(WeatherRequest.class)
            .build();

}
