/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Chhann_chikay
 */
public class send_to_province extends province_transfer{
    private String sender_ph_no;
    private String receiver_ph_no;
    private String province_name;
    private Double sender_money;
    private Double service_money;
    private Double total_money;
    private Double balance;
    private String money_type;
    
    
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    connection_to_ms_sql db = new connection_to_ms_sql();

    public send_to_province(String sender_ph_no, String receiver_ph_no, String province_name, Double sender_money, Double service_money, Double total_money, Double balance, String money_type) {
        this.sender_ph_no = sender_ph_no;
        this.receiver_ph_no = receiver_ph_no;
        this.province_name = province_name;
        this.sender_money = sender_money;
        this.service_money = service_money;
        this.total_money = total_money;
        this.balance = balance;
        this.money_type = money_type;
    }

    public String getMoney_type() {
        return money_type;
    }


    public String getSender_ph_no() {
        return sender_ph_no;
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

    public Double getService_money() {
        return service_money;
    }

    public Double getTotal_money() {
        return total_money;
    }

    public Double getBalance() {
        return balance;
    }
}
