package es.david.core.exceptions;

public class DiceThrowNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = -6734632083182210179L;

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
