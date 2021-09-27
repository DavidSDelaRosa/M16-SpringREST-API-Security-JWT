package es.david.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.*;

import java.io.Serializable;
import java.util.*;

@Document(collection="dice_throws")
public class DiceThrow implements Serializable{
	
	@Id
	private String idDiceThrow;
	
	private Integer firstDie;
	
	private Integer secondDie;
	
	private String result;
	
	//private Player player;
	
	
	//Constructor that initialice the two dices and calcules the result
	public DiceThrow() {
		
		Random diceRandomness = new Random();
		
		this.firstDie = diceRandomness.nextInt(6) + 1; //as it always goes from 0 to the specific number, so I have to add +1
		this.secondDie = diceRandomness.nextInt(6) + 1;
		this.result = calculateResult(firstDie, secondDie);
	}
	
	public String calculateResult(Integer firstDice, Integer secondDice) {
		
		String resultado = ((firstDice + secondDice) == 7)? "WIN" : "LOSE";
		
		return resultado;
	}

	public String getIdDiceThrow() {
		return idDiceThrow;
	}

	public void setIdDiceThrow(String idDiceThrow) {
		this.idDiceThrow = idDiceThrow;
	}

	public Integer getFirstDie() {
		return firstDie;
	}

	public void setFirstDie(Integer firstDice) {
		this.firstDie = firstDice;
	}

	public Integer getSecondDie() {
		return secondDie;
	}

	public void setSecondDie(Integer secondDice) {
		this.secondDie = secondDice;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	/*@JsonBackReference
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}*/

	@Override
	public String toString() {
		return "DiceThrow [idDiceThrow=" + idDiceThrow + ", firstDie=" + firstDie + ", secondDie=" + secondDie
				+ ", result=" + result + "]";
	}
}
