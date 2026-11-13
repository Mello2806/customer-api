package br.com.mello.customerapi.controller;

import br.com.mello.customerapi.model.Cliente;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    /** Por enquanto a lista é fixa, em memória. No próximo passo ela vem do PostgreSQL. */
    @GetMapping
    public List<Cliente> listar() {
        return List.of(
                new Cliente(1, "Ana Souza", "ana@email.com", "52998224725", "11999990000"),
                new Cliente(2, "Bruno Lima", "bruno@email.com", "11144477735", null));
    }
}
