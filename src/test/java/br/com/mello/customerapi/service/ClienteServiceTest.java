package br.com.mello.customerapi.service;

import br.com.mello.customerapi.dto.ClienteRequest;
import br.com.mello.customerapi.exception.ClienteNaoEncontradoException;
import br.com.mello.customerapi.exception.DadoDuplicadoException;
import br.com.mello.customerapi.model.Cliente;
import br.com.mello.customerapi.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Teste de unidade puro: sem Spring e sem banco. O repository é um "dublê" do Mockito. */
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    private final ClienteRequest ana =
            new ClienteRequest("Ana", "ana@email.com", "529.982.247-25", null);

    @Test
    void criarDeveGuardarCpfSoComNumeros() {
        when(repository.save(any(Cliente.class))).thenAnswer(chamada -> chamada.getArgument(0));

        Cliente criado = service.criar(ana);

        assertThat(criado.getCpf()).isEqualTo("52998224725");
    }

    @Test
    void criarDeveRecusarEmailRepetido() {
        when(repository.existsByEmail("ana@email.com")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(ana)).isInstanceOf(DadoDuplicadoException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void criarDeveRecusarCpfRepetido() {
        when(repository.existsByCpf("52998224725")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(ana)).isInstanceOf(DadoDuplicadoException.class);
    }

    @Test
    void buscarIdInexistenteDeveLancarNaoEncontrado() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscar(99))
                .isInstanceOf(ClienteNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void atualizarDeveTrocarOsDados() {
        Cliente existente = new Cliente("Velho", "velho@email.com", "11144477735", null);
        when(repository.findById(1)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        Cliente atualizado = service.atualizar(1, ana);

        assertThat(atualizado.getNome()).isEqualTo("Ana");
        assertThat(atualizado.getCpf()).isEqualTo("52998224725");
    }

    @Test
    void excluirIdInexistenteDeveLancarNaoEncontrado() {
        when(repository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> service.excluir(99)).isInstanceOf(ClienteNaoEncontradoException.class);
        verify(repository, never()).deleteById(any());
    }
}
