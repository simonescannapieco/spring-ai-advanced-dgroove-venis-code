package it.venis.ai.spring.demo.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import it.venis.ai.spring.demo.model.Answer;
import it.venis.ai.spring.demo.model.QuestionRequest;

@Service
@Configuration
public class RAGServiceImpl implements RAGService {

    private final ChatClient geminiChatClient;
    private final ChatClient ollamaChatClient;
    private final ChatClient ollamaMemoryChatClient;
    private VectorStore geminiVectorStore;
    private VectorStore ollamaVectorStore;

    public RAGServiceImpl(
            @Qualifier("geminiChatClient") ChatClient geminiChatClient,
            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient,
            @Qualifier("ollamaMemoryChatClient") ChatClient ollamaMemoryChatClient,
            @Qualifier("geminiVectorStore") VectorStore geminiVectorStore,
            @Qualifier("ollamaVectorStore") VectorStore ollamaVectorStore) {

        this.geminiChatClient = geminiChatClient;
        this.ollamaChatClient = ollamaChatClient;
        this.ollamaMemoryChatClient = ollamaMemoryChatClient;
        this.geminiVectorStore = geminiVectorStore;
        this.ollamaVectorStore = ollamaVectorStore;

    }

    @Value("classpath:templates/get-rag-data-system-eng-prompt.st")
    private Resource ragDataSystemEngPrompt;

    @Override
    public Answer getGeminiRAGTextToVectorStoreAnswer(QuestionRequest request) {

        SearchRequest searchRequest = SearchRequest.builder()
                .query(request.body().question())
                .topK(4)
                .similarityThreshold(.2)
                .build();

        List<Document> similarDocs = geminiVectorStore.similaritySearch(searchRequest);

        String similarDocsString = similarDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));

        return new Answer(this.geminiChatClient.prompt()
                // .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,
                // request.username()))
                .system(s -> s.text(this.ragDataSystemEngPrompt)
                        .params(Map.of("documenti", similarDocsString)))
                .user(request.body().question())
                .templateRenderer(StTemplateRenderer.builder().startDelimiterToken('<')
                        .endDelimiterToken('>')
                        .build())
                .call()
                .content());
    }

    @Value("classpath:templates/get-rag-data-system-ita-prompt.st")
    private Resource ragDataSystemItaPrompt;

    @Override
    public Answer getOllamaRAGTextToVectorStoreAnswer(QuestionRequest request) {

        SearchRequest searchRequest = SearchRequest.builder()
                .query(request.body().question())
                .topK(4)
                .similarityThreshold(.3)
                .build();

        List<Document> similarDocs = ollamaVectorStore.similaritySearch(searchRequest);

        String similarDocsString = similarDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));

        return new Answer(this.ollamaMemoryChatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, request.username()))
                .system(s -> s.text(this.ragDataSystemItaPrompt)
                        .params(Map.of("documenti", similarDocsString)))
                .user(request.body().question())
                .templateRenderer(StTemplateRenderer.builder().startDelimiterToken('<')
                        .endDelimiterToken('>')
                        .build())
                .call()
                .content());
    }

}