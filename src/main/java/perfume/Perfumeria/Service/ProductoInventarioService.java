package perfume.Perfumeria.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perfume.Perfumeria.Dtos.DetalleInventarioDto;
import perfume.Perfumeria.Dtos.SolicitarProductoDto;
import perfume.Perfumeria.execption.SolicitudInvalidaException;
import perfume.Perfumeria.model.Inventario;
import perfume.Perfumeria.model.Perfume;
import perfume.Perfumeria.repository.InventarioRepository;
import perfume.Perfumeria.repository.perfumeRepository;

@Service
@RequiredArgsConstructor
public class ProductoInventarioService {

    private final perfumeRepository perfumeRepositoryepo;
    private final InventarioRepository inventarioRepositoryepo;
    private final ServicioInventario servicioInventario;
    private  final AuditariaServices auditariaServices;
    private final ConsultaInventarioService consultaInventarioService;

    @Transactional
    public DetalleInventarioDto crear(SolicitarProductoDto solicitud, String usuario) {
        if(inventarioRepositoryepo.findByCodigoUnico(solicitud.codigoUnico()).isPresent()) {
            throw new SolicitudInvalidaException("Ya existe un producto con el codigo" + solicitud.codigoUnico());
        }

        Perfume nuevo =  new Perfume();
        aplicarCatalogo(nuevo, solicitud);
        nuevo.setStock(0);
        nuevo =  perfumeRepositoryepo.save(nuevo);

        Inventario inventario = new Inventario();
        inventario.setCodigoUnico(solicitud.codigoUnico());
        inventario.setPrecioCompra(solicitud.precioCompra());
        inventario.setCostoPromedio(solicitud.precioCompra());
        inventario.setStockMinimo(solicitud.stockMinimo());
        inventario.setStock(0);
        inventario.setEstado(true);
        inventario.setPerfume(nuevo);
        inventario =  inventarioRepositoryepo.save(inventario);

        int stockInicial = solicitud.stockInicial() == null ? 0 : solicitud.stockInicial();
        if (stockInicial > 0) {
            servicioInventario.registrarStockIniciarl(inventario, stockInicial,solicitud.precioCompra(), usuario);
        }

        auditariaServices.registrar(usuario, "CREAR_PRODUCTO", "perfume", nuevo.getId(),
                solicitud.nombre() + "(" + solicitud.marca() + "," + solicitud.presentacionMl() + "ml), código" +
                solicitud.codigoUnico() + "stock incial" + stockInicial);
        return consultaInventarioService.obtenerDetalle(nuevo.getId());

    }

    private void aplicarCatalogo(Perfume p, SolicitarProductoDto s){
        p.setNombre(s.nombre().trim());
        p.setMarca(s.marca().trim());
        p.setPresentacionMl(s.presentacionMl());
        p.setPrecio(s.precioVenta().doubleValue());
        if (s.genero() != null) {
            p.setGenero(s.genero());
        }
        if (s.categoria() != null) {
            p.setCategoria(s.categoria());
        }
        if (s.descipcion() != null) { p.setDescripcion(s.descipcion().trim()); }
        if (s.imagen() != null) { p.setImagen(s.imagen().trim()); }

    }
}
