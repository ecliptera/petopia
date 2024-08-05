package net.gb.knox.petopia.repository;

import net.gb.knox.petopia.model.PetModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Rollback
public class PetRepositoryIntegrationTest {

    @Autowired
    private PetRepository petRepository;

    @Test
    public void testSave() {
        var petModel = new PetModel();
        petRepository.save(petModel);
        assertNotNull(petModel);
    }

    @Test
    public void testFindById() {
        var petModel = new PetModel();
        petRepository.save(petModel);

        var foundPetModel = petRepository.findById(petModel.getId());
        assertNotNull(foundPetModel);
    }

    @Test
    public void testFindAll() {
        var petModel = new PetModel();
        petRepository.save(petModel);

        var foundPetModels = petRepository.findAll();
        assertNotNull(foundPetModels);
    }

    @Test
    public void testUpdate() {
        var petModel = new PetModel();
        petRepository.save(petModel);
        assertNull(petModel.getName());

        petModel.setName("Buddy");
        petRepository.save(petModel);
        assertEquals("Buddy", petModel.getName());
    }

    @Test
    public void testDeleteById() {
        var petModel = new PetModel();
        petRepository.save(petModel);

        petRepository.deleteById(petModel.getId());

        var foundPetModel = petRepository.findById(petModel.getId());
        assertEquals(Optional.empty(), foundPetModel);
    }
}
