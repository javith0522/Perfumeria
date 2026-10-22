package perfume.Perfumeria.enums;

public enum TipoMovimiento {
    ENTRADA,
    SALIDA_VENTA,
    SALIDA_USO,
    SALIDA_DANO,
    SALIDA_PERDIDA,
    AJUSTES_POSTIVO,
    AJUSTES_NEGATIVO;

    public boolean esEntrada(){
        return this == ENTRADA || this == AJUSTES_POSTIVO;
    }

    public boolean esPerdida(){
        return this == SALIDA_PERDIDA || this == SALIDA_DANO;
    }
}
