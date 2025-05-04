package com.dio.urlshortener.infrastructure.generator;

import com.dio.urlshortener.domain.service.ShortCodeGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class Base62ShortCodeGeneratorTest {

    private final ShortCodeGenerator generator = new Base62ShortCodeGenerator();
    private static final int CODE_LENGTH = 6;

    @Test
    void generate_shouldReturnNonNullCodeOfCorrectLengthAndCharset() {
        String code = generator.generate(CODE_LENGTH);

        assertThat(code)
                .isNotNull()
                .hasSize(CODE_LENGTH)
                .matches("[a-zA-Z0-9]+");
    }

    @Test
    void generate_shouldProduceDiverseResults() {
        Set<String> generated = new HashSet<>();


        for (int i = 0; i < 100_000; i++) {
            String code = generator.generate(CODE_LENGTH);
            assertThat(code).hasSize(CODE_LENGTH).matches("[a-zA-Z0-9]+");
            generated.add(code);
        }

        assertThat(generated).hasSizeGreaterThan(99_990);
    }

    @Test
    void generate_shouldThrowIfLengthIsZeroOrNegative() {
        assertThatThrownBy(() -> generator.generate(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Length must be greater than zero");

        assertThatThrownBy(() -> generator.generate(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Length must be greater than zero");
    }

}