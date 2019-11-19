package Exceptions;

public class EmptyList extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** method return a message 
	 * @param message String
	 */
	public EmptyList(String message) {
		super(message);
	}

}
