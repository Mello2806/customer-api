package br.com.mello.customerapi.controller;

import br.com.mello.customerapi.model.Cliente;
import br.com.mello.customerapi.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** @MockBean troca o service de verdade por um "dublê": testamos só a parte web. */
@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService service;

    @Test
    void deveListarClientesEmJson() throws Exception {
        when(service.listar()).thenReturn(List.of(
                new Cliente("Ana Souza", "ana@email.com", "52998224725", "11999990000"),
                new Cliente("Bruno Lima", "bruno@email.com", "11144477735", null)));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Ana Souza"));
    }
}
