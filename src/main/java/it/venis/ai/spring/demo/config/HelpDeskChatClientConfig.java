package it.venis.ai.spring.demo.config;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import it.venis.ai.spring.demo.advisors.OllamaCostSavingsAdvisor;
import it.venis.ai.spring.demo.tools.HelpDeskTools;

@Configuration
public class HelpDeskChatClientConfig {

    @Value("classpath:/templates/get-help-desk-system-prompt.st")
    Resource helpDeskSystemPrompt;

    @Bean
    public ChatClient geminiHelpDeskToolsChatClient(OpenAiChatModel geminiChatModel, HelpDeskTools helpDeskTools, ChatMemory chatMemory, @Qualifier("messageChatMemoryAdvisor") BaseChatMemoryAdvisor chatMemoryAdvisor) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(geminiChatModel);

        return chatClientBuilder
                .defaultTools(helpDeskTools)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), chatMemoryAdvisor))
                .defaultSystem(helpDeskSystemPrompt)
                .build();
    }

    @Bean
    public ChatClient ollamaHelpDeskToolsChatClient(OllamaChatModel ollamaChatModel, HelpDeskTools helpDeskTools, ChatMemory chatMemory, @Qualifier("messageChatMemoryAdvisor") BaseChatMemoryAdvisor chatMemoryAdvisor) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(ollamaChatModel);

        return chatClientBuilder
                .defaultTools(helpDeskTools)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new OllamaCostSavingsAdvisor(), chatMemoryAdvisor))
                .defaultSystem(helpDeskSystemPrompt)
                .build();
    }

}