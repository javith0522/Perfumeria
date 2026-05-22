package perfume.Perfumeria.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String marca;
    private String categoria;
    private String descripcion;
    private double precio;
    private Integer stock;
    private String imagen;

    private String seccion;
    private String referenciaArtista;
    private String beneficioTecnico;
    private Integer descuento;

    private String intensidad;
    private String ocasion;
    private String notasOlfativas;

    public perfume() {
    }

    public perfume(String nombre, String marca, Double precio) {
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
    }

}
