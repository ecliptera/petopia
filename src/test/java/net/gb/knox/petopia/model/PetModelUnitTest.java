package net.gb.knox.petopia.model;

import net.gb.knox.petopia.domain.Species;
import net.gb.knox.petopia.domain.Taxon;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PetModelUnitTest {

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
}
