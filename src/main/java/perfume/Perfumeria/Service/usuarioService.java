package perfume.Perfumeria.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import perfume.Perfumeria.model.Usuario;
import perfume.Perfumeria.repository.usuarioRepostery;

@Service
public class usuarioService {

    @Autowired
    private usuarioRepostery usuarioRepo;

    @Transactional
    public Usuario registrarUsuario(Usuario nuevoUsuario){

        if(usuarioRepo.findByEmail(nuevoUsuario.getEmail()).isPresent()){
            throw new RuntimeException("Usuario ya existe");
        }

        return usuarioRepo.save(nuevoUsuario);
    }

    public Usuario login(String email, String password){
        return usuarioRepo.findByEmailAndPassword(email,password).orElseThrow(() -> new  RuntimeException("Correo o contraseña incorreta"));

    }
}
