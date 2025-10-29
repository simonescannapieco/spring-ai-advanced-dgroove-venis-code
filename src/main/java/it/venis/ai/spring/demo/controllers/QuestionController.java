package it.venis.ai.spring.demo.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.venis.ai.spring.demo.model.Answer;
import it.venis.ai.spring.demo.model.Question;
import it.venis.ai.spring.demo.model.QuestionRequest;
import it.venis.ai.spring.demo.services.QuestionService;
import it.venis.ai.spring.demo.services.RAGService;

@RestController
public class QuestionController {

    private final QuestionService service;
    private final RAGService ragService;

    public QuestionController(QuestionService service, RAGService ragService) {

        this.service = service;
        this.ragService = ragService;

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

    @PostMapping("/gemini/ask/rag/text-to-vs/venis")
    public Answer getGeminiRAGTextToVectorStoreAnswer(@RequestBody QuestionRequest request) {

        return this.ragService.getGeminiRAGTextToVectorStoreAnswer(request);

    }

    @PostMapping("/ollama/ask/rag/text-to-vs/cv")
    public Answer getOllamaRAGTextToVectorStoreAnswer(@RequestBody QuestionRequest request) {

        return this.ragService.getOllamaRAGTextToVectorStoreAnswer(request);

    }

}
