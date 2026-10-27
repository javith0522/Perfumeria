package perfume.Perfumeria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "inventario_perfumes")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_unico", nullable = false)
    private Long codigoUnico;

    @Column(name = "precio_compras", nullable = false)
    private BigDecimal precioCompra;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @Column(name = "costo_promedio", nullable = false)
    private BigDecimal costoPromedio = BigDecimal.ZERO;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 5;

    @Column(nullable = false)
    private Boolean estado = true;

    @ManyToOne
    @JoinColumn(name = "perfume_id", nullable = false, unique = true)
    private Perfume perfume;

}
