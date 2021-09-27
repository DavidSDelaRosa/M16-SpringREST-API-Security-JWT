package es.david.core.exceptions;

public class DiceThrowNotFoundException extends RuntimeException{
	
	public DiceThrowNotFoundException(Long id) {
		super("Could not find the dice throw with id: " + id);
	}

	public DiceThrowNotFoundException() {
		super("Could not find the dice throw");
	}
	
	public DiceThrowNotFoundException(String msg) {
		super("Could not find the dice throw. Cause: " + msg);
	}

}
