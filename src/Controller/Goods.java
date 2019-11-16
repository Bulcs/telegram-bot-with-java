package Controller;

import java.util.ArrayList;

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

}
