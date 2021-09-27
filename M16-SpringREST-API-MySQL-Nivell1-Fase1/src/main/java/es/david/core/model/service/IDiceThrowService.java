package es.david.core.model.service;

import java.util.List;
import java.util.Optional;

import es.david.core.model.entities.DiceThrow;

public interface IDiceThrowService {
	
	public Iterable <DiceThrow> findAll();
	
	public Optional<DiceThrow> findById(Long id);
	
	public DiceThrow save(DiceThrow diceThrow);
	
	public DiceThrow saveOnPlayer(DiceThrow diceThrow, Long idPlayer);
	
	public List<DiceThrow> findByPlayerId(Long idPlayer);
	
	public void deleteById(Long id);
	
	public void deleteAll();
	
	public List<DiceThrow> findDiceThrowsByPlayer(Long id);
	
	public void deleteThrowsFromPlayer(Long id);

	public List<DiceThrow> saveAll(List<DiceThrow> diceThrows);

}
