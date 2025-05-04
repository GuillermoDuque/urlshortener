package com.dio.urlshortener.domain.service;

public interface ShortCodeGenerator {
    int DEFAULT_LENGTH = 6;

    String generate();
    String generate(int length);
}
