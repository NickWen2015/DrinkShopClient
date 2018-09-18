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
    private int store_id;
    private int member_id;
    private String order_accept_time;
    private String order_finish_time;
    private String order_type;
    private int delivery_id;
    private int coupon_id;
    private String order_status;
    private List<OrderDetail> orderDetailList;

    public Order() {
        super();
    }

    public Order(int order_id, String invoice_prefix, String invoice_no, int store_id, int member_id, String order_accept_time, String order_finish_time, String order_type, int delivery_id, int coupon_id, String order_status, List<OrderDetail> orderDetailList) {
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
        this.orderDetailList = orderDetailList;
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

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
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

    public int getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(int delivery_id) {
        this.delivery_id = delivery_id;
    }

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
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
