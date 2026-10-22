package perfume.Perfumeria.Dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record SolicitarProductoDto(
        @NotNull @Positive Long codigoUnico,
        @NotNull String nombre,
        @NotNull String marca,
        @NotNull @Positive Integer presentacionMl,
        @NotNull @PositiveOrZero BigDecimal precioCompra,
        @NotNull @Positive BigDecimal precioVenta,
        @NotNull @PositiveOrZero Integer stockMinimo,
        @PositiveOrZero Integer stockInicial,
        String genero,
        String categoria,
        String descipcion,
        String imagen
        ) {
}
