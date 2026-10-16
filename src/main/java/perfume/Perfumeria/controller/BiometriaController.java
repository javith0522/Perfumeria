package perfume.Perfumeria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import perfume.Perfumeria.model.Usuario;
import perfume.Perfumeria.model.UsuarioBiometrico;
import perfume.Perfumeria.repository.UsuarioBiometricoRepository;
import perfume.Perfumeria.repository.usuarioRepostery;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class BiometriaController {

    @Autowired
    private UsuarioBiometricoRepository usuarioBiometricoRepository;

    @Autowired
    private usuarioRepostery usuerioRep;

    @Value("${motor.biometrico.url:http://127.0.0.1:8000}")
    private String motorBiometricoBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/identificar")
    public ResponseEntity<?> identificarRostro(@RequestParam("imagen") MultipartFile imagen) {

        if (imagen == null || imagen.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("reconocido", false, "motivo", "No se envio ninguna imagen"));
        }

        List<UsuarioBiometrico> usuarios = usuarioBiometricoRepository.findAll();

        if (usuarios.isEmpty()) {
            return ResponseEntity.ok(Map.of("reconocido", false, "motivo", "No hay usuarios registrados todavia"));
        }

        try {
            List<Map<String, String>> candidatos = new ArrayList<>();
            for (UsuarioBiometrico u : usuarios) {
                candidatos.add(Map.of(
                        "nombre", u.getNombre(),
                        "embedding", u.getFaceEmbedding()
                ));
            }
            String candidatosJson = objectMapper.writeValueAsString(candidatos);



            MultiValueMap<String, Object> campos = new LinkedMultiValueMap<>();
            campos.add("candidatos", candidatosJson);

            Map<String, Object> resultado = llamarMotorMultipart("/api/identificar", imagen, campos);

            if (Boolean.TRUE.equals(resultado.get("reconocido"))) {
                String nombreReconocido = (String) resultado.get("nombre");

                Optional<UsuarioBiometrico> match = usuarios.stream()
                        .filter(u -> u.getNombre().equals(nombreReconocido))
                        .findFirst();

                if (match.isPresent() && match.get().getUsuario() != null) {
                    resultado.put("usuarioId", match.get().getUsuario().getId());
                }
            }

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al identificar el rostro: " + e.getMessage()));
        }
    }


    @PostMapping("/registrar")
    public ResponseEntity<?> registrarRostro(
            @RequestParam("nombre") String nombre,
            @RequestParam("imagen") MultipartFile imagen) {

        if (nombre == null || nombre.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("exito", false, "motivo", "El nombre no puede estar vacio"));
        }

        if (imagen == null || imagen.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("exito", false, "motivo", "No se envio ninguna imagen"));
        }

        if (usuarioBiometricoRepository.findByNombre(nombre).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("exito", false, "motivo", "Ya existe un Usuario registrado con ese nombre"));
        }

        try {
            Map<String, Object> resultado = llamarMotorMultipart("/api/registrar", imagen, new LinkedMultiValueMap<>());

            boolean exito = Boolean.TRUE.equals(resultado.get("exito"));

            if (!exito) {
                return ResponseEntity.badRequest().body(resultado);
            }

            String embedding = (String) resultado.get("embedding");

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setEmail(nombre.toLowerCase().replaceAll("\\s+", "") + "_" + System.currentTimeMillis() + "@noir.local");
            usuerioRep.save(nuevoUsuario);

            UsuarioBiometrico nuevoBiometrico = new UsuarioBiometrico();
            nuevoBiometrico.setNombre(nombre);
            nuevoBiometrico.setFaceEmbedding(embedding);
            nuevoBiometrico.setFechaRegistro(LocalDateTime.now());
            nuevoBiometrico.setUsuario(nuevoUsuario);

            usuarioBiometricoRepository.save(nuevoBiometrico);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("exito", true, "mensaje", "Usuario registrado correctamente"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al registrar el rostro: " + e.getMessage()));
        }
    }


    private Map<String, Object> llamarMotorMultipart(String path, MultipartFile imagen, MultiValueMap<String, Object> camposExtra) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("imagen", new ByteArrayResource(imagen.getBytes()) {
            @Override
            public String getFilename() {
                return imagen.getOriginalFilename() != null ? imagen.getOriginalFilename() : "imagen.jpg";
            }
        });
        body.addAll(camposExtra);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> respuesta = restTemplate.postForEntity(motorBiometricoBaseUrl + path, requestEntity, Map.class);

        if (!respuesta.getStatusCode().is2xxSuccessful() || respuesta.getBody() == null) {
            throw new RuntimeException("El motor biometrico no respondio correctamente");
        }

        return respuesta.getBody();
    }
}