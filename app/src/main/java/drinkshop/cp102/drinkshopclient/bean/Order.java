package drinkshop.cp102.drinkshopclient.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 訂單主檔
 * @author Nick
 * @date 2018/9/3
 */
public class Order implements Serializable {
    private int order_id;
    private String invoice_prefix;
    private String invoice_no;
    private String store_id;
    private String member_id;
    private String order_accept_time;
    private String order_finish_time;
    private String order_type;
    private String delivery_id;
    private String coupon_id;
    private String order_status;
    private List<OrderDetail> orderDetailList;

    public Order() {
        super();
    }

    public Order(int order_id, String invoice_prefix, String invoice_no, String store_id, String member_id, String order_accept_time, String order_finish_time, String order_type, String delivery_id, String coupon_id, String order_status) {
        this.order_id = order_id;
        this.invoice_prefix = invoice_prefix;
        this.invoice_no = invoice_no;
        this.store_id = store_id;
        this.member_id = member_id;
        this.order_accept_time = order_accept_time;
        this.order_finish_time = order_finish_time;
        this.order_type = order_type;
        this.delivery_id = delivery_id;
        this.coupon_id = coupon_id;
        this.order_status = order_status;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getInvoice_prefix() {
        return invoice_prefix;
    }

    public void setInvoice_prefix(String invoice_prefix) {
        this.invoice_prefix = invoice_prefix;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getOrder_accept_time() {
        return order_accept_time;
    }

    public void setOrder_accept_time(String order_accept_time) {
        this.order_accept_time = order_accept_time;
    }

    public String getOrder_finish_time() {
        return order_finish_time;
    }

    public void setOrder_finish_time(String order_finish_time) {
        this.order_finish_time = order_finish_time;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
}
