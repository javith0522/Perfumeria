package perfume.Perfumeria.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class detalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private perfume perfume;

    private Integer _cantidad;
    private Double precioUnitario;

    @ManyToOne
    private perfume producto;

    @ManyToOne
    @JoinColumn(name = "orden_id")
    private orden orden;
}
