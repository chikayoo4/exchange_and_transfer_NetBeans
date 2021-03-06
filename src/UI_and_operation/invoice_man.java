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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chhann_chikay
 */
public class invoice_man {

    private String Rial;
    private String Dollar;
    private String Bart;
    private String Bank_Bart;

    public invoice_man() {
        Rial = "0";
        Dollar = "0";
        Bart = "0";
        Bank_Bart = "0";
    }

    void get_R_D_B_B_top_1_from_db() {

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

    void get_R_D_B_B_directly_from_db(int id_acc, int id_pur, int id_invoice) {

        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            if (!is_null_acc_id_invoice_man(id_acc)) {
                pst = con.prepareStatement("SELECT rial, dollar, bart, bank_bart "
                        + "FROM invoice_management_tb "
                        + "WHERE id_acc = ? "
                        + "AND id_pur = ? "
                        + "AND id_invoice = ? ORDER BY invoice_man_date DESC;");
                pst.setInt(1, id_acc);
                pst.setInt(2, id_pur);
                pst.setInt(3, id_invoice);
                rs = pst.executeQuery();
                while (rs.next()) {
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

    public static Boolean is_null_acc_id_invoice_man(int acc_id) {

        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT id_invoice "
                    + "FROM invoice_management_tb "
                    + "WHERE id_acc = ?");
            pst.setInt(1, acc_id);
            rs = pst.executeQuery();

            if (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public static int get_id_invoice(int id_invoice_man) {

        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT id_invoice "
                    + "FROM invoice_management_tb "
                    + "WHERE id_invoice_man = ?;");
            pst.setInt(1, id_invoice_man);
            rs = pst.executeQuery();
            while (rs.next()) {
                return rs.getInt("id_invoice");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public static void update_inv_man_money(String Rial, String Dollar, String Bart, String Bart_bank, int id, String acc, String pur) {

        Connection con;
        PreparedStatement pst;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );

            pst = con.prepareStatement("UPDATE invoice_management_tb "
                    + "SET dollar = dollar + " + Dollar + ", rial = rial + " + Rial + " , "
                    + "bart = bart + " + Bart + ", bank_bart = bank_bart + " + Bart_bank + " "
                    + "WHERE id_invoice_man > (SELECT id_invoice_man "
                    + "FROM invoice_management_tb "
                    + "WHERE id_invoice = ? "
                    + "AND id_acc = (select id_acc FROM account_tb WHERE account_tb.user_name = ?) "
                    + "AND id_pur = (select id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?));");
            pst.setInt(1, id);
            pst.setString(2, acc);
            pst.setString(3, pur);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int get_count_id_invoice_man_from_db(int id, String acc, String pur) {

        int count_id_invoice_man = 0;
        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT COUNT(*) AS count_id_invoice_man "
                    + "FROM invoice_management_tb "
                    + "WHERE id_invoice_man >= (SELECT id_invoice_man "
                    + "FROM invoice_management_tb "
                    + "WHERE id_invoice = ? "
                    + "AND id_acc = (select id_acc FROM account_tb WHERE account_tb.user_name = ?) "
                    + "AND id_pur = (select id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?));");
            pst.setInt(1, id);
            pst.setString(2, acc);
            pst.setString(3, pur);
            rs = pst.executeQuery();
            while (rs.next()) {
                count_id_invoice_man = rs.getInt("count_id_invoice_man");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count_id_invoice_man;
    }

    public static void delete_inv_man(int id, String acc, String pur) {

        Connection con;
        PreparedStatement pst;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //update sql query to access
            pst = con.prepareStatement("delete from invoice_management_tb "
                    + "where id_invoice = ? "
                    + "AND id_acc = (select  id_acc FROM account_tb "
                    + "WHERE account_tb.user_name = ?) "
                    + "AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?);");
            pst.setInt(1, id);
            pst.setString(2, acc);
            pst.setString(3, pur);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
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
