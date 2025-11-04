package it.venis.ai.spring.demo.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class TimeTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTools.class);

    @Tool(name="getCurrentLocalTime", description = "Ottieni l'orario corrente nella timezone dell'utente.")
    String getCurrentLocalTime() {

        LOGGER.info("Ritorno dell'orario corrente nella timezone dell'utente");
        return LocalTime.now().toString();

    }

    @Tool(name = "getCurrentTime",
            description = "Ottieni l'orario corrente nella timezone specificata.")
    public String getCurrentTime(@ToolParam(
            description = "Valore che rappresenta la timezone.") String timeZone) {

        LOGGER.info("Ritorno dell'orario corrente nella timezone {}", timeZone);
        return LocalTime.now(ZoneId.of(timeZone)).toString();
        
    }
}