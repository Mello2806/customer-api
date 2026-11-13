package br.com.mello.customerapi.exception;

/** E-mail ou CPF que já pertence a outro cliente. 409 = conflito com o que já existe. */
public class DadoDuplicadoException extends RuntimeException {

    public DadoDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
