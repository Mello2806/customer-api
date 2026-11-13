package br.com.mello.customerapi.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class CpfValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"52998224725", "529.982.247-25", "111.444.777-35"})
    void deveAceitarCpfsValidos(String cpf) {
        assertThat(CpfValidator.isValido(cpf)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"52998224724", "111.111.111-11", "123", "abcdefghijk", ""})
    void deveRecusarCpfsInvalidos(String cpf) {
        assertThat(CpfValidator.isValido(cpf)).isFalse();
    }
}
