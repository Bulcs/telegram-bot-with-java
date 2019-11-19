package Controller;

import java.util.ArrayList;

import Exceptions.EmptyList;
import Exceptions.OffTheList;
import Model.Good;



public class Goods extends Controller{
	/** ArrayList creation
	 *  @param categories the arrayList that we gonna use on goods
	 */
	 ArrayList<Good> goods;
	 
	public Goods(){
		goods = new ArrayList<Good>();
	}
    /** Register a good.
     * @param nome The goods name.
     * @param descricao The goods description.
     * @param code The goods code
     * @param location The goods location
     * @param category the goods category
     */
    public void register(String nome, String descricao, String code, String location, String category){
        Good default_good = new Good(nome, descricao,code, location, category);
        goods.add(default_good);
    }
    
    
    /** List all GOODS running in the arraylist goods 
     * @return list of goods*/    
	@Override
    public ArrayList<Good> list(){
    	return goods;
    }
	/** This method search a good by name
	 * @param searchName String - user searched name
	 * @return true - if the good searched exist or return a Exception saying the good doesn't exist
	 * */
	@Override
	public boolean findByName(String searchName) throws OffTheList{
		for (Good good: goods) {
			if(good.getGoodsName().toLowerCase().equals(searchName.toLowerCase())) {
				return true;
			}
		} 
		throw new OffTheList("Não há nenhum bem cadastrado com esse nome.");
	}
	/** This method verify if the arrayList of goods is empty
	 * throw for an exception
	 * */
	@Override
	public void sizeOfList() throws EmptyList {
		if(goods.size() == 0) {
			throw new EmptyList("Não há nenhum bem cadastrado ainda!");
		}
	}
	
	/** List the goods by the location inserted
	 * 	@param locationSearch String - location inserted by the user
	 * 	@return goodsByLocation - the goods in the location inserted
	 * */
	
	public ArrayList<Good> listByLocation(String locationSearch){
		ArrayList<Good> goodsByLocation = new ArrayList<Good>();
		for (Good good: goods) {
			if(good.getGoodsLocation().toLowerCase().equals(locationSearch.toLowerCase())) {
				goodsByLocation.add(good);
			}
		}
		return goodsByLocation;
	}
	
	/** Find a good by an code inserted, if there's no good with the code inserted, throw to an Exception
	 * 	@param searchCode String - code inserted by the user
	 * 	@return good - the good with the location inserted
	 *  @throws OffTheList - there is no good with this code
	 * */
	
	public Good findByCode(String searchCode) throws OffTheList{
		for (Good good: goods) {
			if(good.getGoodsCode().toLowerCase().equals(searchCode.toLowerCase())) {
				return good;
			}
		} 
		throw new OffTheList("Não há nenhum bem cadastrado com esse código.");
	}
	/** Search a goods by the name inserted
	 * 	@param nameSearch String - name inserted by the user
	 * 	@return goodsByName - the goods with that name inserted
	 * 	@throws EmptyList - there is no good with this name
	 * */
	public ArrayList<Good> listByName(String nameSearch) throws EmptyList{
		ArrayList<Good> goodsByName = new ArrayList<Good>();
		for (Good good: goods) {
			if(good.getGoodsName().toLowerCase().equals(nameSearch.toLowerCase())) {
				goodsByName.add(good);
			}
		}
		
		if(goodsByName.size() == 0) {
			throw new EmptyList("Não há nenhum bem cadastrado com esse nome");
		}
		
		return goodsByName;
	}
	/** List the goods by the description inserted
	 * 	@param descriptionSearch String - description inserted by the user
	 * 	@return goodsByDescription - the goods in the description inserted
	 *  @throws EmptyList - there is no good with this description
	 * */
	public ArrayList<Good> listByDescription(String descriptionSearch) throws EmptyList{
		ArrayList<Good> goodsByDescription = new ArrayList<Good>();
		for (Good good: goods) {
			if(good.getGoodsDescription().toLowerCase().equals(descriptionSearch.toLowerCase())) {
				goodsByDescription.add(good);
			}
		}
		
		if(goodsByDescription.size() == 0) {
			throw new EmptyList("Não há nenhum bem cadastrado com essa descrição");
		}
		
		return goodsByDescription;
	}
	/** Delete the good by the code inserted
	 * @param code String - code inserted by the user
	 * */
	public void deleteByCode(String code) {
		for (Good good: goods) {
			if(good.getGoodsCode().toLowerCase().equals(code.toLowerCase())) {
				goods.remove(good);
			}
		} 
	}

}
