package perfume.Perfumeria.Dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SolicitudEntrada(@NotNull Long perfumeId,
                               @NotNull @Positive Integer cantidad,
                               @NotNull @Positive BigDecimal costoUnitario,
                               @NotNull Long proveedorId,
                               String nota) {
}
