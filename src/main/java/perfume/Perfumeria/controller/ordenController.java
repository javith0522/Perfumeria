package perfume.Perfumeria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import perfume.Perfumeria.Service.ordenService;
import perfume.Perfumeria.model.Orden;
import perfume.Perfumeria.repository.ordenRepository;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "*")
public class ordenController {

    @Autowired
    public ordenService Service;
    @Autowired
    private ordenRepository ordenRepo;

    @PostMapping
    public Orden realizarCompra(@RequestBody CompraRequest request){
        return Service.crearOrden(request.getUsuarioId(), request.getItems());
    }

    public static class CompraRequest{
        private Long usuarioId;
        private List<DetalleRequest> items;

        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        public List<DetalleRequest> getItems(){ return items; }
        public void setItems(List<DetalleRequest> items) { this.items = items; }
    }

    public static class DetalleRequest{
        private Long perfumeriaId;
        private Integer cantidad;

        public Long getPerfumeriaId() { return perfumeriaId; }
        public void setPerfumeriaId(Long perfumeriaId) { this.perfumeriaId = perfumeriaId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }


    @GetMapping("/usuario/{id}")
    public List<Orden> getOrdenes(@PathVariable Long id){
        return ordenRepo.findByClienteId(id);
    }
}

