package it.venis.ai.spring.demo.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
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
        private RetrievalAugmentationAdvisor geminiRetrievalAugmentationAdvisor;
        private RetrievalAugmentationAdvisor webSearchRetrievalAugmentationAdvisor;

        public RAGServiceImpl(
                        @Qualifier("geminiChatClient") ChatClient geminiChatClient,
                        @Qualifier("ollamaChatClient") ChatClient ollamaChatClient,
                        @Qualifier("ollamaMemoryChatClient") ChatClient ollamaMemoryChatClient,
                        @Qualifier("geminiVectorStore") VectorStore geminiVectorStore,
                        @Qualifier("ollamaVectorStore") VectorStore ollamaVectorStore,
                        @Qualifier("geminiRetrievalAugmentationAdvisor") RetrievalAugmentationAdvisor geminiRetrievalAugmentationAdvisor,
                        @Qualifier("webSearchRetrievalAugmentationAdvisor") RetrievalAugmentationAdvisor webSearchRetrievalAugmentationAdvisor) {

                this.geminiChatClient = geminiChatClient;
                this.ollamaChatClient = ollamaChatClient;
                this.ollamaMemoryChatClient = ollamaMemoryChatClient;
                this.geminiVectorStore = geminiVectorStore;
                this.ollamaVectorStore = ollamaVectorStore;
                this.geminiRetrievalAugmentationAdvisor = geminiRetrievalAugmentationAdvisor;
                this.webSearchRetrievalAugmentationAdvisor = webSearchRetrievalAugmentationAdvisor;
        }

        @Value("${demo.rag.prompt.system.eng}")
        private Resource ragDataSystemEngPrompt;

        @Override
        public Answer getGeminiRAGAnswer(QuestionRequest request) {

                /*
                 * This code is no longer needed, because all is handled by the logic behind the
                 * geminiRetrievalAugmentationAdvisor!
                 * 
                 * SearchRequest searchRequest = SearchRequest.builder()
                 * .query(request.body().question())
                 * .topK(4)
                 * .similarityThreshold(.2)
                 * .build();
                 *
                 * List<Document> similarDocs =
                 * geminiVectorStore.similaritySearch(searchRequest);
                 *
                 * String similarDocsString = similarDocs.stream()
                 * .map(Document::getText)
                 * .collect(Collectors.joining(System.lineSeparator()));
                 */

                return new Answer(this.geminiChatClient.prompt()
                                .advisors(List.of(new SimpleLoggerAdvisor(), geminiRetrievalAugmentationAdvisor))
                                /*
                                 * The geminiRetrievalAugmentationAdvisor takes also care to create
                                 * a dedicated system prompt to handle the RAG strategy!
                                 * 
                                 * .system(s -> s.text(this.ragDataSystemEngPrompt)
                                 * .params(Map.of("documenti", similarDocsString)))
                                 */
                                .user(request.body().question())
                                /*
                                 * The template rendered is now useless, since the system prompt is
                                 * automatically created!
                                 *
                                 * .templateRenderer(StTemplateRenderer.builder().startDelimiterToken('<')
                                 * .endDelimiterToken('>')
                                 * .build())
                                 */
                                .call()
                                .content());
        }

        @Value("${demo.rag.prompt.system.ita}")
        private Resource ragDataSystemItaPrompt;

        @Override
        public Answer getOllamaRAGAnswer(QuestionRequest request) {

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
                                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,
                                                request.username()))
                                .system(s -> s.text(this.ragDataSystemItaPrompt)
                                                .params(Map.of("documenti", similarDocsString)))
                                .user(request.body().question())
                                .templateRenderer(StTemplateRenderer.builder().startDelimiterToken('<')
                                                .endDelimiterToken('>')
                                                .build())
                                .call()
                                .content());
        }

        @Override
        public Answer getOllamaWebSearchRAGAnswer(QuestionRequest request) {

                return new Answer(this.ollamaChatClient.prompt()
                                .advisors(List.of(this.webSearchRetrievalAugmentationAdvisor))
                                .user(request.body().question())
                                .call()
                                .content());
        }

}