package perfume.Perfumeria.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import perfume.Perfumeria.Dtos.ActulizarCantidadaRequest;
import perfume.Perfumeria.Dtos.AgregarItemsCarritoDTO;
import perfume.Perfumeria.Dtos.CarritoResponse;
import perfume.Perfumeria.Service.CarritoService;
import perfume.Perfumeria.model.Orden;
import perfume.Perfumeria.repository.CarritoRepository;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/{usuarioId}")
    public CarritoResponse verCarrito(@PathVariable Long usuarioId) {
        return carritoService.obtenerCarrito(usuarioId);
    }

    @PostMapping("/{usuarioId}/agregar")
    public CarritoResponse agregarItem(@PathVariable Long usuarioId,
                                       @Valid @RequestBody AgregarItemsCarritoDTO request) {
        return carritoService.agregarItemCarrito(usuarioId, request.perfumeId(), request.cantidad());
    }

    @PutMapping("/{usuarioId}/item/{itemId}")
    public CarritoResponse actualizarCantidad(@PathVariable Long usuarioId,
                                              @PathVariable Long itemId, @Valid @RequestBody ActulizarCantidadaRequest
                                              request) {
        return carritoService.actualizarCantidad(usuarioId, itemId, request.cantidad());
    }

    @DeleteMapping("/{usuarioId}/item/{itemId}")
    public CarritoResponse eliminarItem(@PathVariable Long usuarioId, @PathVariable Long itemId) {
        return carritoService.eliminarItemCarrito(usuarioId, itemId);
    }

    @DeleteMapping("/{usuarioId}/vaciar")
    public void vaciarCarrito(@PathVariable Long usuarioId) {
         carritoService.vaciarCarrito(usuarioId);
    }

    @PostMapping("/{usuarioId}/checkout")
    public Orden checkout(@PathVariable Long usuarioId) {
        return carritoService.finalizarCompra(usuarioId);
    }
}
