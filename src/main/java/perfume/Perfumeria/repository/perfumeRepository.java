package perfume.Perfumeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import perfume.Perfumeria.model.Perfume;

import java.util.List;
import java.util.Optional;

public interface perfumeRepository extends JpaRepository<Perfume,Long>
{
    List<Perfume> findByMarca(Long perfumeId);
    List<Perfume> findByCategoria(Long perfumeId, Integer stock);

    List<Perfume> findByCategoriaIgnoreCase(String categoria);
    List<Perfume> findByPrecio(Double min, Double max);
    List<Perfume> findByMarcaContainingIgnoreCase(String marca);
    List<Perfume> findByCategoriaContainingIgnoreCase(String categoria);
    List<Perfume> findBySeccionIgnoreCase(String seccion);
    Optional<Perfume> findByNombreIgnoreCase(String categoria);
    List<Perfume> findByCategoriaAndIntensidadIgnoreCaseAndOcasionIgnoreCase(
            String categoria, String intensidad, String ocasion
    );


    @Query("SELECT p FROM perfume p WHERE " +
            "LOWER(p.categoria) = LOWER(:genero) " +
            "AND (:intensidad = '' OR :intensidad IS NULL OR LOWER(p.intensidad) = LOWER(:intensidad)) " +
            "AND (:ocasion = '' OR :ocasion IS NULL OR LOWER(p.ocasion) = LOWER(:ocasion))")
    List<Perfume> findIdeal(
            @Param("genero") String genero,
            @Param("intensidad") String intensidad,
            @Param("ocasion") String ocasion
    );
}
