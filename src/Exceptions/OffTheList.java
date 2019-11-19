package Exceptions;

public class OffTheList extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Method return a message 
	 * @param message String*/
	public OffTheList(String message) {
		super(message);
	}

}
