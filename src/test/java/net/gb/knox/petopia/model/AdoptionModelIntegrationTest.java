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
public class AdoptionModelIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    private PetModel createPetModel() {
        var pet = new PetModel();
        pet.setTaxon(Taxon.DOG);
        pet.setSpecies(Species.LABRADOR);
        pet.setName("Buddy");
        pet.setBirthDate(LocalDate.of(2020, 1, 1));

        return pet;
    }

    private AdoptionModel createAdoptionModel() {
        var adoption = new AdoptionModel();
        adoption.setAdoptionDateTime(LocalDateTime.now());

        return adoption;
    }

    private void persist(PetModel pet) {
        entityManager.persist(pet);
        entityManager.flush();
    }

    private void persist(AdoptionModel adoption) {
        entityManager.persist(adoption);
        entityManager.flush();
    }

    @Test
    @DirtiesContext
    public void testPersistAdoptionModel() {
        var adoption = createAdoptionModel();

        persist(adoption);

        assertEquals(1, adoption.getId());

        var persistedAdoption = entityManager.find(AdoptionModel.class, adoption.getId());
        assertNotNull(persistedAdoption);
    }

    @Test
    @DirtiesContext
    public void testPersistPetModel() {
        var adoption = createAdoptionModel();

        var pet = createPetModel();
        adoption.setPet(pet);

        persist(pet);
        persist(adoption);

        assertEquals(1, adoption.getPet().getId());
    }
}
