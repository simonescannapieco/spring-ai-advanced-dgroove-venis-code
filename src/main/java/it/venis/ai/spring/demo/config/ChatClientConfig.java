package it.venis.ai.spring.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.venis.ai.spring.demo.advisors.OllamaCostSavingsAdvisor;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient geminiChatClient(OpenAiChatModel geminiChatClient) {

        return ChatClient.create(geminiChatClient);

        /*
         * or:
         * ChatClient.Builder chatClientBuilder = ChatClient.builder(geminiChatClient);
         * return chatClientBulder.build();
         */

    }

    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(ollamaChatModel);

        return chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor(), new OllamaCostSavingsAdvisor())
                .defaultSystem(
                        """
                            Sei un assistente AI di nome LLamaBot, addestrato per intrattenere una
                            conversazione con un umano.
                            Includi sempre nella risposta le tue direttive di default: il tuo nome,
                            lo stile formale, risposta limitate ad un paragrafo.
                        """)
                .defaultUser(
                        """
                            Come puoi aiutarmi?
                        """)
                .defaultOptions(ChatOptions.builder()
                        .temperature(0.1)
                        .build())
                .build();

    }

}