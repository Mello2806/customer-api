package br.com.mello.customerapi.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Um lugar só para transformar exceções em respostas HTTP.
 * Sem isto, cada controller teria que ter try/catch — aqui o Spring "pega" a exceção
 * que escapou de qualquer controller e chama o método com o @ExceptionHandler certo.
 */
@RestControllerAdvice
public class TratadorDeErros {

    /** @Valid encontrou campos inválidos → 400 com a lista de campos. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta camposInvalidos(MethodArgumentNotValidException e) {
        Map<String, String> campos = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(erro -> campos.putIfAbsent(erro.getField(), erro.getDefaultMessage()));
        return new ErroResposta(LocalDateTime.now(), 400, "Dados inválidos", campos);
    }

    /** Corpo que nem é JSON válido (vírgula faltando, aspas erradas...). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta jsonInvalido() {
        return ErroResposta.de(400, "JSON inválido ou mal formatado");
    }

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta naoEncontrado(ClienteNaoEncontradoException e) {
        return ErroResposta.de(404, e.getMessage());
    }

    @ExceptionHandler(DadoDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta duplicado(DadoDuplicadoException e) {
        return ErroResposta.de(409, e.getMessage());
    }

    /** Rede de segurança: duas requisições ao mesmo tempo podem passar pelo service e bater no UNIQUE do banco. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta violouRestricaoDoBanco() {
        return ErroResposta.de(409, "E-mail ou CPF já cadastrado");
    }
}
