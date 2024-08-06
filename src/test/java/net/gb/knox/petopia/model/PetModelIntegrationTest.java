package net.gb.knox.petopia.model;

import jakarta.persistence.EntityManager;
import net.gb.knox.petopia.domain.Species;
import net.gb.knox.petopia.domain.Taxon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class PetModelIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    private AdoptionModel createAdoptionModel() {
        var adoption = new AdoptionModel();
        adoption.setAdopterId("006620a5-c90a-431a-9192-e23014620380");
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
    public void testPersistPetModel() {
        var pet = createPetModel();

        persist(pet);

        assertNotNull(pet.getId());

        var persistedPet = entityManager.find(PetModel.class, pet.getId());
        assertNotNull(persistedPet);
    }

    @Test
    public void testPersistAdoptionModel() {
        var pet = createPetModel();

        var adoption = createAdoptionModel();
        pet.setAdoption(adoption);

        persist(pet);

        assertNotNull(pet.getId());

        var persistedAdoption = entityManager.find(AdoptionModel.class, adoption.getId());
        assertNotNull(persistedAdoption);
    }

    @Test
    public void testPersistStatusModel() {
        var pet = createPetModel();

        var status = new StatusModel();
        pet.setStatus(status);

        persist(pet);

        assertNotNull(pet.getId());

        var persistedStatus = entityManager.find(StatusModel.class, status.getId());
        assertNotNull(persistedStatus);
    }
}
