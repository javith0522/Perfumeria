package perfume.Perfumeria.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "detalle_orden")
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "perfume_id")
    private perfume perfume;

    private Integer cantidad;
    private Double precioUnitario;

    private Double costoUnitario;


    @ManyToOne
    @JoinColumn(name = "orden_id")
    private Orden orden;

    @Transient
    public double getSubtotal() {
        return  precioUnitario * cantidad;
    }
}
