package net.gb.knox.petopia.domain;

import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record UpdatePetRequestDto(
        Taxon taxon,
        Species species,
        String name,
        @PastOrPresent LocalDate birthDate
) {
}
