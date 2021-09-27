package es.david.core.model.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.david.core.model.entities.DiceThrow;

@Repository
public interface DiceThrowRepository extends JpaRepository<DiceThrow, Long>{

}
