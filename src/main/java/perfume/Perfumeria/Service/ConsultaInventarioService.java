package perfume.Perfumeria.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perfume.Perfumeria.Dtos.*;
import perfume.Perfumeria.enums.EstadoStock;
import perfume.Perfumeria.enums.TipoMovimiento;
import perfume.Perfumeria.execption.RecursoNoEncontradoException;
import perfume.Perfumeria.model.Inventario;
import perfume.Perfumeria.model.Perfume;
import perfume.Perfumeria.repository.InventarioRepository;
import perfume.Perfumeria.repository.MovimientoInventarioRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsultaInventarioService {

    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    public ResumenInventarioDto resumen(){
        List<Inventario> todos =  inventarioRepository.findAll();
        List<Inventario> activos = todos.stream().filter(i -> Boolean.TRUE.equals(i.getEstado())).toList();

        long unidades = activos.stream().mapToLong(Inventario::getStock).sum();

        BigDecimal valorCosto = activos.stream().map(i -> i.getCostoPromedio().multiply(BigDecimal.valueOf(i.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorVenta = activos.stream().map(i -> BigDecimal.valueOf(i.getPerfume().getPrecio())
                .multiply(BigDecimal.valueOf(i.getStock()))).reduce(BigDecimal.ZERO, BigDecimal::add);

        long agotados = activos.stream().filter(i -> EstadoStock.de(i) == EstadoStock.AGOTADO).count();

        long bajos = activos.stream().filter(i -> EstadoStock.de(i) == EstadoStock.BAJO).count();

        return new ResumenInventarioDto(todos.size(), activos.size(), unidades, valorCosto, valorVenta,
                valorVenta.subtract(valorCosto), agotados, bajos);
    }

    public PaginaRespuesta<ItemInventarioDto> listar(String texto, int pagina, int tamano){
        var pageable = PageRequest.of(Math.max(pagina, 0), Math.min(Math.max(tamano, 1), 100),
                Sort.by("perfume.nombre").ascending());
        String filtro = texto == null ? "" : texto.trim();
        return PaginaRespuesta.de(inventarioRepository.buscar(filtro,pageable).map(ItemInventarioDto::de));
    }

    public DetalleInventarioDto obtenerDetalle(Long perfumeId){
        Inventario inventario = inventarioRepository.findById(perfumeId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe inventario del perfume" +  perfumeId));
        Perfume p = inventario.getPerfume();

        BigDecimal precioVenta = BigDecimal.valueOf(p.getPrecio());
        BigDecimal costo = inventario.getCostoPromedio();
        BigDecimal utilidad = precioVenta.subtract(costo);
        BigDecimal margen = precioVenta.signum() > 0 ? utilidad.multiply(BigDecimal.valueOf(100)).divide(precioVenta,
                2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        Map<TipoMovimiento, Long> totales = new EnumMap<>(TipoMovimiento.class);


        for (Object[] fila : movimientoInventarioRepository.totalesPorTipo(perfumeId)) {
            totales.put((TipoMovimiento) fila[0], ((Number) fila[1]).longValue());
    }

        List<MovimientoDto> ultimos = movimientoInventarioRepository.findTop10ByPerfumeIdOrderByFechaHoraDesc(perfumeId)
                .stream().map(MovimientoDto::de).toList();

        return new DetalleInventarioDto(
                p.getId(), inventario.getCodigoUnico(), p.getNombre(), p.getMarca(), p.getGenero(),
                p.getCategoria(), p.getDescripcion(), p.getImagen(), p.getPresentacionMl(), p.getDescuento(),
                inventario.getPrecioCompra(), costo, precioVenta, utilidad, margen,
                inventario.getStock(), inventario.getStockMinimo(), EstadoStock.de(inventario), inventario.getEstado(),
                costo.multiply(BigDecimal.valueOf(inventario.getStock())),
                precioVenta.multiply(BigDecimal.valueOf(inventario.getStock())),
                totales.getOrDefault(TipoMovimiento.ENTRADA, 0L),
                Math.abs(totales.getOrDefault(TipoMovimiento.SALIDA_VENTA, 0L)),
                Math.abs(totales.getOrDefault(TipoMovimiento.SALIDA_USO, 0L)),
                Math.abs(totales.getOrDefault(TipoMovimiento.SALIDA_DANO, 0L)),
                Math.abs(totales.getOrDefault(TipoMovimiento.SALIDA_PERDIDA, 0L)),
                totales.getOrDefault(TipoMovimiento.AJUSTES_POSTIVO, 0L) + totales.getOrDefault(TipoMovimiento.AJUSTES_NEGATIVO, 0L),
                movimientoInventarioRepository.ultimaFechaPorTipo(perfumeId, TipoMovimiento.ENTRADA).orElse(null),
                movimientoInventarioRepository.ultimaFechaPorTipo(perfumeId, TipoMovimiento.SALIDA_VENTA).orElse(null),
                ultimos);


    }

    public PaginaRespuesta<MovimientoDto> historia(Long perfumeId, TipoMovimiento tipo, LocalDate desde, LocalDate hasta,
                                                    int pagina, int tamano){

        LocalDate hastaFinal = hasta != null ? hasta : LocalDate.now();
        LocalDate desdeFinal = desde != null ? desde : hastaFinal.minusDays(30);

        LocalDateTime inicio = desdeFinal.atStartOfDay();
        LocalDateTime fin = hastaFinal.atTime(LocalTime.MAX);

        var pagaeable = PageRequest.of(Math.max(pagina, 0), Math.min(Math.max(tamano,1), 100));

        return PaginaRespuesta.de(movimientoInventarioRepository.buscar(perfumeId, tipo, inicio, fin, pagaeable).map(MovimientoDto::de) );

    }
    public List<AlertaStock> alertaStockBajo(){
        return  inventarioRepository.buscarStockBajo().stream()
                .map(i -> new AlertaStock(
                        i.getPerfume().getId(), i.getCodigoUnico(),
                        i.getPerfume().getNombre(), i.getPerfume().getMarca(),
                        i.getStock(), i.getStockMinimo(), Math.max(0,i.getStockMinimo() - i.getStock()),
                        EstadoStock.de(i)

                ))
                .toList();
    }



}
