/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import static UI_and_operation.UI_and_operation.convert_pur_to_kh;
import static UI_and_operation.UI_and_operation.convert_to_short_money_type;
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
import javax.swing.JOptionPane;

/**
 *
 * @author Chhann_chikay
 */
public class add_total_money {

    public static Vector get_add_total_db_set_to_tb(int id_invoice) {

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
                inv_man_obj.get_R_D_B_B_directly_from_db(get_acc_id(),
                        get_id_pur_from_db(UI_and_operation.purpose_type.add_total_money),
                        id_invoice);

                pst = con.prepareStatement("SELECT  id_add, "
                        + "(SELECT id_invoice_man FROM invoice_management_tb WHERE id_invoice = ? AND id_acc = " + get_acc_id() + " AND id_pur = " + get_id_pur_from_db(UI_and_operation.purpose_type.add_total_money) + ") AS id_invoice_man, "
                        + "(select  user_name FROM account_tb WHERE account_tb.id_acc = add_money_history_tb.id_acc) AS acc , "
                        + "(select  pur_type FROM purpose_tb WHERE purpose_tb.id_pur = add_money_history_tb.id_pur) AS pur, "
                        + "add_date, add_money, type_of_money "
                        + "FROM add_money_history_tb INNER JOIN money_type_tb "
                        + "ON add_money_history_tb.id_type_of_money = money_type_tb.id_type_of_money"
                        + " WHERE id_add = ? ");
                pst.setInt(1, id_invoice);
                pst.setInt(2, id_invoice);
                rs = pst.executeQuery();

                while (rs.next()) {

                    //Date format string is passed as an argument to the Date format object
                    SimpleDateFormat objSDF = new SimpleDateFormat("yyyy-MMM-dd  a hh:mm:ss");

                    //get date of exchanging money
                    String date_history = objSDF.format(rs.getTimestamp("add_date"));
                    String add_type_of_money = rs.getString("type_of_money");
                    //set to v2 all data only 1 row
                    v3.add(date_history);
                    v3.add(String.valueOf(rs.getInt("id_invoice_man")));
                    v3.add(rs.getString("acc"));
                    v3.add(convert_pur_to_kh(rs.getString("pur")));
                    v3.add((add_type_of_money.equals("Rial")) ? money_S_B_R_validate(type_of_money.Rial, inv_man_obj.getRial(), true) : "");
                    v3.add((add_type_of_money.equals("Dollar")) ? money_S_B_R_validate(type_of_money.Dollar, inv_man_obj.getDollar(), true) : "");
                    v3.add((add_type_of_money.equals("Bart")) ? money_S_B_R_validate(type_of_money.Bart, inv_man_obj.getBart(), true) : "");
                    v3.add("");
                    v3.add("បន្ថែម: " + rs.getString("add_money") + " " + convert_to_short_money_type(add_type_of_money));
                }
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return v3;
    }

    public static void detele_add_total_to_db(int id, String acc, String pur, Boolean is_update_inv_man) {

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
                String add_money = "";
                String money_type = "";

                //query to access
                pst = con.prepareStatement("SELECT add_money, type_of_money "
                        + "FROM add_money_history_tb INNER JOIN money_type_tb ON add_money_history_tb.id_type_of_money = money_type_tb.id_type_of_money "
                        + "where id_add = ? "
                        + "AND id_acc = (select  id_acc FROM account_tb WHERE account_tb.user_name = ?) "
                        + "AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?) ;");
                pst.setInt(1, id);
                pst.setString(2, acc);
                pst.setString(3, pur);
                rs = pst.executeQuery();
                while (rs.next()) {
                    add_money = clear_cvot(rs.getString("add_money"));
                    money_type = rs.getString("type_of_money");
                }

                switch (money_type) {
                    case "Rial":
                        update_inv_man_money("-" + add_money, "0", "0", "0", id, acc, pur);
                        break;
                    case "Dollar":
                        update_inv_man_money("0", "-" + add_money, "0", "0", id, acc, pur);
                        break;
                    case "Bart":
                        update_inv_man_money("0", "0", "-" + add_money, "0", id, acc, pur);
                        break;
                    default:
                        all_type_error_mes error_mes = new all_type_error_mes("error function add_total_money class: detele_add_total_to_db");
                }
            }
            //update sql query to access
            pst = con.prepareStatement("delete from add_money_history_tb where id_add = ? AND id_acc = (select  id_acc FROM account_tb WHERE account_tb.user_name = ?) AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?)");

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
