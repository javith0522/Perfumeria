package perfume.Perfumeria.model;

import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class MovimientoInventario {
}
