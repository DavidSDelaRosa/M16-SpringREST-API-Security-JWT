package es.david.core.services;

import java.util.List;

import org.springframework.stereotype.Service;

import es.david.core.exceptions.PlayerNotFoundException;
import es.david.core.model.DiceThrow;
import es.david.core.model.Player;
import es.david.core.repositories.DiceThrowRepository;
import es.david.core.repositories.PlayerRepository;

@Service
public class DiceThrowService {

	private final DiceThrowRepository diceRepository;
	private final PlayerRepository  playerRepository;
	
	public DiceThrowService(DiceThrowRepository repository, PlayerRepository playerRepository) {
		this.diceRepository = repository;
		this.playerRepository = playerRepository;
	}

	public DiceThrow saveOnPlayer(DiceThrow diceThrow, String id) {
		
		Player player = playerRepository.findById(id).orElseThrow(()->new PlayerNotFoundException(id));
		
		player.addThrow(diceThrow);
		
		player.setPlayerWinrate(player.calculateWinRate());
		player.setTotalGames(player.getDiceThrows().size());
		
		playerRepository.save(player);
		
		return diceRepository.save(diceThrow);
	}
	
	public void deleteThrowsFromPlayer(String id) {
		
		Player player = playerRepository.findById(id).get();
		
		List<DiceThrow> diceThrowsFromPlayer = player.getDiceThrows();
		
		
		diceThrowsFromPlayer.forEach(System.out::println);
		
		diceRepository.deleteAll(diceThrowsFromPlayer);
		
	}
}
