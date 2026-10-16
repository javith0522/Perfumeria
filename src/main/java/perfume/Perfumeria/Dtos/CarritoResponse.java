package perfume.Perfumeria.Dtos;

import java.time.LocalDateTime;
import java.util.List;

public record CarritoResponse(
        Long id,
        String estado,
        LocalDateTime fechaActualizacion,
        List<ItemCarritoDto> items,
        double total
) {
}
