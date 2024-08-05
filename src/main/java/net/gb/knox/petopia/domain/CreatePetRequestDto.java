package net.gb.knox.petopia.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record CreatePetRequestDto(
        @NotNull
        Taxon taxon,
        @NotNull
        Species species,
        @NotBlank
        String name,
        @NotNull
        @PastOrPresent
        LocalDate birthDate
) {
}
