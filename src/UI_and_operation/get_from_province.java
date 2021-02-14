/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Chhann_chikay
 */
public class get_from_province extends province_transfer{
    private String receiver_ph_no;
    private String province_name;
    private Double sender_money;
    private Double total_money;
    private Double balance;
    private String money_type;
    
    
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    connection_to_ms_sql db = new connection_to_ms_sql();

    public get_from_province(String receiver_ph_no, String province_name, Double sender_money, Double total_money, Double balance, String money_type) {

        this.receiver_ph_no = receiver_ph_no;
        this.province_name = province_name;
        this.sender_money = sender_money;
        this.total_money = total_money;
        this.balance = balance;
        this.money_type = money_type;
    }

    public String getMoney_type() {
        return money_type;
    }

    public String getReceiver_ph_no() {
        return receiver_ph_no;
    }

    public String getProvince_name() {
        return province_name;
    }

    public Double getSender_money() {
        return sender_money;
    }

    public Double getTotal_money() {
        return total_money;
    }

    public Double getBalance() {
        return balance;
    }
    
}
