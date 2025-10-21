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

    private final ChatClient geminiChatClient;
    private final ChatClient ollamaChatClient;

    public QuestionServiceImpl(@Qualifier("geminiChatClient") ChatClient geminiChatClient,
            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient) {

        this.geminiChatClient = geminiChatClient;
        this.ollamaChatClient = ollamaChatClient;

    }

    @Override
    public Answer getGeminiAnswer(Question question) {

        return new Answer(this.geminiChatClient.prompt()
                .user(question.question())
                .call()
                .content());

    }

    @Override
    public Answer getOllamaAnswer(Question question) {

        return new Answer(this.ollamaChatClient.prompt()
                .user(question.question())
                .call()
                .content());

    }

}
