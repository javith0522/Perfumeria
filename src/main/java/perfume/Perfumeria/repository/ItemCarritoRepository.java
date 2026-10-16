package perfume.Perfumeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import perfume.Perfumeria.model.ItemCarrito;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
}
