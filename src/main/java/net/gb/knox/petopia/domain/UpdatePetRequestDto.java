package net.gb.knox.petopia.domain;

import java.time.LocalDate;

public record UpdatePetRequestDto(
        Taxon taxon,
        Species species,
        String name,
        LocalDate birthDate
) {
}
