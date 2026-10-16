package perfume.Perfumeria.Dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ActulizarCantidadaRequest(
        @NotNull(message = "cantidad obligatoria")
        @Min(value = 0, message = "la cantidad no puede ser negativa")
        Integer cantidad
) {
}
