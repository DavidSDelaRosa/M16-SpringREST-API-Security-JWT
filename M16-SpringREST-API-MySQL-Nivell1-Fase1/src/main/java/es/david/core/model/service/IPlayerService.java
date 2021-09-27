package es.david.core.model.service;

import java.util.List;
import java.util.Optional;

import es.david.core.model.entities.Player;

public interface IPlayerService {
	
	public Iterable <Player> findAll();
	
	public Optional<Player> findById(Long id);
	
	public Player save(Player player);
	
	public void deleteById(Long id);
	
	public void deleteAll();
	
	public List<Player> saveAll(List<Player> players);
	
	public Double getGlobalAverageWinRate();
	
	public Integer getTotalAmountOfPlayers();

	public Player findWorstPlayer();
	
	public Player findBestPlayer();
	
	public Player getPlayerByPlayerName(String playerName);
}
