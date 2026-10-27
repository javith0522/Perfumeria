package perfume.Perfumeria.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    private Integer cantidad;

    private Double precioUnitario;

    public ItemCarrito(Carrito carrito, Perfume perfume, Integer cantidad) {
        this.carrito = carrito;
        this.perfume = perfume;
        this.cantidad = cantidad;
        this.precioUnitario = perfume.getPrecio();
    }

    @Transient
    public double getSubtotal() {
        return precioUnitario * cantidad;
    }

}
