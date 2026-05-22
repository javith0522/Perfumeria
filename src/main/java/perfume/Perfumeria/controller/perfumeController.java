package perfume.Perfumeria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perfume.Perfumeria.Service.perfumeriaService;
import perfume.Perfumeria.model.perfume;
import perfume.Perfumeria.repository.perfumeRepository;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/perfume")
public class perfumeController {

    @Autowired
    private perfumeRepository repo;

    @Autowired
    private perfumeriaService service;


    @GetMapping
    public List<perfume> getcatalogo(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Double minPrecio,
            @RequestParam(required = false) Double maxPrecio) {

        if (marca != null) {
            return repo.findByMarcaContainingIgnoreCase(marca);
        }
        if (categoria != null) {
            return repo.findByCategoriaContainingIgnoreCase(categoria);
        }
        if (minPrecio != null && maxPrecio != null) {
            return repo.findByPrecio(minPrecio, maxPrecio);
        }
        return repo.findAll();
    }


    @GetMapping("/buscar-ideal")
    public List<perfume> buscarIdeal(
            @RequestParam("genero") String genero,
            @RequestParam("intensidad") String intensidad,
            @RequestParam("ocasion") String ocasion
    ) {

        return repo.findByCategoriaAndIntensidadIgnoreCaseAndOcasionIgnoreCase(genero, intensidad, ocasion);
    }

    @GetMapping("/seccion/{nombreSeccion}")
    public List<perfume> getPorSeccion(@PathVariable String nombreSeccion) {
        return repo.findBySeccionIgnoreCase(nombreSeccion);
    }


    @PostMapping("/guardar")
    public ResponseEntity<perfume> guardar(@RequestBody perfume p) {

        Optional<perfume> existente = repo.findByNombreIgnoreCase(p.getNombre());

        if (existente.isPresent()) {
            perfume pOriginal = existente.get();
            // Actualizamos stock y precio
            pOriginal.setStock(pOriginal.getStock() + (p.getStock() > 0 ? p.getStock() : 0));
            pOriginal.setPrecio(p.getPrecio());
            pOriginal.setSeccion(p.getSeccion());
            pOriginal.setCategoria(p.getCategoria());
            pOriginal.setImagen(p.getImagen());


            return ResponseEntity.ok(repo.save(pOriginal));
        }


        return ResponseEntity.ok(repo.save(p));
    }


    @PostMapping("/comprar/{id}")
    public ResponseEntity<String> comprar(@PathVariable Long id, @RequestParam int cantidad) {
                try {
                    service.venderPefume(id, cantidad);
            return ResponseEntity.ok("Compra realizada con éxito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar la compra: " + e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<perfume> obtenerPorId(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}