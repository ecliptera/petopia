package net.gb.knox.petopia.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdoptionModelUnitTest {

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
}
