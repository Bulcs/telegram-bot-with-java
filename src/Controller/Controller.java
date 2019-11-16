package Controller;

import java.util.ArrayList;

import Exceptions.EmptyList;
import Exceptions.OffTheList;


public abstract class Controller {
	
	//public abstract void registerLocation(String atributeOne, String atributeTwo);
	
	public abstract ArrayList<?> list();
	
	public abstract boolean findByName(String searchName) throws OffTheList;
	
	public abstract void sizeOfList() throws EmptyList;


	
}
