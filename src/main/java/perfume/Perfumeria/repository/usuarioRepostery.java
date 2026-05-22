package perfume.Perfumeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import perfume.Perfumeria.model.usuario;

import java.util.Optional;

@Repository
public interface usuarioRepostery extends JpaRepository<usuario, Long> {
    Optional<usuario>findByEmail(String email);
    Optional<usuario>findByPassword(String email, String password);
    Optional<usuario> findByEmailAndPassword(String email, String password);

}
