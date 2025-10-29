package it.venis.ai.spring.demo.config;

import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;

@Configuration
public class RAGConfig {

    @Value("${spring.ai.vectorstore.qdrant.host:#{null}}")
    private String qdrantHost;
    @Value("${spring.ai.vectorstore.qdrant.port:#{null}}")
    private String qdrantPort;
    @Value("${spring.ai.vectorstore.qdrant.use-tls:#{null}}")
    private String useTls;

    @Bean
    public QdrantClient qdrantClient() {

        QdrantGrpcClient.Builder grpcClientBuilder = QdrantGrpcClient.newBuilder(
                qdrantHost == null ? "localhost" : qdrantHost,
                qdrantPort == null ? 6334 : Integer.valueOf(qdrantPort),
                useTls == null ? false : Boolean.valueOf(useTls));

        return new QdrantClient(grpcClientBuilder.build());

    }

    @Value("${spring.ai.vectorstore.qdrant.collection-name.gemini:#{null}}")
    private String qdrantCollectionNameGemini;
    @Value("${spring.ai.vectorstore.qdrant.collection-name.ollama:#{null}}")
    private String qdrantCollectionNameOllama;
    @Value("${spring.ai.vectorstore.qdrant.initialize-schema:#{null}}")
    private String qdrantInitializeSchema;

    @Bean
    public VectorStore geminiVectorStore(QdrantClient qdrantClient, OpenAiEmbeddingModel geminiEmbeddingModel) {

        return QdrantVectorStore.builder(qdrantClient, geminiEmbeddingModel)
                .collectionName(qdrantCollectionNameGemini == null ? "vector_store_gemini" : qdrantCollectionNameGemini)
                .initializeSchema(qdrantInitializeSchema == null ? false : Boolean.valueOf(qdrantInitializeSchema))
                .build();

    }

    @Bean
    public VectorStore ollamaVectorStore(QdrantClient qdrantClient, OllamaEmbeddingModel ollamaEmbeddingModel) {

        return QdrantVectorStore.builder(qdrantClient, ollamaEmbeddingModel)
                .collectionName(qdrantCollectionNameOllama == null ? "vector_store_ollama" : qdrantCollectionNameOllama)
                .initializeSchema(qdrantInitializeSchema == null ? false : Boolean.valueOf(qdrantInitializeSchema))
                .build();

    }

}
