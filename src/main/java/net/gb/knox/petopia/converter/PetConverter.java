package net.gb.knox.petopia.converter;

import net.gb.knox.petopia.domain.CreatePetRequestDto;
import net.gb.knox.petopia.domain.PetResponseDto;
import net.gb.knox.petopia.model.PetModel;

public class PetConverter {

    public static PetModel createRequestDtoToModel(CreatePetRequestDto createPetRequestDto) {
        var petModel = new PetModel();
        petModel.setTaxon(createPetRequestDto.taxon());
        petModel.setSpecies(createPetRequestDto.species());
        petModel.setName(createPetRequestDto.name());
        petModel.setBirthDate(createPetRequestDto.birthDate());

        return petModel;
    }

    public static PetResponseDto modelToResponseDto(PetModel petModel) {
        return new PetResponseDto(petModel.getId(), petModel.getTaxon(), petModel.getSpecies(), petModel.getName(),
                                  petModel.getBirthDate(), petModel.getAdoption(), petModel.getStatus()
        );
    }
}
