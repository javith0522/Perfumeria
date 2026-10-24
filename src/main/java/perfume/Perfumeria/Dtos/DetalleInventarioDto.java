package perfume.Perfumeria.Dtos;

import perfume.Perfumeria.enums.EstadoStock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DetalleInventarioDto(
        Long perfumeId,
        Long codigoUnico,
        String nombre,
        String marca,
        String genero,
        String categoria,
        String descripcion,
        String imagen,
        Integer presentacionMl,
        Integer descuento,
        BigDecimal precioCompra,
        BigDecimal costoPromedio,
        BigDecimal precioVenta,
        BigDecimal utilidadUnitaria,
        BigDecimal margenPorcentaje,
        Integer stock,
        Integer stockMinimo,
        EstadoStock estadoStock,
        Boolean activo,
        BigDecimal valorInventarioCosto,
        BigDecimal valorInventarioVenta,
        Long unidadesIngresadas,
        Long unidadesVendidas,
        Long unidadesUsadas,
        Long unidadesDanadas,
        Long unidadesPerdidas,
        Long ajustesNetos,
        LocalDateTime ultimaEntrada,
        LocalDateTime ultimaVenta,
        List<MovimientoDto> ultimosMovimientos
) {
}
