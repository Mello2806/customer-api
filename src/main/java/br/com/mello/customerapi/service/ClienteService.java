package br.com.mello.customerapi.service;

import br.com.mello.customerapi.dto.ClienteRequest;
import br.com.mello.customerapi.exception.ClienteNaoEncontradoException;
import br.com.mello.customerapi.exception.DadoDuplicadoException;
import br.com.mello.customerapi.model.Cliente;
import br.com.mello.customerapi.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Cliente buscar(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    @Transactional
    public Cliente criar(ClienteRequest dados) {
        String cpf = somenteDigitos(dados.cpf());
        if (repository.existsByEmail(dados.email())) {
            throw new DadoDuplicadoException("E-mail já cadastrado: " + dados.email());
        }
        if (repository.existsByCpf(cpf)) {
            throw new DadoDuplicadoException("CPF já cadastrado");
        }
        return repository.save(new Cliente(dados.nome(), dados.email(), cpf, dados.telefone()));
    }

    @Transactional
    public Cliente atualizar(Integer id, ClienteRequest dados) {
        Cliente cliente = buscar(id);
        String cpf = somenteDigitos(dados.cpf());
        if (repository.existsByEmailAndIdNot(dados.email(), id)) {
            throw new DadoDuplicadoException("E-mail já cadastrado: " + dados.email());
        }
        if (repository.existsByCpfAndIdNot(cpf, id)) {
            throw new DadoDuplicadoException("CPF já cadastrado");
        }
        cliente.setNome(dados.nome());
        cliente.setEmail(dados.email());
        cliente.setCpf(cpf);
        cliente.setTelefone(dados.telefone());
        return repository.save(cliente);
    }

    @Transactional
    public void excluir(Integer id) {
        if (!repository.existsById(id)) {
            throw new ClienteNaoEncontradoException(id);
        }
        repository.deleteById(id);
    }

    /** "529.982.247-25" vira "52998224725" — no banco o CPF fica só com números. */
    private static String somenteDigitos(String cpf) {
        return cpf == null ? null : cpf.replaceAll("\\D", "");
    }
}
