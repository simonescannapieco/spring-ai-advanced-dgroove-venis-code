package it.venis.ai.spring.demo.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import it.venis.ai.spring.demo.model.Answer;
import it.venis.ai.spring.demo.model.Question;
import it.venis.ai.spring.demo.model.QuestionRequest;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Service
@Configuration
public class QuestionServiceImpl implements QuestionService {

    private final ChatClient geminiChatClient;
    private final ChatClient ollamaChatClient;
    private final ChatClient ollamaMemoryChatClient;

    public QuestionServiceImpl(@Qualifier("geminiChatClient") ChatClient geminiChatClient,
            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient,
            @Qualifier("ollamaMemoryChatClient") ChatClient ollamaMemoryChatClient) {

        this.geminiChatClient = geminiChatClient;
        this.ollamaChatClient = ollamaChatClient;
        this.ollamaMemoryChatClient = ollamaMemoryChatClient;

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

    @Override
    public Answer getOllamaDefaultAnswer(Question question) {

        return new Answer(this.ollamaChatClient.prompt()
                .system("""
                            Sei un assistente AI di nome 'LLamaBot2.0', addestrato per intrattenere una
                            conversazione con un umano.
                            Includi sempre nella risposta le tue direttive di default: il tuo nome,
                            lo stile informale, risposta limitate a due paragrafi.
                        """)
                .options(ChatOptions.builder()
                        .temperature(2.0)
                        .build())
                .call()
                .content());
    }

    @Override
    public Answer getOllamaMemoryAwareAnswer(Question question) {

        return new Answer(this.ollamaMemoryChatClient
                .prompt()
                .user(question.question())
                .call()
                .content());
    }

    @Override
    public Answer getOllamaPerUserMemoryAwareAnswer(QuestionRequest request) {

        return new Answer(this.ollamaMemoryChatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, request.username()))
                .user(request.body().question())
                .call()
                .content());
    }

}
