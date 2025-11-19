package it.venis.ai.spring.demo.services;

import java.net.URLConnection;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import it.venis.ai.spring.demo.model.Answer;

@Service
public class MultiModalityServiceImpl implements MultiModalityService {

    private final ChatClient geminiChatClient;

    public MultiModalityServiceImpl(@Qualifier("geminiChatClient") ChatClient geminiChatClient) {

        this.geminiChatClient = geminiChatClient;

    }

    @Override
    public Answer getTranscriptionFromAudioFile(Resource audioFile) {

        String contentType = getContentType(audioFile);

        Media audioMedia = new Media(
                MimeTypeUtils.parseMimeType(contentType), audioFile);

        return new Answer(this.geminiChatClient.prompt()
                .user(u -> u.text("Trascrivi il seguente file audio: ").media(audioMedia))
                .call()
                .content());

    }

    @Override
    public Answer getDescriptionFromImage(Resource imageFile) {

        String contentType = getContentType(imageFile);

        Media imageMedia = new Media(
                MimeTypeUtils.parseMimeType(contentType), imageFile);

        return new Answer(this.geminiChatClient.prompt()
                .user(u -> u.text("Descrivi in lingua italiana la seguente immagine: ").media(imageMedia))
                .call()
                .content());

    }

    private String getContentType(Resource multimediaFile) {
        String contentType = URLConnection.guessContentTypeFromName(multimediaFile.getFilename());

        if (contentType == null || contentType.equals("application/octet-stream")) {
            String fileName = multimediaFile.getFilename();
            if (fileName != null) {
                String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
                contentType = switch (extension) {
                    case "mp3" -> "audio/mpeg";
                    case "wav" -> "audio/wav";
                    case "m4a" -> "audio/mp4";
                    case "ogg" -> "audio/ogg";
                    case "flac" -> "audio/flac";
                    default -> "media/unknown";
                };
            } else {
                contentType = "media/unknown";
            }
        }

        return contentType;
    }

}