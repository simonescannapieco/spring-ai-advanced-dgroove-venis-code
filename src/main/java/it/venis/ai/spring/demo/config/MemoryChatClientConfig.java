package it.venis.ai.spring.demo.config;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.venis.ai.spring.demo.advisors.OllamaCostSavingsAdvisor;

@Configuration
public class MemoryChatClientConfig {

    /*
     * Done under-the-hood via Spring auto-configuration.
     */
    @Bean
    public ChatMemoryRepository chatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

    /*
     * Custom bean for chat memory, based on the (auto-)configured chat memory repository.
     * Done under-the-hood via Spring auto-configuration with maxMessages = 20.
     */
    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(20)
                .build();
    }

    /*
     * Custom bean for message chat memory advisor, based on the configured chat memory.
     */
    @Bean
    public BaseChatMemoryAdvisor messageChatMemoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory)
                .order(1)
                .build();
    }

    /*
     * Custom bean for prompt chat memory advisor, based on the configured chat memory.
     */
    @Bean
    public BaseChatMemoryAdvisor promptChatMemoryAdvisor(ChatMemory chatMemory) {
        return PromptChatMemoryAdvisor.builder(chatMemory)
                .order(1)
                .build();
    }

    /*
     * Custom chat client based on the configured chat advisor.
     */
    @Bean
    public ChatClient ollamaMemoryChatClient(OllamaChatModel ollamaChatModel, ChatMemory chatMemory, @Qualifier("promptChatMemoryAdvisor") BaseChatMemoryAdvisor chatMemoryAdvisor) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(ollamaChatModel);

        return chatClientBuilder
                .defaultAdvisors(List.of(
                        new SimpleLoggerAdvisor(),
                        new OllamaCostSavingsAdvisor(),
                        chatMemoryAdvisor))
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