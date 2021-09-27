package es.david.core.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import es.david.core.model.Player;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String>{
	
	Optional<Player> findByPlayerName(String playerName);

}
