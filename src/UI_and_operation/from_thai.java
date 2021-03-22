/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import static UI_and_operation.UI_and_operation.convert_to_short_money_type;
import UI_and_operation.UI_and_operation.purpose_type;
import UI_and_operation.UI_and_operation.type_of_money;
import static UI_and_operation.account.get_acc_id;
import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import static UI_and_operation.invoice_man.delete_inv_man;
import static UI_and_operation.invoice_man.is_null_acc_id_invoice_man;
import static UI_and_operation.invoice_man.update_inv_man_money;
import static UI_and_operation.purpose.get_id_pur_from_db;
import static UI_and_operation.validate_value.clear_cvot;
import static UI_and_operation.validate_value.money_S_B_R_validate;
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
public class from_thai {

    public static void get_from_thai_db_set_to_tb(int id_invoice, ArrayList<Vector> v2) {

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
                inv_man_obj.get_R_D_B_B_directly_from_db(get_acc_id(),
                        get_id_pur_from_db(purpose_type.from_thai),
                        id_invoice);

                pst = con.prepareStatement("SELECT  "
                        + "(SELECT id_invoice_man FROM invoice_management_tb WHERE id_invoice = ? AND id_acc = " + get_acc_id() + " AND id_pur = " + get_id_pur_from_db(purpose_type.from_thai) + ") AS id_invoice_man, "
                        + "(SELECT invoice_man_date FROM invoice_management_tb WHERE id_invoice = ? AND id_acc = " + get_acc_id() + " AND id_pur = " + get_id_pur_from_db(purpose_type.from_thai) + ") AS invoice_man_date,"
                        + "(select  user_name FROM account_tb WHERE account_tb.id_acc = from_thai_invoice_tb.id_acc) AS acc, "
                        + "(select  pur_type FROM purpose_tb WHERE purpose_tb.id_pur = from_thai_invoice_tb.id_pur) AS pur, "
                        + "receiver_ph_no, receiver_money, total_money, date_from_thai "
                        + "FROM from_thai_invoice_tb "
                        + "WHERE id_invoice = ?");
                pst.setInt(1, id_invoice);
                pst.setInt(2, id_invoice);
                pst.setInt(3, id_invoice);
                rs = pst.executeQuery();

                while (rs.next()) {

                    Vector v3 = new Vector();

                    //Date format string is passed as an argument to the Date format object
                    SimpleDateFormat objSDF = new SimpleDateFormat("yyyy-MMM-dd  a hh:mm:ss");
                    SimpleDateFormat objSDF_fr_thai = new SimpleDateFormat("yyyy-MMM-dd  a hh:mm");

                    //get date of exchanging money
                    String date_history = objSDF.format(rs.getTimestamp("invoice_man_date"));
                    String date_his_fr_thai = objSDF_fr_thai.format(rs.getTimestamp("date_from_thai"));

                    //set to v2 all data only 1 row
                    v3.add(date_history);
                    v3.add(String.valueOf(rs.getInt("id_invoice_man")));
                    v3.add(rs.getString("acc"));
                    v3.add(rs.getString("pur"));
                    v3.add("");
                    v3.add("");
                    v3.add("");
                    v3.add(money_S_B_R_validate(type_of_money.Bart, inv_man_obj.getBank_Bart(), true));
                    v3.add("លុយទទូល: -" + rs.getString("receiver_money") + " ฿"
                            + " | លុយទទូល: -" + rs.getString("total_money") + " ฿"
                            + " | date: " + date_his_fr_thai
                            + " | លេខទទូល: " + rs.getString("receiver_ph_no"));
                    v2.add(v3);
                }
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    public static void detele_from_thai_to_db(int id, String acc, String pur, Boolean is_update_inv_man) {

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
                        + "FROM from_thai_invoice_tb "
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
                update_inv_man_money("0", "0", "0", money, id, acc, pur);
            }
            //update sql query to access
            pst = con.prepareStatement("delete from from_thai_invoice_tb "
                    + "where id_invoice = ? "
                    + "AND id_acc = (select  id_acc FROM account_tb WHERE account_tb.user_name = ?) "
                    + "AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?)");

            pst.setInt(1, id);
            pst.setString(2, acc);
            pst.setString(3, pur);
            pst.executeUpdate();

            delete_inv_man(id, acc, pur);

            //dialog when added to access is success
            //                        JOptionPane.showMessageDialog(this, "records update");
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }
}
