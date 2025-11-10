package it.venis.ai.spring.demo.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.venis.ai.spring.demo.model.Answer;
import it.venis.ai.spring.demo.model.Question;
import it.venis.ai.spring.demo.model.QuestionRequest;
import it.venis.ai.spring.demo.model.WeatherRequest;
import it.venis.ai.spring.demo.model.TemperatureResponse;
import it.venis.ai.spring.demo.services.QuestionService;
import it.venis.ai.spring.demo.services.RAGService;
import it.venis.ai.spring.demo.services.TimeToolsService;

@RestController
@Configuration
public class QuestionController {

    private final QuestionService service;
    private final RAGService ragService;
    private final TimeToolsService timeToolsService;

    private final ChatClient geminiWeatherToolsChatClient;
    private final ChatClient ollamaWeatherToolsChatClient;

    public QuestionController(QuestionService service,
            RAGService ragService,
            TimeToolsService timeToolsService,
            @Qualifier("geminiWeatherToolsChatClient") ChatClient geminiWeatherToolsChatClient,
            @Qualifier("ollamaWeatherToolsChatClient") ChatClient ollamaWeatherToolsChatClient) {

        this.service = service;
        this.ragService = ragService;
        this.timeToolsService = timeToolsService;
        this.geminiWeatherToolsChatClient = geminiWeatherToolsChatClient;
        this.ollamaWeatherToolsChatClient = ollamaWeatherToolsChatClient;

    }

    @PostMapping("/gemini/ask")
    public Answer geminiAskQuestion(@RequestBody Question question) {

        return this.service.getGeminiAnswer(question);

    }

    @PostMapping("/ollama/ask")
    public Answer ollamaAskQuestion(@RequestBody Question question) {

        return this.service.getOllamaAnswer(question);

    }

    @PostMapping("/ollama/ask/default")
    public Answer ollamaAskDefaultQuestion(@RequestBody Question question) {

        return this.service.getOllamaDefaultAnswer(question);

    }

    @PostMapping("/ollama/ask/memory")
    public Answer getOllamaMemoryAwareAnswer(@RequestBody Question question) {

        return this.service.getOllamaMemoryAwareAnswer(question);

    }

    @PostMapping("/ollama/ask/memory/user")
    public Answer getOllamaPerUserMemoryAwareAnswer(@RequestBody QuestionRequest request) {

        return this.service.getOllamaPerUserMemoryAwareAnswer(request);

    }

    @PostMapping("/gemini/ask/rag")
    public Answer getGeminiRAGAnswer(@RequestBody QuestionRequest request) {

        return this.ragService.getGeminiRAGAnswer(request);

    }

    @PostMapping("/ollama/ask/rag")
    public Answer getOllamaRAGAnswer(@RequestBody QuestionRequest request) {

        return this.ragService.getOllamaRAGAnswer(request);

    }

    @PostMapping("/ollama/ask/rag/web-search")
    public Answer getOllamaWebSearchRAGAnswer(@RequestBody QuestionRequest request) {

        return this.ragService.getOllamaWebSearchRAGAnswer(request);

    }

    @PostMapping("/gemini/ask/time-tools/local-time")
    public Answer getGeminiToolLocalTimeAnswer(@RequestBody QuestionRequest request) {

        return this.timeToolsService.getGeminiToolLocalTimeAnswer(request);

    }

    @PostMapping("/ollama/ask/time-tools/local-time")
    public Answer getOllamaToolLocalTimeAnswer(@RequestBody QuestionRequest request) {

        return this.timeToolsService.getOllamaToolLocalTimeAnswer(request);

    }

    @PostMapping("/gemini/ask/weather-tools/temperature")
    public TemperatureResponse getGeminiTemperatureToolAnswer(@RequestBody WeatherRequest request) {

        return this.geminiWeatherToolsChatClient
                .prompt()
                .call()
                .entity(TemperatureResponse.class);
    }

    @PostMapping("/ollama/ask/weather-tools/temperature")
    public TemperatureResponse getOllamaTemperatureToolAnswer(@RequestBody WeatherRequest request) {

        return this.ollamaWeatherToolsChatClient
                .prompt()
                .call()
                .entity(TemperatureResponse.class);
    }

}
