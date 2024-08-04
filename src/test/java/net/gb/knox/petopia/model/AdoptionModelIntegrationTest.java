package net.gb.knox.petopia.model;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class AdoptionModelIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    private AdoptionModel base() {
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
        var adoption = base();

        persist(adoption);

        assertEquals(1, adoption.getId());

        var persistedAdoption = entityManager.find(AdoptionModel.class, adoption.getId());
        assertNotNull(persistedAdoption);
    }

    @Test
    @DirtiesContext
    public void testPersistPetModel() {
        var adoption = base();

        var pet = new PetModel();
        adoption.setPet(pet);

        persist(pet);
        persist(adoption);

        assertEquals(1, adoption.getPet().getId());
    }
}
