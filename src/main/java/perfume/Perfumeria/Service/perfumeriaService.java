package perfume.Perfumeria.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import perfume.Perfumeria.model.detalleOrden;
import perfume.Perfumeria.model.orden;
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
    public orden realizarVentaCompleta(orden nuevaOrden) {
        double totalCalculado = 0;

        for (detalleOrden detalle : nuevaOrden.getDetalles()) {
            perfume p = perfumeRepo.findById(detalle.getPerfume().getId())
                    .orElseThrow(() -> new RuntimeException("Perfume no encontrado"));

            if (p.getStock() < detalle.get_cantidad()) {
                throw new RuntimeException("No hay stock suficiente de: " + p.getNombre());
            }


            p.setStock(p.getStock() - detalle.get_cantidad());
            perfumeRepo.save(p);

            totalCalculado += p.getPrecio() * detalle.get_cantidad();
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



