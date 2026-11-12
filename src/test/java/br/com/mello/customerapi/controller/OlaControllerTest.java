package br.com.mello.customerapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @WebMvcTest sobe só a parte web do Spring (sem banco, sem servidor de verdade)
 * e o MockMvc simula uma requisição HTTP, como se fosse o navegador.
 */
@WebMvcTest(OlaController.class)
class OlaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveResponderNoEndpointOla() throws Exception {
        mockMvc.perform(get("/ola"))
                .andExpect(status().isOk())
                .andExpect(content().string("Olá! A API está no ar."));
    }
}
