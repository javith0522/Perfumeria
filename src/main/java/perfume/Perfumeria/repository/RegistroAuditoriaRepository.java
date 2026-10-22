package perfume.Perfumeria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import perfume.Perfumeria.model.RegistroAduitoria;
import java.time.LocalDateTime;

public interface RegistroAuditoriaRepository extends Repository<RegistroAduitoria, Long> {

    RegistroAduitoria save(RegistroAduitoria registroAduitoria);

    @Query("""

            select r from RegistroAduitoria r
              where (:usuario is null or  r.usuario = :usuario)
              and (:accion is null or r.accion = :accion)
              and r.fechaHora between :desde and :hasta
              order by r.fechaHora desc 
       """)
       Page<RegistroAduitoria> buscar(@Param("usuario") String usuario,
                                      @Param("accion") String accion,
                                      @Param("desde") LocalDateTime desde,
                                      @Param("hasta") LocalDateTime hasta,
                                      Pageable pageable
                                      );

}