/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import static UI_and_operation.UI_and_operation.convert_to_short_money_type;
import static UI_and_operation.UI_and_operation.current_date;
import static UI_and_operation.UI_and_operation.get_id_money_type_from_db;
import static UI_and_operation.UI_and_operation.get_id_province_name_from_db;
import static UI_and_operation.UI_and_operation.is_has_history_list_db;
import UI_and_operation.UI_and_operation.purpose_type;
import static UI_and_operation.UI_and_operation.set_history_list_db;
import static UI_and_operation.UI_and_operation.set_is_change_true;
import UI_and_operation.UI_and_operation.type_of_money;
import static UI_and_operation.account.get_acc_id;
import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import static UI_and_operation.invoice_man.delete_ind_man;
import static UI_and_operation.invoice_man.delete_inv_man;
import static UI_and_operation.invoice_man.is_null_acc_id_invoice_man;
import static UI_and_operation.invoice_man.set_ind_man_db;
import static UI_and_operation.invoice_man.set_invoice_man_db;
import static UI_and_operation.invoice_man.update_ind_man_money;
import static UI_and_operation.invoice_man.update_inv_man_money;
import static UI_and_operation.purpose.get_id_pur_from_db;
import static UI_and_operation.validate_value.bart_validation;
import static UI_and_operation.validate_value.clear_cvot;
import static UI_and_operation.validate_value.cut_the_lastest_point;
import static UI_and_operation.validate_value.dollar_validation;
import static UI_and_operation.validate_value.money_S_B_R_validate;
import static UI_and_operation.validate_value.rial_validation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chhann_chikay
 */
public class to_province {

    public static String get_service_from_db(String money, type_of_money money_type) {
        switch (money_type) {
            case Rial:

                break;
            case Dollar:

                return String.valueOf(Double.parseDouble(money) / 1000);
            default:
                System.out.println("error");
        }
        return "";
    }

    public static void set_to_pro_to_db(javax.swing.JTextField two_three_sender_phone_no_tf, javax.swing.JTextField two_three_receiver_phone_no_tf,
            javax.swing.JTextField two_one_total_money_tf, javax.swing.JTextField two_three_sender_money_tf, javax.swing.JTextField two_three_balance_money_tf,
            javax.swing.JTextField two_three_service_money_tf, javax.swing.JComboBox<String> two_one_pro_name_cb,
            javax.swing.JRadioButton two_three_rial_money_rb, javax.swing.JRadioButton two_three_dollar_money_rb,
            javax.swing.JRadioButton two_three_bart_money_rb, type_of_money selected_money_type_to_pro,
            javax.swing.ButtonGroup buttonGroup2, UI_and_operation ui_ope) {
        if ((!two_three_sender_phone_no_tf.getText().isEmpty() && !two_three_receiver_phone_no_tf.getText().isEmpty()
                && !two_one_pro_name_cb.getSelectedItem().equals("none") && !two_three_sender_money_tf.getText().isEmpty()
                && !two_three_service_money_tf.getText().isEmpty()) && (two_three_rial_money_rb.isSelected()
                || two_three_dollar_money_rb.isSelected() || two_three_bart_money_rb.isSelected())) {
            int lastinsert_id_invoice = -1;
            Connection con;
            PreparedStatement pst;
            try {
                con = DriverManager.getConnection(
                        getLocal_host(),
                        getLocal_host_user_name(),
                        getLocal_host_password()
                );

                if (!is_has_history_list_db(two_one_pro_name_cb.getSelectedItem().toString(), "transfer_province", "province_name_tb")) {
                    set_history_list_db(two_one_pro_name_cb.getSelectedItem().toString(), "transfer_province", "province_name_tb");
                }
                //write sql query to access
                pst = con.prepareStatement("insert into to_province_invoice_tb "
                        + "(service_money, balance_money, "
                        + "transfering_money, total_money, "
                        + "sender_phone_no, receiver_phone_no, "
                        + "id_type_of_money, id_province, "
                        + "id_acc, id_pur) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS);

                //set value to ? in query
                pst.setString(1, cut_the_lastest_point(two_three_service_money_tf.getText()));
                pst.setString(2, two_three_balance_money_tf.getText());
                pst.setString(3, cut_the_lastest_point(two_three_sender_money_tf.getText()));
                pst.setString(4, two_one_total_money_tf.getText());
                pst.setString(5, two_three_sender_phone_no_tf.getText().trim());
                pst.setString(6, two_three_receiver_phone_no_tf.getText().trim());
                pst.setInt(7, get_id_money_type_from_db(selected_money_type_to_pro));
                pst.setInt(8, get_id_province_name_from_db(two_one_pro_name_cb.getSelectedItem().toString()));
                pst.setInt(9, get_acc_id());
                pst.setInt(10, get_id_pur_from_db(purpose_type.to_province));
                pst.executeUpdate();
                ResultSet generatekey = pst.getGeneratedKeys();
                if (generatekey.next()) {
                    lastinsert_id_invoice = generatekey.getInt(1);
                }

                if (!is_has_history_list_db(two_three_sender_phone_no_tf.getText().trim(), "sender_phone_no", "to_pro_sender_ph_no_history_tb")) {
                    set_history_list_db(two_three_sender_phone_no_tf.getText().trim(), "sender_phone_no", "to_pro_sender_ph_no_history_tb");
                }
                if (!is_has_history_list_db(two_three_receiver_phone_no_tf.getText().trim(), "receiver_phone_no", "to_pro_receiver_ph_no_history_tb")) {
                    set_history_list_db(two_three_receiver_phone_no_tf.getText().trim(), "receiver_phone_no", "to_pro_receiver_ph_no_history_tb");
                }
                invoice_man in_man = new invoice_man();
                in_man.get_R_D_B_B_top_1_from_db();
                in_man.get_ind_R_D_B_B_top_1_from_db();

                String rial = rial_validation(Double.parseDouble(clear_cvot(in_man.getRial())));
                String dollar = dollar_validation(Double.parseDouble(clear_cvot(in_man.getDollar())));
                String bart = bart_validation(Double.parseDouble(clear_cvot(in_man.getBart())));
                String bart_bank = bart_validation(Double.parseDouble(clear_cvot(in_man.getBank_Bart())));

                String ind_rial = rial_validation(Double.parseDouble(clear_cvot(in_man.getInd_Rial())));
                String ind_dollar = dollar_validation(Double.parseDouble(clear_cvot(in_man.getInd_Dollar())));
                String ind_bart = bart_validation(Double.parseDouble(clear_cvot(in_man.getInd_Bart())));
                String ind_bart_bank = bart_validation(Double.parseDouble(clear_cvot(in_man.getInd_Bank_Bart())));
                switch (selected_money_type_to_pro) {
                    case Rial:
                        rial = rial_validation(Double.parseDouble(clear_cvot(two_one_total_money_tf.getText())) + Double.parseDouble(clear_cvot(in_man.getRial())));
                        ind_rial = rial_validation(Double.parseDouble(clear_cvot(two_one_total_money_tf.getText())) + Double.parseDouble(clear_cvot(in_man.getInd_Rial())));
                        break;
                    case Dollar:
                        dollar = dollar_validation(Double.parseDouble(clear_cvot(two_one_total_money_tf.getText())) + Double.parseDouble(clear_cvot(in_man.getDollar())));
                        ind_dollar = dollar_validation(Double.parseDouble(clear_cvot(two_one_total_money_tf.getText())) + Double.parseDouble(clear_cvot(in_man.getInd_Dollar())));
                        break;
                    case Bart:
                        bart = bart_validation(Double.parseDouble(clear_cvot(two_one_total_money_tf.getText())) + Double.parseDouble(clear_cvot(in_man.getBart())));
                        ind_bart = bart_validation(Double.parseDouble(clear_cvot(two_one_total_money_tf.getText())) + Double.parseDouble(clear_cvot(in_man.getInd_Bart())));
                        break;
                }
                int id_ind_man = set_invoice_man_db(rial, dollar, bart, bart_bank, lastinsert_id_invoice, get_acc_id(), purpose_type.to_province, current_date());
                set_ind_man_db(ind_rial, ind_dollar, ind_bart, ind_bart_bank, id_ind_man);
            } catch (SQLException ex) {
                sql_con sql_con_obj = new sql_con(ex);
                sql_con_obj.setVisible(true);
            }
            two_three_sender_phone_no_tf.setText("");
            two_three_receiver_phone_no_tf.setText("");
            two_one_pro_name_cb.getModel().setSelectedItem("none");
            two_three_sender_money_tf.setText("");
            two_three_service_money_tf.setText("");
            buttonGroup2.clearSelection();
//            ui_ope.set_history();

            set_is_change_true();
        }
    }

    public static void two_one_cal(type_of_money selected_money_type_to_pro, javax.swing.JTextField two_three_sender_money_tf,
            javax.swing.JTextField two_three_service_money_tf, javax.swing.JTextField two_one_total_money_tf,
            javax.swing.JTextField two_three_balance_money_tf, javax.swing.JRadioButton two_three_rial_money_rb,
            javax.swing.JRadioButton two_three_dollar_money_rb, javax.swing.JRadioButton two_three_bart_money_rb) {
        if (!two_three_sender_money_tf.getText().isEmpty() && !two_three_service_money_tf.getText().isEmpty()
                && (two_three_rial_money_rb.isSelected() || two_three_dollar_money_rb.isSelected()
                || two_three_bart_money_rb.isSelected())) {
            try {
                two_one_total_money_tf.setText(money_S_B_R_validate(selected_money_type_to_pro,
                        String.valueOf(Double.parseDouble(clear_cvot(two_three_sender_money_tf.getText()))
                                + Double.parseDouble(clear_cvot(two_three_service_money_tf.getText()))), true));

                two_three_balance_money_tf.setText(money_S_B_R_validate(selected_money_type_to_pro,
                        String.valueOf(Double.parseDouble(clear_cvot(two_three_service_money_tf.getText())) / 2), true));
            } catch (Exception e) {
                System.out.println("to_province \n" + e);
            }
        } else {
            two_one_total_money_tf.setText("");
            two_three_balance_money_tf.setText("");
        }
    }

    public static void get_to_pro_db_set_to_tb(int id_invoice, ArrayList<Vector> v2) {

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
                        get_id_pur_from_db(purpose_type.to_province),
                        id_invoice);

                pst = con.prepareStatement("SELECT  "
                        + "(SELECT id_invoice_man FROM invoice_management_tb WHERE id_invoice = ? AND id_acc = " + get_acc_id() + " AND id_pur = " + get_id_pur_from_db(purpose_type.to_province) + ") AS id_invoice_man, "
                        + "(SELECT invoice_man_date FROM invoice_management_tb WHERE id_invoice = ? AND id_acc = " + get_acc_id() + " AND id_pur = " + get_id_pur_from_db(purpose_type.to_province) + ") AS invoice_man_date,"
                        + "(select  user_name FROM account_tb WHERE account_tb.id_acc = to_province_invoice_tb.id_acc) AS acc , "
                        + "(select  pur_type FROM purpose_tb WHERE purpose_tb.id_pur = to_province_invoice_tb.id_pur) AS pur, "
                        + "(select  transfer_province FROM province_name_tb WHERE province_name_tb.id_province = to_province_invoice_tb.id_province) AS pro_name, "
                        + "service_money, balance_money, transfering_money, total_money, type_of_money, sender_phone_no, receiver_phone_no "
                        + "FROM to_province_invoice_tb INNER JOIN money_type_tb "
                        + "ON to_province_invoice_tb.id_type_of_money = money_type_tb.id_type_of_money "
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
                    v3.add("លុយផ្ញើរ: " + rs.getString("transfering_money") + " " + convert_to_short_money_type(money_type)
                            + " | លុយសរុប: " + rs.getString("total_money") + " " + convert_to_short_money_type(money_type)
                            + " | បើនៅ: " + rs.getString("pro_name")
                            + " | លេខផ្ញើរ: " + rs.getString("sender_phone_no")
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

    public static void detele_to_pro_to_db(int id, String acc, String pur, Boolean is_update_inv_man) {

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
                String money_type = "";

                //query to access
                pst = con.prepareStatement("SELECT total_money, type_of_money "
                        + "FROM to_province_invoice_tb INNER JOIN money_type_tb "
                        + "ON to_province_invoice_tb.id_type_of_money = money_type_tb.id_type_of_money "
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
                        update_inv_man_money("-" + money, "0", "0", "0", id, acc, pur);
                        update_ind_man_money("-" + money, "0", "0", "0", id, acc, pur);
                        break;
                    case "Dollar":
                        update_inv_man_money("0", "-" + money, "0", "0", id, acc, pur);
                        update_ind_man_money("0", "-" + money, "0", "0", id, acc, pur);
                        break;
                    case "Bart":
                        update_inv_man_money("0", "0", "-" + money, "0", id, acc, pur);
                        update_ind_man_money("0", "0", "-" + money, "0", id, acc, pur);
                        break;
                    default:
                        System.out.println("Error");
                }
            }
            //update sql query to access
            pst = con.prepareStatement("delete from to_province_invoice_tb "
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
}
