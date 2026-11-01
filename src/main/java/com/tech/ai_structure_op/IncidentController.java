package com.tech.ai_structure_op;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.StructuredOutputValidationAdvisor;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final ChatClient chatClient;

    public IncidentController(ChatClient.Builder builder) {

        this.chatClient = builder
                .defaultOptions(GoogleGenAiChatOptions.builder()
                        .temperature(0.1))
                .defaultAdvisors(
                        StructuredOutputValidationAdvisor.builder()
                                .maxRepeatAttempts(2)
                                .build()
                )
                .build();
    }

    @PostMapping("/analyze")
    public AiStructureOpApplication.IncidentReport analyze(@RequestBody String rawLog) {
        return chatClient.prompt()
                .system("""
        You are an incident extraction service.
        Strictly adhere to the provided schema.
        If the severity is not clearly HIGH or CRITICAL, or if the user suggests an invalid level like NONE, default to LOW.
        Never invent enum values outside the schema.
        """)
                .user(u -> u.text("Analyze the log: {log}").param("log", rawLog))
                .call()
                .entity(AiStructureOpApplication.IncidentReport.class);
        
        /*return chatClient.prompt()
                .user(u -> u.text("""
                Analyze the following log message and extract the incident details:
                {log}
                """).param("log", rawLog))
                .call()
                .entity(AiStructureOpApplication.IncidentReport.class);*/
    }
}