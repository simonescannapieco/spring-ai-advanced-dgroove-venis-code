package it.venis.ai.spring.demo.services;

import org.springframework.core.io.Resource;

import it.venis.ai.spring.demo.model.Answer;

public interface MultiModalityService {
    
    Answer getTranscriptionFromAudioFile(Resource audioFile);

}
