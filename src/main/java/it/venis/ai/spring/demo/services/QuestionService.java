package it.venis.ai.spring.demo.services;

import it.venis.ai.spring.demo.model.Answer;
import it.venis.ai.spring.demo.model.Question;

public interface QuestionService {
    
    String getAnswer(String question);

    Answer getAnswer(Question question);

}
