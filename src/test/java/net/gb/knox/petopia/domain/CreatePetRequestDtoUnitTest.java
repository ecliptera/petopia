package net.gb.knox.petopia.domain;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreatePetRequestDtoUnitTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    private static Stream<Arguments> dtoValidationProvider() {
        var validDto = new CreatePetRequestDto(Taxon.DOG, Species.LABRADOR, "Buddy", LocalDate.of(2020, 1, 1));
        var nullDto = new CreatePetRequestDto(null, null, null, null);
        var futureDto = new CreatePetRequestDto(Taxon.DOG, Species.LABRADOR, "Buddy", LocalDate.of(3000, 1, 1));

        return Stream.of(Arguments.of(validDto, 0), Arguments.of(nullDto, 4), Arguments.of(futureDto, 1));
    }

    @ParameterizedTest
    @MethodSource("dtoValidationProvider")
    public void testValidation(CreatePetRequestDto createPetRequestDto, int numberOfViolations) {
        var validator = validatorFactory.getValidator();

        var violations = validator.validate(createPetRequestDto);

        assertEquals(numberOfViolations, violations.size());
    }
}
