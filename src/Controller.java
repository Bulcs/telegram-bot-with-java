import java.util.ArrayList;

import com.pengrad.telegrambot.response.GetUpdatesResponse;

public class Controller {
    //arrays para location, category e goods
    ArrayList<Location> locations;
    ArrayList<Category> categories;
    ArrayList<Goods> goods;
    public Controller(){
        locations = new ArrayList();
        categories = new ArrayList();
        goods = new ArrayList();
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

    /** Register a good.
     * @param nome The goods  name.
     * @param descricao The goods description.
     * @param code The goods code
     * Precisa colocar a localização e a categoria junto do Bem (Goods.java)
     */
    public void registerGood(String nome, String descricao, String code){
        Goods default_good = new Goods(nome, descricao,code);
        goods.add(default_good);
    }

    /** Lists all LOCATIONS running through arraylist locations */
    public ArrayList<Location> listLocations(){
    	return locations;
    }

    /** Lists all CATEGORIES running through arraylist categories */
    public void listCategories(){
        for(Category category : categories){
            System.out.println(category.getCategoryName() + " - " +
                    category.getCategoryDescription() + " - " +
                    category.getCategoryCode() + "\n");
        }
    }

    /** List all GOODS running in the arraylist goods 
     * @return */
    public ArrayList<Goods> listGoods(){
    	return goods;
    }
}
