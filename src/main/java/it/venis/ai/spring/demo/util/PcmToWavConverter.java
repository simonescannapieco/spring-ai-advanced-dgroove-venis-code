package it.venis.ai.spring.demo.util;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PcmToWavConverter {
    
    /**
     * Converte dati audio PCM in formato WAV.
     * 
     * @param pcmData I dati audio PCM grezzi come array di byte
     * @param sampleRate La frequenza di campionamento in Hz (es. 44100, 48000)
     * @param sampleSizeInBits La profondità in bit (es. 8, 16, 24, 32)
     * @param channels Numero di canali (1 per mono, 2 per stereo)
     * @param signed Se i dati PCM sono con segno
     * @param bigEndian Se i dati PCM sono in formato big-endian
     * @return Contenuto del file WAV come array di byte
     * @throws IOException Se si verifica un errore di I/O durante la conversione
     */
    public static byte[] pcmToWav(byte[] pcmData, float sampleRate, int sampleSizeInBits, 
                                  int channels, boolean signed, boolean bigEndian) throws IOException {
        
        // Crea la specifica del formato audio
        AudioFormat audioFormat = new AudioFormat(
            sampleRate,           // Frequenza di campionamento (Hz)
            sampleSizeInBits,     // Dimensione del campione in bit
            channels,             // Numero di canali
            signed,               // Con segno o senza segno
            bigEndian             // Big-endian o little-endian
        );
        
        // Racchiude i dati PCM in un ByteArrayInputStream
        ByteArrayInputStream pcmInputStream = new ByteArrayInputStream(pcmData);
        
        // Calcola la lunghezza del frame
        long frameLength = pcmData.length / audioFormat.getFrameSize();
        
        // Crea un AudioInputStream dai dati PCM
        AudioInputStream audioInputStream = new AudioInputStream(
            pcmInputStream,
            audioFormat,
            frameLength
        );
        
        // Scrive in un ByteArrayOutputStream come WAV
        ByteArrayOutputStream wavOutputStream = new ByteArrayOutputStream();
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavOutputStream);
        
        // Chiude gli stream
        audioInputStream.close();
        wavOutputStream.close();
        
        return wavOutputStream.toByteArray();
    }
    
    /**
     * Metodo di convenienza con impostazioni predefinite comuni per audio PCM a 16 bit.
     * Presuppone: frequenza di campionamento 44.1kHz, profondità 16 bit, stereo, con segno, little-endian
     * 
     * @param pcmData I dati audio PCM grezzi come array di byte
     * @return Contenuto del file WAV come array di byte
     * @throws IOException Se si verifica un errore di I/O durante la conversione
     */
    public static byte[] pcmToWavDefault(byte[] pcmData) throws IOException {
        return pcmToWav(pcmData, 44100.0f, 16, 2, true, false);
    }
    
    /**
     * Esempio di utilizzo che dimostra la conversione
     */
    public static void main(String[] args) {
        try {
            // Esempio: Crea alcuni dati PCM fittizi (in pratica, questi verrebbero letti da un file)
            byte[] pcmData = new byte[44100 * 2 * 2]; // 1 secondo di audio stereo a 16 bit a 44.1kHz
            
            // Riempie con una semplice onda sinusoidale a scopo dimostrativo
            for (int i = 0; i < pcmData.length / 2; i++) {
                short sample = (short) (Math.sin(2 * Math.PI * 440 * i / 44100.0) * Short.MAX_VALUE * 0.5);
                pcmData[i * 2] = (byte) (sample & 0xFF);
                pcmData[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
            }
            
            // Converte PCM in WAV
            byte[] wavData = pcmToWavDefault(pcmData);
            
            System.out.println("PCM data size: " + pcmData.length + " bytes");
            System.out.println("WAV data size: " + wavData.length + " bytes");
            System.out.println("WAV header size: " + (wavData.length - pcmData.length) + " bytes");
            
            // Ora è possibile scrivere wavData in un file:
            // Files.write(Paths.get("output.wav"), wavData);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}