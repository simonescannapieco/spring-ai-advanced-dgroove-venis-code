package it.venis.ai.spring.demo.advisors;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

import it.venis.ai.spring.demo.model.ModelPricing;

public class OllamaCostSavingsAdvisor implements CallAdvisor {

    public static final Integer ORDER_ID = 1;
    private static final Map<String, ModelPricing> COMMERCIAL_LLM_PRICING = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(OllamaCostSavingsAdvisor.class);

    static {
        /*
         * Commercial API usage costs, updated 2025/10/22, jtlyk.
         */

        /*
         * OpenAI GPT-5
         */
        COMMERCIAL_LLM_PRICING.put("gpt-5-pro", new ModelPricing(15.0f, 120.0f));
        COMMERCIAL_LLM_PRICING.put("gpt-5", new ModelPricing(1.25f, 10.0f));
        COMMERCIAL_LLM_PRICING.put("gpt-5-mini", new ModelPricing(0.25f, 2.0f));
        COMMERCIAL_LLM_PRICING.put("gpt-5-nano", new ModelPricing(0.05f, 0.4f));

        /*
         * Anthropic Claude
         */
        COMMERCIAL_LLM_PRICING.put("claude-4.1-opus", new ModelPricing(15.0f, 75.0f));
        COMMERCIAL_LLM_PRICING.put("claude-4.5-sonnet", new ModelPricing(3.0f, 15.0f));
        COMMERCIAL_LLM_PRICING.put("claude-4.5-haiku", new ModelPricing(1.0f, 5.0f));

        /*
         * Google Gemini
         */
        COMMERCIAL_LLM_PRICING.put("gemini-2.5-pro", new ModelPricing(1.25f, 10.0f));
        COMMERCIAL_LLM_PRICING.put("gemini-2.5-flash", new ModelPricing(0.3f, 2.5f));
        COMMERCIAL_LLM_PRICING.put("gemini-2.5-flash-lite", new ModelPricing(0.1f, 0.4f));
    }

    @Override
    public String getName() {
        return "OllamaCostSavingsAdvisor";
    }

    @Override
    public int getOrder() {
        return ORDER_ID;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        /*
         * Return directly the response object returned by the LLM, but first
         * we extract some metadata and log the required information.
         */
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        ChatResponse chatResponse = chatClientResponse.chatResponse();

        if (chatResponse.getMetadata() != null) {
            /*
             * Extract the usage metadata from the response.
             */
            Usage callUsage = chatResponse.getMetadata().getUsage();

            CostAnalysis analysis = new CostAnalysis();

            for (Map.Entry<String, ModelPricing> entry : COMMERCIAL_LLM_PRICING.entrySet()) {
                String model = entry.getKey();
                ModelPricing pricing = entry.getValue();

                Float inputCost = (callUsage.getPromptTokens().floatValue() / 1000000) * pricing.inputPrice();
                Float outputCost = (callUsage.getCompletionTokens().floatValue() / 1000000) * pricing.outputPrice();
                Float totalCost = inputCost + outputCost;

                analysis.costByModel.put(model, totalCost);

            }

            logger.info("╔══════════════════════════════════════════════════════════════╗");
            logger.info("║               OLLAMA TOKEN USAGE & COST SAVINGS              ║");
            logger.info("╠══════════════════════════════════════════════════════════════╣");
            logger.info(" Richiesta corrente:                                          ");
            logger.info(String.format("   >>> Token di input:     %03d tokens", callUsage.getPromptTokens()));
            logger.info(String.format("   >>> Token di output:    %03d tokens", callUsage.getCompletionTokens()));
            logger.info(String.format("   >>> Token totali:       %03d tokens", callUsage.getTotalTokens()));
            logger.info("╠══════════════════════════════════════════════════════════════╣");
            logger.info(" Confronto costi (se avessi usato servizi a pagamento):       ");

            /*
             * Order by decreasing costs
             */

            analysis.costByModel.entrySet().stream()
                    .sorted(Map.Entry.<String, Float>comparingByValue().reversed())
                    .forEach(entry -> {
                        logger.info(String.format("   >>> %-25s: %.6f USD", entry.getKey(), entry.getValue()));
                    });

            logger.info("╚══════════════════════════════════════════════════════════════╝");
        }

        return chatClientResponse;

    }

    private static class CostAnalysis {
        Map<String, Float> costByModel = new HashMap<>();
    }

}
