package Controller;

import java.util.ArrayList;

import Exceptions.EmptyList;
import Exceptions.OffTheList;
import Model.Category;

public class Categories extends Controller {
	/** ArrayList creation
	 *  @param categories the arrayList that we gonna use on categories
	 */
	ArrayList<Category> categories;
	
	public Categories() {
		categories = new ArrayList<Category>();
	}
	
    /** Register a category.
     * @param categoryName The category's  name.
     * @param categoryDescription The category's description.
     * @param categoryCode The category's code
     */
    public void register(String categoryName, String categoryDescription, String categoryCode){
        Category default_category = new Category(categoryName, categoryDescription, categoryCode);
        categories.add(default_category);
    }
	
    /** Lists all CATEGORIES running through arraylist categories */
    
    /** list the categories method
     * @return all the categories added 
     * */
	@Override
    public ArrayList<Category> list(){
    	return categories;
    }
	/** This method search a category by name
	 * @param searchName String - user searched name
	 * @return true - if the category searched exist or return a warning saying the category don't exist
	 * */
	@Override
	public boolean findByName(String searchName) throws OffTheList{
		for (Category category: categories) {
			if(category.getCategoryName().toLowerCase().equals(searchName.toLowerCase())) {
				return true;
			}
		}
		throw new OffTheList("A categoria que você buscou não existe, tente novamente.");
	}
	/** This method verify if the arrayList of category is empty
	 * throw for an exception
	 * */
	@Override
	public void sizeOfList() throws EmptyList {
		if(categories.size() == 0) {
			throw new EmptyList("Não há nenhuma categoria cadastrada ainda!");
		}
	}
}
