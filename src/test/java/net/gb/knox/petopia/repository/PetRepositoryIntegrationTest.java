package net.gb.knox.petopia.repository;

import net.gb.knox.petopia.domain.Species;
import net.gb.knox.petopia.domain.Taxon;
import net.gb.knox.petopia.model.AdoptionModel;
import net.gb.knox.petopia.model.PetModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PetRepositoryIntegrationTest {

    @Autowired
    private PetRepository petRepository;

    private PetModel createPetModel() {
        var petModel = new PetModel();
        petModel.setTaxon(Taxon.DOG);
        petModel.setSpecies(Species.LABRADOR);
        petModel.setName("Buddy");
        petModel.setBirthDate(LocalDate.of(2020, 1, 1));

        return petModel;
    }

    private AdoptionModel createAdoptionModel() {
        var adoptionModel = new AdoptionModel();
        adoptionModel.setAdopterId("006620a5-c90a-431a-9192-e23014620380");
        adoptionModel.setAdoptionDateTime(LocalDateTime.now());

        return adoptionModel;
    }

    @Test
    public void testSave() {
        var petModel = createPetModel();
        petRepository.save(petModel);
        assertNotNull(petModel);
    }

    @Test
    public void testFindById() {
        var petModel = createPetModel();
        petRepository.saveAndFlush(petModel);

        var foundPetModel = petRepository.findById(petModel.getId());
        assertNotNull(foundPetModel);
    }

    @Test
    public void testFindAll() {
        var petModel = createPetModel();
        petRepository.saveAndFlush(petModel);

        var foundPetModels = petRepository.findAll();
        assertNotNull(foundPetModels);
    }

    @Test
    public void testFindAllByAdoptionIdNull() {
        var petModel = createPetModel();

        var adoptedPetModel = createPetModel();
        adoptedPetModel.setAdoption(createAdoptionModel());

        petRepository.saveAllAndFlush(List.of(petModel, adoptedPetModel));

        var foundPetModels = petRepository.findAllByAdoptionIdNull();

        assertNotNull(foundPetModels);
        assertEquals(1, foundPetModels.size());
        assertEquals(petModel, foundPetModels.getFirst());
    }

    @Test
    public void testFindAllByAdoptionIdNullSorted() {
        var petModel1 = createPetModel();
        var petModel2 = createPetModel();
        petModel2.setName("Ziggy");

        var adoptedPetModel = createPetModel();
        adoptedPetModel.setAdoption(createAdoptionModel());

        petRepository.saveAllAndFlush(List.of(petModel1, adoptedPetModel, petModel2));

        var foundPetModels = petRepository.findAllByAdoptionIdNull(Sort.by(Sort.Direction.DESC, "name"));

        assertNotNull(foundPetModels);
        assertEquals(2, foundPetModels.size());
        assertArrayEquals(new String[]{"Ziggy", "Buddy"}, foundPetModels.stream().map(PetModel::getName).toArray());
    }

    @Test
    public void testFindByIdAndAdoptionIdNull() {
        var petModel = createPetModel();
        var adoptedPetModel = createPetModel();
        adoptedPetModel.setAdoption(createAdoptionModel());

        petRepository.saveAllAndFlush(List.of(petModel, adoptedPetModel));

        var foundPetModel = petRepository.findByIdAndAdoptionIdNull(petModel.getId());
        assertNotNull(foundPetModel);

        var foundAdoptedPetModel = petRepository.findByIdAndAdoptionIdNull(adoptedPetModel.getId());
        assertNull(foundAdoptedPetModel);
    }

    @Test
    public void testUpdate() {
        var petModel = createPetModel();
        petRepository.saveAndFlush(petModel);
        assertEquals("Buddy", petModel.getName());

        petModel.setName("Stan");
        petRepository.save(petModel);
        assertEquals("Stan", petModel.getName());
    }

    @Test
    public void testDeleteById() {
        var petModel = createPetModel();
        petRepository.saveAndFlush(petModel);

        petRepository.deleteById(petModel.getId());

        var foundPetModel = petRepository.findById(petModel.getId());
        assertEquals(Optional.empty(), foundPetModel);
    }
}
