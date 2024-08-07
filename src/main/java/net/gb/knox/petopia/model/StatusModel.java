package net.gb.knox.petopia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "status")
public class StatusModel {
    @Id
    @GeneratedValue
    private Integer id;

    @Min(0)
    @Max(100)
    private int happiness = 100;

    @Min(0)
    @Max(100)
    private int cleanliness = 100;

    @Min(0)
    @Max(100)
    private int hunger = 0;

    @Min(0)
    @Max(100)
    private int tiredness = 0;
}
