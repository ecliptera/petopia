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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PetServiceUnitTest {

    private final PetRepository petRepository = mock(PetRepository.class);
    private final PetService petService = new PetService(petRepository);

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
    public void testCreate() {
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

        var petResponseDto = petService.create(createPetResponseDto);

        verify(petRepository, times(1)).save(any(PetModel.class));
        assertNotNull(petResponseDto);
    }

    @Test
    public void testGetAll() {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findAll()).thenReturn(List.of(petModel));

        var petResponseDtos = petService.getAll(null, null);

        verify(petRepository, times(1)).findAll();
        assertNotNull(petResponseDtos);
    }

    @ParameterizedTest
    @CsvSource({"name, asc", "name, desc", "name, invalid"})
    public void testGetAllWithSort(String sortBy, String direction) {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findAll(any(Sort.class))).thenReturn(List.of(petModel));

        var petResponseDtos = petService.getAll(sortBy, direction);

        var numberOfInvocations = List.of("asc", "desc").contains(direction) ? 1 : 0;
        verify(petRepository, times(numberOfInvocations)).findAll(any(Sort.class));
        assertNotNull(petResponseDtos);
    }

    @Test
    public void testGetAllUnadopted() {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findAllByAdoptionIdNull()).thenReturn(List.of(petModel));

        var petResponseDtos = petService.getAllUnadopted(null, null);

        verify(petRepository, times(1)).findAllByAdoptionIdNull();
        assertNotNull(petResponseDtos);
    }

    @ParameterizedTest
    @CsvSource({"name, asc", "name, desc", "name, invalid"})
    public void testGetAllUnadoptedWithSort(String sortBy, String direction) {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findAllByAdoptionIdNull(any(Sort.class))).thenReturn(List.of(petModel));

        var petResponseDtos = petService.getAllUnadopted(sortBy, direction);

        var numberOfInvocations = List.of("asc", "desc").contains(direction) ? 1 : 0;
        verify(petRepository, times(numberOfInvocations)).findAllByAdoptionIdNull(any(Sort.class));
        assertNotNull(petResponseDtos);
    }

    @Test
    public void testGet() {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findById(anyInt())).thenReturn(Optional.of(petModel));

        var petResponseDto = petService.get(1);

        verify(petRepository, times(1)).findById(anyInt());
        assertNotNull(petResponseDto);
    }

    @Test
    public void testGetThrows() {
        when(petRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> petService.get(1));
    }

    @Test
    public void testGetUnadopted() {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findByIdAndAdoptionIdNull(anyInt())).thenReturn(petModel);

        var petResponseDto = petService.getUnadopted(1);

        verify(petRepository, times(1)).findByIdAndAdoptionIdNull(anyInt());
        assertNotNull(petResponseDto);
    }

    @Test
    public void testGetUnadoptedThrows() {
        when(petRepository.findByIdAndAdoptionIdNull(anyInt())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> petService.getUnadopted(1));
    }

    @ParameterizedTest
    @MethodSource("updatePetRequestDtoProvider")
    public void testUpdate(UpdatePetRequestDto updatePetRequestDto) {
        var petModel = new PetModel();
        petModel.setId(1);
        when(petRepository.findById(anyInt())).thenReturn(Optional.of(petModel));

        var petResponseDto = petService.update(1, updatePetRequestDto);

        verify(petRepository, times(1)).save(any(PetModel.class));
        assertEquals(updatePetRequestDto.taxon(), petResponseDto.taxon());
        assertEquals(updatePetRequestDto.species(), petResponseDto.species());
        assertEquals(updatePetRequestDto.name(), petResponseDto.name());
        assertEquals(updatePetRequestDto.birthDate(), petResponseDto.birthDate());
    }

    @Test
    public void testDelete() {
        petService.delete(1);
        verify(petRepository, times(1)).deleteById(anyInt());
    }
}