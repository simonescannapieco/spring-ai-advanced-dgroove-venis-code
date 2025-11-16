package it.venis.ai.spring.demo.controllers;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.venis.ai.spring.demo.model.Answer;
import it.venis.ai.spring.demo.model.Question;
import it.venis.ai.spring.demo.model.QuestionRequest;
import it.venis.ai.spring.demo.model.WeatherRequest;
import it.venis.ai.spring.demo.model.TemperatureResponse;
import it.venis.ai.spring.demo.services.MultiModalityService;
import it.venis.ai.spring.demo.services.QuestionService;
import it.venis.ai.spring.demo.services.RAGService;
import it.venis.ai.spring.demo.services.TimeToolsService;

@RestController
@Configuration
public class QuestionController {

    private final QuestionService service;
    private final RAGService ragService;
    private final TimeToolsService timeToolsService;
    private final MultiModalityService multiModalityService;

    private final ChatClient geminiWeatherToolsChatClient;
    private final ChatClient ollamaWeatherToolsChatClient;
    private final ChatClient geminiHelpDeskToolsChatClient;
    private final ChatClient ollamaHelpDeskToolsChatClient;

    public QuestionController(QuestionService service,
            RAGService ragService,
            TimeToolsService timeToolsService,
            MultiModalityService multiModalityService,
            @Qualifier("geminiWeatherToolsChatClient") ChatClient geminiWeatherToolsChatClient,
            @Qualifier("ollamaWeatherToolsChatClient") ChatClient ollamaWeatherToolsChatClient,
            @Qualifier("geminiHelpDeskToolsChatClient") ChatClient geminiHelpDeskToolsChatClient,
            @Qualifier("ollamaHelpDeskToolsChatClient") ChatClient ollamaHelpDeskToolsChatClient) {

        this.service = service;
        this.ragService = ragService;
        this.timeToolsService = timeToolsService;
        this.multiModalityService = multiModalityService;
        this.geminiWeatherToolsChatClient = geminiWeatherToolsChatClient;
        this.ollamaWeatherToolsChatClient = ollamaWeatherToolsChatClient;
        this.geminiHelpDeskToolsChatClient = geminiHelpDeskToolsChatClient;
        this.ollamaHelpDeskToolsChatClient = ollamaHelpDeskToolsChatClient;

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

    @PostMapping("/gemini/ask/time-tools/time")
    public Answer getGeminiToolLocalTimeAnswer(@RequestBody QuestionRequest request) {

        return this.timeToolsService.getGeminiTimeToolsAnswer(request);

    }

    @PostMapping("/ollama/ask/time-tools/time")
    public Answer getOllamaToolLocalTimeAnswer(@RequestBody QuestionRequest request) {

        return this.timeToolsService.getOllamaTimeToolsAnswer(request);

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

    @PostMapping("/gemini/ask/help-desk-tools/help-desk")
    public Answer getGeminiHelpDeskToolAnswer(@RequestHeader("username") String username,
            @RequestParam("message") String message) {
        return new Answer(
            this.geminiHelpDeskToolsChatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, username))
                .user(message)
                .toolContext(Map.of("username", username))
                .call().content()
            );
    }

    @PostMapping("/ollama/ask/help-desk-tools/help-desk")
    public Answer getOllamaHelpDeskToolAnswer(@RequestHeader("username") String username,
            @RequestParam("message") String message) {
        return new Answer(
            this.ollamaHelpDeskToolsChatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, username))
                .user(message)
                .toolContext(Map.of("username", username))
                .call().content()
            );
    }

    @PostMapping("/gemini/ask/multi-modality/transcribe")
    public Answer getTranscriptionFromAudioFile(@Value("classpath:Venis_descrizione_azienda.wav") Resource audioFile) {
        
        return this.multiModalityService.getTranscriptionFromAudioFile(audioFile);

    }
    

}
