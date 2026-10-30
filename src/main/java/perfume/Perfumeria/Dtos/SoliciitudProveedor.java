package perfume.Perfumeria.Dtos;

import jakarta.validation.constraints.NotBlank;

public record SoliciitudProveedor(
        @NotBlank String nombre,
        String contacto,
        String telefono
) {
}
