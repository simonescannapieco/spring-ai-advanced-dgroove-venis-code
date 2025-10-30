package it.venis.ai.spring.demo.rag;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Profile("rag-document-to-vector-store")
public class DocumentDataLoader {

    private final VectorStore geminiVectorStore;
    private final VectorStore ollamaVectorStore;

    public DocumentDataLoader(@Qualifier("geminiVectorStore") VectorStore geminiVectorStore,
            @Qualifier("ollamaVectorStore") VectorStore ollamaVectorStore) {
        this.geminiVectorStore = geminiVectorStore;
        this.ollamaVectorStore = ollamaVectorStore;

    }

    @Value("classpath:Venis_HR_Policies_ENG.pdf")
    Resource venisHREngDocument;

    @PostConstruct
    public void loadVenisHREngDocumentIntoVectorStore() {

        SearchRequest searchRequest = SearchRequest.builder()
                .query("Check")
                .similarityThresholdAll()
                .build();

        List<Document> similarDocs = geminiVectorStore.similaritySearch(searchRequest);

        if (similarDocs.size() == 0) {
            TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(venisHREngDocument);

            List<Document> docs = tikaDocumentReader.get();

            TextSplitter textSplitter = TokenTextSplitter.builder()
                    .withChunkSize(100)
                    .withMaxNumChunks(400)
                    .withKeepSeparator(true)
                    .build();

            this.geminiVectorStore.add(textSplitter.split(docs));
        }

    }

    @Value("classpath:Venis_Politiche_HR_ITA.pdf")
    Resource venisHRItaDocument;

    @PostConstruct
    public void loadVenisHRItaDocumentIntoVectorStore() {

        SearchRequest searchRequest = SearchRequest.builder()
                .query("Check")
                .similarityThresholdAll()
                .build();

        List<Document> similarDocs = ollamaVectorStore.similaritySearch(searchRequest);

        if (similarDocs.size() == 0) {
            TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(venisHRItaDocument);

            List<Document> docs = tikaDocumentReader.get();

            TextSplitter textSplitter = TokenTextSplitter.builder()
                    .withChunkSize(100)
                    .withMaxNumChunks(400)
                    .withKeepSeparator(false)
                    .build();

            this.ollamaVectorStore.add(textSplitter.split(docs));
        }

    }

}
