package perfume.Perfumeria.Dtos;

import perfume.Perfumeria.enums.EstadoStock;

public record AlertaStock(
        Long perfumeId,
        Long codigoUnico,
        String nombre,
        String marca,
        Integer stock,
        Integer stockMinimo,
        Integer faltante,
        EstadoStock estado
) {
}
