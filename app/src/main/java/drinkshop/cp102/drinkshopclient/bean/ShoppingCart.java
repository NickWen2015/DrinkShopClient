package drinkshop.cp102.drinkshopclient.bean;

public class ShoppingCart {
    private long id;
    private String Category;
    private String ProductName;
    private int HotOrice;
    private int Size;
    private int SizePrice;
    private int Quantity;
    private int Suger;
    private int Temperature;

    public ShoppingCart(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getHotOrice() {
        return HotOrice;
    }

    public void setHotOrice(int hotOrice) {
        HotOrice = hotOrice;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public int getSizePrice() {
        return SizePrice;
    }

    public void setSizePrice(int sizePrice) {
        SizePrice = sizePrice;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getSuger() {
        return Suger;
    }

    public void setSuger(int suger) {
        Suger = suger;
    }

    public int getTemperature() {
        return Temperature;
    }

    public void setTemperature(int temperature) {
        Temperature = temperature;
    }
}
