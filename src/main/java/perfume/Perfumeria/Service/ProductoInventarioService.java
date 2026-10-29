package perfume.Perfumeria.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perfume.Perfumeria.Dtos.DetalleInventarioDto;
import perfume.Perfumeria.Dtos.SolicitarProductoDto;
import perfume.Perfumeria.execption.RecursoNoEncontradoException;
import perfume.Perfumeria.execption.SolicitudInvalidaException;
import perfume.Perfumeria.model.Inventario;
import perfume.Perfumeria.model.Perfume;
import perfume.Perfumeria.repository.InventarioRepository;
import perfume.Perfumeria.repository.perfumeRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Transactional
    public DetalleInventarioDto editar(Long perfumeId, SolicitarProductoDto solicitud, String usuario) {
        Inventario inventario = inventarioRepositoryepo.findByPerfumeId(perfumeId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el producto con el id" + perfumeId));

        inventarioRepositoryepo.findByCodigoUnico(solicitud.codigoUnico())
                .filter(otro -> !otro.getId().equals(inventario.getId()))
                .ifPresent(otro -> {
                    throw new SolicitudInvalidaException("Ya existe un producto con el id" + perfumeId);
                });

        Perfume p = inventario.getPerfume();
        List<String> cambios = new ArrayList<>();
        cambiarTexto(cambios, "nombre", p.getNombre(),solicitud.nombre());
        cambiarTexto(cambios, "marca", p.getMarca(),solicitud.marca());
        cambiarTexto(cambios, "presentacionMl", p.getPresentacionMl(),solicitud.presentacionMl());
        cambiarTexto(cambios, "codigo", inventario.getCodigoUnico(), solicitud.codigoUnico());
        cambiarTexto(cambios, "stockMinimo", inventario.getStockMinimo(),solicitud.stockMinimo());
        cambiarNumero(cambios, "precioVenta", inventario.getPrecioCompra(),solicitud.precioVenta());
        cambiarNumero(cambios, "precioCompra",  inventario.getPrecioCompra(),solicitud.precioCompra());

        aplicarCatalogo(p, solicitud);
        perfumeRepositoryepo.save(p);
        inventario.setCodigoUnico(solicitud.codigoUnico());
        inventario.setPrecioCompra(solicitud.precioCompra());
        inventario.setStockMinimo(solicitud.stockMinimo());
        inventarioRepositoryepo.save(inventario);

        auditariaServices.registrar(usuario, "EDITAR_PRODCUTO", "perfune", perfumeId,
                cambios.isEmpty() ? "Sin cambios en campos principales" :
                String.join(", ", cambios));
        return consultaInventarioService.obtenerDetalle(perfumeId);

    }

    @Transactional
    public DetalleInventarioDto cambiarEstado(Long perfumeId, Boolean activo, String usuario) {
        Inventario inventario = inventarioRepositoryepo.findByPerfumeId(perfumeId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe inventario del perfume" + perfumeId));

        inventario.setEstado(activo);
        inventarioRepositoryepo.save(inventario);
        auditariaServices.registrar(usuario, activo ? "ACTIVAR_PRODUCTO" : "DESCATIVAR_PRODUCTO",
                "perfume", perfumeId, inventario.getPerfume().getNombre());
        return consultaInventarioService.obtenerDetalle(perfumeId);

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

    private void cambiarTexto(List<String> cambios, String campo, Object viejo, Object nuevo){
        if (!Objects.equals(viejo, nuevo)) cambios.add(campo + ": " + viejo + "->" +  nuevo);
    }

    private void cambiarNumero(List<String> cambios, String campo, BigDecimal viejo, BigDecimal nuevo){
        if(viejo == null || viejo.compareTo(nuevo) != 0) cambios.add(campo + ": " + viejo + "->" +  nuevo);

    }
}
