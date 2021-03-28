/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_p;

import admin_p.admin_view.purpose_type;
import static admin_p.account.get_acc_id;
import static admin_p.connection_to_ms_sql.getLocal_host;
import static admin_p.connection_to_ms_sql.getLocal_host_password;
import static admin_p.connection_to_ms_sql.getLocal_host_user_name;
import static admin_p.purpose.get_id_pur_from_db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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

    private String ind_Rial;
    private String ind_Dollar;
    private String ind_Bart;
    private String ind_Bank_Bart;

    public String getInd_Rial() {
        return ind_Rial;
    }

    public String getInd_Dollar() {
        return ind_Dollar;
    }

    public String getInd_Bart() {
        return ind_Bart;
    }

    public String getInd_Bank_Bart() {
        return ind_Bank_Bart;
    }

    public invoice_man() {
        Rial = "0";
        Dollar = "0";
        Bart = "0";
        Bank_Bart = "0";

        ind_Rial = "0";
        ind_Dollar = "0";
        ind_Bart = "0";
        ind_Bank_Bart = "0";
    }

    public void get_R_D_B_B_top_1_from_db() {

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
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    public void get_ind_R_D_B_B_top_1_from_db() {

        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT id_invoice_man FROM individual_total_money_management_tb");
            rs = pst.executeQuery();
            if (rs.next()) {
                pst = con.prepareStatement("SELECT TOP 1 individual_total_money_management_tb.rial AS rial, "
                        + "individual_total_money_management_tb.dollar AS dollar, "
                        + "individual_total_money_management_tb.bart AS bart, "
                        + "individual_total_money_management_tb.bank_bart AS bart_bank "
                        + "FROM individual_total_money_management_tb INNER JOIN invoice_management_tb "
                        + "ON individual_total_money_management_tb.id_invoice_man = invoice_management_tb.id_invoice_man "
                        + "WHERE id_acc = ? "
                        + "ORDER BY invoice_man_date DESC;");
                pst.setInt(1, get_acc_id());
                rs = pst.executeQuery();
                while (rs.next()) {
                    //set to v2 all data only 1 row
                    ind_Rial = rs.getString("rial");
                    ind_Dollar = rs.getString("dollar");
                    ind_Bart = rs.getString("bart");
                    ind_Bank_Bart = rs.getString("bart_bank");
                }
            }

        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
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
                        + "AND id_invoice = ?;");
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
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }
    void get_R_D_B_B_ind_directly_from_db(int id_acc, int id_pur, int id_invoice) {

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
                        + "FROM individual_total_money_management_tb "
                        + "WHERE id_invoice_man = "
                        + "(SELECT invoice_management_tb.id_invoice_man "
                        + "FROM invoice_management_tb "
                        + "WHERE id_acc = ? "
                        + "AND id_pur = ? "
                        + "AND id_invoice = ?);");
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
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
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
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
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
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
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
                    + "SET dollar = CAST(dollar as DECIMAL(30,2)) + CAST(" + Dollar + " as DECIMAL(30,2)), "
                    + "rial = CAST(rial as DECIMAL(30,2)) + CAST(" + Rial + " as DECIMAL(30,2)), "
                    + "bart = CAST(bart as DECIMAL(30,2)) + CAST(" + Bart + " as DECIMAL(30,2)), "
                    + "bank_bart = CAST(bank_bart as DECIMAL(30,2)) + CAST(" + Bart_bank + " as DECIMAL(30,2)) "
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
            System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    public static void update_ind_man_money(String Rial, String Dollar, String Bart, String Bart_bank, int id, String acc, String pur) {

        Connection con;
        PreparedStatement pst;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );

            pst = con.prepareStatement("UPDATE individual_total_money_management_tb "
                    + "SET dollar = CAST(dollar as DECIMAL(30,2)) + CAST(" + Dollar + " as DECIMAL(30,2)), "
                    + "rial = CAST(rial as DECIMAL(30,2)) + CAST(" + Rial + " as DECIMAL(30,2)), "
                    + "bart = CAST(bart as DECIMAL(30,2)) + CAST(" + Bart + " as DECIMAL(30,2)), "
                    + "bank_bart = CAST(bank_bart as DECIMAL(30,2)) + CAST(" + Bart_bank + " as DECIMAL(30,2)) "
                    + "WHERE id_invoice_man > (SELECT invoice_management_tb.id_invoice_man "
                    + "FROM invoice_management_tb "
                    + "WHERE id_invoice = ? "
                    + "AND id_acc = (select id_acc FROM account_tb WHERE account_tb.user_name = ?) "
                    + "AND id_pur = (select id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?)) "
                    + "AND id_invoice_man IN (SELECT invoice_management_tb.id_invoice_man "
                    + "FROM invoice_management_tb "
                    + "WHERE id_acc = (select id_acc FROM account_tb WHERE account_tb.user_name = ?));");
            pst.setInt(1, id);
            pst.setString(2, acc);
            pst.setString(3, pur);
            pst.setString(4, acc);
            pst.executeUpdate();
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
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
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
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
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }

    }

    public static void delete_ind_man(int id, String acc, String pur) {

        Connection con;
        PreparedStatement pst;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //update sql query to access
            pst = con.prepareStatement("delete from individual_total_money_management_tb "
                    + "WHERE individual_total_money_management_tb.id_invoice_man = "
                    + "(SELECT invoice_management_tb.id_invoice_man "
                    + "FROM invoice_management_tb "
                    + "WHERE id_invoice = ? "
                    + "AND id_acc = (select  id_acc FROM account_tb WHERE account_tb.user_name = ?) "
                    + "AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?));");
            pst.setInt(1, id);
            pst.setString(2, acc);
            pst.setString(3, pur);
            pst.executeUpdate();
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
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

    public static int set_invoice_man_db(String rial, String dollar, String bart, String bank_bart,
            int id_invoice, int id_acc, purpose_type pur, Timestamp invoice_man_date) {
        int id_inv_man = -1;
        Connection con;
        PreparedStatement pst;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //write sql query to access
            pst = con.prepareStatement("INSERT INTO invoice_management_tb (rial, dollar, bart, bank_bart, id_invoice, id_acc, id_pur, invoice_man_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, rial);
            pst.setString(2, dollar);
            pst.setString(3, bart);
            pst.setString(4, bank_bart);
            pst.setInt(5, id_invoice);
            pst.setInt(6, id_acc);
            pst.setInt(7, get_id_pur_from_db(pur));
            pst.setTimestamp(8, invoice_man_date);
            pst.executeUpdate();
            ResultSet generatekey = pst.getGeneratedKeys();
            if (generatekey.next()) {
                id_inv_man = generatekey.getInt(1);
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return id_inv_man;
    }

    public static int set_ind_man_db(String rial, String dollar, String bart, String bank_bart, int id_invoice_man) {
        int id_ind_man = -1;
        Connection con;
        PreparedStatement pst;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //write sql query to access
            pst = con.prepareStatement("INSERT INTO individual_total_money_management_tb (rial, dollar, bart, bank_bart, id_invoice_man) "
                    + "VALUES (?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, rial);
            pst.setString(2, dollar);
            pst.setString(3, bart);
            pst.setString(4, bank_bart);
            pst.setInt(5, id_invoice_man);
            pst.executeUpdate();
            ResultSet generatekey = pst.getGeneratedKeys();
            if (generatekey.next()) {
                id_ind_man = generatekey.getInt(1);
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return id_ind_man;
    }
}
