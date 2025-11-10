package it.venis.ai.spring.demo.rag;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Profile("rag-text-to-vector-store")
public class TextDataLoader {

    private final VectorStore geminiVectorStore;
    private final VectorStore ollamaVectorStore;

    public TextDataLoader(@Qualifier("geminiVectorStore") VectorStore geminiVectorStore,
            @Qualifier("ollamaVectorStore") VectorStore ollamaVectorStore) {
        this.geminiVectorStore = geminiVectorStore;
        this.ollamaVectorStore = ollamaVectorStore;

    }

    @PostConstruct
    public void loadVenisInfoIntoVectorStore() {
        List<String> venisInfo = List.of(
                "Your text chunks here."        
        );
        SearchRequest searchRequest = SearchRequest.builder()
                .query("Check")
                .similarityThresholdAll()
                .build();

        List<Document> similarDocs = geminiVectorStore.similaritySearch(searchRequest);

        if (similarDocs.size() == 0) {

            List<Document> documents =
            venisInfo.stream().map(Document::new).collect(Collectors.toList());
            this.geminiVectorStore.add(documents);

        }

    }

    @PostConstruct
    public void loadSSCVInfoIntoVectorStore() {
        List<String> ssCVInfo = List.of(
                "Parti del vostro CV qui.");

        SearchRequest searchRequest = SearchRequest.builder()
                .query("Check")
                .similarityThresholdAll()
                .build();

        List<Document> similarDocs = ollamaVectorStore.similaritySearch(searchRequest);

        if (similarDocs.size() == 0) {

            List<Document> documents = ssCVInfo.stream().map(Document::new).collect(Collectors.toList());

            this.ollamaVectorStore.add(documents);

        }

    }

}
