package perfume.Perfumeria.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TtsController {

    @Value("${elevenlabs.api.key}")
    private String apiKey;

    @PostMapping("/tts")
    public ResponseEntity<byte[]> tts(@RequestBody Map<String, String> body) throws Exception {
        String texto = body.get("texto");
        String voiceId = "imFXYz8XIletRKLZZQaA";

        HttpClient client = HttpClient.newHttpClient();


        String json = "{"
                + "\"text\":\"" + texto + "\","
                + "\"model_id\":\"eleven_multilingual_v2\","
                + "\"voice_settings\":{"
                + "\"stability\":0.6,"
                + "\"similarity_boost\":0.75,"
                + "\"speed\":0.88"
                + "}}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.elevenlabs.io/v1/text-to-speech/" + voiceId))
                .header("xi-api-key", apiKey)
                .header("Content-Type", "application/json")
                .header("Accept", "audio/mpeg")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        System.out.println("ElevenLabs status: " + response.statusCode());

        if (response.statusCode() != 200) {
            // Si hay error, imprimimos el cuerpo para saber qué pasó (ej: falta de saldo o API Key mal)
            System.out.println("Error body: " + new String(response.body()));
            return ResponseEntity.status(response.statusCode()).build();
        }

        return ResponseEntity.ok()
                .header("Content-Type", "audio/mpeg")
                .body(response.body());
    }
}
