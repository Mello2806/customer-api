package br.com.mello.customerapi.repository;

import br.com.mello.customerapi.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Compare com o ClienteDAOJdbc do customer-management-system: aqui não escrevemos SQL nenhum.
 * O Spring Data cria a implementação sozinho — findAll, findById, save, deleteById já vêm prontos.
 * Métodos extras são gerados a partir do NOME: existsByEmail vira "SELECT ... WHERE email = ?".
 */
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findAllByOrderByNomeAsc();

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    /** Usado na atualização: "existe OUTRO cliente (id diferente) com este e-mail?" */
    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByCpfAndIdNot(String cpf, Integer id);
}
