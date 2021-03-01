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
public class invoice_man {
    private String Rial;
    private String Dollar;
    private String Bart;
    private String Bank_Bart;
    
    public invoice_man(){
        Rial = "0";
        Dollar = "0";
        Bart = "0";
        Bank_Bart = "0";
    }
    
    void get_R_D_B_B_from_db(){
        
        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT id_invoice FROM invoice_management_tb");
            rs = pst.executeQuery();
            if (rs.next()) {
                pst = con.prepareStatement("SELECT TOP 1 rial, dollar, bart, bank_bart FROM invoice_management_tb ORDER BY invoice_man_date DESC;");
                rs = pst.executeQuery();
                while (rs.next()) {
                    //set to v2 all data only 1 row
                    Rial = rs.getString("rial");
                    Dollar = rs.getString("dollar");
                    Bart = rs.getString("bart");
                    Bank_Bart = rs.getString("bank_bart");
                }
            }
            
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public String getRial() {
        return Rial;
    }

    public String getDollar() {
        return Dollar;
    }

    public String getBart() {
        return Bart;
    }

    public String getBank_Bart() {
        return Bank_Bart;
    }
    
}
