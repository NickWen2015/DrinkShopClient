package drinkshop.cp102.drinkshopclient.builder;

import drinkshop.cp102.drinkshopclient.bean.AddingProduct;

public class AddingProductBuilder {
    private String myCategory;
    private String myProductName;
    private int myHotOrIce;
    private int mySize;
    private int mySizePrice;
    private int myQuantity;
    private int mySuger;
    private int myTemperature;

    public AddingProductBuilder setMyCategory(String myCategory) {
        this.myCategory = myCategory;
        return this;
    }

    public AddingProductBuilder setMyProductName(String myProductName) {
        this.myProductName = myProductName;
        return this;
    }

    public AddingProductBuilder setMyHotOrIce(int myHotOrIce) {
        this.myHotOrIce = myHotOrIce;
        return this;
    }

    public AddingProductBuilder setMySize(int mySize) {
        this.mySize = mySize;
        return this;
    }

    public AddingProductBuilder setMySizePrice(int mySizePrice) {
        this.mySizePrice = mySizePrice;
        return this;
    }

    public AddingProductBuilder setMyQuantity(int myQuantity) {
        this.myQuantity = myQuantity;
        return this;
    }

    public AddingProductBuilder setMySuger(int mySuger) {
        this.mySuger = mySuger;
        return this;
    }

    public AddingProductBuilder setMyTemperature(int myTemperature) {
        this.myTemperature = myTemperature;
        return this;
    }

    public AddingProduct createAddingProduct() {
        return new AddingProduct(myCategory, myProductName, myHotOrIce, mySize, mySizePrice, myQuantity, mySuger, myTemperature);
    }
}