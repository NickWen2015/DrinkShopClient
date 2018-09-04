package drinkshop.cp102.drinkshopclient.bean;

/**
 * 商品
 * @author mrosstro
 * @date 2018/8/29
 * */
public class Product {
    private String productCategory;
    private int productImg;
    private String productName;
    private int productMPrice;
    private int productLPrice;
    private int productQuantity;

    public Product(String productCategory, int productImg, String productName, int productMPrice, int productLPrice, int productQuantity) {
        this.productCategory = productCategory;
        this.productImg = productImg;
        this.productName = productName;
        this.productMPrice = productMPrice;
        this.productLPrice = productLPrice;
        this.productQuantity = productQuantity;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public int getProductImg() {
        return productImg;
    }

    public void setProductImg(int productImg) {
        this.productImg = productImg;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductMPrice() {
        return productMPrice;
    }

    public void setProductMPrice(int productMPrice) {
        this.productMPrice = productMPrice;
    }

    public int getProductLPrice() {
        return productLPrice;
    }

    public void setProductLPrice(int productLPrice) {
        this.productLPrice = productLPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}
