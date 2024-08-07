package net.gb.knox.petopia.controller;

import net.gb.knox.petopia.domain.CreatePetRequestDto;
import net.gb.knox.petopia.domain.PetResponseDto;
import net.gb.knox.petopia.domain.UpdatePetRequestDto;
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
        var petResponseDto = petService.create(createPetRequestDto);
        var location = new URI(String.format("/pets/%s", petResponseDto.id()));
        return ResponseEntity.created(location).body(petResponseDto);
    }

    @GetMapping("/admin/pets")
    public ResponseEntity<List<PetResponseDto>> getAllPets(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        var petResponseDtos = petService.getAll(sortBy, direction);
        return ResponseEntity.ok(petResponseDtos);
    }

    @GetMapping("/admin/pets/{id}")
    public ResponseEntity<PetResponseDto> getPetById(@PathVariable Integer id) {
        var petResponseDto = petService.get(id);
        return ResponseEntity.ok(petResponseDto);
    }

    @PatchMapping("/admin/pets/{id}")
    public ResponseEntity<PetResponseDto> updatePet(
            @PathVariable Integer id,
            @RequestBody UpdatePetRequestDto updatePetRequestDto
    ) {
        var petResponseDto = petService.update(id, updatePetRequestDto);
        return ResponseEntity.ok(petResponseDto);
    }

    @DeleteMapping("/admin/pets/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Integer id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/pets")
    public ResponseEntity<List<PetResponseDto>> getAllUserPets(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        var petResponseDtos = petService.getAllUserPets(jwt.getSubject(), sortBy, direction);
        return ResponseEntity.ok(petResponseDtos);
    }

    @GetMapping("/pets")
    public ResponseEntity<List<PetResponseDto>> getAllUnadoptedPets(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        var petResponseDtos = petService.getAllUnadopted(sortBy, direction);
        return ResponseEntity.ok(petResponseDtos);
    }

    @GetMapping("/pets/{id}")
    public ResponseEntity<PetResponseDto> getUnadoptedPet(@PathVariable Integer id) {
        var petResponseDto = petService.getUnadopted(id);
        return ResponseEntity.ok(petResponseDto);
    }

    @PostMapping("/pets/{id}/adoptions")
    public ResponseEntity<PetResponseDto> adoptPet(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Integer id
    ) throws URISyntaxException {
        var petResponseDto = petService.adopt(jwt.getSubject(), id);
        var location = new URI(String.format("/pets/%s/adoptions/%s", id, petResponseDto.adoption().getId()));
        return ResponseEntity.created(location).body(petResponseDto);
    }
}
