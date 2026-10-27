package perfume.Perfumeria.Dtos;

import perfume.Perfumeria.enums.EstadoStock;
import perfume.Perfumeria.model.Inventario;
import perfume.Perfumeria.model.Perfume;

import java.math.BigDecimal;

public record ItemInventarioDto(
        Long perfumeId,
        Long codigoUnico,
        String nombre,
        String marca,
        Integer presentacionMl,
        Integer stock,
        Integer stockMinimo,
        EstadoStock estadoStock,
        BigDecimal precioCompra,
        BigDecimal costoPromedio,
        BigDecimal precioVenta,
        BigDecimal valorInventarioCosto,
        Boolean activo
) {
    public static ItemInventarioDto de(Inventario i) {
        Perfume p =  i.getPerfume();
        return  new ItemInventarioDto(
                p.getId(),
                i.getCodigoUnico(),
                p.getNombre(),
                p.getMarca(),
                p.getPresentacionMl(),
                i.getStock(),
                i.getStockMinimo(),
                EstadoStock.de(i),
                i.getPrecioCompra(),
                i.getCostoPromedio(),
                BigDecimal.valueOf(p.getPrecio()),
                i.getCostoPromedio().multiply(BigDecimal.valueOf(i.getStock())),
                i.getEstado()

        );
    }
}
