package drinkshop.cp102.drinkshopclient.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 會員
 * @author Nick
 * @date 2018/9/1
 * */
public class Member implements Serializable{
    private int member_id;
    private String member_account;
    private String member_password;
    private String member_name;
    private String member_birthday;
    private String member_sex;
    private String member_mobile;
    private String member_email;
    private String member_address;
    private String member_status;


    public Member() {
        super();
    }

    public Member(String member_account, String member_password, String member_name, String member_birthday, String member_sex, String member_mobile, String member_email, String member_address) {
        this.member_account = member_account;
        this.member_password = member_password;
        this.member_name = member_name;
        this.member_birthday = member_birthday;
        this.member_sex = member_sex;
        this.member_mobile = member_mobile;
        this.member_email = member_email;
        this.member_address = member_address;
    }

    public Member(int member_id, String member_password, String member_name, String member_birthday, String member_sex, String member_mobile, String member_email, String member_address) {

        this.member_id = member_id;
        this.member_password = member_password;
        this.member_name = member_name;
        this.member_birthday = member_birthday;
        this.member_sex = member_sex;
        this.member_mobile = member_mobile;
        this.member_email = member_email;
        this.member_address = member_address;
    }

    public Member(int member_id, String member_account, String member_password, String member_name, String member_birthday, String member_sex, String member_mobile, String member_email, String member_address, String member_status) {
        this.member_id = member_id;
        this.member_account = member_account;
        this.member_password = member_password;
        this.member_name = member_name;
        this.member_birthday = member_birthday;
        this.member_sex = member_sex;
        this.member_mobile = member_mobile;
        this.member_email = member_email;
        this.member_address = member_address;
        this.member_status = member_status;
    }

    public String getMember_sex() {
        return member_sex;
    }

    public void setMember_sex(String member_sex) {
        this.member_sex = member_sex;
    }



    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMember_account() {
        return member_account;
    }

    public void setMember_account(String member_account) {
        this.member_account = member_account;
    }

    public String getMember_password() {
        return member_password;
    }

    public void setMember_password(String member_password) {
        this.member_password = member_password;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_birthday() {
        return member_birthday;
    }

    public void setMember_birthday(String member_birthday) {
        this.member_birthday = member_birthday;
    }

    public String getMember_mobile() {
        return member_mobile;
    }

    public void setMember_mobile(String member_mobile) {
        this.member_mobile = member_mobile;
    }

    public String getMember_email() {
        return member_email;
    }

    public void setMember_email(String member_email) {
        this.member_email = member_email;
    }

    public String getMember_address() {
        return member_address;
    }

    public void setMember_address(String member_address) {
        this.member_address = member_address;
    }

    public String getMember_status() {
        return member_status;
    }

    public void setMember_status(String member_status) {
        this.member_status = member_status;
    }
}
