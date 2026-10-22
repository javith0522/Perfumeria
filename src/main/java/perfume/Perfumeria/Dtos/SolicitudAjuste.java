package perfume.Perfumeria.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record SolicitudAjuste(@NotNull Long perfumeId,
                              @NotNull @PositiveOrZero Integer stockContado,
                              @NotBlank String motivo) {
}
