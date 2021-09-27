package es.david.core;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import es.david.core.model.DiceThrow;
import es.david.core.model.Player;
import es.david.core.repositories.PlayerRepository;

@SpringBootApplication
public class M16SpringRestApiNoSqlNivell1Fase2Application{

	public static void main(String[] args) {
		SpringApplication.run(M16SpringRestApiNoSqlNivell1Fase2Application.class, args);
	}

	@Bean
	CommandLineRunner runner(PlayerRepository repository) {
		
		return args ->{
			
			DiceThrow dt1 = new DiceThrow();
			dt1.setIdDiceThrow("614c393f7a46612ef3b9db11");
			
			DiceThrow dt2 = new DiceThrow();
			dt2.setIdDiceThrow("614c395c941f26e25c1bb128");
			
			DiceThrow dt3 = new DiceThrow();
			dt3.setIdDiceThrow("614c3961cd568193e77cf686");
			
			DiceThrow dt4 = new DiceThrow();
			dt4.setIdDiceThrow("614c3966001c53ad09a8296e");
			
			//List<DiceThrow> diceThrows = new ArrayList<>();
			//List<DiceThrow> diceThrows2 = new ArrayList<>();
			
//			diceThrows.add(dt1);
//			diceThrows.add(dt2);
//			
//			diceThrows2.add(dt3);
//			diceThrows2.add(dt4);
			
			String nombre = "bungo96";
			String nombre2 = "willywallace666";
			Player player = new Player(nombre, "4321", 0.0, Timestamp.valueOf(LocalDateTime.now()), 0,  new ArrayList<>());
			Player player2 = new Player(nombre2, "1234", 0.0, Timestamp.valueOf(LocalDateTime.now()), 0,  new ArrayList<>());
			
			player.addThrow(dt1);
			player.addThrow(dt2);
			
			player2.addThrow(dt3);
			player2.addThrow(dt4);
			
			player.setPlayerWinrate(player.calculateWinRate());
			player.setTotalGames(player.getDiceThrows().size());
			
			System.out.println("Starting player CommandLineRunner: " + player.toString());
			
			
			player2.setPlayerWinrate(player.calculateWinRate());
			player2.setTotalGames(player.getDiceThrows().size());
			
			System.out.println("Starting player CommandLineRunner: " + player2.toString());
			
			//repository.insert(player);
			
			repository.findByPlayerName(nombre).ifPresentOrElse(p -> {
				
				System.out.println(p + " already exists");
				
			}, () ->{
				System.out.println("Inserting player" + player);
				
				repository.insert(player);
			});
			
			repository.findByPlayerName(nombre2).ifPresentOrElse(p -> {
				
				System.out.println(p + " already exists");
				
			}, () ->{
				System.out.println("Inserting player" + player2);
				
				repository.insert(player2);
			});
		};
	}
}
