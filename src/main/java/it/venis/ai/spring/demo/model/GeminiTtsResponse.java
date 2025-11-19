package it.venis.ai.spring.demo.model;

public class GeminiTtsResponse {
    private String audioBase64;
    private String mimeType;
    private String message;

    public GeminiTtsResponse(String audioBase64, String mimeType, String message) {
        this.audioBase64 = audioBase64;
        this.mimeType = mimeType;
        this.message = message;
    }

    public String getAudioBase64() {
        return audioBase64;
    }

    public void setAudioBase64(String audioBase64) {
        this.audioBase64 = audioBase64;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}