package net.gb.knox.petopia.domain;

import net.gb.knox.petopia.model.AdoptionModel;
import net.gb.knox.petopia.model.StatusModel;

import java.time.LocalDate;

public record PetResponseDto(
        int id,
        Taxon taxon,
        Species species,
        String name,
        LocalDate birthDate,
        AdoptionModel adoption,
        StatusModel status
) {
}
