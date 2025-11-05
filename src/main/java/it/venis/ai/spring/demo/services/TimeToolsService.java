package it.venis.ai.spring.demo.services;

import it.venis.ai.spring.demo.model.Answer;
import it.venis.ai.spring.demo.model.QuestionRequest;

public interface TimeToolsService {
    
    public Answer getGeminiToolLocalTimeAnswer(QuestionRequest request);

    public Answer getOllamaToolLocalTimeAnswer(QuestionRequest request);
    
}
