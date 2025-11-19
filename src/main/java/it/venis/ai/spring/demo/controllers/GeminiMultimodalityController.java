package it.venis.ai.spring.demo.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import it.venis.ai.spring.demo.model.Answer;
import it.venis.ai.spring.demo.model.GeminiTtsRequest;
import it.venis.ai.spring.demo.model.GeminiTtsResponse;
import it.venis.ai.spring.demo.model.VoiceInfo;
import it.venis.ai.spring.demo.services.MultiModalityService;
import it.venis.ai.spring.demo.util.PcmToWavConverter;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;

import java.util.*;

/**
 * Controller REST per la gestione delle funzionalità multimodali di Google Gemini AI.
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

    /** Chiave API di Google AI recuperata dalle proprietà di configurazione */
    @Value("${GOOGLE_AI_API_KEY}")
    private String geminiApiKey;

    /** URL base dell'API Gemini per le richieste ai modelli */
    @Value("${gemini.api.models.url}")
    private String geminiApiUrl;

    /** Servizio per le operazioni multimodali (STT, ITT, TTS) */
    private final MultiModalityService multiModalityService;

    /** Client HTTP REST per le chiamate all'API di Gemini */
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Costruttore del controller per la gestione delle funzionalità multimodali di Gemini.
     *
     * @param multiModalityService servizio per le operazioni multimodali (STT, ITT, TTS)
     */
    public GeminiMultimodalityController(MultiModalityService multiModalityService) {

        this.multiModalityService = multiModalityService;
    }

    /**
     * Endpoint per la trascrizione di file audio in testo (Speech-to-Text).
     * Utilizza un file audio predefinito dal classpath e ne estrae la trascrizione testuale
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
     * Carica un'immagine predefinita dal classpath e utilizza le capacità di visione
     * di Gemini per generare una descrizione testuale del contenuto dell'immagine.
     *
     * @param audioFile risorsa immagine dal classpath (file PNG predefinito)
     * @return Answer oggetto contenente la descrizione generata dell'immagine
     */
    @PostMapping("/itt")
    public Answer getDescriptionFromImage(@Value("classpath:multimodal.test.png") Resource audioFile) {

        return this.multiModalityService.getDescriptionFromImage(audioFile);

    }

    /**
     * Endpoint per la generazione di audio da testo (Text-to-Speech) utilizzando Gemini TTS.
     * Converte il testo fornito in audio sintetizzato, con supporto per diverse voci e stili.
     * L'audio generato viene restituito in formato base64 come WAV.
     *
     * Processo:
     * 1. Valida l'input del testo
     * 2. Seleziona il modello Gemini (default: gemini-2.5-flash-preview-tts)
     * 3. Costruisce e invia la richiesta all'API di Gemini
     * 4. Estrae i dati audio in formato PCM dalla risposta
     * 5. Converte PCM in formato WAV (24kHz, 16-bit, mono)
     * 6. Restituisce l'audio come stringa base64
     *
     * @param request richiesta contenente testo, voce, modello e parametri di stile opzionali
     * @return ResponseEntity con GeminiTtsResponse contenente audio base64, tipo MIME e messaggio di stato
     */
    @PostMapping("/tts/generate")
    public ResponseEntity<GeminiTtsResponse> generateSpeech(
            @RequestBody GeminiTtsRequest request) {

        try {
            // Validazione input
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new GeminiTtsResponse(null, null, "Il testo non può essere vuoto"));
            }

            // Selezione modello
            String model = request.getModel() != null ? request.getModel() : "gemini-2.5-flash-preview-tts";

            // Costruisci l'URL completo
            String url = String.format("%s/%s:generateContent?key=%s",
                    geminiApiUrl, model, geminiApiKey);

            // Prepara la richiesta per Gemini API
            Map<String, Object> requestBody = buildGeminiRequest(request);

            // Esegui la chiamata a Gemini API
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(
                    requestBody,
                    createHeaders());

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(
                    url,
                    httpEntity,
                    Map.class);

            // Estrai i dati audio dalla risposta
            String audioBase64 = extractAudioData(response);

            if (audioBase64 == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new GeminiTtsResponse(null, null,
                                "Errore nell'estrazione dei dati audio dalla risposta"));
            }

            byte[] audioData = Base64.getDecoder().decode(audioBase64);

            return ResponseEntity.ok(new GeminiTtsResponse(
                    Base64.getEncoder()
                            .encodeToString(PcmToWavConverter.pcmToWav(audioData, 24000.0f, 16, 1, false, false)),
                    "audio/wav",
                    "Audio generato con successo usando Gemini TTS"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GeminiTtsResponse(null, null,
                            "Errore durante la generazione dell'audio: " + e.getMessage()));
        }
    }

    /**
     * Endpoint per generare e scaricare direttamente un file audio WAV (Text-to-Speech).
     * Similare a /tts/generate, ma restituisce direttamente i byte dell'audio come file
     * scaricabile invece di una rappresentazione JSON base64.
     *
     * Processo:
     * 1. Valida l'input del testo
     * 2. Seleziona il modello Gemini (default: gemini-2.5-flash-preview-tts)
     * 3. Invia la richiesta all'API di Gemini
     * 4. Converte la risposta PCM in formato WAV
     * 5. Imposta gli header HTTP per il download del file
     * 6. Restituisce i byte dell'audio come attachment
     *
     * @param request richiesta contenente testo, voce, modello e parametri di stile opzionali
     * @return ResponseEntity con array di byte dell'audio WAV e header per il download come "gemini-speech.wav"
     */
    @PostMapping("/tts/download")
    public ResponseEntity<byte[]> downloadAudio(@RequestBody GeminiTtsRequest request) {
        try {
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            String model = request.getModel() != null ? request.getModel() : "gemini-2.5-flash-preview-tts";

            String url = String.format("%s/%s:generateContent?key=%s",
                    geminiApiUrl, model, geminiApiKey);

            Map<String, Object> requestBody = buildGeminiRequest(request);

            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(
                    requestBody,
                    createHeaders());

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(
                    url,
                    httpEntity,
                    Map.class);

            String audioBase64 = extractAudioData(response);

            if (audioBase64 == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            byte[] audioData = Base64.getDecoder().decode(audioBase64);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("audio/wav"));
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment").filename("gemini-speech.wav").build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(PcmToWavConverter.pcmToWav(audioData, 24000.0f, 16, 1, false, false));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint per recuperare l'elenco delle voci disponibili per la sintesi vocale.
     * Restituisce un elenco predefinito di voci Gemini TTS con i loro dettagli,
     * inclusi i nomi, le lingue supportate e le caratteristiche tonali.
     *
     * Voci disponibili:
     * - Puck: voce neutra multilingua (en-US, it-IT, multi)
     * - Charon: voce neutra multilingua (en-US, it-IT, multi)
     * - Kore: voce neutra multilingua (en-US, it-IT, multi)
     * - Fenrir: voce neutra multilingua (en-US, it-IT, multi)
     * - Aoede: voce neutra multilingua (en-US, it-IT, multi)
     *
     * @return ResponseEntity con lista di VoiceInfo contenente dettagli delle voci disponibili
     */
    @GetMapping("/voices")
    public ResponseEntity<List<VoiceInfo>> getAvailableVoices() {
        List<VoiceInfo> voices = Arrays.asList(
                new VoiceInfo("Puck", Arrays.asList("en-US", "it-IT", "multi"), "NEUTRAL"),
                new VoiceInfo("Charon", Arrays.asList("en-US", "it-IT", "multi"), "NEUTRAL"),
                new VoiceInfo("Kore", Arrays.asList("en-US", "it-IT", "multi"), "NEUTRAL"),
                new VoiceInfo("Fenrir", Arrays.asList("en-US", "it-IT", "multi"), "NEUTRAL"),
                new VoiceInfo("Aoede", Arrays.asList("en-US", "it-IT", "multi"), "NEUTRAL"));
        return ResponseEntity.ok(voices);
    }

    /**
     * Costruisce il corpo della richiesta in formato JSON per l'API di Gemini TTS.
     * Configura i parametri per la generazione audio con un singolo speaker,
     * includendo il testo da sintetizzare, la configurazione della voce e le modalità di risposta.
     *
     * Struttura della richiesta:
     * - contents: array con il testo da convertire (con eventuale prompt di stile)
     * - generationConfig: configurazione con modalità audio e parametri voce
     *   - responseModalities: ["AUDIO"] per richiedere output audio
     *   - speechConfig: configurazione voce (nome voce predefinita: "Puck")
     *
     * @param request oggetto GeminiTtsRequest contenente testo, voce selezionata e prompt di stile opzionale
     * @return Map rappresentante il corpo della richiesta JSON per l'API Gemini
     */
    private Map<String, Object> buildGeminiRequest(GeminiTtsRequest request) {
        Map<String, Object> requestBody = new HashMap<>();

        // Contents
        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, Object>> parts = new ArrayList<>();
        Map<String, Object> part = new HashMap<>();

        // Aggiungi prompt se specificato
        String text = request.getText();
        if (request.getStylePrompt() != null && !request.getStylePrompt().isEmpty()) {
            text = request.getStylePrompt() + " " + text;
        }

        part.put("text", text);
        parts.add(part);
        content.put("parts", parts);
        contents.add(content);
        requestBody.put("contents", contents);

        // Configurazione TTS
        Map<String, Object> config = new HashMap<>();
        config.put("responseModalities", Arrays.asList("AUDIO"));

        Map<String, Object> speechConfig = new HashMap<>();
        Map<String, Object> voiceConfig = new HashMap<>();
        Map<String, Object> prebuiltVoiceConfig = new HashMap<>();

        String voiceName = request.getVoice() != null ? request.getVoice() : "Puck";
        prebuiltVoiceConfig.put("voiceName", voiceName);

        voiceConfig.put("prebuiltVoiceConfig", prebuiltVoiceConfig);
        speechConfig.put("voiceConfig", voiceConfig);
        config.put("speechConfig", speechConfig);

        requestBody.put("generationConfig", config);

        return requestBody;
    }

    /**
     * Estrae i dati audio in formato base64 dalla risposta JSON dell'API Gemini.
     * Naviga attraverso la struttura nidificata della risposta per recuperare i dati audio
     * codificati in base64 contenuti nel campo inlineData.
     *
     * Struttura della risposta Gemini:
     * response -> candidates[0] -> content -> parts[0] -> inlineData -> data (base64)
     *
     * @param response mappa contenente la risposta JSON completa dall'API Gemini
     * @return String contenente i dati audio in formato base64, oppure null se l'estrazione fallisce
     */
    @SuppressWarnings("unchecked")
    private String extractAudioData(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");

            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> candidate = candidates.get(0);
                Map<String, Object> content = (Map<String, Object>) candidate.get("content");

                if (content != null) {
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

                    if (parts != null && !parts.isEmpty()) {
                        Map<String, Object> part = parts.get(0);
                        Map<String, Object> inlineData = (Map<String, Object>) part.get("inlineData");

                        if (inlineData != null) {
                            return (String) inlineData.get("data");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Errore nell'estrazione audio: " + e.getMessage());
        }
        return null;
    }

    /**
     * Crea gli header HTTP necessari per le richieste all'API di Gemini.
     * Imposta il Content-Type appropriato per l'invio di dati JSON.
     *
     * @return HttpHeaders oggetto contenente gli header configurati per richieste JSON
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}