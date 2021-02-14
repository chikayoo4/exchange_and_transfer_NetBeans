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
public class province_transfer {
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    connection_to_ms_sql db = new connection_to_ms_sql();
    
    
    public Boolean is_already_has_province_name(String province_name){
        Boolean already_has = false;
         try {

                //call classs Ucanaccess 
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

                //connect java code with access
                con = DriverManager.getConnection(db.getUrl_db());

                int IsHas_province_name = -1;
                //query to access
                pst = con.prepareStatement("SELECT COUNT(*) AS IsHas_province_name "
                                                            + "FROM province_name_tb "
                                                            + "WHERE transfer_province='"+ province_name +"';");
                rs = pst.executeQuery();

                while (rs.next()){
                        IsHas_province_name = rs.getInt("IsHas_province_name");
                }
                if(IsHas_province_name == 0) already_has = false;
                else if(IsHas_province_name == 1) already_has = true;
            } catch (ClassNotFoundException | SQLException ex) {
                System.err.println( ex);
            } 
        return already_has;
    }
    
    public int get_id_province_from_db(String province_name){
        int id = -1;
         try {
                //call classs Ucanaccess 
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

                //connect java code with access
                con = DriverManager.getConnection(db.getUrl_db());

                    pst = con.prepareStatement("SELECT id_province FROM province_name_tb WHERE transfer_province = '"+ province_name +"'");
                    rs = pst.executeQuery();
                    while (rs.next()){
                        id = rs.getInt("id_province");
                    }
            } catch (ClassNotFoundException | SQLException ex) {
                System.err.println(ex);
            }
        return id;
    }
}
