package es.david.core.model.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.david.core.exceptions.DiceThrowNotFoundException;
import es.david.core.exceptions.PlayerNotFoundException;
import es.david.core.model.entities.DiceThrow;
import es.david.core.model.entities.Player;
import es.david.core.model.repository.DiceThrowRepository;
import es.david.core.model.repository.PlayerRepository;

@Service
public class DiceThrowServiceImpl implements IDiceThrowService {

	@Autowired
	DiceThrowRepository diceThrowRepository;
	@Autowired
	PlayerRepository playerRepository;
	
	@Override
	public Iterable<DiceThrow> findAll() {
		return diceThrowRepository.findAll();
	}

	@Override
	public Optional<DiceThrow> findById(Long id) {
		
		if(!diceThrowRepository.findById(id).isPresent()) throw new DiceThrowNotFoundException(id);
		
		return diceThrowRepository.findById(id);
	}

	@Override
	public DiceThrow save(DiceThrow diceThrow) {
		return diceThrowRepository.save(diceThrow);
	}

	@Override
	public DiceThrow saveOnPlayer(DiceThrow diceThrow, Long idPlayer) {
		
		Player player = playerRepository.findById(idPlayer).orElseThrow(()->new PlayerNotFoundException(idPlayer));
		
		diceThrow.setPlayer(player);
		player.addThrow(diceThrow);
		player.setPlayerWinrate(player.calculateWinRate());
		player.setTotalGames(player.getPlayerDiceThrows().size());
		
		return diceThrowRepository.save(diceThrow);
	}

	@Override
	public List<DiceThrow> findByPlayerId(Long idTienda) {
		return null;
	}

	@Override
	public void deleteById(Long id) {
		
		if(!diceThrowRepository.findById(id).isPresent()) throw new DiceThrowNotFoundException(id);
		
		diceThrowRepository.deleteById(id);
	}

	@Override
	public List<DiceThrow> saveAll(List<DiceThrow> diceThrows) {
		return diceThrowRepository.saveAll(diceThrows);
	}

	@Override
	public void deleteAll() {
		diceThrowRepository.deleteAll();
	}
	
	@Override
	public void deleteThrowsFromPlayer(Long id) {
		
		Player player = playerRepository.findById(id).get();
		
		for(DiceThrow diceThrow : player.getPlayerDiceThrows()) {
			diceThrowRepository.deleteById(diceThrow.getIdDiceThrow());
		}
		
	}

	@Override
	public List<DiceThrow> findDiceThrowsByPlayer(Long id) {
		
		return diceThrowRepository.findAll().stream()
				.filter(diceThrow->diceThrow.getPlayer().getIdPlayer()==id)
				.collect(Collectors.toList());
	}

}
