package perfume.Perfumeria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "carrito")
@AllArgsConstructor
@NoArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;

    private String estado = "ACTIVO";

    LocalDateTime fechaCreacion = LocalDateTime.now();
    LocalDateTime fechaActualizacion = LocalDateTime.now();

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemCarrito> items = new ArrayList<>();

    public Carrito(Long usuarioId) {
        this.usuarioId = usuarioId;
        this.estado = "ACTIVO";
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.items = new ArrayList<>();
    }

    @Transient
    public double getTotal() {
        return items.stream()
                .mapToDouble(i -> i.getPrecioUnitario() * i.getCantidad()).sum();
    }
}
