package perfume.Perfumeria.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perfume.Perfumeria.Dtos.ResumenGastoDto;
import perfume.Perfumeria.Dtos.SolicitudGasto;
import perfume.Perfumeria.enums.CategoriaGasto;
import perfume.Perfumeria.model.Gasto;
import perfume.Perfumeria.repository.GastoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepository gastoRepository;
    private final AuditariaServices auditariaServices;

    @Transactional
    public Gasto registar(SolicitudGasto solicitud, String usuario){
        Gasto gasto = new Gasto();
        gasto.setFecha(solicitud.fecha());
        gasto.setDescipcion(solicitud.descripcion().trim());
        gasto.setValor(solicitud.valor());
        gasto.setCategoria(solicitud.categoria());
        gasto.setUsuario(usuario);
        gasto = gastoRepository.save(gasto);

        auditariaServices.registrar(usuario, "REGISTRAR_GASTOS","gasto", gasto.getId(),
                solicitud.categoria() + "$" + solicitud.valor() + "_" + solicitud.descripcion().trim());

        return gasto;

    }

    @Transactional(readOnly = true)
    public ResumenGastoDto resumen(LocalDate desde, LocalDate hasta){
        LocalDate hastaFinal = hasta !=  null ? hasta : LocalDate.now();
        LocalDate desdeFinal = desde != null ? desde : hastaFinal.withDayOfMonth(1);

        List<Gasto> gastos = gastoRepository.findByFechaBetweenOrderByFechaDesc(desdeFinal, hastaFinal);
        Map<CategoriaGasto, BigDecimal> porCategoria = new EnumMap<>(CategoriaGasto.class);
        for (CategoriaGasto categoria : CategoriaGasto.values()) porCategoria.put(categoria, BigDecimal.ZERO);
        BigDecimal total = BigDecimal.ZERO;
        for (Gasto gasto : gastos) {
            porCategoria.merge(gasto.getCategoria(), gasto.getValor(), BigDecimal::add);

            total = total.add(gasto.getValor());
        }
        return new ResumenGastoDto(desdeFinal, hastaFinal, total, porCategoria, gastos);
    }

}
