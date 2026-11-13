package br.com.mello.customerapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Mesma conta do ValidadorCpf do customer-management-system, agora plugada no Bean Validation.
 *
 * Os 9 primeiros dígitos são multiplicados por pesos decrescentes (10, 9, ..., 2); o resto
 * da soma por 11 define o 1º dígito verificador. O 2º usa os 10 primeiros, pesos de 11 a 2.
 */
public class CpfValidator implements ConstraintValidator<Cpf, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext contexto) {
        if (cpf == null) {
            return true; // campo vazio é problema do @NotBlank, não deste validador
        }
        return isValido(cpf);
    }

    public static boolean isValido(String cpf) {
        String digitos = cpf.replaceAll("\\D", "");
        if (digitos.length() != 11) {
            return false;
        }
        // CPFs com todos os dígitos iguais (111.111.111-11) passam na conta, mas são inválidos.
        if (digitos.chars().distinct().count() == 1) {
            return false;
        }
        return calcularDigito(digitos, 9) == digitos.charAt(9) - '0'
                && calcularDigito(digitos, 10) == digitos.charAt(10) - '0';
    }

    private static int calcularDigito(String digitos, int quantidade) {
        int soma = 0;
        int peso = quantidade + 1;
        for (int i = 0; i < quantidade; i++) {
            soma += (digitos.charAt(i) - '0') * peso--;
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
