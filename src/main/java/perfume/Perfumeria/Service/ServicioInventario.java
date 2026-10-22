package perfume.Perfumeria.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perfume.Perfumeria.Dtos.MovimientoDto;
import perfume.Perfumeria.Dtos.SolicitudAjuste;
import perfume.Perfumeria.Dtos.SolicitudEntrada;
import perfume.Perfumeria.Dtos.SolicitudSalida;
import perfume.Perfumeria.enums.CategoriaGasto;
import perfume.Perfumeria.enums.TipoMovimiento;
import perfume.Perfumeria.execption.RecursoNoEncontradoException;
import perfume.Perfumeria.execption.SolicitudInvalidaException;
import perfume.Perfumeria.execption.StockInsuficienteException;
import perfume.Perfumeria.model.Gasto;
import perfume.Perfumeria.model.Inventario;
import perfume.Perfumeria.model.MovimientoInventario;
import perfume.Perfumeria.model.Proveedor;
import perfume.Perfumeria.repository.GastoRepository;
import perfume.Perfumeria.repository.InventarioRepository;
import perfume.Perfumeria.repository.MovimientoInventarioRepository;
import perfume.Perfumeria.repository.ProveedorRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ServicioInventario {
    private  final MovimientoInventarioRepository moviRepository;
    private  final InventarioRepository inventarioRepository;
    private  final ProveedorRepository proveedorRepository;
    private  final GastoRepository gastoRepository;
    private final AuditariaServices auditariaServices;

    @Transactional
    public MovimientoDto registrarMovimiento(SolicitudEntrada solicitud, String usuario){

        Inventario inventario = bloquear(solicitud.perfumeId());
        exigirActivo(inventario);

        Proveedor proveedor = proveedorRepository.findById(solicitud.proveedorId())
                .filter(Proveedor::isActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado o inactivo"));

        String motivo = (solicitud.nota() == null || solicitud.nota().isBlank() ? "Ingreso de mercancia" : solicitud.nota().trim());

        MovimientoInventario movimiento = aplicar(inventario, TipoMovimiento.ENTRADA, solicitud.cantidad(),
                solicitud.costoUnitario(), motivo, proveedor, null, usuario);

        Gasto gasto = new Gasto();
        gasto.setFecha(LocalDate.now());
        gasto.setDescipcion("Compra de:" + solicitud.cantidad() + "x" + inventario.getPerfume().getNombre() + "a"
        + proveedor.getNombre() + "(mov #" + movimiento.getId() + ")");
        gasto.setValor(solicitud.costoUnitario().multiply(BigDecimal.valueOf(solicitud.cantidad())));
        gasto.setCategoria(CategoriaGasto.MERCANCIA);
        gasto.setUsuario(usuario);
        gastoRepository.save(gasto);

        auditariaServices.registrar(usuario, "ENTRADA_INVENTARIO", "perfume", solicitud.perfumeId(),
                solicitud.cantidad() + "uds a" + solicitud.costoUnitario() + "c/u, proveedor" + proveedor.getNombre() +
                ", stock resulante" + movimiento.getStockResultante());

        return MovimientoDto.de(movimiento);

    }

    @Transactional
    private void registrarStockIniciarl(Inventario inventario, int cantidad, BigDecimal costoUnitario, String usuario){
        aplicar(inventario, TipoMovimiento.ENTRADA, cantidad, costoUnitario, "Stock inicial ", null, null, usuario);
    }

    @Transactional
    public MovimientoDto registrarSalida(SolicitudSalida salida, String usuario){
        TipoMovimiento tipo = salida.tipo();
        if (tipo != TipoMovimiento.SALIDA_USO && tipo != TipoMovimiento.SALIDA_DANO && tipo != TipoMovimiento.ENTRADA
        && tipo != TipoMovimiento.SALIDA_PERDIDA) {
            throw new SolicitudInvalidaException("Tipo de salida no es valida");
        }

            Inventario inventario = bloquear(salida.perfumeId());

            MovimientoInventario movimiento = aplicar(inventario, tipo, salida.cantidad(), null,
                    salida.motivo().trim(), null, null, usuario);

            auditariaServices.registrar(usuario, tipo.name(), "perfume", salida.perfumeId(),
                    salida.cantidad() + "usd, motivo:" + salida.motivo().trim() +
                    ", stock resulante:" + movimiento.getStockResultante());

            return  MovimientoDto.de(movimiento);

    }

    @Transactional
    public MovimientoDto ajustarConteo(SolicitudAjuste solicitud, String usuario){
        Inventario inventario = bloquear(solicitud.perfumeId());
        int diferencia = solicitud.stockContado() - inventario.getStock();
        if (diferencia == 0) {
            throw new SolicitudInvalidaException("El conteo con el stock actual. No hay nada que ajustar");
        }
        TipoMovimiento tipo = diferencia > 0 ? TipoMovimiento.AJUSTES_POSTIVO : TipoMovimiento.AJUSTES_NEGATIVO;
        int stockAnterior = inventario.getStock();

        MovimientoInventario movimiento = aplicar(inventario, tipo, Math.abs(diferencia), null,
                solicitud.motivo().trim(), null, null, usuario );

        auditariaServices.registrar(usuario, tipo.name(), "perfume", solicitud.perfumeId(),
                "stock" + stockAnterior + "->" + solicitud.stockContado() + ", motivo" + solicitud.motivo().trim());

        return  MovimientoDto.de(movimiento);

    }

    @Transactional
    public MovimientoInventario registraVenta(Long perfumeId, int cantidad, Long ordenId, String usuario){
        Inventario inventario = bloquear(perfumeId);
        exigirActivo(inventario);
        return aplicar(inventario, TipoMovimiento.SALIDA_VENTA, cantidad, null, "Venta orden #" + ordenId, null, ordenId, usuario);
    }

    private Inventario bloquear(Long perfumeId){
        return inventarioRepository.buscarParaActualizar(perfumeId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe inventario para el perfume" + perfumeId));
    }

    private void exigirActivo(Inventario inventario){
        if (Boolean.TRUE.equals(inventario.getEstado())){
            throw new SolicitudInvalidaException("El producto" + inventario.getPerfume().getNombre() + "esta desactivado");
        }
    }

    private MovimientoInventario aplicar(Inventario inventario, TipoMovimiento tipo, int cantidad,
                                         BigDecimal costoUnitario, String motivo, Proveedor proveedor, Long ordenId, String usuario) {

        if (cantidad <= 0){
            throw new SolicitudInvalidaException("La cantidad debe ser mayor a 0");
        }

        boolean exigeMotivo = tipo != TipoMovimiento.ENTRADA && tipo != TipoMovimiento.SALIDA_VENTA;
        if (exigeMotivo && (motivo == null || motivo.isBlank())){
            throw new SolicitudInvalidaException("El motivo es obligatorio");
        }

        int stockActual = inventario.getStock();
        int delta = tipo.esEntrada() ? cantidad : -cantidad;
        int nuevoStock = stockActual + delta;
        if (nuevoStock < 0){
            throw  new StockInsuficienteException(inventario.getPerfume().getNombre(), stockActual, cantidad);
        }

        BigDecimal costoDeMovimiento;
        if (tipo == TipoMovimiento.ENTRADA){
            BigDecimal costoActualTotal = inventario.getCostoPromedio().multiply(BigDecimal.valueOf(stockActual));
            BigDecimal costoTotalEntrada =  costoUnitario.multiply(BigDecimal.valueOf(cantidad));
            inventario.setCostoPromedio(costoActualTotal.add(costoTotalEntrada).divide(BigDecimal.valueOf(nuevoStock), 2, RoundingMode.HALF_UP));
            inventario.setPrecioCompra(costoUnitario);
            costoDeMovimiento = costoUnitario;
        } else {
            costoDeMovimiento = inventario.getCostoPromedio();
        }
        inventario.setStock(nuevoStock);

        inventario.getPerfume().setStock(nuevoStock);
        inventarioRepository.save(inventario);

        return moviRepository.save(MovimientoInventario.builder()
                        .perfume(inventario.getPerfume())
                        .tipo(tipo)
                        .cantidad(delta)
                        .stockResultante(nuevoStock)
                        .costoUnitario(costoDeMovimiento)
                        .motivo(motivo)
                        .proveedor(proveedor)
                        .ordenId(ordenId)
                        .usuario(usuario)
                        .fechaHora(LocalDateTime.now())
                .build());
    }
}
