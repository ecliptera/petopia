package net.gb.knox.petopia.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.gb.knox.petopia.domain.*;
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
@Tag(name = "Pet Management", description = "Operations related to pet management")
@ApiResponse(responseCode = "401", description = "User is unauthenticated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
@ApiResponse(responseCode = "403", description = "User does not have the correct privilege for resource", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/admin/pets")
    @ApiResponse(responseCode = "201", description = "Created pet", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponseDto.class)))
    public ResponseEntity<PetResponseDto> createPet(@Valid @RequestBody CreatePetRequestDto createPetRequestDto) throws URISyntaxException {
        var petResponseDto = petService.createPet(createPetRequestDto);
        var location = new URI(String.format("/pets/%s", petResponseDto.id()));
        return ResponseEntity.created(location).body(petResponseDto);
    }

    @GetMapping("/admin/pets")
    @ApiResponse(responseCode = "200", description = "Got all pets", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponseDto[].class)))
    public ResponseEntity<List<PetResponseDto>> getAllPets(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        var petResponseDtos = petService.getAllPets(sortBy, direction);
        return ResponseEntity.ok(petResponseDtos);
    }

    @GetMapping("/admin/pets/{id}")
    @ApiResponse(responseCode = "200", description = "Got pet", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    public ResponseEntity<PetResponseDto> getPetById(@PathVariable Integer id) throws ResourceNotFoundException {
        var petResponseDto = petService.getPet(id);
        return ResponseEntity.ok(petResponseDto);
    }

    @PatchMapping("/admin/pets/{id}")
    @ApiResponse(responseCode = "200", description = "Updated pet", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    public ResponseEntity<PetResponseDto> updatePet(
            @PathVariable Integer id,
            @Valid @RequestBody UpdatePetRequestDto updatePetRequestDto
    ) throws ResourceNotFoundException {
        var petResponseDto = petService.updatePet(id, updatePetRequestDto);
        return ResponseEntity.ok(petResponseDto);
    }

    @DeleteMapping("/admin/pets/{id}")
    @ApiResponse(responseCode = "204", description = "Deleted pet")
    public ResponseEntity<Void> deletePet(@PathVariable Integer id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/pets")
    @ApiResponse(responseCode = "200", description = "Got all user's pets", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponseDto[].class)))
    public ResponseEntity<List<PetResponseDto>> getAllUserPets(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        var petResponseDtos = petService.getAllUserPets(jwt.getSubject(), sortBy, direction);
        return ResponseEntity.ok(petResponseDtos);
    }

    @GetMapping("/users/pets/{id}")
    @ApiResponse(responseCode = "200", description = "Got user's pet", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    public ResponseEntity<PetResponseDto> getUserPet(@AuthenticationPrincipal Jwt jwt, @PathVariable Integer id)
            throws ResourceNotFoundException {
        var petResponseDto = petService.getUserPet(id, jwt.getSubject());
        return ResponseEntity.ok(petResponseDto);
    }

    @PatchMapping("/users/pets/{id}/status")
    @ApiResponse(responseCode = "200", description = "Perform action with user's pet to modify their status", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    public ResponseEntity<PetResponseDto> patchUserPetStatus(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Integer id,
            @Valid @RequestBody PatchUserPetStatusRequestDto patchUserPetStatusRequestDto
    ) throws InvalidStatusActionException, UnsupportedStatusActionException, ResourceNotFoundException {
        var petResponseDto = petService.patchUserPetStatus(id, jwt.getSubject(), patchUserPetStatusRequestDto);
        return ResponseEntity.ok(petResponseDto);
    }

    @GetMapping("/pets")
    @ApiResponse(responseCode = "200", description = "Got all unadopted pets", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponseDto[].class)))
    public ResponseEntity<List<PetResponseDto>> getAllUnadoptedPets(
            @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        var petResponseDtos = petService.getAllUnadoptedPets(sortBy, direction);
        return ResponseEntity.ok(petResponseDtos);
    }

    @GetMapping("/pets/{id}")
    @ApiResponse(responseCode = "200", description = "Got unadopted pet", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    public ResponseEntity<PetResponseDto> getUnadoptedPet(@PathVariable Integer id) throws ResourceNotFoundException {
        var petResponseDto = petService.getUnadoptedPet(id);
        return ResponseEntity.ok(petResponseDto);
    }

    @PostMapping("/pets/{id}/adoptions")
    @ApiResponse(responseCode = "200", description = "Adopt a pet as the current user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    public ResponseEntity<PetResponseDto> adoptPet(@AuthenticationPrincipal Jwt jwt, @PathVariable Integer id)
            throws URISyntaxException, ResourceNotFoundException {
        var petResponseDto = petService.adoptPet(jwt.getSubject(), id);
        var location = new URI(String.format("/pets/%s/adoptions/%s", id, petResponseDto.adoption().getId()));
        return ResponseEntity.created(location).body(petResponseDto);
    }
}
