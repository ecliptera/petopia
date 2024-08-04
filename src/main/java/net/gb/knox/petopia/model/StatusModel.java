package net.gb.knox.petopia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "status")
public class StatusModel {
    @Id
    @GeneratedValue
    private Integer id;

    @Size(max = 100)
    private int happiness = 100;

    @Size(max = 100)
    private int cleanliness = 100;

    @Size(max = 100)
    private int hunger = 100;

    @Size(max = 100)
    private int tiredness = 100;
}
