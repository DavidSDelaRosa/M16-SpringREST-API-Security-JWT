package es.david.core.model.entities;

public class Ranking {
	
	private Integer amountOfPlayers;
	
	private Double averageWinrate;
	
	public Ranking() {
		
	}
	
	public Ranking( Integer amountOfPlayers, Double averageWinrate) {
		
		this.amountOfPlayers = amountOfPlayers;
		this.averageWinrate = averageWinrate;
	}

	public Integer getAmountOfPlayers() {
		return amountOfPlayers;
	}

	public void setAmountOfPlayers(Integer amountOfPlayers) {
		this.amountOfPlayers = amountOfPlayers;
	}

	public Double getAverageWinrate() {
		return averageWinrate;
	}

	public void setAverageWinrate(Double averageWinrate) {
		this.averageWinrate = averageWinrate;
	}

	@Override
	public String toString() {
		return "Ranking [amountOfPlayers=" + amountOfPlayers + ", averageWinrate=" + averageWinrate + "]";
	}
	
}
