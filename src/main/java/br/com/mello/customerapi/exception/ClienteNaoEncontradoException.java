package br.com.mello.customerapi.exception;

public class ClienteNaoEncontradoException extends RuntimeException {

    public ClienteNaoEncontradoException(Integer id) {
        super("Cliente " + id + " não encontrado");
    }
}
