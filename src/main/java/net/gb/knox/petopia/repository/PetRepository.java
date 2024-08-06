package net.gb.knox.petopia.repository;

import net.gb.knox.petopia.model.PetModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<PetModel, Integer> {
    List<PetModel> findAllByAdoptionIdNull();

    List<PetModel> findAllByAdoptionIdNull(Sort sort);

    PetModel findByIdAndAdoptionIdNull(Integer id);
}
