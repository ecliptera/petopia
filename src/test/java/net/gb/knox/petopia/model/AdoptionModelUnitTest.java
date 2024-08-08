package net.gb.knox.petopia.model;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdoptionModelUnitTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    private static Stream<Arguments> modelValidationProvider() {
        var validModel = new AdoptionModel(1, "006620a5-c90a-431a-9192-e23014620380",
                                           LocalDateTime.of(2024, 1, 1, 12, 0), new PetModel()
        );
        var nullModel = new AdoptionModel(1, "006620a5-c90a-431a-9192-e23014620380",
                                          LocalDateTime.of(2024, 1, 1, 12, 0), new PetModel()
        );
        var futureModel = new AdoptionModel(
                1, "006620a5-c90a-431a-9192-e23014620380", LocalDateTime.of(3000, 1, 1, 12, 0), null);

        return Stream.of(Arguments.of(validModel, 0), Arguments.of(nullModel, 2), Arguments.of(futureModel, 1));
    }

    @Test
    public void testAdoptionModel() {
        var adoption = new AdoptionModel();
        adoption.setId(1);
        adoption.setAdoptionDateTime(LocalDateTime.now());

        var pet = new PetModel();
        adoption.setPet(pet);

        assertEquals(1, adoption.getId());
        assertNotNull(adoption.getAdoptionDateTime());
        assertNotNull(adoption.getPet());
    }

    @ParameterizedTest
    @MethodSource("modelValidationProvider")
    public void testValidation(AdoptionModel adoptionModel, int numberOfViolations) {
        var validator = validatorFactory.getValidator();

        var violations = validator.validate(adoptionModel);

        assertEquals(numberOfViolations, violations.size());
    }
}
