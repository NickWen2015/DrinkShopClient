package drinkshop.cp102.drinkshopclient.bean;

import java.io.Serializable;

/**
 * 訂單明細
 *
 * @author Nick
 * @date 2018/9/1
 */
public class OrderDetail implements Serializable {
    private String order_detail_id;
    private String order_id;
    private String product_name;
    private String ice_name;
    private String sugar_name;
    private String size_name;
    private String product_quantity;

    public OrderDetail() {
        super();
    }

    public OrderDetail(String order_detail_id, String order_id, String product_name, String ice_name, String sugar_name, String size_name, String product_quantity) {
        this.order_detail_id = order_detail_id;
        this.order_id = order_id;
        this.product_name = product_name;
        this.ice_name = ice_name;
        this.sugar_name = sugar_name;
        this.size_name = size_name;
        this.product_quantity = product_quantity;
    }

    public String getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(String order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getIce_name() {
        return ice_name;
    }

    public void setIce_name(String ice_name) {
        this.ice_name = ice_name;
    }

    public String getSugar_name() {
        return sugar_name;
    }

    public void setSugar_name(String sugar_name) {
        this.sugar_name = sugar_name;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }
}
