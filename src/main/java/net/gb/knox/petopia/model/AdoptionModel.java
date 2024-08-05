package net.gb.knox.petopia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "adoption")
public class AdoptionModel {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @PastOrPresent
    private LocalDateTime adoptionDateTime;

    @OneToOne(mappedBy = "adoption")
    private PetModel pet;
}
