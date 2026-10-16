package perfume.Perfumeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import perfume.Perfumeria.model.Usuario;

import java.util.Optional;

@Repository
public interface usuarioRepostery extends JpaRepository<Usuario, Long> {
    Optional<Usuario>findByEmail(String email);
    Optional<Usuario>findByPassword(String email, String password);
    Optional<Usuario> findByEmailAndPassword(String email, String password);

}
