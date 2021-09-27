package es.david.core.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.david.core.exceptions.APIException;
import es.david.core.exceptions.PlayerNotFoundException;
import es.david.core.model.DiceThrow;
import es.david.core.model.Player;
import es.david.core.repositories.PlayerRepository;

@Service
public class PlayerService implements UserDetailsService{
	
	//The player passwords are encoded once the player is created or updated
	
	private final PlayerRepository playerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public PlayerService(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	public void createOrUpdatePlayer(Player player) {
		
		System.out.println("Creating or updaring player with ID: " + player.getId());
		
		if(player.getId() == null) {
			
			System.out.println("CREATING NEW PLAYER");
			
			if(player.getPlayerName()==null || player.getPlayerName().equals("")) //if empty -> annonymous
				player.setPlayerName("Anonymous");
			
			else if(playerRepository.findByPlayerName(player.getPlayerName()).isPresent()) throw new APIException("There is an existing player with that name.");
			
			if(player.getPlayerWinrate() == null) player.setPlayerWinrate(0.0);
			
			player.setPassword(passwordEncoder.encode(player.getPassword()));
			
			player.setRegisterDate(Timestamp.valueOf(LocalDateTime.now()));
			
			Integer totalGames = 
					(player.getDiceThrows()==null)? 0 : player.getDiceThrows().size();
			
			player.setTotalGames(totalGames);
			
			if(player.getDiceThrows() == null) player.setDiceThrows(new ArrayList<DiceThrow>());
			
			playerRepository.insert(player);
			
		}else {
			
			System.out.println("UPDATING A PLAYER");
			
			player.setPassword(passwordEncoder.encode(player.getPassword()));
			
			playerRepository.save(player);
		}
	}
		
	
	public void updatePlayer(Player player) {
		
		Player savedPlayer = playerRepository.findById(player.getId()).orElseThrow(()-> new RuntimeException(
				String.format("Cannot Find Player by ID %s", player.getId())));
		
		savedPlayer.setPlayerName(player.getPlayerName());
		savedPlayer.setPassword(passwordEncoder.encode(player.getPassword()));
		savedPlayer.setPlayerWinrate(player.getPlayerWinrate());
		savedPlayer.setRegisterDate(player.getRegisterDate());
		savedPlayer.setTotalGames(player.getTotalGames());
		
		playerRepository.save(savedPlayer);
	}
	
	public List<Player> getAllPlayers() {
		
		return playerRepository.findAll();
	}
	
	public Player getPlayerById(String id) {
		
		Optional<Player> player = playerRepository.findById(id);
		
		if(!player.isPresent()) throw new PlayerNotFoundException(id);
		
		return player.get();
	}
	
	public List<DiceThrow> diceThrowsByPlayer(String id){
		
		List<DiceThrow> throwsByPlayer = playerRepository.findById(id).get().getDiceThrows();
		
		for(DiceThrow dt: throwsByPlayer) System.out.println(dt.toString());
		
		return throwsByPlayer;
		
	}
	
	public Integer getTotalAmountOfPlayers() {
		
		return Integer.valueOf((int)(StreamSupport
				.stream(playerRepository.findAll().spliterator(),false)
				.collect(Collectors.toList()).stream().count()));
		
	}
	
	public Double getGlobalAverageWinRate() {
		
		return playerRepository.findAll().stream()
				.mapToDouble(player->player.getPlayerWinrate()).average().getAsDouble();
	}
	
	public Player findWorstPlayer() {
		
		return playerRepository.findAll().stream()
				.sorted(Comparator.comparingDouble(Player::getPlayerWinrate))
				.findFirst().get();
	}

	public Player findBestPlayer() {
		
		return playerRepository.findAll().stream()
				.sorted(Comparator.comparingDouble(Player::getPlayerWinrate).reversed())
				.findFirst().get();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		
		List <SimpleGrantedAuthority> roles = null;
		
		if (username.equals("admin")) {			
			roles = Arrays.asList(new SimpleGrantedAuthority ("ROLE_ADMIN"));
			return new User("admin", passwordEncoder.encode("admin"),roles);
		}
		
		if (username.equals("user")) {			
			roles = Arrays.asList(new SimpleGrantedAuthority ("ROLE_USER"));
			return new User("user",passwordEncoder.encode("user"),roles);
		}
		
		if (username.equals("anonymous")) {			
			roles = Arrays.asList(new SimpleGrantedAuthority ("ROLE_ANONYMOUS"));
			return new User("anonymous",passwordEncoder.encode("anonymous"),roles);
		}
		
		throw new UsernameNotFoundException("User not found with the name "+ username);
		
	}
	
}
