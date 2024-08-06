package net.gb.knox.petopia.service;

import jakarta.persistence.EntityNotFoundException;
import net.gb.knox.petopia.converter.PetConverter;
import net.gb.knox.petopia.domain.CreatePetRequestDto;
import net.gb.knox.petopia.domain.PetResponseDto;
import net.gb.knox.petopia.domain.UpdatePetRequestDto;
import net.gb.knox.petopia.model.PetModel;
import net.gb.knox.petopia.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    private Sort createSort(String sortBy, String direction) {
        Sort sort = null;

        if (sortBy != null) {
            if (direction.equalsIgnoreCase("asc")) {
                sort = Sort.by(Sort.Direction.ASC, sortBy);
            } else if (direction.equalsIgnoreCase("desc")) {
                sort = Sort.by(Sort.Direction.DESC, sortBy);
            }
        }

        return sort;
    }

    private PetModel findById(int id) throws EntityNotFoundException {
        var foundPetModel = petRepository.findById(id);
        if (foundPetModel.isEmpty()) {
            throw new EntityNotFoundException(String.format("No pet model found with id = %s", id));
        }

        return foundPetModel.get();
    }

    public PetResponseDto create(CreatePetRequestDto createPetRequestDto) {
        var petModel = PetConverter.createRequestDtoToModel(createPetRequestDto);

        petRepository.save(petModel);

        return PetConverter.modelToResponseDto(petModel);
    }

    public List<PetResponseDto> getAll(String sortBy, String direction) {
        var sort = createSort(sortBy, direction);
        var petModels = sort == null ? petRepository.findAll() : petRepository.findAll(sort);

        return petModels.stream().map(PetConverter::modelToResponseDto).toList();
    }

    public List<PetResponseDto> getAllUnadopted(String sortBy, String direction) {
        var sort = createSort(sortBy, direction);
        var petModels = sort == null ? petRepository.findAllByAdoptionIdNull() : petRepository.findAllByAdoptionIdNull(sort);

        return petModels.stream().map(PetConverter::modelToResponseDto).toList();
    }

    public PetResponseDto get(int id) throws EntityNotFoundException {
        var petModel = findById(id);
        return PetConverter.modelToResponseDto(petModel);
    }

    public PetResponseDto update(int id, UpdatePetRequestDto updatePetRequestDto) throws EntityNotFoundException {
        var petModel = findById(id);

        if (updatePetRequestDto.taxon() != null) {
            petModel.setTaxon(updatePetRequestDto.taxon());
        }
        if (updatePetRequestDto.species() != null) {
            petModel.setSpecies(updatePetRequestDto.species());
        }
        if (updatePetRequestDto.name() != null) {
            petModel.setName(updatePetRequestDto.name());
        }
        if (updatePetRequestDto.birthDate() != null) {
            petModel.setBirthDate(updatePetRequestDto.birthDate());
        }

        petRepository.save(petModel);

        return PetConverter.modelToResponseDto(petModel);
    }

    public void delete(int id) {
        petRepository.deleteById(id);
    }
}
