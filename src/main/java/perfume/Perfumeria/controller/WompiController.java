package perfume.Perfumeria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import perfume.Perfumeria.Service.CarritoService;
import perfume.Perfumeria.model.Orden;

import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class WompiController {

    @Value("${wompi.llave.privada}")
    private String llavePrivada;

    @Value("${wompi.api.url}")
    private String wompiUrl;

    @Autowired
    private CarritoService carritoService;

    private final RestTemplate restTemplate = new RestTemplate();

    public static class ConfirmarPagoRequest {

        private String transaccionId;
        private Long usuarioId;

        public String getTransactionId() { return transaccionId; }
        public void setTransactionId(String transactionId) { this.transaccionId = transaccionId; }
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    }

    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmarPago(@RequestBody ConfirmarPagoRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(llavePrivada);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> respuestaWompi = restTemplate.exchange(wompiUrl + "/transaccion" + request.getTransactionId(),
                    HttpMethod.GET, entity, Map.class);

            Map<String, Object> cuerpo = (Map<String, Object>) respuestaWompi.getBody().get("data");
            String estado = (String) cuerpo.get("status");

            if (!"APPROVED".equals(estado)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "exito", false,
                        "motivo", "El pago no fue aprobado" + estado
                ));
            }

            try {
                Orden orden = carritoService.finalizarCompra(request.getUsuarioId());
                return ResponseEntity.ok(Map.of("exito", true, "orden", orden));
            }catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                        "exito", false,
                        "motivo", "Pago recibido pero hubo un problema con el inventario"
                ));
            }


        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("exito", false, "motivo", "Hubo un error al finalizar compra"));
        }

    }

}