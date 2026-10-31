package perfume.Perfumeria.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import perfume.Perfumeria.Dtos.*;
import perfume.Perfumeria.Service.ConsultaInventarioService;
import perfume.Perfumeria.Service.ServicioInventario;
import perfume.Perfumeria.enums.TipoMovimiento;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/inventario")
@RequiredArgsConstructor
public class MovimientoController {

    private final ConsultaInventarioService consultaInventarioService;
    private final ServicioInventario servicioInventario;

    @PostMapping("/entradas")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoDto entrada(@Valid @RequestBody SolicitudEntrada solicitud,
                                 Authentication authentication){
        return servicioInventario.registrarMovimiento(solicitud, authentication.getName());
    }

    @PostMapping("/salidas")
    @ResponseStatus(HttpStatus.CREATED)
    public  MovimientoDto salida(@Valid @RequestBody SolicitudSalida solictud, Authentication authentication){

        return servicioInventario.registrarSalida( solictud, authentication.getName());
    }

    @PostMapping("/ajuste")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoDto ajuste(@Valid @RequestBody SolicitudAjuste solicitud, Authentication authentication){
        return servicioInventario.ajustarConteo(solicitud, authentication.getName());
    }

    @GetMapping("/movimientos")
    public PaginaRespuesta<MovimientoDto> historial(
            @RequestParam(required = false) Long perfumeId,
            @RequestParam(required = false)TipoMovimiento tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int size

            ){
        return consultaInventarioService.historia(perfumeId, tipo, desde, hasta, pagina, size);
    }
}
