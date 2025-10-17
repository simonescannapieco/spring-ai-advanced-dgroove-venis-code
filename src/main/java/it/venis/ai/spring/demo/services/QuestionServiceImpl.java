package it.venis.ai.spring.demo.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import it.venis.ai.spring.demo.model.Answer;
import it.venis.ai.spring.demo.model.Question;

@Service
@Configuration
public class QuestionServiceImpl implements QuestionService {

    private final ChatClient chatClient;

    public QuestionServiceImpl(ChatClient chatClient) {

        this.chatClient = chatClient;

    }

    @Override
    public String getAnswer(String question) {

        return this.chatClient.prompt()
                .user(question)
                .call()
                .content();

    }

    @Override
    public Answer getAnswer(Question question) {

        return new Answer(getAnswer(question.question()));

    }

}
