package net.gb.knox.petopia.model;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import net.gb.knox.petopia.domain.Species;
import net.gb.knox.petopia.domain.Taxon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PetModelUnitTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    private static Stream<Arguments> modelValidationProvider() {
        return Stream.of(
                Arguments.of(
                        new PetModel(
                                1,
                                Taxon.DOG,
                                Species.LABRADOR,
                                "Buddy",
                                LocalDate.of(2020, 1, 1),
                                new AdoptionModel(),
                                new StatusModel()
                        ),
                        0
                ),
                Arguments.of(
                        new PetModel(
                                1, null, null, null, null, null, null
                        ),
                        4
                ),
                Arguments.of(
                        new PetModel(
                                1,
                                Taxon.DOG,
                                Species.LABRADOR,
                                "Buddy",
                                LocalDate.of(3000, 1, 1),
                                new AdoptionModel(),
                                new StatusModel()
                        ),
                        1
                )
        );
    }

    @Test
    public void testPetModel() {
        var pet = new PetModel();
        pet.setId(1);
        pet.setTaxon(Taxon.DOG);
        pet.setSpecies(Species.LABRADOR);
        pet.setName("Buddy");
        pet.setBirthDate(LocalDate.of(2020, 1, 1));

        var adoption = new AdoptionModel();
        pet.setAdoption(adoption);

        var status = new StatusModel();
        pet.setStatus(status);

        assertEquals(1, pet.getId());
        assertEquals(Taxon.DOG, pet.getTaxon());
        assertEquals(Species.LABRADOR, pet.getSpecies());
        assertEquals("Buddy", pet.getName());
        assertEquals(LocalDate.of(2020, 1, 1), pet.getBirthDate());
        assertNotNull(pet.getAdoption());
        assertNotNull(pet.getStatus());
    }

    @ParameterizedTest
    @MethodSource("modelValidationProvider")
    public void testValidation(PetModel petModel, int numberOfViolations) {
        var validator = validatorFactory.getValidator();

        var violations = validator.validate(petModel);

        assertEquals(numberOfViolations, violations.size());
    }
}
