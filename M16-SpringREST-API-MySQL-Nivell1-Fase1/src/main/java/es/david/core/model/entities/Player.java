package es.david.core.model.entities;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="players")
public class Player {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="player_id")
	private Long idPlayer;
	
	@Column(name="name", length = 100, nullable = false)
	private String playerName;
	
	@Column(name="password")
	private String playerPassword;
	
	@Column(name="winrate")
	private Double playerWinrate;
	
	@Column(name="register_date", nullable= false)
	private Date registerDate;
	
	@OneToMany(mappedBy = "player")
	private List<DiceThrow> playerDiceThrows; //= new ArrayList<>();

	@Column(name="total_games")
	private Integer totalGames;
	
	
	public Long getIdPlayer() {
		return idPlayer;
	}

	public void setIdPlayer(Long idPlayer) {
		this.idPlayer = idPlayer;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerPassword() {
		return playerPassword;
	}

	public void setPlayerPassword(String playerPassword) {
		this.playerPassword = playerPassword;
	}

	public Double getPlayerWinrate() {
		return playerWinrate;
	}

	public void setPlayerWinrate(Double playerWinrate) {
		this.playerWinrate = playerWinrate;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	@JsonManagedReference
	public List<DiceThrow> getPlayerDiceThrows() {
		return playerDiceThrows;
	}

	public void setPlayerDiceThrows(List<DiceThrow> playerDiceThrows) {
		this.playerDiceThrows = playerDiceThrows;
	}
	
	public double calculateWinRate() {
		
		double wonGames = (double)playerDiceThrows.stream()
				.filter(diceThrows -> diceThrows.getResult().equals("WIN"))
				.count();
		
		return (wonGames/playerDiceThrows.size())*100;
	}
	
	public void addThrow(DiceThrow diceThrow) {
		playerDiceThrows.add(diceThrow);
	}
	

	public Integer getTotalGames() {
		return totalGames;
	}

	public void setTotalGames(Integer totalGames) {
		
		this.totalGames = totalGames;
	}

	@Override
	public String toString() {
		return "Player [idPlayer=" + idPlayer + ", playerName=" + playerName + ", playerPassword=" + playerPassword
				+ ", playerWinrate=" + playerWinrate + ", registerDate=" + registerDate + ", playerDiceThrows="
				+ playerDiceThrows + ", totalGames=" + totalGames + "]";
	}
}
