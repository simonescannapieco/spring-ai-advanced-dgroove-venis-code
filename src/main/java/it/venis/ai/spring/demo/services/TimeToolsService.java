package it.venis.ai.spring.demo.services;

import it.venis.ai.spring.demo.model.Answer;
import it.venis.ai.spring.demo.model.QuestionRequest;

public interface TimeToolsService {
    
    public Answer getGeminiTimeToolsAnswer(QuestionRequest request);

    public Answer getOllamaTimeToolsAnswer(QuestionRequest request);
    
}
