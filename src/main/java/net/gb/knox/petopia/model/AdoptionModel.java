package net.gb.knox.petopia.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private String adopterId;

    @NotNull
    @PastOrPresent
    private LocalDateTime adoptionDateTime;

    @OneToOne(mappedBy = "adoption")
    @JsonBackReference
    @ToString.Exclude
    private PetModel pet;
}
