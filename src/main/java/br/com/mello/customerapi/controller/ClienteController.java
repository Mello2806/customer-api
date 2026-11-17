package br.com.mello.customerapi.controller;

import br.com.mello.customerapi.dto.ClienteRequest;
import br.com.mello.customerapi.dto.PaginaResponse;
import br.com.mello.customerapi.model.Cliente;
import br.com.mello.customerapi.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * CRUD de clientes. Cada método = um verbo HTTP + caminho:
 *   GET    /clientes       lista (paginado)      → 200
 *          ?nome=ana&pagina=0&tamanho=10   (todos opcionais)
 *   GET    /clientes/{id}  busca um              → 200 ou 404
 *   POST   /clientes       cadastra              → 201 + cabeçalho Location
 *   PUT    /clientes/{id}  atualiza              → 200 ou 404
 *   DELETE /clientes/{id}  exclui                → 204 (sem corpo) ou 404
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public PaginaResponse<Cliente> listar(@RequestParam(required = false) String nome,
                                          @RequestParam(defaultValue = "0") int pagina,
                                          @RequestParam(defaultValue = "10") int tamanho) {
        return PaginaResponse.de(service.listar(nome, pagina, tamanho));
    }

    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable Integer id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody ClienteRequest dados) {
        Cliente criado = service.criar(dados);
        return ResponseEntity.created(URI.create("/clientes/" + criado.getId())).body(criado);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Integer id, @Valid @RequestBody ClienteRequest dados) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
