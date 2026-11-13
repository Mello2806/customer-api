package br.com.mello.customerapi.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Formato padrão de erro da API. Todo erro sai com o mesmo "jeitão", por exemplo:
 * {"momento": "...", "status": 400, "erro": "Dados inválidos", "campos": {"cpf": "CPF inválido"}}
 * NON_NULL: "campos" só aparece quando existe.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroResposta(LocalDateTime momento, int status, String erro, Map<String, String> campos) {

    public static ErroResposta de(int status, String erro) {
        return new ErroResposta(LocalDateTime.now(), status, erro, null);
    }
}
