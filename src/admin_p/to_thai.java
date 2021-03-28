/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_p;

import static admin_p.admin_view.convert_pur_to_kh;
import static admin_p.admin_view.convert_to_short_money_type;
import admin_p.admin_view.purpose_type;
import admin_p.admin_view.type_of_money;
import static admin_p.account.get_acc_id;
import static admin_p.connection_to_ms_sql.getLocal_host;
import static admin_p.connection_to_ms_sql.getLocal_host_password;
import static admin_p.connection_to_ms_sql.getLocal_host_user_name;
import static admin_p.invoice_man.delete_ind_man;
import static admin_p.invoice_man.delete_inv_man;
import static admin_p.invoice_man.is_null_acc_id_invoice_man;
import static admin_p.invoice_man.update_ind_man_money;
import static admin_p.invoice_man.update_inv_man_money;
import static admin_p.purpose.get_id_pur_from_db;
import static admin_p.validate_value.clear_cvot;
import static admin_p.validate_value.money_S_B_R_validate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chhann_chikay
 */
public class to_thai {

    public static void detele_to_thai_to_db(int id, String acc, String pur, Boolean is_update_inv_man) {

        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );

            if (is_update_inv_man) {
                String money = "";
                //query to access
                pst = con.prepareStatement("SELECT total_money "
                        + "FROM to_thai_invoice_tb "
                        + "where id_invoice = ? "
                        + "AND id_acc = (select  id_acc FROM account_tb WHERE account_tb.user_name = ?) "
                        + "AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?);");
                pst.setInt(1, id);
                pst.setString(2, acc);
                pst.setString(3, pur);
                rs = pst.executeQuery();
                while (rs.next()) {
                    money = clear_cvot(rs.getString("total_money"));
                }
                update_inv_man_money("0", "0", "0", "-" + money, id, acc, pur);
                update_ind_man_money("0", "0", "0", "-" + money, id, acc, pur);
            }
            //update sql query to access
            pst = con.prepareStatement("delete from to_thai_invoice_tb "
                    + "where id_invoice = ? "
                    + "AND id_acc = (select  id_acc FROM account_tb WHERE account_tb.user_name = ?) "
                    + "AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?)");

            pst.setInt(1, id);
            pst.setString(2, acc);
            pst.setString(3, pur);
            pst.executeUpdate();

            delete_ind_man(id, acc, pur);
            delete_inv_man(id, acc, pur);

            //dialog when added to access is success
            //                        JOptionPane.showMessageDialog(this, "records update");
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    public static Vector get_to_thai_db_set_to_tb(int id_invoice, Boolean is_ind) {

        Vector v3 = new Vector();
        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            if (!is_null_acc_id_invoice_man(get_acc_id())) {

                invoice_man inv_man_obj = new invoice_man();
                if (is_ind) {
                    inv_man_obj.get_R_D_B_B_directly_from_db(get_acc_id(),
                            get_id_pur_from_db(purpose_type.to_thai),
                            id_invoice);
                } else {
                    inv_man_obj.get_R_D_B_B_ind_directly_from_db(get_acc_id(),
                            get_id_pur_from_db(purpose_type.to_thai),
                            id_invoice);
                }
                pst = con.prepareStatement("SELECT "
                        + "(SELECT id_invoice_man FROM invoice_management_tb WHERE id_invoice = ? AND id_acc = " + get_acc_id() + " AND id_pur = " + get_id_pur_from_db(purpose_type.to_thai) + ") AS id_invoice_man, "
                        + "(SELECT invoice_man_date FROM invoice_management_tb WHERE id_invoice = ? AND id_acc = " + get_acc_id() + " AND id_pur = " + get_id_pur_from_db(purpose_type.to_thai) + ") AS invoice_man_date, "
                        + "(SELECT  user_name FROM account_tb WHERE account_tb.id_acc = to_thai_invoice_tb.id_acc) AS acc , "
                        + "(SELECT  pur_type FROM purpose_tb WHERE purpose_tb.id_pur = to_thai_invoice_tb.id_pur) AS pur, "
                        + "(SELECT bank_name FROM to_thai_bank_tb WHERE to_thai_bank_tb.id_bank = to_thai_sender_tb.id_bank) AS bank_name, "
                        + "sender_money, servise_money, total_money, sender_name, sender_num_acc "
                        + "FROM (to_thai_invoice_tb INNER JOIN to_thai_sender_tb "
                        + "ON to_thai_invoice_tb.id_sender = to_thai_sender_tb.id_sender) "
                        + "WHERE id_invoice = ?");

                pst.setInt(1, id_invoice);
                pst.setInt(2, id_invoice);
                pst.setInt(3, id_invoice);
                rs = pst.executeQuery();

                while (rs.next()) {

                    //Date format string is passed as an argument to the Date format object
                    SimpleDateFormat objSDF = new SimpleDateFormat("yyyy-MMM-dd  a hh:mm:ss");

                    //get date of exchanging money
                    String date_history = objSDF.format(rs.getTimestamp("invoice_man_date"));

                    //set to v2 all data only 1 row
                    v3.add(date_history);
                    v3.add(String.valueOf(rs.getInt("id_invoice_man")));
                    v3.add(rs.getString("acc"));
                    v3.add(convert_pur_to_kh(rs.getString("pur")));
                    v3.add("");
                    v3.add("");
                    v3.add("");
                    v3.add(money_S_B_R_validate(type_of_money.Bart, inv_man_obj.getBank_Bart(), true));
                    v3.add("លុយផ្ញើរ: " + rs.getString("sender_money") + " ฿B"
                            + "  |  លុយសាវា: " + rs.getString("servise_money") + " B"
                            + "  |  លុយសរុប: " + rs.getString("total_money") + " ฿B"
                            + "  |  ធនាគា: " + rs.getString("bank_name") 
                            + "  |  ឈ្មោះ: " + rs.getString("sender_name")
                            + "  |  លេខគង់: " + rs.getString("sender_num_acc"));
                }
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return v3;
    }
}
