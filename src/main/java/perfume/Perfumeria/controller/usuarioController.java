package perfume.Perfumeria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import perfume.Perfumeria.Service.usuarioService;
import perfume.Perfumeria.model.Usuario;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class usuarioController {

    @Autowired
    private usuarioService _serivice;

    @PostMapping("/registro")
    public Usuario crearUsario(@RequestBody Usuario user){
        return _serivice.registrarUsuario(user);
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody LoginRequest datos){
        return  _serivice.login(datos.getEmail(), datos.getPassword());
    }

    public static  class  LoginRequest{
        private String email;
        private String password;

        public String getEmail() { return email;}
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }


}
