package it.venis.ai.spring.demo.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.venis.ai.spring.demo.model.Answer;
import it.venis.ai.spring.demo.model.QuestionRequest;

@Service
public class ToolServiceImpl implements ToolService {

    private final ChatClient geminiToolChatClient;
    private final ChatClient ollamaToolChatClient;

    public ToolServiceImpl(
            @Qualifier("geminiToolChatClient") ChatClient geminiToolChatClient,
            @Qualifier("ollamaToolChatClient") ChatClient ollamaToolChatClient) {

        this.geminiToolChatClient = geminiToolChatClient;
        this.ollamaToolChatClient = ollamaToolChatClient;

    }

    @Override
    public Answer getGeminiToolLocalTimeAnswer(QuestionRequest request) {

        return new Answer(this.geminiToolChatClient
                .prompt()
                .user(request.body().question())
                .call()
                .content());

    }

    @Override
    public Answer getOllamaToolLocalTimeAnswer(QuestionRequest request) {

        return new Answer(this.ollamaToolChatClient
                .prompt()
                .user(request.body().question())
                .call()
                .content());
    }

}
