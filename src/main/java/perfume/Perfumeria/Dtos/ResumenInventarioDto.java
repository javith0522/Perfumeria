package perfume.Perfumeria.Dtos;

import java.math.BigDecimal;

public record ResumenInventarioDto (
        int totalProductos,
        long productosActivo,
        long unidadesTotales,
        BigDecimal valorInventarioCosto,
        BigDecimal valorInventarioVenta,
        BigDecimal utilidadPotencial,
        long productosAgotados,
        long productosStockBajos
){
}
