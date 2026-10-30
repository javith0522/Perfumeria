package perfume.Perfumeria.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perfume.Perfumeria.Dtos.SoliciitudProveedor;
import perfume.Perfumeria.execption.RecursoNoEncontradoException;
import perfume.Perfumeria.model.Proveedor;
import perfume.Perfumeria.repository.ProveedorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final AuditariaServices auditariaServices;

    @Transactional(readOnly = true)
    public List<Proveedor> listar(boolean soloActivos){
        return soloActivos ? proveedorRepository.findByActivoTrueOrderByNombreAsc() :  proveedorRepository.findAll();
    }

    @Transactional
    public Proveedor crear(SoliciitudProveedor solicitud, String usuario){
        Proveedor nuevo = new Proveedor();
        nuevo.setNombre(solicitud.nombre().trim());
        nuevo.setContacto(solicitud.contacto());
        nuevo.setTelefono(solicitud.telefono());
        nuevo.setActivo(true);
        nuevo =  proveedorRepository.save(nuevo);
        auditariaServices.registrar(usuario, "CREAR_PROVEEDOR", "proveedor",
                nuevo.getId(), nuevo.getNombre());

        return nuevo;
    }

    @Transactional
    public Proveedor cambiarEstado(Long id, boolean activo, String usuario){
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado"));

        if (proveedor.isActivo() == activo){
            return proveedor;
        }

        proveedor.setActivo(activo);
        auditariaServices.registrar(usuario, activo ? "ACTIVAR_PROVEEDOR" : "DESACTIVAR_PROVEEDOR", "proveedor",
                proveedor.getId(), proveedor.getNombre());
        return proveedorRepository.save(proveedor);
    }
}
