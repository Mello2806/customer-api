package br.com.mello.customerapi.repository;

import br.com.mello.customerapi.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

/** @DataJpaTest sobe só a parte de banco, usando o H2 em memória. */
@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository repository;

    @Test
    void deveSalvarGerandoIdEDataDeCadastro() {
        Cliente salvo = repository.save(new Cliente("Ana", "ana@email.com", "52998224725", null));

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getDataCadastro()).isNotNull();
    }

    @Test
    void deveListarEmOrdemAlfabetica() {
        repository.save(new Cliente("Carla", "carla@email.com", "11144477735", null));
        repository.save(new Cliente("Ana", "ana@email.com", "52998224725", null));

        assertThat(repository.findAllByOrderByNomeAsc())
                .extracting(Cliente::getNome)
                .containsExactly("Ana", "Carla");
    }

    @Test
    void deveDetectarEmailECpfJaCadastrados() {
        repository.save(new Cliente("Ana", "ana@email.com", "52998224725", null));

        assertThat(repository.existsByEmail("ana@email.com")).isTrue();
        assertThat(repository.existsByCpf("52998224725")).isTrue();
        assertThat(repository.existsByEmail("outro@email.com")).isFalse();
    }

    @Test
    void deveBuscarPorParteDoNomeSemDiferenciarMaiusculas() {
        repository.save(new Cliente("Ana Souza", "ana@email.com", "52998224725", null));
        repository.save(new Cliente("Mariana Lima", "mari@email.com", "11144477735", null));
        repository.save(new Cliente("Bruno", "bruno@email.com", "39053344705", null));

        Page<Cliente> pagina = repository.findByNomeContainingIgnoreCase("ANA", PageRequest.of(0, 10, Sort.by("nome")));

        assertThat(pagina.getContent()).extracting(Cliente::getNome).containsExactly("Ana Souza", "Mariana Lima");
    }

    @Test
    void devePaginarOsResultados() {
        repository.save(new Cliente("Ana", "ana@email.com", "52998224725", null));
        repository.save(new Cliente("Bruno", "bruno@email.com", "11144477735", null));
        repository.save(new Cliente("Carla", "carla@email.com", "39053344705", null));

        Page<Cliente> segunda = repository.findAll(PageRequest.of(1, 2, Sort.by("nome")));

        assertThat(segunda.getContent()).extracting(Cliente::getNome).containsExactly("Carla");
        assertThat(segunda.getTotalElements()).isEqualTo(3);
        assertThat(segunda.getTotalPages()).isEqualTo(2);
    }
}
