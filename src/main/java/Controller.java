import java.util.ArrayList;

public class Controller {
    //arrays para location, category e goods
    ArrayList<Location> locations;
    ArrayList<Category> categories;

    public Controller(){
        locations = new ArrayList();
        categories = new ArrayList();
    }

    /** Register a location.
     * @param locationName The location's  name.
     * @param locationDescription The location’s description.
     *
     *  Esse cadastro foi feito com passagem de parâmetro porque a gente fica recebendo
     *                            as váriaveis no main, pelo bot, e chama a função já
     *                            passando tudo o que precisa.
     *
     *  Tanto nesse como no próximo precisa ter algo pra verificar que só existe um de cada
     *                            (teve algo em uma das ultimas aulas dele sobre um jeito padrão de fazer isso)
     */
    public void registerLocation(String locationName, String locationDescription){
        Location default_location = new Location(locationName, locationDescription);
        locations.add(default_location);
    }

    /** Register a category.
     * @param categoryName The category's  name.
     * @param categoryDescription The category’s description.
     * @param categoryCode The category's code
     */
    public void registerCategory(String categoryName, String categoryDescription, String categoryCode){
        Category default_category = new Category(categoryName, categoryDescription, categoryCode);
        categories.add(default_category);
    }

    /** Lists all locations running through arraylist locations */
    public void listLocations(){
        for(Location local : locations){
            System.out.println(local.getLocationName() + " - " + local.getLocationDescription() + "\n");
        }
    }

    /** Lists all categories running through arraylist categories */
    public void listCategories(){
        for(Category category : categories){
            System.out.println(category.getCategoryName() + " - " +
                    category.getCategoryDescription() + " - " +
                    category.getCategoryCode() + "\n");
        }
    }

}
