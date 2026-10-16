package perfume.Perfumeria.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perfume.Perfumeria.Dtos.CarritoResponse;
import perfume.Perfumeria.Dtos.ItemCarritoDto;
import perfume.Perfumeria.model.*;
import perfume.Perfumeria.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private perfumeRepository perfumeRepository;

    @Autowired
    private usuarioRepostery usuarioRepo;

    @Autowired
    private ordenRepository ordenRepository;

    @Transactional
    public CarritoResponse obtenerCarrito(Long usuarioId){
        return  mapearADTO(obtenerOcrearCarritoEntidad(usuarioId));
    }

    @Transactional
    public CarritoResponse agregarItemCarrito(Long usuarioId, Long perfumeId, Integer cantidad){
        Carrito c = obtenerOcrearCarritoEntidad(usuarioId);

        perfume p = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new RuntimeException("perfume no encontrado"));

        if (p.getStock() != null && p.getStock() < cantidad){
            throw new RuntimeException("No hay stock suficiente para: " + p.getNombre());
        }

        ItemCarrito existente = c.getItems().stream()
                .filter(i -> i.getPerfume().getId().equals(perfumeId))
                .findFirst()
                .orElse(null);

        if (existente != null) {
            existente.setCantidad((int) (existente.getCantidad() + cantidad));
        } else {
            c.getItems().add(new ItemCarrito(c, p, cantidad.intValue()));
        }

        c.setFechaActualizacion(LocalDateTime.now());
        return mapearADTO(carritoRepository.save(c));
    }

    @Transactional
    public CarritoResponse actualizarCantidad(Long usuarioId, Long itemId, Integer cantidad){

        Carrito c = obtenerOcrearCarritoEntidad(usuarioId);

        ItemCarrito item = c.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("item no encontrado en tu carrito"));

        if (cantidad <= 0) {
            c.getItems().remove(item);
        } else {
            item.setCantidad(cantidad);
        }

        c.setFechaActualizacion(LocalDateTime.now());
        return mapearADTO(carritoRepository.save(c));
    }

    @Transactional
    public CarritoResponse eliminarItemCarrito(Long usuarioId, Long itemId){
        Carrito c = obtenerOcrearCarritoEntidad(usuarioId);

        boolean pertenecer = c.getItems().stream().anyMatch(i -> i.getId().equals(itemId));
        if(!pertenecer){
            throw new RuntimeException("item no encontrado en tu carrito");
        }
        c.getItems().removeIf(i -> i.getId().equals(itemId));
        c.setFechaActualizacion(LocalDateTime.now());
        return mapearADTO(carritoRepository.save(c));
    }

    @Transactional
    public void vaciarCarrito(Long usuarioId) {
        Carrito c =  obtenerOcrearCarritoEntidad(usuarioId);
        c.getItems().clear();
        carritoRepository.save(c);
    }

    private Carrito obtenerOcrearCarritoEntidad(Long usuarioId){
        return carritoRepository.findByUsuarioIdAndEstado(usuarioId, "ACTIVO")
                .orElseGet(() -> carritoRepository.save(new Carrito(usuarioId)));
    }

    private CarritoResponse mapearADTO(Carrito c){

        List<ItemCarritoDto> itemsDto = c.getItems().stream()
                .map(i -> new ItemCarritoDto(
                        i.getId(),
                        i.getPerfume().getId(),
                        i.getPerfume().getNombre(),
                        i.getPerfume().getMarca(),
                        i.getPerfume().getImagen(),
                        i.getCantidad(),
                        i.getPrecioUnitario()
                ))
                .collect(Collectors.toList());

        double total = itemsDto.stream().mapToDouble(ItemCarritoDto::subtotal).sum();

        return new CarritoResponse(c.getId(), c.getEstado(), c.getFechaActualizacion(), itemsDto, total);
    }

    @Transactional
    public Orden finalizarCompra(Long usuarioId) {
        Carrito c =  obtenerOcrearCarritoEntidad(usuarioId);

        if (c.getItems().isEmpty()){
            throw new RuntimeException("El carrito esta vacío");
        }
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("usuario no encontrado"));

        for (ItemCarrito item : c.getItems()){
            perfume p = item.getPerfume();
            if (p.getStock() != null && p.getStock() < item.getCantidad()){
                throw new RuntimeException("stock insuficiente para:" + p.getNombre() +
                        "(disponible: " + p.getStock() + ", solicitado: " + item.getCantidad() + ")");
            }
        }

        Orden orden = new Orden();
        orden.setCliente(usuario);
        orden.setTotal(c.getTotal());
        orden.setFecha(LocalDateTime.now());

        List<DetalleOrden> detalles = c.getItems().stream().map(item -> {
            DetalleOrden d = new DetalleOrden();
            d.setPerfume(item.getPerfume());
            d.setCantidad(item.getCantidad());
            d.setPrecioUnitario(item.getPrecioUnitario());
            d.setOrden(orden);
            return d;
                }).collect(Collectors.toList());

        orden.setDetalles(detalles);
        Orden guardar = ordenRepository.save(orden);

        for(ItemCarrito item : c.getItems()){
            perfume p = item.getPerfume();
            if (p.getStock() != null) {
                p.setStock(p.getStock() - item.getCantidad());
                perfumeRepository.save(p);
            }
        }

        c.getItems().clear();
        c.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(c);

        return guardar;
    }
}
