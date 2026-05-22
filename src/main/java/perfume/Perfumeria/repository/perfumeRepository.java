package perfume.Perfumeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import perfume.Perfumeria.model.perfume;
import perfume.Perfumeria.model.orden;
import java.util.List;
import java.util.Optional;

public interface perfumeRepository extends JpaRepository<perfume,Long>
{
    List<perfume> findByMarca(Long perfumeId);
    List<perfume> findByCategoria(Long perfumeId, Integer stock);

    List<perfume> findByCategoriaIgnoreCase(String categoria);
    List<perfume> findByPrecio(Double min, Double max);
    List<perfume> findByMarcaContainingIgnoreCase(String marca);
    List<perfume> findByCategoriaContainingIgnoreCase(String categoria);
    List<perfume> findBySeccionIgnoreCase(String seccion);
    Optional<perfume> findByNombreIgnoreCase(String categoria);
    List<perfume> findByCategoriaAndIntensidadIgnoreCaseAndOcasionIgnoreCase(
            String categoria, String intensidad, String ocasion
    );


    @Query("SELECT p FROM perfume p WHERE " +
            "LOWER(p.categoria) = LOWER(:genero) " +
            "AND (:intensidad = '' OR :intensidad IS NULL OR LOWER(p.intensidad) = LOWER(:intensidad)) " +
            "AND (:ocasion = '' OR :ocasion IS NULL OR LOWER(p.ocasion) = LOWER(:ocasion))")
    List<perfume> findIdeal(
            @Param("genero") String genero,
            @Param("intensidad") String intensidad,
            @Param("ocasion") String ocasion
    );
}
