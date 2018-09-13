package drinkshop.cp102.drinkshopclient.bean;

import java.io.Serializable;

/**
 * 訂單主檔
 * @author Nick
 * @date 2018/9/3
 */
public class Order implements Serializable {
    private String order_id;
    private String store_name;
    private String order_accept_time;
    private String order_type;
    private String coupon_discount;
    private String order_status;

    public Order() {
        super();
    }

    public Order(String order_id, String store_name, String order_accept_time, String order_type, String coupon_discount, String order_status) {
        this.order_id = order_id;
        this.store_name = store_name;
        this.order_accept_time = order_accept_time;
        this.order_type = order_type;
        this.coupon_discount = coupon_discount;
        this.order_status = order_status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getOrder_accept_time() {
        return order_accept_time;
    }

    public void setOrder_accept_time(String order_accept_time) {
        this.order_accept_time = order_accept_time;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(String coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
