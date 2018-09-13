package drinkshop.cp102.drinkshopclient.bean;

/**
 * 商品
 * @author mrosstro
 * @date 2018/8/29
 * */
public class Product {
    private int id;
    private String Category;
    private String Name;
    private int MPrice;
    private int LPrice;

    public Product() { }

    public Product(int id, String category, String name, int MPrice, int LPrice) {
        this.id = id;
        Category = category;
        Name = name;
        this.MPrice = MPrice;
        this.LPrice = LPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getMPrice() {
        return MPrice;
    }

    public void setMPrice(int MPrice) {
        this.MPrice = MPrice;
    }

    public int getLPrice() {
        return LPrice;
    }

    public void setLPrice(int LPrice) {
        this.LPrice = LPrice;
    }
}
