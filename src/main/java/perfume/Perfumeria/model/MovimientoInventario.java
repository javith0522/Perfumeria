package perfume.Perfumeria.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Immutable;
import perfume.Perfumeria.enums.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_inventario", indexes = {
        @Index(name = "idx_movimiento_perfume_fecha", columnList = "perfume_id, fecha_hora"),
        @Index(name = "idx_movimiento_tipo_fecha", columnList = "tipo, fecha_hora")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Setter
@Getter
@Builder
@Immutable
public class MovimientoInventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "perfume_id", nullable = false)
    private perfume perfume;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "stock_resultante", nullable = false)
    private Integer stockResultante;

    @Column(name = "costo_unitario", nullable = false)
    private BigDecimal costoUnitario;

    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @Column(name = "orden_id")
    private Long ordenId;

    @Column(nullable = false)
    private String usuario;

    @Builder.Default
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();
}
