package br.com.mello.customerapi.dto;

/**
 * O que o cliente da API envia no corpo do POST/PUT.
 *
 * Por que não receber a entidade Cliente direto? Porque aí quem chama a API poderia
 * mandar "id" ou "dataCadastro" e bagunçar o banco. O DTO deixa entrar só o que pode.
 * "record" é uma classe compacta do Java 17: campos finais, construtor e getters prontos.
 */
public record ClienteRequest(String nome, String email, String cpf, String telefone) {
}
