package net.gb.knox.petopia.converter;

import net.gb.knox.petopia.domain.CreatePetRequestDto;
import net.gb.knox.petopia.domain.Species;
import net.gb.knox.petopia.domain.Taxon;
import net.gb.knox.petopia.model.AdoptionModel;
import net.gb.knox.petopia.model.PetModel;
import net.gb.knox.petopia.model.StatusModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetConverterUnitTest {

    @Test
    public void testCreateRequestDtoToModel() {
        var createPetRequestDto = new CreatePetRequestDto(
                Taxon.DOG, Species.LABRADOR, "Buddy", LocalDate.of(2020, 1, 1));
        var petModel = PetConverter.createRequestDtoToModel(createPetRequestDto);

        assertEquals(createPetRequestDto.taxon(), petModel.getTaxon());
        assertEquals(createPetRequestDto.species(), petModel.getSpecies());
        assertEquals(createPetRequestDto.name(), petModel.getName());
        assertEquals(createPetRequestDto.birthDate(), petModel.getBirthDate());
    }

    @Test
    public void testConvertModelToRequestDto() {
        var petModel = new PetModel();
        petModel.setId(1);
        petModel.setTaxon(Taxon.DOG);
        petModel.setSpecies(Species.LABRADOR);
        petModel.setName("Buddy");
        petModel.setBirthDate(LocalDate.of(2020, 1, 1));
        petModel.setAdoption(new AdoptionModel());
        petModel.setStatus(new StatusModel());

        var petResponseDto = PetConverter.modelToResponseDto(petModel);

        assertEquals(petModel.getId(), petResponseDto.id());
        assertEquals(petModel.getTaxon(), petResponseDto.taxon());
        assertEquals(petModel.getSpecies(), petResponseDto.species());
        assertEquals(petModel.getName(), petResponseDto.name());
        assertEquals(petModel.getBirthDate(), petResponseDto.birthDate());
        assertEquals(petModel.getAdoption(), petResponseDto.adoption());
        assertEquals(petModel.getStatus(), petResponseDto.status());
    }
}
