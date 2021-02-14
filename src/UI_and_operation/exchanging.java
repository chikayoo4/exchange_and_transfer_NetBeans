/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;
import UI_and_operation.UI_and_operation.purpose_type;
import UI_and_operation.UI_and_operation.type_of_exchange;
import static UI_and_operation.account.get_acc_id;
import static UI_and_operation.connection_to_ms_sql.*;
import static UI_and_operation.purpose.get_id_pur_from_db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author Chhann_chikay
 */
public class exchanging {
    private String customer_money;
    private String customer_result; 
    private String rate; 
    private Timestamp date;
    private type_of_exchange selected_exchange_rate;

    

    public exchanging(String customer_money, String customer_result, String rate, Timestamp date,type_of_exchange selected_exchange_rate) {
        this.customer_money = customer_money;
        this.customer_result = customer_result;
        this.rate = rate;
        this.date = date;
        this.selected_exchange_rate = selected_exchange_rate;
    }

    public type_of_exchange getSelected_exchange_rate() {
        return selected_exchange_rate;
    }
    
    public Timestamp getDate() {
        return date;
    }

    public String getCustomer_money() {
        return customer_money;
    }

    public String getCustomer_result() {
        return customer_result;
    }

    public String getRate() {
        return rate;
    }
    
    public int insert_to_db(){
        int lastinsertid = -1;
            Connection con;
        PreparedStatement pst;
        PreparedStatement pst1;
        try {
            con = DriverManager.getConnection(
                 getLocal_host(),
                  getLocal_host_user_name(),
                  getLocal_host_password()
            );

                    //write sql query to access
                    pst = con.prepareStatement("insert into exc_invoice_tb(exchanging_money, result_exchanging_money, invoice_date, id_type, exchange_rate, id_acc, id_pur)"
                        + "values(? , ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);

                    //set value to ? in query
                    pst.setString(1, customer_money);
                    pst.setString(2, customer_result);
                    pst.setTimestamp(3, getDate());
                    pst.setInt(4, get_id_type_from_db());
                    pst.setString(5, rate_S_B_R());
                    pst.setInt(6, get_acc_id());
                    pst.setInt(7, get_id_pur_from_db(purpose_type.exchanging.toString()));
                    pst.executeUpdate();
                    ResultSet generatekey = pst.getGeneratedKeys();
                    if(generatekey.next()){
                        lastinsertid = generatekey.getInt(1);
                    }

                    //write sql query to access
                    pst1 = con.prepareStatement("insert into invoice_management_tb (id_invoice, id_acc, id_pur)"
                        + "values(? , ?, ?);");

                    //set value to ? in query
                    pst1.setInt(1, lastinsertid);
                    pst1.setInt(2, get_acc_id());
                    pst1.setInt(3, get_id_pur_from_db(UI_and_operation.purpose_type.exchanging.toString()));
                    pst1.executeUpdate();
                } catch (SQLException ex) {
                    System.err.println(ex);
                }
        return lastinsertid;
    }

    public int get_id_type_from_db(){
        
        int id = -1;
            Connection con;
        PreparedStatement pst;
    ResultSet rs;
        try {
            con = DriverManager.getConnection(
                 getLocal_host(),
                  getLocal_host_user_name(),
                  getLocal_host_password()
            );
                //query to access
                pst = con.prepareStatement("SELECT id_type FROM exc_type_tb WHERE type_of_exchanging = '"+ selected_exchange_rate.toString() +"';");
                rs = pst.executeQuery();

                while (rs.next()){
                        id = rs.getInt("id_type");
                }
            } catch (SQLException ex) {
                System.err.println(ex);
            }
        return id;
    }
    
    public String rate_S_B_R() {
        String str = "";
        switch(selected_exchange_rate){
            case R_to_S:
            case S_to_R:
               str =  S_R_validation(Double.parseDouble(rate));
                break;

            case B_to_S:
            case S_to_B:
                str = S_B_validation(Double.parseDouble(rate));
                break;

            case B_to_R:
            case R_to_B:
                str = R_B_validation(Double.parseDouble(rate));
                break;
        }
        return str;
                 
    }
    
    //20 < x < 40
    //S_B 3050.0 -> incorrect
    //S_B 30.0 -> 30.50
    public static String S_B_validation(Double num) {
        String str = String.format("%.2f", num);
        if(!(20 < Integer.parseInt(String.format("%.0f", num)) && Integer.parseInt(String.format("%.0f", num)) < 40)){
            str = "incorrect";
        }
        return str;
    }
    
    //750 < x < 1500
    //B_R1 124.5 -> 124.50
    //S_B 1234.0 -> incorrect
    public static  String R_B_validation(Double num) {
        String str = String.format("%.1f", num);
        if(!(75 < Integer.parseInt(String.format("%.0f", num)) && Integer.parseInt(String.format("%.0f", num)) < 150)){
            str = "incorrect";
        }
        return str;
    }
    
    
    //3000 < x < 5000
    //S_R 4050.0 -> 4050
    //S_R 405.0 -> incorrect
    public static  String S_R_validation(Double num) {
        String str = String.format("%.0f", num);
        if(!(3000 < Integer.parseInt(str) && Integer.parseInt(str) < 5000)){
            str = "incorrect";
        }
        return str;
    }
    
    
}
