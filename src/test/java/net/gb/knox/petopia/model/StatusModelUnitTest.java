package net.gb.knox.petopia.model;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusModelUnitTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    private static Stream<Arguments> modelValidationProvider() {
        return Stream.of(
                Arguments.of(
                        new StatusModel(1, 100, 100, 100, 100),
                        0
                ),
                Arguments.of(
                        new StatusModel(1, 0, 0, 0, 0),
                        0
                ),
                Arguments.of(
                        new StatusModel(1, 101, 101, 101, 101),
                        4
                ),
                Arguments.of(
                        new StatusModel(1, -1, -1, -1, -1),
                        4
                )
        );
    }

    @Test
    public void testStatusModel() {
        var status = new StatusModel();
        status.setId(1);
        status.setHappiness(50);
        status.setCleanliness(50);
        status.setHunger(50);
        status.setTiredness(50);

        assertEquals(1, status.getId());
        assertEquals(50, status.getHappiness());
        assertEquals(50, status.getCleanliness());
        assertEquals(50, status.getHunger());
        assertEquals(50, status.getTiredness());
    }

    @Test
    public void testDefaultStatusModel() {
        var status = new StatusModel();

        assertEquals(100, status.getHappiness());
        assertEquals(100, status.getCleanliness());
        assertEquals(100, status.getHunger());
        assertEquals(100, status.getTiredness());
    }

    @ParameterizedTest
    @MethodSource("modelValidationProvider")
    public void testValidation(StatusModel statusModel, int numberOfViolations) {
        var validator = validatorFactory.getValidator();

        var violations = validator.validate(statusModel);

        assertEquals(numberOfViolations, violations.size());
    }
}
