package perfume.Perfumeria.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Immutable;

import java.time.LocalDateTime;

@Entity
@Immutable
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "registro_auditorio", indexes = {
        @Index(name = "idx_auditoria_fecha", columnList = "fecha_hora"),
        @Index(name = "idx_auditoria_usuario", columnList = "usuario")
})
public class RegistroAduitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora =  LocalDateTime.now();

    @Column(nullable = false)
    private String usuario;

    @Column(nullable = false)
    private String accion;

    @Column(length = 60)
    private String entidad;

    @Column(name = "entidad_id")
    private Long entidadId;

    @Column(columnDefinition = "TEXT")
    private String detalle;

    @Column(length = 60)
    private String ip;
}
