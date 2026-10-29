package perfume.Perfumeria.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import perfume.Perfumeria.Dtos.*;
import perfume.Perfumeria.Service.ConsultaInventarioService;
import perfume.Perfumeria.Service.ProductoInventarioService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/inventario")
@RequiredArgsConstructor
public class InventarioAdminController {

    private final ProductoInventarioService productoInventarioService;
    private final ConsultaInventarioService consultaInventarioService;

    @GetMapping("/resumen")
    public ResumenInventarioDto resumen() {
        return consultaInventarioService.resumen();
    }

    @GetMapping("/productos")
    public PaginaRespuesta<ItemInventarioDto> listar(@RequestParam(defaultValue = "") String texto,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size){
        return consultaInventarioService.listar(texto, page, size);
    }

    @GetMapping("/productos/{perfumeId}")
    public DetalleInventarioDto detalle(@PathVariable Long perfumeId){
        return consultaInventarioService.obtenerDetalle(perfumeId);
    }

    @PostMapping("/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public DetalleInventarioDto crear(@Valid @RequestBody SolicitarProductoDto solicitud,  Authentication authentication){
        return productoInventarioService.crear(solicitud, authentication.getName());
    }

    @PutMapping("/productos/{perfumeId}")
    public DetalleInventarioDto editar(@PathVariable Long perfumeId,
                                       @Valid @RequestBody SolicitarProductoDto solicitud,
                                       Authentication authentication){
        return productoInventarioService.editar(perfumeId, solicitud, authentication.getName());
    }

     @PatchMapping("/productos/{perfumeId}/estado")
    public DetalleInventarioDto cambiarEstado(@PathVariable Long perfumeId,
                                              @RequestParam Boolean activo,
                                              Authentication authentication){
        return productoInventarioService.cambiarEstado(perfumeId, activo, authentication.getName());
     }

     @GetMapping("/alertas/stock-bajo")
    public List<AlertaStock> alertaStockBajo(){
        return consultaInventarioService.alertaStockBajo();
     }

}
