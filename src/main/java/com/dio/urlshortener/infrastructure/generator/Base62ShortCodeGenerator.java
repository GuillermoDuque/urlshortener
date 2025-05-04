package com.dio.urlshortener.infrastructure.generator;

import com.dio.urlshortener.domain.service.ShortCodeGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class Base62ShortCodeGenerator implements ShortCodeGenerator {
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {
        return generate(DEFAULT_LENGTH);
    }

    @Override
    public String generate(int length) {

        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than zero");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(BASE62.length());
            sb.append(BASE62.charAt(index));
        }
        return sb.toString();
    }
}
