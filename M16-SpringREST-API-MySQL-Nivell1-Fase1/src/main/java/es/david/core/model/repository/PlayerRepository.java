package es.david.core.model.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.david.core.model.entities.Player;

public interface PlayerRepository extends JpaRepository<Player, Long>{
	
	public Optional<Player> findByPlayerName(String playerName);

}
