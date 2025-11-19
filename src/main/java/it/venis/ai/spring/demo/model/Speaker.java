package it.venis.ai.spring.demo.model;

public class Speaker {
    private String name;
    private String voice;

    public Speaker() {
    }

    public Speaker(String name, String voice) {
        this.name = name;
        this.voice = voice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }
}