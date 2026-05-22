package perfume.Perfumeria.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import perfume.Perfumeria.controller.ordenController;
import perfume.Perfumeria.model.detalleOrden;
import perfume.Perfumeria.model.orden;
import perfume.Perfumeria.model.perfume;
import perfume.Perfumeria.model.usuario;
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
    public orden crearOrden(Long usuerioId, List<ordenController.DetalleRequest> items){
        usuario cliente = usuarioRepo.findById(usuerioId).orElseThrow(()->new RuntimeException("Usuario no encontrado"));

        orden nuevaOrden = new orden();
        nuevaOrden.setFecha(LocalDateTime.now());
        nuevaOrden.setCliente(cliente);

        List<detalleOrden> detalles = new ArrayList<>();
        double totalOrden = 0;

        for (ordenController.DetalleRequest item : items){
            perfume p = perfumeRepo.findById(item.getPerfumeriaId()).orElseThrow(()->new RuntimeException("Perfume no encontrado"));

            if (p.getStock() < item.getCantidad()){
                throw new RuntimeException("Stock insuficiente para:" + p.getNombre());
            }
            p.setStock(p.getStock() - item.getCantidad());
            perfumeRepo.save(p);

            detalleOrden detalle = new detalleOrden();
            detalle.setProducto(p);
            detalle.set_cantidad(item.getCantidad());
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
