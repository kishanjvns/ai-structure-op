package com.tech.ai_structure_op;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiStructureOpApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiStructureOpApplication.class, args);
	}

	public record IncidentReport(
			@NotNull(message = "Severity must be LOW, MEDIUM, HIGH, or CRITICAL")
			Severity severity,

			@NotBlank(message = "Affected service cannot be blank")
			@Size(min = 2, max = 50)
			String affectedService,

			@NotBlank(message = "Summary cannot be blank")
			String summary,

			boolean requiresImmediatePage
	) {
		public enum Severity {
			LOW, MEDIUM, HIGH, CRITICAL
		}
	}
}

