package perfume.Perfumeria.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import perfume.Perfumeria.model.Carrito;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUsuarioIdAndEstado(Long usuarioId, String estado);
}
