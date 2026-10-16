package perfume.Perfumeria.Dtos;

public record ItemCarritoDto(
        Long itemId,
        Long perfumeId,
        String nombre,
        String marca,
        String imagen,
        Integer cantidad,
        double precioUnitario,
        double subtotal
) {

    public ItemCarritoDto(Long itemId, Long perfumeId, String nombre, String marca, String imagen,
                          Integer cantidad, double precioUnitario) {
        this(itemId, perfumeId, nombre, marca, imagen, cantidad, precioUnitario, precioUnitario * cantidad);
    }
}
