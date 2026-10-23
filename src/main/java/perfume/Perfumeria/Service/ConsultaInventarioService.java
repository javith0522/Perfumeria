package perfume.Perfumeria.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perfume.Perfumeria.Dtos.ResumenInventarioDto;
import perfume.Perfumeria.enums.EstadoStock;
import perfume.Perfumeria.model.Inventario;
import perfume.Perfumeria.repository.InventarioRepository;
import perfume.Perfumeria.repository.MovimientoInventarioRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsultaInventarioService {

    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    private ResumenInventarioDto resumen(){
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

}
