package Model;
public class Category {
    private String categoryName;
    private String categoryDescription;
    private String categoryCode;

    public Category(String categoryName, String categoryDescription, String categoryCode){
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryCode = categoryCode;
    }

    public String getCategoryName(){
        return this.categoryName;
    }

    public String getCategoryDescription(){
        return this.categoryDescription;
    }

    public String getCategoryCode(){
        return this.categoryCode;
    }
}
