package Controller;

import java.util.ArrayList;

import Exceptions.EmptyList;
import Exceptions.OffTheList;


public abstract class Controller {
	
	//public abstract void registerLocation(String atributeOne, String atributeTwo);
	/** Abstract ArrayList method 
	 * 	@return ArrayList
	 * */
	public abstract ArrayList<?> list();
	/** Abstract findByName method
	 *  @param searchName String
	 *  @return boolean
	 *  @throws OffTheList - warning message
	 *  */
	public abstract boolean findByName(String searchName) throws OffTheList;
	/** Abstract sizeOfList method
	 * @throws EmptyList - warning message */
	public abstract void sizeOfList() throws EmptyList;


	
}
