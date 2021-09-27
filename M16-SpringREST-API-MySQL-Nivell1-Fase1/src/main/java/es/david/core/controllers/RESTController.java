package es.david.core.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.david.core.model.entities.DiceThrow;
import es.david.core.model.entities.Player;
import es.david.core.model.entities.Ranking;
import es.david.core.model.service.IDiceThrowService;
import es.david.core.model.service.IPlayerService;

@RestController
@RequestMapping("/api/players")
public class RESTController {
	
	@Autowired
	private IPlayerService playerService;
	@Autowired
	private IDiceThrowService diceThrowService;
	
	@PostMapping("")
	public ResponseEntity<?> create(@RequestBody Player player){
		
		return ResponseEntity.status(HttpStatus.CREATED).body(playerService.save(player));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@RequestBody Player player, @PathVariable Long id){
		
		Optional<Player> oPlayer = playerService.findById(id);
		
		oPlayer.get().setPlayerName(player.getPlayerName());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(playerService.save(oPlayer.get()));
	}
	
	@PostMapping("/{id}/games/")
	public ResponseEntity<?> play(@PathVariable Long id){
		
		DiceThrow diceThrow = new DiceThrow();
		
		DiceThrow diceThrowAttached = diceThrowService.saveOnPlayer(diceThrow, id);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(diceThrowAttached);
	}
	
	@DeleteMapping("/{id}/games")
	public ResponseEntity<?> delete (@PathVariable Long id){
		
		diceThrowService.deleteThrowsFromPlayer(id);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/")
	public ResponseEntity<?> readAll(){
		
		List<Player> listOfPlayers = StreamSupport
				.stream(playerService.findAll().spliterator(),false)
				.collect(Collectors.toList());
	
		return ResponseEntity.ok(listOfPlayers);
		
	}
	
	@GetMapping("/{id}/games")
	public ResponseEntity<?> getThrowsByPlayer(@PathVariable Long id){
		
		List<DiceThrow> listOfDiceThrows = diceThrowService.findDiceThrowsByPlayer(id);
		
		return ResponseEntity.ok(listOfDiceThrows);
	}
	
	@GetMapping("/ranking")
	public ResponseEntity<?> getRanking(){
		
		Ranking ranking = new Ranking(playerService.getTotalAmountOfPlayers(), playerService.getGlobalAverageWinRate());
		
		
		return ResponseEntity.ok(ranking);
	}
	
	@GetMapping("/ranking/winner")
	public ResponseEntity<?> getWinner(){
		
		Player winner = playerService.findBestPlayer();
		
		return ResponseEntity.ok(winner);
	}
	
	@GetMapping("/ranking/loser")
	public ResponseEntity<?> getLoser(){
		
		Player loser = playerService.findWorstPlayer();
		
		return ResponseEntity.ok(loser);
	}
}
