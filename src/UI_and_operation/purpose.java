/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Chhann_chikay
 */
public class purpose {
   
    public static int get_id_pur_from_db(String type_of_purpose){
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
                pst = con.prepareStatement("SELECT id_pur FROM purpose_tb WHERE pur_type = '"+ type_of_purpose +"';");
                rs = pst.executeQuery();

                while (rs.next()){
                        id = rs.getInt("id_pur");
                }
            } catch ( SQLException ex) {
                System.err.println(ex);
            }
        return id;
    }
}
