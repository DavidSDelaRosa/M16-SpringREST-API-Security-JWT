package es.david.core.exceptions;

public class PlayerNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -6950662642836083676L;

	public PlayerNotFoundException(Long id) {
		super("Could not find the player with id: " + id);
	}

	public PlayerNotFoundException() {
		super("Could not find the player");
	}
	
	public PlayerNotFoundException(String msg) {
		super("Could not find the player. Cause: " + msg);
	}

}
