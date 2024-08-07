package net.gb.knox.petopia.controller;

import net.gb.knox.petopia.domain.CreatePetRequestDto;
import net.gb.knox.petopia.domain.PatchUserPetStatusRequestDto;
import net.gb.knox.petopia.domain.PetResponseDto;
import net.gb.knox.petopia.domain.UpdatePetRequestDto;
import net.gb.knox.petopia.exception.InvalidStatusActionException;
import net.gb.knox.petopia.exception.ResourceNotFoundException;
import net.gb.knox.petopia.exception.UnsupportedStatusActionException;
import net.gb.knox.petopia.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/admin/pets")
    public ResponseEntity<PetResponseDto> createPet(@RequestBody CreatePetRequestDto createPetRequestDto) throws URISyntaxException {
        var petResponseDto = petService.createPet(createPetRequestDto);
        var location = new URI(String.format("/pets/%s", petResponseDto.id()));
        return ResponseEntity.created(location).body(petResponseDto);
    }

    @GetMapping("/admin/pets")
    public ResponseEntity<List<PetResponseDto>> getAllPets(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        var petResponseDtos = petService.getAllPets(sortBy, direction);
        return ResponseEntity.ok(petResponseDtos);
    }

    @GetMapping("/admin/pets/{id}")
    public ResponseEntity<PetResponseDto> getPetById(@PathVariable Integer id) throws ResourceNotFoundException {
        var petResponseDto = petService.getPet(id);
        return ResponseEntity.ok(petResponseDto);
    }

    @PatchMapping("/admin/pets/{id}")
    public ResponseEntity<PetResponseDto> updatePet(
            @PathVariable Integer id,
            @RequestBody UpdatePetRequestDto updatePetRequestDto
    ) throws ResourceNotFoundException {
        var petResponseDto = petService.updatePet(id, updatePetRequestDto);
        return ResponseEntity.ok(petResponseDto);
    }

    @DeleteMapping("/admin/pets/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Integer id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/pets")
    public ResponseEntity<List<PetResponseDto>> getAllUserPets(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        var petResponseDtos = petService.getAllUserPets(jwt.getSubject(), sortBy, direction);
        return ResponseEntity.ok(petResponseDtos);
    }

    @GetMapping("/users/pets/{id}")
    public ResponseEntity<PetResponseDto> getUserPet(@AuthenticationPrincipal Jwt jwt, @PathVariable Integer id)
            throws ResourceNotFoundException {
        var petResponseDto = petService.getUserPet(id, jwt.getSubject());
        return ResponseEntity.ok(petResponseDto);
    }

    @PatchMapping("/users/pets/{id}/status")
    public ResponseEntity<PetResponseDto> patchUserPetStatus(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Integer id,
            @RequestBody PatchUserPetStatusRequestDto patchUserPetStatusRequestDto
    ) throws InvalidStatusActionException, UnsupportedStatusActionException, ResourceNotFoundException {
        var petResponseDto = petService.patchUserPetStatus(id, jwt.getSubject(), patchUserPetStatusRequestDto);
        return ResponseEntity.ok(petResponseDto);
    }

    @GetMapping("/pets")
    public ResponseEntity<List<PetResponseDto>> getAllUnadoptedPets(
            @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        var petResponseDtos = petService.getAllUnadoptedPets(sortBy, direction);
        return ResponseEntity.ok(petResponseDtos);
    }

    @GetMapping("/pets/{id}")
    public ResponseEntity<PetResponseDto> getUnadoptedPet(@PathVariable Integer id) throws ResourceNotFoundException {
        var petResponseDto = petService.getUnadoptedPet(id);
        return ResponseEntity.ok(petResponseDto);
    }

    @PostMapping("/pets/{id}/adoptions")
    public ResponseEntity<PetResponseDto> adoptPet(@AuthenticationPrincipal Jwt jwt, @PathVariable Integer id)
            throws URISyntaxException, ResourceNotFoundException {
        var petResponseDto = petService.adoptPet(jwt.getSubject(), id);
        var location = new URI(String.format("/pets/%s/adoptions/%s", id, petResponseDto.adoption().getId()));
        return ResponseEntity.created(location).body(petResponseDto);
    }
}
