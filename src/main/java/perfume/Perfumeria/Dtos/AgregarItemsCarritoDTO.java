package perfume.Perfumeria.Dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AgregarItemsCarritoDTO(
        @NotNull(message = "perdumeId es obligatorio")
        Long perfumeId,

        @NotNull(message = "cantidad es obligatoria")
        @Min(value = 1, message = "la cantidad debe ser almenos 1")
        Integer cantidad
) {
}
