package perfume.Perfumeria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import perfume.Perfumeria.Service.ordenService;
import perfume.Perfumeria.Service.perfumeriaService;
import perfume.Perfumeria.model.orden;
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
    public orden realizarCompra(@RequestBody CompraRequest request){
        return Service.crearOrden(request.getUsuarioId(), request.getItems());

    }
    public static class CompraRequest{
        public Long usuarioId;
        private List<DetalleRequest> items;
        private Long getUsuarioId() {
            return usuarioId;}
        public List<DetalleRequest> getItems(){
            return items;
        }
    }

    public static class DetalleRequest{
        private Long perfumeriaId;
        private Integer cantidad;
        public Long getPerfumeriaId() {
            return perfumeriaId;
        }
        public void setPerfumeriaId(Long perfumeriaId) {
            this.perfumeriaId = perfumeriaId;
        }
        public Integer getCantidad() { return cantidad; }
    }
    @GetMapping("/usuario/{id}")
    public List<orden> getOrdenes(@PathVariable Long id){
        return ordenRepo.findByClienteId(id);
    }

}

