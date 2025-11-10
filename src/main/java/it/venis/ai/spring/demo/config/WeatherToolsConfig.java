package it.venis.ai.spring.demo.config;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.metadata.ToolMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.venis.ai.spring.demo.advisors.OllamaCostSavingsAdvisor;
import it.venis.ai.spring.demo.model.WeatherRequest;
import it.venis.ai.spring.demo.services.TemperatureService;

@Configuration
public class WeatherToolsConfig {

    public static final String GET_TEMP_IN_LOCATION_FUNCTION_NAME = "getTemperatureInLocation";

    ToolCallback toolCallback = FunctionToolCallback
        .builder(GET_TEMP_IN_LOCATION_FUNCTION_NAME, new TemperatureService())
        .description("Ottieni la temperatura corrente nella località specificata.")
        .inputType(WeatherRequest.class)
        .toolMetadata(ToolMetadata.builder()
            .returnDirect(true)
            .build())
        .build();

    @Bean
    public ChatClient geminiWeatherToolsChatClient(OpenAiChatModel geminiChatModel) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(geminiChatModel);

        return chatClientBuilder
                .defaultToolCallbacks(toolCallback)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor()))
                .defaultSystem(
                    """
                        Sei un assistente AI di nome WeatherGeminiBot, addestrato per fornire
                        informazioni meteo alle persone.
                        Utilizza quando possibile il tool 'getTemperatureInLocation'.
                    """)
                .build();
    }

    @Bean
    public ChatClient ollamaWeatherToolsChatClient(OllamaChatModel ollamaChatModel) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(ollamaChatModel);

        return chatClientBuilder
                .defaultToolCallbacks(toolCallback)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new OllamaCostSavingsAdvisor()))
                .defaultSystem(
                    """
                        Sei un assistente AI di nome WeatherLlamaBot, addestrato per fornire
                        informazioni meteo alle persone.
                    """)
                .build();
    }

}
