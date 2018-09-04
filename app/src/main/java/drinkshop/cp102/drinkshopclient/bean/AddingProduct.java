package drinkshop.cp102.drinkshopclient.bean;

/**
 * 訂購商品
 * @author mrosstro
 * @date 2018/8/29
 * */
public class AddingProduct {
    private String myCategory;
    private String myProductName;
    private int myHotOrIce;
    private int mySize;
    private int mySizePrice;
    private int myQuantity;
    private int mySuger;
    private int myTemperature;

    public AddingProduct(String myCategory, String myProductName, int myHotOrIce, int mySize, int mySizePrice, int myQuantity, int mySuger, int myTemperature) {
        this.myCategory = myCategory;
        this.myProductName = myProductName;
        this.myHotOrIce = myHotOrIce;
        this.mySize = mySize;
        this.mySizePrice = mySizePrice;
        this.myQuantity = myQuantity;
        this.mySuger = mySuger;
        this.myTemperature = myTemperature;
    }

    public String getMyCategory() {
        return myCategory;
    }

    public void setMyCategory(String myCategory) {
        this.myCategory = myCategory;
    }

    public String getMyProductName() {
        return myProductName;
    }

    public void setMyProductName(String myProductName) {
        this.myProductName = myProductName;
    }

    public int getMyHotOrIce() {
        return myHotOrIce;
    }

    public void setMyHotOrIce(int myHotOrIce) {
        this.myHotOrIce = myHotOrIce;
    }

    public int getMySize() {
        return mySize;
    }

    public void setMySize(int mySize) {
        this.mySize = mySize;
    }

    public int getMySizePrice() {
        return mySizePrice;
    }

    public void setMySizePrice(int mySizePrice) {
        this.mySizePrice = mySizePrice;
    }

    public int getMyQuantity() {
        return myQuantity;
    }

    public void setMyQuantity(int myQuantity) {
        this.myQuantity = myQuantity;
    }

    public int getMySuger() {
        return mySuger;
    }

    public void setMySuger(int mySuger) {
        this.mySuger = mySuger;
    }

    public int getMyTemperature() {
        return myTemperature;
    }

    public void setMyTemperature(int myTemperature) {
        this.myTemperature = myTemperature;
    }
}
