package perfume.Perfumeria.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import perfume.Perfumeria.model.DetalleOrden;
import perfume.Perfumeria.model.Orden;
import perfume.Perfumeria.model.perfume;
import perfume.Perfumeria.repository.ordenRepository;
import perfume.Perfumeria.repository.perfumeRepository;


@Service

public class perfumeriaService {
    @Autowired
    private perfumeRepository perfumeRepo;

    @Autowired
    private ordenRepository ordenRepo;

    @Transactional
    public Orden realizarVentaCompleta(Orden nuevaOrden) {
        double totalCalculado = 0;

        for (DetalleOrden detalle : nuevaOrden.getDetalles()) {
            perfume p = perfumeRepo.findById(detalle.getPerfume().getId())
                    .orElseThrow(() -> new RuntimeException("Perfume no encontrado"));

            if (p.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("No hay stock suficiente de: " + p.getNombre());
            }


            p.setStock(p.getStock() - detalle.getCantidad());
            perfumeRepo.save(p);

            totalCalculado += p.getPrecio() * detalle.getCantidad();
        }

        nuevaOrden.setTotal(totalCalculado);
        return ordenRepo.save(nuevaOrden);
    }

    @Transactional
    public void venderPefume(Long id, int cantidad) {
        perfume p = perfumeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfume no hallado"));

        if (p.getStock() < cantidad) {
            throw new RuntimeException("No hay suficiente stock");
        }
        p.setStock(p.getStock() - cantidad);
        perfumeRepo.save(p);
    }
}



