package perfume.Perfumeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import perfume.Perfumeria.model.UsuarioBiometrico;

import java.util.Optional;

public interface UsuarioBiometricoRepository extends JpaRepository<UsuarioBiometrico, Long> {
    Optional<UsuarioBiometrico> findByNombre(String nombre);
}
