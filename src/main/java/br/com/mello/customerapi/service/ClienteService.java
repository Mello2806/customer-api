package br.com.mello.customerapi.service;

import br.com.mello.customerapi.model.Cliente;
import br.com.mello.customerapi.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** Regras de negócio. O controller só conversa com o service, nunca direto com o banco. */
@Service
public class ClienteService {

    private final ClienteRepository repository;

    // Injeção de dependência: o Spring entrega o repository pronto pelo construtor.
    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> listar() {
        return repository.findAllByOrderByNomeAsc();
    }
}
