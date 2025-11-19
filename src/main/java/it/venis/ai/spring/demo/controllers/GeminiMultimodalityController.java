package it.venis.ai.spring.demo.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import it.venis.ai.spring.demo.model.Answer;
import it.venis.ai.spring.demo.services.MultiModalityService;

/**
 * Controller REST per la gestione delle funzionalità multimodali di Google
 * Gemini AI.
 *
 * Questo controller fornisce endpoint per:
 * - Speech-to-Text (STT): trascrizione di audio in testo
 * - Image-to-Text (ITT): analisi e descrizione di immagini
 * - Text-to-Speech (TTS): sintesi vocale da testo ad audio
 *
 * Gli endpoint TTS supportano:
 * - Generazione audio con risposta JSON (base64)
 * - Download diretto di file audio WAV
 * - Selezione di voci multiple (Puck, Charon, Kore, Fenrir, Aoede)
 * - Configurazione di stili e prompt personalizzati
 *
 * @author Simone Scannapieco
 * @version 1.0
 */
@RestController
@RequestMapping("/gemini/multi-modality")
public class GeminiMultimodalityController {

    /** Servizio per le operazioni multimodali (STT, ITT, TTS) */
    private final MultiModalityService multiModalityService;

    /**
     * Costruttore del controller per la gestione delle funzionalità multimodali di
     * Gemini.
     *
     * @param multiModalityService servizio per le operazioni multimodali (STT, ITT,
     *                             TTS)
     */
    public GeminiMultimodalityController(MultiModalityService multiModalityService) {

        this.multiModalityService = multiModalityService;
    }

    /**
     * Endpoint per la trascrizione di file audio in testo (Speech-to-Text).
     * Utilizza un file audio predefinito dal classpath e ne estrae la trascrizione
     * testuale
     * tramite i servizi di riconoscimento vocale di Gemini.
     *
     * @param audioFile risorsa audio dal classpath (file WAV predefinito)
     * @return Answer oggetto contenente la trascrizione del file audio
     */
    @PostMapping("/stt")
    public Answer getTranscriptionFromAudioFile(@Value("classpath:Venis_descrizione_azienda.wav") Resource audioFile) {

        return this.multiModalityService.getTranscriptionFromAudioFile(audioFile);

    }

    /**
     * Endpoint per l'analisi e descrizione di immagini (Image-to-Text).
     * Carica un'immagine predefinita dal classpath e utilizza le capacità di
     * visione
     * di Gemini per generare una descrizione testuale del contenuto dell'immagine.
     *
     * @param audioFile risorsa immagine dal classpath (file PNG predefinito)
     * @return Answer oggetto contenente la descrizione generata dell'immagine
     */
    @PostMapping("/itt")
    public Answer getDescriptionFromImage(@Value("classpath:multimodal.test.png") Resource audioFile) {

        return this.multiModalityService.getDescriptionFromImage(audioFile);

    }

}