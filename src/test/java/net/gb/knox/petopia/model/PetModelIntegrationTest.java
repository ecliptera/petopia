package net.gb.knox.petopia.model;

import jakarta.persistence.EntityManager;
import net.gb.knox.petopia.domain.Species;
import net.gb.knox.petopia.domain.Taxon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class PetModelIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    private AdoptionModel createAdoptionModel() {
        var adoption = new AdoptionModel();
        adoption.setAdoptionDateTime(LocalDateTime.now());

        return adoption;
    }

    private PetModel createPetModel() {
        var pet = new PetModel();
        pet.setTaxon(Taxon.DOG);
        pet.setSpecies(Species.LABRADOR);
        pet.setName("Buddy");
        pet.setBirthDate(LocalDate.of(2020, 1, 1));

        return pet;
    }

    private void persist(PetModel pet) {
        entityManager.persist(pet);
        entityManager.flush();
    }

    @Test
    @DirtiesContext
    public void testPersistPetModel() {
        var pet = createPetModel();

        persist(pet);

        assertEquals(1, pet.getId());

        var persistedPet = entityManager.find(PetModel.class, pet.getId());
        assertNotNull(persistedPet);
    }

    @Test
    @DirtiesContext
    public void testPersistAdoptionModel() {
        var pet = createPetModel();

        var adoption = createAdoptionModel();
        pet.setAdoption(adoption);

        persist(pet);

        assertEquals(1, adoption.getId());

        var persistedAdoption = entityManager.find(AdoptionModel.class, adoption.getId());
        assertNotNull(persistedAdoption);
    }

    @Test
    @DirtiesContext
    public void testPersistStatusModel() {
        var pet = createPetModel();

        var status = new StatusModel();
        pet.setStatus(status);

        persist(pet);

        assertEquals(1, status.getId());

        var persistedStatus = entityManager.find(StatusModel.class, status.getId());
        assertNotNull(persistedStatus);
    }
}
