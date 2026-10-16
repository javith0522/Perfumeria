package perfume.Perfumeria.enums;

import perfume.Perfumeria.model.Inventario;

public enum EstadoStock {
    AGOTADO,
    BAJO,
    NORMAL,
    INACTIVO;

    public static EstadoStock de(Inventario inventario) {
        if (!Boolean.TRUE.equals(inventario.getEstado())) return  INACTIVO;
        if (inventario.getStock() <= 0) return  AGOTADO;
        if (inventario.getStock() <= inventario.getStockMinimo()) return  BAJO;
        return NORMAL;
    }

}

