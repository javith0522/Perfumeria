package perfume.Perfumeria.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String rol;
    private int identificacion;
}
