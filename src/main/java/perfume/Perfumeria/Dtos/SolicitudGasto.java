package perfume.Perfumeria.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import perfume.Perfumeria.enums.CategoriaGasto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SolicitudGasto (
        @NotNull LocalDate fecha,
        @NotBlank String descripcion,
        @NotNull @Positive BigDecimal valor,
        @NotNull CategoriaGasto categoria
        ){
}
