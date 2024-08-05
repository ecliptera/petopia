package net.gb.knox.petopia.repository;

import net.gb.knox.petopia.model.PetModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<PetModel, Integer> {
}
