package es.david.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

//import org.springframework.data.mongodb.core.mapping.DBRef;
//import io.github.kaiso.relmongo.annotation.CascadeType;
//import io.github.kaiso.relmongo.annotation.FetchType;
//import io.github.kaiso.relmongo.annotation.JoinProperty;
//import io.github.kaiso.relmongo.annotation.OneToMany;


@Document(collection="players")
public class Player implements Serializable{
	
	@Id
	private String id;
	
	@Field(name="name")
	@Indexed(unique = true)
	private String playerName;
	
	@Field(name="password")
	private String password;
	
	@Field(name="winrate")
	private Double playerWinrate;
	
	@Field(name="register_date")
	private Date registerDate;
	
	@Field(name="games")
	private Integer totalGames;
	
	//@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	//@JoinProperty(name="diceThrows")
	//@DBRef
	private List<DiceThrow> diceThrows = new ArrayList<>();

	public Player() {
		
	}
	
	public Player(String playerName, String password, Double playerWinrate, Date registerDate,
			Integer totalGames, List<DiceThrow> diceThrows) {
		
		this.playerName = playerName;
		this.password = password;
		this.playerWinrate = playerWinrate;
		this.registerDate = registerDate;
		this.totalGames = totalGames;
		this.diceThrows = diceThrows;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Integer getTotalGames() {
		return totalGames;
	}

	public void setTotalGames(Integer totalGames) {
		this.totalGames = totalGames;
	}

	public List<DiceThrow> getDiceThrows() {
		return diceThrows;
	}

	public void setDiceThrows(List<DiceThrow> diceThrows) {
		this.diceThrows = diceThrows;
	}
	
	public double calculateWinRate() {
		
		double wonGames = (double)diceThrows.stream()
				.filter(diceThrows -> diceThrows.getResult().equals("WIN"))
				.count();
		
		return (wonGames/diceThrows.size())*100;
	}
	
	public void addThrow(DiceThrow diceThrow) {
		this.diceThrows.add(diceThrow);
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", playerName=" + playerName + ", password=" + password + ", playerWinrate="
				+ playerWinrate + ", registerDate=" + registerDate + ", totalGames=" + totalGames + ", diceThrows="
				+ diceThrows + "]";
	}
}
