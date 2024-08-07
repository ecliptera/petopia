package net.gb.knox.petopia.domain;

import jakarta.validation.constraints.NotNull;

public record PatchUserPetStatusRequestDto(@NotNull StatusAction action) {
}
