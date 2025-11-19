package it.venis.ai.spring.demo.model;

import java.util.List;

public class VoiceInfo {
    private String name;
    private List<String> languageCodes;
    private String gender;

    public VoiceInfo(String name, List<String> languageCodes, String gender) {
        this.name = name;
        this.languageCodes = languageCodes;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLanguageCodes() {
        return languageCodes;
    }

    public void setLanguageCodes(List<String> languageCodes) {
        this.languageCodes = languageCodes;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}