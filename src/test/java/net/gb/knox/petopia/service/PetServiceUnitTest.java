package net.gb.knox.petopia.service;

import jakarta.persistence.EntityNotFoundException;
import net.gb.knox.petopia.domain.CreatePetRequestDto;
import net.gb.knox.petopia.domain.Species;
import net.gb.knox.petopia.domain.Taxon;
import net.gb.knox.petopia.domain.UpdatePetRequestDto;
import net.gb.knox.petopia.model.PetModel;
import net.gb.knox.petopia.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Sort;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PetServiceUnitTest {

    private final PetRepository petRepository = mock(PetRepository.class);
    private final PetService petService = new PetService(Clock.systemDefaultZone(), petRepository);

    private static Stream<Arguments> updatePetRequestDtoProvider() {
        return Stream.of(
                Arguments.of(new UpdatePetRequestDto(
                        Taxon.DOG,
                        Species.LABRADOR,
                        "Buddy",
                        LocalDate.of(2020, 1, 1)
                )),
                Arguments.of(new UpdatePetRequestDto(null, null, null, null))
        );
    }

    @Test
    public void testCreatePet() {
        when(petRepository.save(any(PetModel.class))).then(invocation -> {
            PetModel petModel = invocation.getArgument(0);
            petModel.setId(1);
            return petModel;
        });
        var createPetResponseDto = new CreatePetRequestDto(
                Taxon.DOG,
                Species.LABRADOR,
                "Buddy",
                LocalDate.of(2020, 1, 1)
        );

        var petResponseDto = petService.createPet(createPetResponseDto);

        verify(petRepository, times(1)).save(any(PetModel.class));
        assertNotNull(petResponseDto);
    }

    @Test
    public void testAdoptPet() {
        when(petRepository.findById(anyInt())).thenReturn(Optional.of(new PetModel()));
        when(petRepository.save(any(PetModel.class))).then(invocation -> {
            PetModel petModel = invocation.getArgument(0);
            petModel.setId(1);
            return petModel;
        });

        var petResponseDto = petService.adoptPet("006620a5-c90a-431a-9192-e23014620380", 1);

        verify(petRepository, times(1)).save(any(PetModel.class));
        assertNotNull(petResponseDto);
        assertNotNull(petResponseDto.adoption());
        assertNotNull(petResponseDto.status());
    }

    @Test
    public void testGetAllPets() {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findAll()).thenReturn(List.of(petModel));

        var petResponseDtos = petService.getAllPets(null, null);

        verify(petRepository, times(1)).findAll();
        assertNotNull(petResponseDtos);
    }

    @ParameterizedTest
    @CsvSource({"name, asc", "name, desc", "name, invalid"})
    public void testGetAllPetsWithSort(String sortBy, String direction) {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findAll(any(Sort.class))).thenReturn(List.of(petModel));

        var petResponseDtos = petService.getAllPets(sortBy, direction);

        var numberOfInvocations = List.of("asc", "desc").contains(direction) ? 1 : 0;
        verify(petRepository, times(numberOfInvocations)).findAll(any(Sort.class));
        assertNotNull(petResponseDtos);
    }

    @Test
    public void testGetAllUnadoptedPets() {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findAllByAdoptionIdNull()).thenReturn(List.of(petModel));

        var petResponseDtos = petService.getAllUnadoptedPets(null, null);

        verify(petRepository, times(1)).findAllByAdoptionIdNull();
        assertNotNull(petResponseDtos);
    }

    @ParameterizedTest
    @CsvSource({"name, asc", "name, desc", "name, invalid"})
    public void testGetAllUnadoptedPetsWithSort(String sortBy, String direction) {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findAllByAdoptionIdNull(any(Sort.class))).thenReturn(List.of(petModel));

        var petResponseDtos = petService.getAllUnadoptedPets(sortBy, direction);

        var numberOfInvocations = List.of("asc", "desc").contains(direction) ? 1 : 0;
        verify(petRepository, times(numberOfInvocations)).findAllByAdoptionIdNull(any(Sort.class));
        assertNotNull(petResponseDtos);
    }

    @Test
    public void testGetAllUserPets() {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findAllByAdopterId(anyString())).thenReturn(List.of(petModel));

        var petResponseDtos = petService.getAllUserPets("", null, null);

        verify(petRepository, times(1)).findAllByAdopterId(anyString());
        assertNotNull(petResponseDtos);
    }

    @ParameterizedTest
    @CsvSource({"name, asc", "name, desc", "name, invalid"})
    public void tesGetAllUserPetsSorted(String sortBy, String direction) {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findAllByAdopterId(anyString())).thenReturn(List.of(petModel));

        var petResponseDtos = petService.getAllUserPets("", sortBy, direction);

        var numberOfInvocations = List.of("asc", "desc").contains(direction) ? 1 : 0;
        verify(petRepository, times(numberOfInvocations)).findAllByAdopterId(anyString(), any(Sort.class));
        assertNotNull(petResponseDtos);
    }

    @Test
    public void testGetPet() {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findById(anyInt())).thenReturn(Optional.of(petModel));

        var petResponseDto = petService.getPet(1);

        verify(petRepository, times(1)).findById(anyInt());
        assertNotNull(petResponseDto);
    }

    @Test
    public void testGetPetThrows() {
        when(petRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> petService.getPet(1));
    }

    @Test
    public void testGetUserPet() {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findByIdAndAdopterId(anyInt(), anyString())).thenReturn(petModel);

        var petResponseDto = petService.getUserPet(1, "");

        verify(petRepository, times(1)).findByIdAndAdopterId(anyInt(), anyString());
        assertNotNull(petResponseDto);
    }

    @Test
    public void testGetUserPetThrows() {
        when(petRepository.findByIdAndAdopterId(anyInt(), anyString())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> petService.getUserPet(1, ""));
    }

    @Test
    public void testGetUnadoptedPet() {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findByIdAndAdoptionIdNull(anyInt())).thenReturn(petModel);

        var petResponseDto = petService.getUnadoptedPet(1);

        verify(petRepository, times(1)).findByIdAndAdoptionIdNull(anyInt());
        assertNotNull(petResponseDto);
    }

    @Test
    public void testGetUnadoptedPetThrows() {
        when(petRepository.findByIdAndAdoptionIdNull(anyInt())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> petService.getUnadoptedPet(1));
    }

    @ParameterizedTest
    @MethodSource("updatePetRequestDtoProvider")
    public void testUpdatePet(UpdatePetRequestDto updatePetRequestDto) {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findById(anyInt())).thenReturn(Optional.of(petModel));

        var petResponseDto = petService.updatePet(1, updatePetRequestDto);

        verify(petRepository, times(1)).save(any(PetModel.class));
        assertEquals(updatePetRequestDto.taxon(), petResponseDto.taxon());
        assertEquals(updatePetRequestDto.species(), petResponseDto.species());
        assertEquals(updatePetRequestDto.name(), petResponseDto.name());
        assertEquals(updatePetRequestDto.birthDate(), petResponseDto.birthDate());
    }

    @Test
    public void testDeletePet() {
        petService.deletePet(1);
        verify(petRepository, times(1)).deleteById(anyInt());
    }
}