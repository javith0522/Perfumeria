package perfume.Perfumeria.Dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import perfume.Perfumeria.enums.TipoMovimiento;

public record SolicitudSalida(@NotNull Long perfumeId,
                              @NotNull TipoMovimiento tipo,
                              @NotNull @Positive Integer cantidad,
                              String motivo) {
}
