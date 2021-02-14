/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Chhann_chikay
 */
public class sent_to_thai {
    private Date date;
    private String cus_name;
    private String cus_no;
    private String bank;
    private Double cus_money;
    private Double service_money;
    private Double total_money;
    private String cus_phone_no;
    
    
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    connection_to_ms_sql db = new connection_to_ms_sql();


    public sent_to_thai(Date date, String cus_name, String cus_no, String bank, Double cus_money, Double service_money, Double total_money, String cus_phone_no) {
        this.date = date;
        this.cus_name = cus_name;
        this.cus_no = cus_no;
        this.bank = bank;
        this.cus_money = cus_money;
        this.service_money = service_money;
        this.total_money = total_money;
        this.cus_phone_no = cus_phone_no;
    }

    public String getCus_phone_no() {
        return cus_phone_no;
    }

    public Date getDate() {
        return date;
    }

    public String getCus_name() {
        return cus_name;
    }

    public String getCus_no() {
        return cus_no;
    }

    public String getBank() {
        return bank;
    }

    public Double getCus_money() {
        return cus_money;
    }

    public Double getService_money() {
        return service_money;
    }

    public Double getTotal_money() {
        return total_money;
    }
    
    public int get_id_invoice_from_db(int id_acc, int id_pur){
        
        int id = -1;
         try {

                //call classs Ucanaccess 
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

                //connect java code with access
                con = DriverManager.getConnection(db.getUrl_db());

                //query to access
                pst = con.prepareStatement("SELECT id_invoice " +
                                                                "FROM to_thai_invoice_tb " +
                                                                "WHERE sender_money = ? " +
                                                                "AND servise_money = ? " +
                                                                "AND total_money = ? " +
                                                                "AND invoice_date = ? " +
                                                                "AND id_sender = " + get_id_sender_from_db() + " " +
                                                                "AND id_acc = ? " +
                                                                "AND id_pur = ?;");
                
                pst.setDouble(1, cus_money);
                pst.setDouble(2, service_money);
                pst.setDouble(3, total_money);
                pst.setDate(4, date);
                pst.setInt(5, id_acc);
                pst.setInt(6, id_pur);
                rs = pst.executeQuery();

                while (rs.next()){
                        id = rs.getInt("id_invoice");
                }
            } catch (ClassNotFoundException | SQLException ex) {
                System.err.println(ex);
            }
        return id;
    }
    
    public Boolean is_already_has_bank(){
        Boolean already_has = false;
         try {

                //call classs Ucanaccess 
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

                //connect java code with access
                con = DriverManager.getConnection(db.getUrl_db());

                int IsHas_bank_name = -1;
                //query to access
                pst = con.prepareStatement("SELECT COUNT(*) AS IsHas_bank_name "
                                                            + "FROM to_thai_bank_tb "
                                                            + "WHERE bank_name='"+ bank +"';");
                rs = pst.executeQuery();

                while (rs.next()){
                        IsHas_bank_name = rs.getInt("IsHas_bank_name");
                }
                if(IsHas_bank_name == 0) already_has = false;
                else if(IsHas_bank_name == 1) already_has = true;
            } catch (ClassNotFoundException | SQLException ex) {
                System.err.println(ex);
            } 
        return already_has;
    }
    
    public Boolean is_already_has_cus_no(){
        Boolean already_has = false;
         try {
                //call classs Ucanaccess 
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

                //connect java code with access
                con = DriverManager.getConnection(db.getUrl_db());

                int IsHas_sender_num_acc = -1;
                //query to access
                pst = con.prepareStatement("SELECT COUNT(*) AS IsHas_sender_num_acc "
                                                            + "FROM to_thai_sender_tb "
                                                            + "WHERE id_bank = (SELECT id_bank "
                                                                                            + "FROM to_thai_bank_tb "
                                                                                            + "WHERE bank_name = '"+ bank +"') "
                                                            + "AND sender_num_acc = '"+ cus_no +"';");            

                rs = pst.executeQuery();
                while (rs.next()){
                        IsHas_sender_num_acc = rs.getInt("IsHas_sender_num_acc");
                }
                if(IsHas_sender_num_acc == 0) already_has = false;
                else if(IsHas_sender_num_acc == 1) already_has = true;
            } catch (ClassNotFoundException | SQLException ex) {
                System.err.println(ex);
            }
        return already_has;
    }
    
    public Boolean is_already_has_ph_no(){
        Boolean already_has = false;
         try {

                //call classs Ucanaccess 
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

                //connect java code with access
                con = DriverManager.getConnection(db.getUrl_db());

                int IsHas_sender_ph_no = -1;
                //query to access
                pst = con.prepareStatement("SELECT COUNT(*) AS IsHas_sender_ph_no "
                                                            + "FROM to_thai_sender_ph_no_tb "
                                                            + "WHERE sender_ph_no='"+ cus_phone_no +"';");
                rs = pst.executeQuery();

                while (rs.next()){
                        IsHas_sender_ph_no = rs.getInt("IsHas_sender_ph_no");
                }
                if(IsHas_sender_ph_no == 0) already_has = false;
                else if(IsHas_sender_ph_no == 1) already_has = true;
            } catch (ClassNotFoundException | SQLException ex) {
                System.err.println(ex);
            } 
        return already_has;
    }
    
    public int get_id_bank_from_db(){
        int id = -1;
         try {
                //call classs Ucanaccess 
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

                //connect java code with access
                con = DriverManager.getConnection(db.getUrl_db());

                    pst = con.prepareStatement("SELECT id_bank FROM to_thai_bank_tb WHERE bank_name = '"+ bank +"'");
                    rs = pst.executeQuery();
                    while (rs.next()){
                        id = rs.getInt("id_bank");
                    }
            } catch (ClassNotFoundException | SQLException ex) {
                System.err.println(ex);
            }
        return id;
    }
    
    public int get_id_sender_from_db(){
        int id = -1;
         try {
                //call classs Ucanaccess 
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

                //connect java code with access
                con = DriverManager.getConnection(db.getUrl_db());

                    pst = con.prepareStatement("SELECT id_sender FROM to_thai_sender_tb WHERE sender_num_acc = '"+ cus_no +"'");
                    rs = pst.executeQuery();
                    while (rs.next()){
                        id = rs.getInt("id_sender");
                    }
            } catch (ClassNotFoundException | SQLException ex) {
                System.err.println(ex);
            }
        return id;
    }
}
