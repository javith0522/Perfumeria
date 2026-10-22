package perfume.Perfumeria.Dtos;



import org.springframework.data.domain.Page;

import java.util.List;

public record PaginaRespuesta<T>(List<T> contenido, int pagina, int tamano, long totalElementos, int totalPaginas) {

    public static <T> PaginaRespuesta<T> de(Page<T> page) {
        return new PaginaRespuesta<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
