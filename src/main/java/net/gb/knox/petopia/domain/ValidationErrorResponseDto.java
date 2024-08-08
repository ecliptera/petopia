package net.gb.knox.petopia.domain;

import java.util.Map;

public record ValidationErrorResponseDto(String code, Map<String, String> fieldToError) {
    public ValidationErrorResponseDto(Map<String, String> fieldToError) {
        this("ValidationError", fieldToError);
    }
}
