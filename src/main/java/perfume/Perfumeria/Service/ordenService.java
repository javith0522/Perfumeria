package perfume.Perfumeria.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import perfume.Perfumeria.controller.ordenController;
import perfume.Perfumeria.model.DetalleOrden;
import perfume.Perfumeria.model.Orden;
import perfume.Perfumeria.model.Usuario;
import perfume.Perfumeria.model.perfume;
import perfume.Perfumeria.repository.ordenRepository;
import perfume.Perfumeria.repository.perfumeRepository;
import perfume.Perfumeria.repository.usuarioRepostery;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ordenService {

    @Autowired
    private ordenRepository ordenRepo;
    @Autowired
    private usuarioRepostery usuarioRepo;
    @Autowired
    private perfumeRepository perfumeRepo;

    @Transactional
    public Orden crearOrden(Long usuerioId, List<ordenController.DetalleRequest> items){
        Usuario cliente = usuarioRepo.findById(usuerioId).orElseThrow(()->new RuntimeException("Usuario no encontrado"));

        Orden nuevaOrden = new Orden();
        nuevaOrden.setFecha(LocalDateTime.now());
        nuevaOrden.setCliente(cliente);

        List<DetalleOrden> detalles = new ArrayList<>();
        double totalOrden = 0;

        for (ordenController.DetalleRequest item : items){
            perfume p = perfumeRepo.findById(item.getPerfumeriaId()).orElseThrow(()->new RuntimeException("Perfume no encontrado"));

            if (p.getStock() < item.getCantidad()){
                throw new RuntimeException("Stock insuficiente para:" + p.getNombre());
            }
            p.setStock(p.getStock() - item.getCantidad());
            perfumeRepo.save(p);

            DetalleOrden detalle = new DetalleOrden();
            detalle.setPerfume(p);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(p.getPrecio());
            detalle.setOrden(nuevaOrden);

            detalles.add(detalle);
            totalOrden += p.getPrecio() * item.getCantidad() ;
        }
        nuevaOrden.setTotal(totalOrden);
        nuevaOrden.setDetalles(detalles);
        return ordenRepo.save(nuevaOrden);

    }
}
