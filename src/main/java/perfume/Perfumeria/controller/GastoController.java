package perfume.Perfumeria.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import perfume.Perfumeria.Dtos.ResumenGastoDto;
import perfume.Perfumeria.Dtos.SolicitudGasto;
import perfume.Perfumeria.Service.GastoService;
import perfume.Perfumeria.model.Gasto;
import perfume.Perfumeria.repository.GastoRepository;

import java.time.LocalDate;
import java.util.Date;

@RestController("/api/admin/gasto")
@RequestMapping
public class GastoController {

    @Autowired
    GastoRepository gastoRepository;

    @Autowired
    GastoService gastoService;

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Gasto registrar(@Valid @RequestBody SolicitudGasto solicitud, Authentication authentication) {
        return gastoService.registar(solicitud, authentication.getName());
    }

    @GetMapping
    public ResumenGastoDto resumen(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,
            @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta
    ){
        return gastoService.resumen(desde, hasta);
    }
}
