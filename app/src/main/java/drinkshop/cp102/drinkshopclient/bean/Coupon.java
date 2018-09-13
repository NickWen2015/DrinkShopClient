package drinkshop.cp102.drinkshopclient.bean;

import java.io.Serializable;

/**
 * 優惠卷
 * @author Nick
 * @date 2018/9/1
 * */
public class Coupon implements Serializable{

    private String coupon_id;
    private String member_id;
    private String coupon_no;
    private String coupon_discount;
    private String coupon_status;
    private String coupon_start;
    private String coupon_end;

    public Coupon() {
        super();
    }

    public Coupon(String coupon_id, String member_id, String coupon_no, String coupon_discount, String coupon_status, String coupon_start, String coupon_end) {
        this.coupon_id = coupon_id;
        this.member_id = member_id;
        this.coupon_no = coupon_no;
        this.coupon_discount = coupon_discount;
        this.coupon_status = coupon_status;
        this.coupon_start = coupon_start;
        this.coupon_end = coupon_end;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getCoupon_no() {
        return coupon_no;
    }

    public void setCoupon_no(String coupon_no) {
        this.coupon_no = coupon_no;
    }

    public String getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(String coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public String getCoupon_status() {
        return coupon_status;
    }

    public void setCoupon_status(String coupon_status) {
        this.coupon_status = coupon_status;
    }

    public String getCoupon_start() {
        return coupon_start;
    }

    public void setCoupon_start(String coupon_start) {
        this.coupon_start = coupon_start;
    }

    public String getCoupon_end() {
        return coupon_end;
    }

    public void setCoupon_end(String coupon_end) {
        this.coupon_end = coupon_end;
    }


}
