package Controller;

import java.util.ArrayList;

import Exceptions.EmptyList;
import Exceptions.OffTheList;
import Model.Good;



public class Goods extends Controller{
	 ArrayList<Good> goods;
	 
	public Goods(){
		goods = new ArrayList<Good>();
	}
    /** Register a good.
     * @param nome The goods  name.
     * @param descricao The goods description.
     * @param code The goods code
     * Precisa colocar a localização e a categoria junto do Bem (Goods.java)
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
	
	@Override
	public boolean findByName(String searchName) throws OffTheList{
		for (Good good: goods) {
			if(good.getGoodsName().toLowerCase().equals(searchName.toLowerCase())) {
				return true;
			}
		} 
		throw new OffTheList("Não há nenhum bem cadastrado com esse nome.");
	}
	
	@Override
	public void sizeOfList() throws EmptyList {
		if(goods.size() == 0) {
			throw new EmptyList("Não há nenhum bem cadastrado ainda!");
		}
	}
	
	public ArrayList<Good> listByLocation(String locationSearch){
		ArrayList<Good> goodsByLocation = new ArrayList<Good>();
		for (Good good: goods) {
			if(good.getGoodsLocation().toLowerCase().equals(locationSearch.toLowerCase())) {
				goodsByLocation.add(good);
			}
		}
		return goodsByLocation;
	}
	
	public Good findByCode(String searchCode) throws OffTheList{
		for (Good good: goods) {
			if(good.getGoodsCode().toLowerCase().equals(searchCode.toLowerCase())) {
				return good;
			}
		} 
		throw new OffTheList("Não há nenhum bem cadastrado com esse código.");
	}
	
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


}
