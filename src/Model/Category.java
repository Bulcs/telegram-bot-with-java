package Model;
public class Category {
	/** 
	 * @param categoryName String
	 * @param categoryDescription String
	 * @param categoryCode String
	 **/
    private String categoryName;
    private String categoryDescription;
    private String categoryCode;
    /**
     * Constructor Category
     * @param categoryName category name
     * @param categoryDescription category description
     * @param categoryCode category code
     * */
    public Category(String categoryName, String categoryDescription, String categoryCode){
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryCode = categoryCode;
    }
    /** Method get the category name
     * @return categoryName */
    public String getCategoryName(){
        return this.categoryName;
    }
    /** Method get the category description
     * @return categoryDescription */
    public String getCategoryDescription(){
        return this.categoryDescription;
    }
    /** Method get the category code
     * @return categoryCode */
    public String getCategoryCode(){
        return this.categoryCode;
    }
}
