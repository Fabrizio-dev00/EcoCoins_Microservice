package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.dto.ValidationResponse;
import com.ecocoins.ecocoins_microservice.exception.BadRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GeminiAIService {

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    @Value("${gemini.api.key:}")
    private String apiKey;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GeminiAIService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Valida si el material en la imagen corresponde al tipo esperado
     */
    public ValidationResponse validateMaterial(String imageBase64, String expectedMaterial) {
        try {
            log.info("🤖 Validando material con Gemini AI - Material esperado: {}", expectedMaterial);

            // Construir el prompt específico
            String prompt = buildValidationPrompt(expectedMaterial);

            // Construir el request body
            Map<String, Object> requestBody = buildGeminiRequest(imageBase64, prompt);

            // Hacer la llamada a Gemini API
            String responseJson = callGeminiAPI(requestBody);

            // Parsear la respuesta
            ValidationResponse validation = parseGeminiResponse(responseJson);

            log.info("✅ Validación completada - Válido: {}, Confianza: {}%",
                    validation.isEsValido(), validation.getConfianza());

            return validation;

        } catch (Exception e) {
            log.error("❌ Error al validar con Gemini API: {}", e.getMessage());
            throw new BadRequestException("Error al procesar la validación con IA: " + e.getMessage());
        }
    }

    /**
     * Construye el prompt según el material esperado
     */
    private String buildValidationPrompt(String expectedMaterial) {
        Map<String, String> criterios = Map.of(
                "Plastico", "botellas PET, envases de plástico, bolsas plásticas limpias",
                "Papel", "hojas de papel, periódicos, revistas, cartón ligero",
                "Vidrio", "botellas de vidrio, frascos, envases de vidrio",
                "Metal", "latas de aluminio, latas de conserva, envases metálicos",
                "Carton", "cajas de cartón, empaques de cartón corrugado"
        );

        String criterio = criterios.getOrDefault(expectedMaterial, "materiales reciclables");

        return String.format("""
                Eres un experto en identificación de materiales reciclables.
                
                Analiza esta imagen y determina si contiene %s (%s).
                
                Responde ÚNICAMENTE con un JSON en este formato exacto (sin markdown, sin ```json):
                {
                  "es_valido": true o false,
                  "confianza": número entre 0 y 100,
                  "material_detectado": "tipo de material que ves",
                  "explicacion": "breve explicación de 1-2 líneas"
                }
                
                Criterios de validación:
                - es_valido: true si la imagen muestra claramente %s
                - confianza: qué tan seguro estás (0-100)
                - material_detectado: describe exactamente lo que ves
                - explicacion: justifica tu decisión brevemente
                
                IMPORTANTE: Responde SOLO con el JSON, sin texto adicional ni markdown.
                """,
                expectedMaterial, criterio, criterio);
    }

    /**
     * Construye el request para Gemini API
     */
    private Map<String, Object> buildGeminiRequest(String imageBase64, String prompt) {
        // Parte de texto
        Map<String, Object> textPart = Map.of(
                "text", prompt
        );

        // Parte de imagen
        Map<String, Object> imagePart = Map.of(
                "inline_data", Map.of(
                        "mime_type", "image/jpeg",
                        "data", imageBase64
                )
        );

        // Estructura del contenido
        Map<String, Object> content = Map.of(
                "parts", List.of(textPart, imagePart)
        );

        // Request completo
        return Map.of(
                "contents", List.of(content),
                "generationConfig", Map.of(
                        "temperature", 0.4,
                        "maxOutputTokens", 300
                )
        );
    }

    /**
     * Llama a la API de Gemini
     */
    private String callGeminiAPI(Map<String, Object> requestBody) throws Exception {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new BadRequestException(
                    "API Key de Gemini no configurada. Agrega 'gemini.api.key' en application.properties"
            );
        }

        String requestJson = objectMapper.writeValueAsString(requestBody);

        // Gemini usa la API key en la URL
        String urlWithKey = GEMINI_API_URL + "?key=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWithKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            log.error("❌ Error en Gemini API: Status {}, Body: {}", response.statusCode(), response.body());
            throw new BadRequestException("Error al comunicarse con Gemini API: " + response.statusCode());
        }

        return response.body();
    }

    /**
     * Parsea la respuesta de Gemini y extrae el JSON
     */
    private ValidationResponse parseGeminiResponse(String responseJson) throws Exception {
        JsonNode root = objectMapper.readTree(responseJson);

        // Gemini estructura: candidates[0].content.parts[0].text
        String contentText = root.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();

        log.debug("📄 Respuesta de Gemini: {}", contentText);

        // Limpiar markdown si existe (```json ... ```)
        String cleanedText = contentText
                .replaceAll("```json\\s*", "")
                .replaceAll("```\\s*", "")
                .trim();

        // Parsear el JSON de validación
        JsonNode validationNode = objectMapper.readTree(cleanedText);

        ValidationResponse validation = new ValidationResponse();
        validation.setEsValido(validationNode.path("es_valido").asBoolean());
        validation.setConfianza(validationNode.path("confianza").asInt());
        validation.setMaterialDetectado(validationNode.path("material_detectado").asText());
        validation.setExplicacion(validationNode.path("explicacion").asText());

        return validation;
    }
}