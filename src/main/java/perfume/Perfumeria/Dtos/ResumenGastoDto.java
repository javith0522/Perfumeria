package perfume.Perfumeria.Dtos;

import perfume.Perfumeria.enums.CategoriaGasto;
import perfume.Perfumeria.model.Gasto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ResumenGastoDto(
        LocalDate desde,
        LocalDate hasta,
        BigDecimal total,
        Map<CategoriaGasto, BigDecimal> porCategoria,
        List<Gasto> gastos
) {
}
