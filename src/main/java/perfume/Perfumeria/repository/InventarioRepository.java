package perfume.Perfumeria.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import perfume.Perfumeria.model.Inventario;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findByPerfumeId(Long perfumeId);

    Optional<Inventario> findByCodigoUnico(Long codigoUnico);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("Select i from Inventario where i.perfume.id = :perfumeId")
    Optional<Inventario> buscarParaActualizar(@Param("perfumeId") Long perfumeId);

    @Query("select i from Inventario i where i.estado = true and i.stock <= i.stockMinimo\n" +
            "order by i.stock desc")
    List<Inventario> buscarStockBajo();

    @Query("""
select i from Inventario i join i.perfume p
where lower(p.nombre) like lower(concat('%', :texto, '%'))
or lower(p.marca) like lower(concat('%', : texto, '%'))
or cast(i.codigoUnico as string) like concat('%', :texto, '%')
""")
    Page<Inventario> buscar(@Param("texto") String texto, Pageable pageable);
}
