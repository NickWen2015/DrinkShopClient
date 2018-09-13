package drinkshop.cp102.drinkshopclient.bean;

public class ShoppingCartTotol {
    private int price;
    private int quantity;

    public ShoppingCartTotol() { }

    public ShoppingCartTotol(int price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
