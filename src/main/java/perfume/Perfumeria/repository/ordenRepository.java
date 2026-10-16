package perfume.Perfumeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import perfume.Perfumeria.model.Orden;

import java.util.List;

public interface ordenRepository extends JpaRepository<Orden,Integer> {
    List<Orden>findByClienteId(Long usuarioId);
}
