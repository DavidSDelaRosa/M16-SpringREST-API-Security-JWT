package es.david.core.model.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.david.core.exceptions.APIException;
import es.david.core.exceptions.PlayerNotFoundException;
import es.david.core.model.entities.Player;
import es.david.core.model.repository.PlayerRepository;

@Service
public class PlayerServiceImpl implements IPlayerService {

	@Autowired
	PlayerRepository playerRepository;
	
	
	@Override
	public Iterable<Player> findAll() {
		return playerRepository.findAll();
	}

	@Override
	public Optional<Player> findById(Long id) {
		
		if(!playerRepository.findById(id).isPresent()) throw new PlayerNotFoundException(id);
		
		return playerRepository.findById(id);
	}

	@Override
	public Player save(Player player) {
		
		if(player.getPlayerName()==null || player.getPlayerName().equals("")) 
			player.setPlayerName("Anonymous");
		
		else if(playerRepository.findByPlayerName(player.getPlayerName()).isPresent()) throw new APIException("There is an existing player with that name.");
		
		player.setRegisterDate(Timestamp.valueOf(LocalDateTime.now()));
		
		Integer totalGames = 
				(player.getPlayerDiceThrows()==null)? 0 : player.getPlayerDiceThrows().size();
		
		player.setTotalGames(totalGames);
		
		if(player.getPlayerWinrate() == null) player.setPlayerWinrate(0.0);
		
		return playerRepository.save(player);
	}

	@Override
	public void deleteById(Long id) {
		
		if(!playerRepository.findById(id).isPresent()) throw new PlayerNotFoundException(id);
		
		playerRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		playerRepository.deleteAll();
	}

	@Override
	public List<Player> saveAll(List<Player> players) {
		return playerRepository.saveAll(players);
	}

	@Override
	public Double getGlobalAverageWinRate() {
		
		return playerRepository.findAll().stream()
				.mapToDouble(player->player.getPlayerWinrate()).average().getAsDouble();
	}

	@Override
	public Integer getTotalAmountOfPlayers() {
		
		return Integer.valueOf((int)(StreamSupport
				.stream(playerRepository.findAll().spliterator(),false)
				.collect(Collectors.toList()).stream().count()));
		
	}

	@Override
	public Player findWorstPlayer() {
		
		return playerRepository.findAll().stream()
				.sorted(Comparator.comparingDouble(Player::getPlayerWinrate))
				.findFirst().get();
	}

	@Override
	public Player findBestPlayer() {
		
		return playerRepository.findAll().stream()
				.sorted(Comparator.comparingDouble(Player::getPlayerWinrate).reversed())
				.findFirst().get();
	}

	@Override
	public Player getPlayerByPlayerName(String playerName) {
		Player player = playerRepository.findByPlayerName(playerName).orElseThrow(() -> new PlayerNotFoundException("Maybe player with name: " + playerName + " is not registered"));
		
		return player;
	}

}
