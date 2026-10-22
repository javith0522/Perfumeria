package perfume.Perfumeria.Dtos;

import perfume.Perfumeria.enums.TipoMovimiento;
import perfume.Perfumeria.model.MovimientoInventario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MovimientoDto(
        Long id,
        Long perfumeId,
        String perfumeNombre,
        String marca,
        TipoMovimiento tipo,
        Integer cantidad,
        Integer stockResultante,
        BigDecimal costoUnicario,
        BigDecimal valorMovimiento,
        String motivo,
        String proveedor,
        Long ordenId,
        String usuario,
        LocalDateTime fecha


) {
    public static MovimientoDto de(MovimientoInventario m){
        BigDecimal valor = m.getCostoUnitario() == null ? null
                : m.getCostoUnitario().multiply(BigDecimal.valueOf(Math.abs(m.getCantidad())));
        return new MovimientoDto(
                m.getId(),
                m.getPerfume().getId(),
                m.getPerfume().getNombre(),
                m.getPerfume().getMarca(),
                m.getTipo(),
                m.getCantidad(),
                m.getStockResultante(),
                m.getCostoUnitario(),
                valor,
                m.getMotivo(),
                m.getProveedor() == null ? null : m.getProveedor().getNombre(),
                m.getOrdenId(),
                m.getUsuario(),
                m.getFechaHora()
        );
    }
}
