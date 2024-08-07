package net.gb.knox.petopia.domain;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatchUserPetStatusRequestDtoUnitTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    private static Stream<Arguments> dtoValidationProvider() {
        return Stream.of(
                Arguments.of(new PatchUserPetStatusRequestDto(StatusAction.FEED), 0),
                Arguments.of(new PatchUserPetStatusRequestDto(null), 1)
        );
    }

    @ParameterizedTest
    @MethodSource("dtoValidationProvider")
    public void testValidation(PatchUserPetStatusRequestDto patchUserPetStatusRequestDto, int numberOfViolations) {
        var validator = validatorFactory.getValidator();

        var violations = validator.validate(patchUserPetStatusRequestDto);

        assertEquals(numberOfViolations, violations.size());
    }
}
