package perfume.Perfumeria.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perfume.Perfumeria.Dtos.PaginaRespuesta;
import perfume.Perfumeria.model.RegistroAduitoria;
import perfume.Perfumeria.repository.RegistroAuditoriaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AuditariaServices {

    private final RegistroAuditoriaRepository repository;

    @Transactional
    public void registrar(String usario, String accion, String entidad, Long entidadId,
                          String detalle){
        repository.save(RegistroAduitoria.builder()
                        .usuario(usario)
                        .accion(accion)
                        .entidad(entidad)
                        .entidadId(entidadId)
                .detalle(detalle)
                .build());
    }

    @Transactional(readOnly = true)
    public PaginaRespuesta<RegistroAduitoria> buscar(String usuario, String accion, LocalDate desde, LocalDate hasta,
                                                     int pagina, int tamanio){

        LocalDate hastaFinal = hasta != null ? hasta : LocalDate.now();
        LocalDate desdeFinal = desde != null ? desde : hastaFinal.minusDays(30);

        var pageable = PageRequest.of(Math.max(pagina, 0), Math.min(Math.max(tamanio, 1), 100));
        return PaginaRespuesta.de(repository.buscar(vaciAnulo(usuario), vaciAnulo(accion),
                desdeFinal.atStartOfDay(), hastaFinal.atTime(LocalTime.MAX), pageable));
    }

    private String vaciAnulo(String texto){
        return (texto == null || texto.isBlank()) ? null : texto.trim();
    }

}
