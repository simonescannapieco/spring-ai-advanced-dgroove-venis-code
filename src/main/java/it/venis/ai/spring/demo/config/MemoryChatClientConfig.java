package it.venis.ai.spring.demo.config;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.venis.ai.spring.demo.advisors.OllamaCostSavingsAdvisor;

@Configuration
public class MemoryChatClientConfig {

    @Bean
    public ChatClient ollamaMemoryChatClient(OllamaChatModel ollamaChatModel, ChatMemory chatMemory) {

        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        ChatClient.Builder chatClientBuilder = ChatClient.builder(ollamaChatModel);

        return chatClientBuilder
                .defaultAdvisors(List.of(new OllamaCostSavingsAdvisor(), memoryAdvisor))
                .defaultSystem(
                        """
                            Sei un assistente AI di nome LLamaMemoryBot, addestrato per intrattenere una
                            conversazione con un umano.
                        """)
                .defaultOptions(ChatOptions.builder()
                        .temperature(0.1)
                        .build())
                .build();

    }

}