package it.venis.ai.spring.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel openaiChatModel) {
        return ChatClient.create(openaiChatModel);
        /*
         * or:
         * ChatClient.Builder chatClientBulder = ChatClient.builder(openaiChatModel);
         * return chatClientBulder.build();
         */
    }

    @Bean
    @Primary
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.create(ollamaChatModel);
        /*
         * or:
         * ChatClient.Builder chatClientBulder = ChatClient.builder(ollamaChatModel);
         * return chatClientBulder.build();
         */
    }
}