package perfume.Perfumeria.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private usuario cliente;

    private Double total;
    private LocalDateTime fecha = LocalDateTime.now() ;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orden_id")
    private List<detalleOrden> detalles;



}
