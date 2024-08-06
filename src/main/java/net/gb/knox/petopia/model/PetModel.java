package net.gb.knox.petopia.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.gb.knox.petopia.domain.Species;
import net.gb.knox.petopia.domain.Taxon;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    @JsonManagedReference
    private AdoptionModel adoption;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    @JsonManagedReference
    private StatusModel status;
}
