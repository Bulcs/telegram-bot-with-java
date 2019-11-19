package Model;
public class Good {
	/** 
	 * @param goodsName String
	 * @param goodsDescription String
	 * @param goodsCode String
	 * @param goodsLocation String
	 * @param goodsCategory String
	 **/
    private String goodsName;
    private String goodsDescription;
    private String goodsCode;
    private String goodsLocation;
    private String goodsCategory;
    

    /** Creates a good.
     * @param goodsName The goods  name.
     * @param goodsDescription The goods description.
     * @param goodsCode The goods code
     * @param goodsLocation the goods location
     * @param goodsCategory the goods category
     */
    public Good(String goodsName, String goodsDescription, String goodsCode, 
    		String goodsLocation, String goodsCategory){
        this.goodsName = goodsName;
        this.goodsDescription = goodsDescription;
        this.goodsCode = goodsCode;
        this.goodsLocation = goodsLocation;
        this.goodsCategory = goodsCategory;
    }
    /** Method get the good name
     * @return goodsName */
    public String getGoodsName(){
        return this.goodsName;
    }
    /** Method get the good description
     * @return goodsDescription */
    public String getGoodsDescription(){
        return this.goodsDescription;
    }
    /** Method get the good code
     * @return goodsCode */
    public String getGoodsCode(){
        return this.goodsCode;
    }
    /** Method get the good location
     * @return goodsLocation */
    public String getGoodsLocation() {
    	return this.goodsLocation;
    }
    /** Method get the good category
     * @return goodsCategory */
    public String getGoodsCategory() {
    	return this.goodsCategory;
    }

}

