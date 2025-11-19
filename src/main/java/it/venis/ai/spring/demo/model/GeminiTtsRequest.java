package it.venis.ai.spring.demo.model;

public class GeminiTtsRequest {
    private String text;
    private String voice;
    private String model;
    private String stylePrompt;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStylePrompt() {
        return stylePrompt;
    }

    public void setStylePrompt(String stylePrompt) {
        this.stylePrompt = stylePrompt;
    }
}
