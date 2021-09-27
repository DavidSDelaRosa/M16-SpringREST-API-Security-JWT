package es.david.core.controllers;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.david.core.model.DiceThrow;
import es.david.core.model.Player;
import es.david.core.model.Ranking;
import es.david.core.services.DiceThrowService;
import es.david.core.services.PlayerService;


@RestController
@RequestMapping("/api/players")
public class RESTController {
	
	private final PlayerService playerService;
	private final DiceThrowService diceThrowService;
	
	public RESTController(PlayerService playerService, DiceThrowService diceThrowService) {
		this.playerService = playerService;
		this.diceThrowService = diceThrowService;
	}
	
	//CREATES A PLAYER
	@PostMapping("")
	public ResponseEntity<?> createPlayer(@RequestBody Player player){
		
		playerService.createOrUpdatePlayer(player);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	//UPDATES A PLAYER'S NAME
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@RequestBody Player player, @PathVariable String id){
		
		Player playerById = playerService.getPlayerById(id);
		
		playerById.setPlayerName(player.getPlayerName());
		
		playerService.createOrUpdatePlayer(playerById);
		
		System.out.println("Player updated");
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	//A SPECIFIC PLAYER THROWS THE DICE
	@PostMapping("/{id}/games/")
	public ResponseEntity<?> play(@PathVariable String id){
		
		DiceThrow diceThrow = new DiceThrow();
		
		DiceThrow diceThrowAttached = diceThrowService.saveOnPlayer(diceThrow, id);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(diceThrowAttached);
	}
	
	//DELETES ALL THE DICE THROWS FROM A PLAYER
	@DeleteMapping("/delete/{id}/games")
	public ResponseEntity<?> delete (@PathVariable String id){
		
		System.out.println("REST-CONTROLLER: Deleting throws...");
		
		diceThrowService.deleteThrowsFromPlayer(id);
		
		return ResponseEntity.ok().build();
	}
	
	//GETS A LIST OF ALL PLAYERS REGISTERED
	@GetMapping("/")
	public List<Player> getAllPlayers(){
	
		return playerService.getAllPlayers();
		
	}
	
	//GETS ALL THE THROWS FROM A PLAYER
	@GetMapping("/{id}/games")
	public ResponseEntity<?> getThrowsByPlayer(@PathVariable String id){
		
		List<DiceThrow> listOfDiceThrows = playerService.diceThrowsByPlayer(id);
		
		return ResponseEntity.ok(listOfDiceThrows);
	}
	
	//GETS A PLAYER BY HIS ID
	@GetMapping("/{id}")
	public ResponseEntity<?> getPlayerById(@PathVariable String id){
		
		Player player = playerService.getPlayerById(id);
		
		return ResponseEntity.ok(player);
	}
	
	//GETS THE RANKING WITH THE GLOBAL AVERAGE OF WINRATE
	@GetMapping("/ranking")
	public ResponseEntity<?> getRanking(){
		
		Ranking ranking = new Ranking(playerService.getTotalAmountOfPlayers(), playerService.getGlobalAverageWinRate());
		
		
		return ResponseEntity.ok(ranking);
	}
	
	//GETS THE WINNER
	@GetMapping("/ranking/winner")
	public ResponseEntity<?> getWinner(){
		
		Player winner = playerService.findBestPlayer();
		
		return ResponseEntity.ok(winner);
	}
	
	//GETS THE WORST PLAYER
	@GetMapping("/ranking/loser")
	public ResponseEntity<?> getLoser(){
		
		Player loser = playerService.findWorstPlayer();
		
		return ResponseEntity.ok(loser);
	}
	
	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
			
			try {
				
				String refreshToken = authorizationHeader.substring("Bearer ".length()); //removing the word 'bearer'
				
				System.out.println("FILTERING");
				System.out.println(refreshToken);
				
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				
				JWTVerifier verifier = JWT.require(algorithm).build();
				
				DecodedJWT decodedJWT = verifier.verify(refreshToken);
				
				String username = decodedJWT.getSubject();
				
				User user = (User)playerService.loadUserByUsername(username);
				
				String accessToken = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis()+ 10 * 60 * 1000))
						.withIssuer(request.getRequestURI().toString())
						.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
						.sign(algorithm);
				
				Map<String,String> tokens = new HashMap<>();
				
				tokens.put("accessToken", accessToken);
				
			}catch(Exception exception) {
				
				System.out.println("Error login in: " + exception.getMessage());
				response.setHeader("error", exception.getMessage());
				response.setStatus(HttpStatus.FORBIDDEN.value());
				
				Map<String,String> error = new HashMap<>();
				
				error.put("error_message", exception.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
			
		}else {
			
			throw new RuntimeException("Refresh token is missing");
			
		}
		
	}
}
