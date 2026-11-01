# AI Structure Ops

AI Structure Ops is a Spring Boot service that analyzes incident log text with a generative AI model and extracts a structured incident report.

## Overview

This project exposes a REST endpoint that accepts raw log text and returns a validated incident payload containing:

- severity
- affected service
- summary
- whether the incident requires immediate paging

The service uses Spring AI with the Google GenAI model and enforces a strict schema using `StructuredOutputValidationAdvisor`.

## Project structure

- `src/main/java/com/tech/ai_structure_op/AiStructureOpApplication.java` - application bootstrap and schema definition
- `src/main/java/com/tech/ai_structure_op/IncidentController.java` - REST controller that calls the AI model
- `src/main/resources/application.yml` - runtime configuration and model settings
- `src/test/java/com/tech/ai_structure_op/AiStructureOpApplicationTests.java` - basic Spring Boot smoke test

## Features

- AI-powered incident extraction from plain text logs
- Strict output schema validation
- Severity normalization to `LOW`, `MEDIUM`, `HIGH`, or `CRITICAL`
- REST API endpoint for integration into operations tooling
- Spring Boot 3 / Java 27 setup

## Prerequisites

Before running the application, ensure you have:

- Java 27
- Maven or the bundled Maven wrapper (`./mvnw`)
- A Google Gemini API key

## Configuration

Set your API key as an environment variable:

```bash
export GEMINI_API_KEY="your_api_key_here"
```

On Windows PowerShell:

```powershell
$env:GEMINI_API_KEY = "your_api_key_here"
```

The model configuration is set in `src/main/resources/application.yml`:

```yaml
spring:
  ai:
    google:
      genai:
        api-key: ${GEMINI_API_KEY}
        chat:
          model: gemini-3.8-flash
```

## Run locally

### Using Maven wrapper

```bash
./mvnw spring-boot:run
```

### Build the application

```bash
./mvnw clean package
```

Then run the packaged jar:

```bash
java -jar target/ai-structure-op-0.0.1-SNAPSHOT.jar
```

## API usage

### Endpoint

```http
POST /api/incidents/analyze
Content-Type: text/plain
```

### Example request

```bash
curl -X POST http://localhost:8080/api/incidents/analyze \
  -H "Content-Type: text/plain" \
  --data-raw "Database connection pool exhausted in payments API. Error started 10 minutes ago and the service is failing for all requests."
```

### Example response

```json
{
  "severity": "HIGH",
  "affectedService": "payments API",
  "summary": "Database connection pool exhausted causing service-wide request failures in the payments API.",
  "requiresImmediatePage": true
}
```

## Validation behavior

The AI output is constrained to the schema defined in `AiStructureOpApplication.IncidentReport`:

- `severity`: `LOW`, `MEDIUM`, `HIGH`, or `CRITICAL`
- `affectedService`: required, 2-50 characters
- `summary`: required
- `requiresImmediatePage`: boolean flag

If the model does not provide a valid severity, the service defaults to `LOW` as defined in the system prompt.

## Testing

```bash
./mvnw test
```

## Notes

This project is a lightweight Spring AI proof of concept for structured incident extraction. It is designed to be extended with additional routing, persistence, alerting, or downstream incident management integrations.
