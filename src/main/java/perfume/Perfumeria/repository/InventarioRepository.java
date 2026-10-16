package perfume.Perfumeria.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import perfume.Perfumeria.model.Inventario;

import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findByPerfumeId(Long perfumeId);

    Optional<Inventario> findByCodigoUnico(Long codigoUnico);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("Select i from Inventario where i.perfume.id = :perfumeId")
    Optional<Inventario> buscarParaActualizar(@Param("perfumeId") Long perfumeId);
}
