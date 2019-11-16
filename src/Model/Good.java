package Model;
public class Good {
    private String goodsName;
    private String goodsDescription;
    private String goodsCode;
    private String goodsLocation;
    private String goodsCategory;
    

    /** Creates a good.
     * @param goodsName The good's  name.
     * @param goodsDescription The good’s description.
     * @param goodsCode The good's code
     *
     * Não fiz a parte de localização e categoria porque vai ter que fazer um rolê de exceção
     *                  (pra ver se as coisas que a pessoa ta passando existem) e porque tem que ver
     *                  como vai fazer pra buscar isso direitinho
     */
    public Good(String goodsName, String goodsDescription, String goodsCode, 
    		String goodsLocation, String goodsCategory){
        this.goodsName = goodsName;
        this.goodsDescription = goodsDescription;
        this.goodsCode = goodsCode;
        this.goodsLocation = goodsLocation;
        this.goodsCategory = goodsCategory;
    }

    public String getGoodsName(){
        return this.goodsName;
    }

    public String getGoodsDescription(){
        return this.goodsDescription;
    }

    public String getGoodsCode(){
        return this.goodsCode;
    }
    
    public String getGoodsLocation() {
    	return this.goodsLocation;
    }
    
    public String getGoodsCategory() {
    	return this.goodsCategory;
    }

}

