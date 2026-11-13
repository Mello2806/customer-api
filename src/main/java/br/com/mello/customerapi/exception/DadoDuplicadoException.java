package br.com.mello.customerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** E-mail ou CPF que já pertence a outro cliente. 409 = conflito com o que já existe. */
@ResponseStatus(HttpStatus.CONFLICT)
public class DadoDuplicadoException extends RuntimeException {

    public DadoDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
