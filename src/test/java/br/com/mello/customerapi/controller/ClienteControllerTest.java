package br.com.mello.customerapi.controller;

import br.com.mello.customerapi.dto.ClienteRequest;
import br.com.mello.customerapi.exception.ClienteNaoEncontradoException;
import br.com.mello.customerapi.exception.DadoDuplicadoException;
import br.com.mello.customerapi.model.Cliente;
import br.com.mello.customerapi.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** @MockBean troca o service de verdade por um "dublê": testamos só a parte web. */
@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    private static final String JSON_ANA = """
            {"nome": "Ana Souza", "email": "ana@email.com", "cpf": "529.982.247-25", "telefone": "11999990000"}
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService service;

    private static Cliente clienteComId(Integer id, String nome) {
        Cliente cliente = new Cliente(nome, "ana@email.com", "52998224725", null);
        cliente.setId(id);
        return cliente;
    }

    @Test
    void deveListarClientesEmJson() throws Exception {
        when(service.listar()).thenReturn(List.of(clienteComId(1, "Ana Souza"), clienteComId(2, "Bruno Lima")));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Ana Souza"));
    }

    @Test
    void deveBuscarPorId() throws Exception {
        when(service.buscar(1)).thenReturn(clienteComId(1, "Ana Souza"));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarInexistenteDeveDar404() throws Exception {
        when(service.buscar(99)).thenThrow(new ClienteNaoEncontradoException(99));

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Cliente 99 não encontrado"));
    }

    @Test
    void criarDeveDar201ComLocation() throws Exception {
        when(service.criar(any(ClienteRequest.class))).thenReturn(clienteComId(7, "Ana Souza"));

        mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content(JSON_ANA))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/clientes/7"))
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void criarDuplicadoDeveDar409() throws Exception {
        when(service.criar(any(ClienteRequest.class))).thenThrow(new DadoDuplicadoException("CPF já cadastrado"));

        mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content(JSON_ANA))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.erro").value("CPF já cadastrado"));
    }

    @Test
    void criarComCamposInvalidosDeveDar400ListandoOsCampos() throws Exception {
        String invalido = """
                {"nome": "", "email": "nao-e-email", "cpf": "111.111.111-11"}
                """;

        mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content(invalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nome").value("é obrigatório"))
                .andExpect(jsonPath("$.campos.email").value("deve ser um e-mail válido"))
                .andExpect(jsonPath("$.campos.cpf").value("CPF inválido"));
        verify(service, never()).criar(any());
    }

    @Test
    void jsonMalFormadoDeveDar400() throws Exception {
        mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content("{\"nome\": "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("JSON inválido ou mal formatado"));
    }

    @Test
    void deveAtualizar() throws Exception {
        when(service.atualizar(eq(1), any(ClienteRequest.class))).thenReturn(clienteComId(1, "Ana Souza"));

        mockMvc.perform(put("/clientes/1").contentType(MediaType.APPLICATION_JSON).content(JSON_ANA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Souza"));
    }

    @Test
    void deveExcluirCom204() throws Exception {
        mockMvc.perform(delete("/clientes/1")).andExpect(status().isNoContent());
        verify(service).excluir(1);
    }

    @Test
    void excluirInexistenteDeveDar404() throws Exception {
        doThrow(new ClienteNaoEncontradoException(99)).when(service).excluir(99);

        mockMvc.perform(delete("/clientes/99")).andExpect(status().isNotFound());
    }
}
