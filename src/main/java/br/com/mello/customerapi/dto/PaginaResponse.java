package br.com.mello.customerapi.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Resposta paginada enxuta. O Page do Spring tem muitos campos internos;
 * aqui devolvemos só o que quem consome a API precisa.
 *
 * Exemplo: {"conteudo": [...], "pagina": 0, "tamanho": 10, "totalElementos": 23, "totalPaginas": 3}
 */
public record PaginaResponse<T>(List<T> conteudo, int pagina, int tamanho, long totalElementos, int totalPaginas) {

    public static <T> PaginaResponse<T> de(Page<T> page) {
        return new PaginaResponse<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
