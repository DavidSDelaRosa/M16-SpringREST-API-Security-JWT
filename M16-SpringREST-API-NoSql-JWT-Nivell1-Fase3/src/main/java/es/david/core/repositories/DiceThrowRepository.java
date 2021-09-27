package es.david.core.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import es.david.core.model.DiceThrow;

@Repository
public interface DiceThrowRepository extends MongoRepository<DiceThrow, String>{

}
