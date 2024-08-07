package net.gb.knox.petopia.service;

import net.gb.knox.petopia.converter.PetConverter;
import net.gb.knox.petopia.domain.*;
import net.gb.knox.petopia.exception.InvalidStatusActionException;
import net.gb.knox.petopia.exception.ResourceNotFoundException;
import net.gb.knox.petopia.exception.UnsupportedStatusActionException;
import net.gb.knox.petopia.model.AdoptionModel;
import net.gb.knox.petopia.model.PetModel;
import net.gb.knox.petopia.model.StatusModel;
import net.gb.knox.petopia.repository.PetRepository;
import net.gb.knox.petopia.utility.StatusActionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PetService {

    private final Clock clock;

    private final PetRepository petRepository;

    private final StatusActionHelper statusActionHelper;

    @Autowired
    public PetService(Clock clock, PetRepository petRepository, StatusActionHelper statusActionHelper) {
        this.clock = clock;
        this.petRepository = petRepository;
        this.statusActionHelper = statusActionHelper;
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

    private PetModel findById(int id) throws ResourceNotFoundException {
        var foundPetModel = petRepository.findById(id);
        if (foundPetModel.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No pet model found with id = %s", id));
        }

        return foundPetModel.get();
    }

    public PetResponseDto createPet(CreatePetRequestDto createPetRequestDto) {
        var petModel = PetConverter.createRequestDtoToModel(createPetRequestDto);

        petRepository.save(petModel);

        return PetConverter.modelToResponseDto(petModel);
    }

    public PetResponseDto adoptPet(String userId, int petId) throws ResourceNotFoundException {
        var petModel = findById(petId);

        var adoptionModel = new AdoptionModel();
        adoptionModel.setAdoptionDateTime(LocalDateTime.now(clock));
        adoptionModel.setAdopterId(userId);
        petModel.setAdoption(adoptionModel);

        var statusModel = new StatusModel();
        petModel.setStatus(statusModel);

        petRepository.save(petModel);

        return PetConverter.modelToResponseDto(petModel);
    }

    public List<PetResponseDto> getAllPets(String sortBy, String direction) {
        var sort = createSort(sortBy, direction);
        var petModels = sort == null ? petRepository.findAll() : petRepository.findAll(sort);

        return petModels.stream().map(PetConverter::modelToResponseDto).toList();
    }

    public List<PetResponseDto> getAllUnadoptedPets(String sortBy, String direction) {
        var sort = createSort(sortBy, direction);
        var petModels = sort == null
                ? petRepository.findAllByAdoptionIdNull()
                : petRepository.findAllByAdoptionIdNull(sort);

        return petModels.stream().map(PetConverter::modelToResponseDto).toList();
    }

    public List<PetResponseDto> getAllUserPets(String userId, String sortBy, String direction) {
        var sort = createSort(sortBy, direction);
        var petModels = sort == null
                ? petRepository.findAllByAdopterId(userId)
                : petRepository.findAllByAdopterId(userId, sort);

        return petModels.stream().map(PetConverter::modelToResponseDto).toList();
    }

    public PetResponseDto getUserPet(int id, String userId) throws ResourceNotFoundException {
        var petModel = petRepository.findByIdAndAdopterId(id, userId);
        if (petModel == null) {
            throw new ResourceNotFoundException(
                    String.format("No pet model found with id = %s and adopter id = %s", id, userId)
            );
        }

        return PetConverter.modelToResponseDto(petModel);
    }

    public PetResponseDto patchUserPetStatus(
            int id,
            String userId,
            PatchUserPetStatusRequestDto patchUserPetStatusRequestDto
    ) throws InvalidStatusActionException, ResourceNotFoundException, UnsupportedStatusActionException {

        var petModel = petRepository.findByIdAndAdopterId(id, userId);
        if (petModel == null) {
            throw new ResourceNotFoundException(
                    String.format("No pet model found with id = %s and adopter id = %s", id, userId)
            );
        }

        statusActionHelper.setPet(petModel);

        StatusAction statusAction = null;
        if (patchUserPetStatusRequestDto != null) {
            statusAction = patchUserPetStatusRequestDto.action();
        }
        if (statusAction == null) {
            throw new UnsupportedStatusActionException("Unsupported status action = null");
        }
        switch (statusAction) {
            case FEED -> statusActionHelper.feed();
            case PLAY -> statusActionHelper.play();
            case GROOM -> statusActionHelper.groom();
            case SLEEP -> statusActionHelper.sleep();
            default -> throw new UnsupportedStatusActionException(
                    String.format("Unsupported status action = %s", statusAction.name())
            );
        }

        petRepository.save(petModel);

        return PetConverter.modelToResponseDto(petModel);
    }

    public PetResponseDto getPet(int id) throws ResourceNotFoundException {
        var petModel = findById(id);
        return PetConverter.modelToResponseDto(petModel);
    }

    public PetResponseDto getUnadoptedPet(int id) throws ResourceNotFoundException {
        var petModel = petRepository.findByIdAndAdoptionIdNull(id);
        if (petModel == null) {
            throw new ResourceNotFoundException(String.format("No unadopted pet model found with id = %s", id));
        }

        return PetConverter.modelToResponseDto(petModel);
    }

    public PetResponseDto updatePet(int id, UpdatePetRequestDto updatePetRequestDto) throws ResourceNotFoundException {
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

    public void deletePet(int id) {
        petRepository.deleteById(id);
    }
}
