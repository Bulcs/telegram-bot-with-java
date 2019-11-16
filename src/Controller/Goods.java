package Controller;

import java.util.ArrayList;

import Exceptions.EmptyList;
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
	public boolean findByName(String searchName){
		for (Good good: goods) {
			if(good.getGoodsName().equals(searchName)) {
				return true;
			}
		} return false;
	}
	
	@Override
	public void sizeOfList() throws EmptyList {
		if(goods.size() == 0) {
			throw new EmptyList("Não há nenhum bem cadastrado ainda!");
		}
	}

}
