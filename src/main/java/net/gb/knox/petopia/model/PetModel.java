package net.gb.knox.petopia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import net.gb.knox.petopia.domain.Species;
import net.gb.knox.petopia.domain.Taxon;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "pet")
public class PetModel {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private Taxon taxon;

    @NotNull
    private Species species;

    @NotBlank
    private String name;

    @NotNull
    @PastOrPresent
    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private AdoptionModel adoption;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private StatusModel status;
}
