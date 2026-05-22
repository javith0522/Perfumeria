package perfume.Perfumeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import perfume.Perfumeria.model.orden;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ordenRepository extends JpaRepository<orden,Integer> {
    List<orden>findByClienteId(Long usuarioId);
}
