package net.gb.knox.petopia.domain;

import java.time.LocalDate;

public record CreatePetRequestDto(
        Taxon taxon,
        Species species,
        String name,
        LocalDate birthDate
) {
}
