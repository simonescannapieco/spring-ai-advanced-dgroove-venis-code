package it.venis.ai.spring.demo.config;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.venis.ai.spring.demo.tools.TimeTools;

@Configuration
public class TimeToolsConfig {
    
    @Bean
    public ChatClient geminiTimeToolsChatClient(OpenAiChatModel geminiChatModel, TimeTools timeTools) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(geminiChatModel);

        return chatClientBuilder
                .defaultTools(timeTools)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor()))
                .defaultSystem(
                        """
                            Sei un assistente AI di nome ToolGeminiBot, addestrato per intrattenere una
                            conversazione con un umano.
                            Usa i tool forniti dall'utente per fornire la tua risposta.
                        """)
                .defaultUser(
                        """
                            Come puoi aiutarmi?
                        """)
                .build();

    }  

    @Bean
    public ChatClient ollamaTimeToolsChatClient(OllamaChatModel ollamaChatModel, TimeTools timeTools) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(ollamaChatModel);

        return chatClientBuilder
                .defaultTools(timeTools)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor()))
                .defaultSystem(
                        """
                            Sei un assistente AI di nome ToolLlamaBot, addestrato per intrattenere una
                            conversazione con un umano.
                            Usa i tool forniti dall'utente per fornire la tua risposta.
                        """)
                .defaultUser(
                        """
                            Come puoi aiutarmi?
                        """)
                .build();

    }    

}
