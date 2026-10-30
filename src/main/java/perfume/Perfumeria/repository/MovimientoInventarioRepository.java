package perfume.Perfumeria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import perfume.Perfumeria.enums.TipoMovimiento;
import perfume.Perfumeria.model.MovimientoInventario;



import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimientoInventarioRepository extends Repository<MovimientoInventario,Long> {

    MovimientoInventario save(MovimientoInventario movimientoInventario);

    Optional<MovimientoInventario> findById(Long id);

    @Query("""
select m from MovimientoInventario m
where (:perfumeId is null or m.perfume.id = :perfume)
and (:tipo is null or m.tipo = :tipo)
and m.fechaHora between :desde and :hasta
order by m.fechaHora desc
""")
    Page<MovimientoInventario> buscar(@Param("perfumeId") Long perfumeId,
                                      @Param("tipo")TipoMovimiento tipo,
                                      @Param("desde")LocalDateTime desde,
                                      @Param("hasta") LocalDateTime hasta,
                                      Pageable pageable);

    List<MovimientoInventario> findByTipoInAndFechaHoraBetween(List<TipoMovimiento> tipo, LocalDateTime desde, LocalDateTime hasta);

    @Query("""
select m.tipo, coalesce(sum(m.cantidad), 0)
from MovimientoInventario m 
where m.perfume.id = :perfumeId
group by m.tipo
""")
    List<Object[]> totalesPorTipo(@Param("perfumeId") Long perfume);

    List<MovimientoInventario> findTop10ByPerfumeIdOrderByFechaHoraDesc(Long perfume);

    @Query("""
select max(m.fechaHora) from MovimientoInventario m 
where m.perfume.id = :perfumeId and m.tipo = :tipo
""")
    Optional<LocalDateTime> ultimaFechaPorTipo(@Param("perfumeId") Long perfumeId, @Param("tipo") TipoMovimiento tipo);
}
