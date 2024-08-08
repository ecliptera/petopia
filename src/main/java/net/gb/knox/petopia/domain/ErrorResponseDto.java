package net.gb.knox.petopia.domain;

public record ErrorResponseDto(
        String code,
        String message
) {
}
