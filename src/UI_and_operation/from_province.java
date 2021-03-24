/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import static UI_and_operation.UI_and_operation.convert_to_short_money_type;
import UI_and_operation.UI_and_operation.type_of_money;
import static UI_and_operation.account.get_acc_id;
import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import static UI_and_operation.invoice_man.delete_ind_man;
import static UI_and_operation.invoice_man.delete_inv_man;
import static UI_and_operation.invoice_man.is_null_acc_id_invoice_man;
import static UI_and_operation.invoice_man.update_ind_man_money;
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
public class from_province {
    
    public static void get_from_pro_db_set_to_tb(int id_invoice, ArrayList<Vector> v2) {

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
                        get_id_pur_from_db(UI_and_operation.purpose_type.from_province),
                        id_invoice);

                pst = con.prepareStatement("SELECT  "
                        + "(SELECT id_invoice_man FROM invoice_management_tb WHERE id_invoice = ? AND id_acc = " + get_acc_id() + " AND id_pur = " + get_id_pur_from_db(UI_and_operation.purpose_type.from_province) + ") AS id_invoice_man, "
                        + "(SELECT invoice_man_date FROM invoice_management_tb WHERE id_invoice = ? AND id_acc = " + get_acc_id() + " AND id_pur = " + get_id_pur_from_db(UI_and_operation.purpose_type.from_province) + ") AS invoice_man_date,"
                        + "(select  user_name FROM account_tb WHERE account_tb.id_acc = from_province_invoice_tb.id_acc) AS acc , "
                        + "(select  pur_type FROM purpose_tb WHERE purpose_tb.id_pur = from_province_invoice_tb.id_pur) AS pur, "
                        + "(select  transfer_province FROM province_name_tb WHERE province_name_tb.id_province = from_province_invoice_tb.id_province) AS pro_name, "
                        + "balance_money, transfering_money, total_money, type_of_money, receiver_phone_no "
                        + "FROM from_province_invoice_tb INNER JOIN money_type_tb "
                        + "ON from_province_invoice_tb.id_type_of_money = money_type_tb.id_type_of_money "
                        + "WHERE id_invoice = ?");
                pst.setInt(1, id_invoice);
                pst.setInt(2, id_invoice);
                pst.setInt(3, id_invoice);
                rs = pst.executeQuery();

                while (rs.next()) {

                    Vector v3 = new Vector();

                    //Date format string is passed as an argument to the Date format object
                    SimpleDateFormat objSDF = new SimpleDateFormat("yyyy-MMM-dd  a hh:mm:ss");

                    //get date of exchanging money
                    String date_history = objSDF.format(rs.getTimestamp("invoice_man_date"));

                    String money_type = rs.getString("type_of_money");
                    //set to v2 all data only 1 row
                    v3.add(date_history);
                    v3.add(String.valueOf(rs.getInt("id_invoice_man")));
                    v3.add(rs.getString("acc"));
                    v3.add(rs.getString("pur"));
                    v3.add((money_type.equals("Rial")) ? money_S_B_R_validate(type_of_money.Rial, inv_man_obj.getRial(), true) : "");
                    v3.add((money_type.equals("Dollar")) ? money_S_B_R_validate(type_of_money.Dollar, inv_man_obj.getDollar(), true) : "");
                    v3.add((money_type.equals("Bart")) ? money_S_B_R_validate(type_of_money.Bart, inv_man_obj.getBart(), true) : "");
                    v3.add("");
                    v3.add("លុយទទូល: -" + rs.getString("transfering_money") + " " + convert_to_short_money_type(money_type)
                            + " | លុយទទូល: -" + rs.getString("total_money") + " " + convert_to_short_money_type(money_type)
                            + " | លុយសរុប: " + rs.getString("pro_name")
                            + " | លេខទទូល: " + rs.getString("receiver_phone_no"));
//                                    System.out.println(v3);
                    v2.add(v3);
                }
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    
    public static void detele_from_pro_to_db(int id, String acc, String pur, Boolean is_update_inv_man) {

        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            
            if(is_update_inv_man){
            String money = "";
            String money_type = "";

            //query to access
            pst = con.prepareStatement("SELECT total_money, type_of_money "
                    + "FROM from_province_invoice_tb INNER JOIN money_type_tb "
                    + "ON from_province_invoice_tb.id_type_of_money = money_type_tb.id_type_of_money "
                    + "where id_invoice = ? "
                    + "AND id_acc = (select  id_acc FROM account_tb WHERE account_tb.user_name = ?) "
                    + "AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?) ;");
            pst.setInt(1, id);
            pst.setString(2, acc);
            pst.setString(3, pur);
            rs = pst.executeQuery();
            while (rs.next()) {
                money = clear_cvot(rs.getString("total_money"));
                money_type = rs.getString("type_of_money");
            }

            switch (money_type) {
                case "Rial":
                    update_inv_man_money(money, "0", "0", "0", id, acc, pur);
                    update_ind_man_money(money, "0", "0", "0", id, acc, pur);
                    break;
                case "Dollar":
                    update_inv_man_money("0", money, "0", "0", id, acc, pur);
                    update_ind_man_money("0", money, "0", "0", id, acc, pur);
                    break;
                case "Bart":
                    update_inv_man_money("0", "0", money, "0", id, acc, pur);
                    update_ind_man_money("0", "0", money, "0", id, acc, pur);
                    break;
                default:
                    System.out.println("Error");
            }
        }
            //update sql query to access
            pst = con.prepareStatement("delete from from_province_invoice_tb "
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
    
    public static void two_two_cal(type_of_money selected_money_type_from_pro, javax.swing.JTextField two_four_sender_money_tf,
             javax.swing.JTextField two_four_total_money_tf, javax.swing.JTextField two_four_balance_money_tf,
            javax.swing.JRadioButton two_four_rial_money_rb, javax.swing.JRadioButton two_four_dollar_money_rb,
            javax.swing.JRadioButton two_four_bart_money_rb) {
        if ((!two_four_sender_money_tf.getText().isEmpty() && !two_four_total_money_tf.getText().isEmpty())
                && (two_four_rial_money_rb.isSelected() || two_four_dollar_money_rb.isSelected()
                || two_four_bart_money_rb.isSelected())) {
            two_four_balance_money_tf.setText(money_S_B_R_validate(selected_money_type_from_pro,
                    String.valueOf((Double.parseDouble(clear_cvot(two_four_sender_money_tf.getText()))
                            - Double.parseDouble(clear_cvot(two_four_total_money_tf.getText()))) / 2), true));
        }
        else {
            two_four_balance_money_tf.setText("");
        }
    }
}
