package it.venis.ai.spring.demo.services;

import java.io.IOException;
import java.net.URLConnection;
import java.util.Base64;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import it.venis.ai.spring.demo.model.Answer;

@Service
public class MultiModalityServiceImpl implements MultiModalityService {

    @Value("")
    private String apiKey;


    @Value("${spring.ai.openai.base-url}")
    private String geminiUrl;

    private final ChatClient geminiChatClient;

    public MultiModalityServiceImpl(@Qualifier("geminiChatClient") ChatClient geminiChatClient) {

        this.geminiChatClient = geminiChatClient;

    }

    @Override
    public Answer getTranscriptionFromAudioFile(Resource audioFile) {
        
        String contentType = getContentType(audioFile);

        Media audioMedia = new Media(
            MimeTypeUtils.parseMimeType(contentType), audioFile
        );

        return new Answer(this.geminiChatClient.prompt()
        .user(u -> u.text("Trascrivi il seguente file audio: ").media(audioMedia))
        .call()
        .content()
        );
        
    }

    private String getContentType(Resource audioFile) {
        String contentType = URLConnection.guessContentTypeFromName(audioFile.getFilename());
        
        // Se il content type non è specificato, cerca di dedurlo dall'estensione
        if (contentType == null || contentType.equals("application/octet-stream")) {
            String fileName = audioFile.getFilename();
            if (fileName != null) {
                String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
                contentType = switch (extension) {
                    case "mp3" -> "audio/mpeg";
                    case "wav" -> "audio/wav";
                    case "m4a" -> "audio/mp4";
                    case "ogg" -> "audio/ogg";
                    case "flac" -> "audio/flac";
                    default -> "audio/mpeg";
                };
            } else {
                contentType = "audio/mpeg";
            }
        }
        
        return contentType;
    }
    
}
