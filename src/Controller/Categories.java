package Controller;

import java.util.ArrayList;
import Model.Category;


public class Categories extends Controller {

	ArrayList<Category> categories;
	
	public Categories() {
		categories = new ArrayList<Category>();
	}
	
    /** Register a category.
     * @param categoryName The category's  name.
     * @param categoryDescription The category’s description.
     * @param categoryCode The category's code
     */
    public void register(String categoryName, String categoryDescription, String categoryCode){
        Category default_category = new Category(categoryName, categoryDescription, categoryCode);
        categories.add(default_category);
    }
	
    /** Lists all CATEGORIES running through arraylist categories */
	@Override
    public ArrayList<Category> list(){
    	return categories;
    }
}
