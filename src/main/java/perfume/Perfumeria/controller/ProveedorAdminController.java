package perfume.Perfumeria.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import perfume.Perfumeria.Dtos.SoliciitudProveedor;
import perfume.Perfumeria.Service.ProveedorService;
import perfume.Perfumeria.model.Proveedor;

import java.util.List;

@RestController
@RequestMapping("/api/admin/proveedores")
@RequiredArgsConstructor
public class ProveedorAdminController {

    private final ProveedorService proveedorService;

    @GetMapping
    public List<Proveedor> listar(@RequestParam(defaultValue = "true") boolean soloActivos){
        return proveedorService.listar(soloActivos);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Proveedor crear(@Valid @RequestBody SoliciitudProveedor solicitud,
                           Authentication authentication){
        return proveedorService.crear(solicitud, authentication.getName());
    }

    @PatchMapping("/{id}/estado")
    public Proveedor cambiarEstado(@PathVariable Long id,
                                   @RequestParam Boolean activo,
                                   Authentication authentication){
        return proveedorService.cambiarEstado(id, activo, authentication.getName());
    }
}
