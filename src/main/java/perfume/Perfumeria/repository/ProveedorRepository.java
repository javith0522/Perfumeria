package perfume.Perfumeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import perfume.Perfumeria.model.Proveedor;

import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    List<Proveedor> findByActivoTrueOrderByNombreAsc(String name);

}
