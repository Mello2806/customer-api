package br.com.mello.customerapi.repository;

import br.com.mello.customerapi.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
}
