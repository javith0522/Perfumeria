package perfume.Perfumeria.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "perfumes")
public class perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String marca;
    private String genero;
    private String categoria;
    private String descripcion;
    private double precio;
    private Integer stock;
    private String imagen;

    private String seccion;
    private String referenciaArtista;
    private String beneficioTecnico;
    private Integer descuento;
    private Integer presentacionMl;
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
