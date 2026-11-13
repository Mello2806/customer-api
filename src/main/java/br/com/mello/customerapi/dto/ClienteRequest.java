package br.com.mello.customerapi.dto;

import br.com.mello.customerapi.validation.Cpf;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * O que o cliente da API envia no corpo do POST/PUT.
 *
 * Por que não receber a entidade Cliente direto? Porque aí quem chama a API poderia
 * mandar "id" ou "dataCadastro" e bagunçar o banco. O DTO deixa entrar só o que pode.
 * As anotações só são conferidas quando o controller usa @Valid.
 */
public record ClienteRequest(

        @NotBlank(message = "é obrigatório")
        @Size(max = 100, message = "deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "é obrigatório")
        @Email(message = "deve ser um e-mail válido")
        @Size(max = 120, message = "deve ter no máximo 120 caracteres")
        String email,

        @NotBlank(message = "é obrigatório")
        @Cpf
        String cpf,

        @Pattern(regexp = "[0-9()+\\- ]{8,20}", message = "deve ter de 8 a 20 caracteres: números, espaço, ( ) + -")
        String telefone) {
}
