package it.venis.ai.spring.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient geminiChatClient(OpenAiChatModel geminiChatClient) {

        return ChatClient.create(geminiChatClient);

        /*
         * or:
         * ChatClient.Builder chatClientBulder = ChatClient.builder(geminiChatClient);
         * return chatClientBulder.build();
         */

    }

    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {

        ChatClient.Builder chatClientBulder = ChatClient.builder(ollamaChatModel);

        return chatClientBulder.build();

        /*
         * or:
         * return ChatClient.create(ollamaChatModel);
         */

    }

}