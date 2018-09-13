package drinkshop.cp102.drinkshopclient.bean;

public class ShoppingCart {
    private int ID;
    private int ProductID;
    private String Category;
    private String ProductName;
    private int HotOrIce;
    private int Size;
    private int SizePrice;
    private int Quantity;
    private int Suger;
    private int Temperature;

    public ShoppingCart(){
    }

    public ShoppingCart(int productID, String category, String productName, int hotOrIce, int size, int sizePrice, int quantity, int suger, int temperature) {
        ProductID = productID;
        Category = category;
        ProductName = productName;
        HotOrIce = hotOrIce;
        Size = size;
        SizePrice = sizePrice;
        Quantity = quantity;
        Suger = suger;
        Temperature = temperature;
    }

    public ShoppingCart(int ID, int productID, String category, String productName, int hotOrIce, int size, int sizePrice, int quantity, int suger, int temperature) {
        this.ID = ID;
        ProductID = productID;
        Category = category;
        ProductName = productName;
        HotOrIce = hotOrIce;
        Size = size;
        SizePrice = sizePrice;
        Quantity = quantity;
        Suger = suger;
        Temperature = temperature;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
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

    public int getHotOrIce() {
        return HotOrIce;
    }

    public void setHotOrIce(int hotOrIce) {
        HotOrIce = hotOrIce;
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
