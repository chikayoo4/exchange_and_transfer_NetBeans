/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import UI_and_operation.UI_and_operation.type_of_money;
import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import static UI_and_operation.validate_value.clear_cvot;
import static UI_and_operation.validate_value.money_S_B_R_validate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Chhann_chikay
 */
public class to_province {

    public static String get_service_from_db(String money) {

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
            pst = con.prepareStatement("SELECT price "
                    + "FROM province_service "
                    + "WHERE " + clear_cvot(money) + " "
                    + "BETWEEN start_money AND end_money;");
            rs = pst.executeQuery();

            while (rs.next()) {
                return rs.getString("price");
            }
//        set_service_pro_name_cb.addItem();
        } catch (SQLException ex) {
            System.err.println("error between \n" + ex);
        }
        return "";
    }

    public static void two_one_cal(type_of_money selected_money_type_to_pro, javax.swing.JTextField two_three_sender_money_tf,
            javax.swing.JTextField two_three_service_money_tf, javax.swing.JTextField two_three_total_money_tf,
            javax.swing.JTextField two_three_balance_money_tf, javax.swing.JRadioButton two_three_rial_money_rb,
            javax.swing.JRadioButton two_three_dollar_money_rb, javax.swing.JRadioButton two_three_bart_money_rb) {
        if (!two_three_sender_money_tf.getText().isEmpty() && !two_three_service_money_tf.getText().isEmpty()
                && (two_three_rial_money_rb.isSelected() || two_three_dollar_money_rb.isSelected()
                || two_three_bart_money_rb.isSelected())) {
            two_three_total_money_tf.setText(money_S_B_R_validate(selected_money_type_to_pro,
                    String.valueOf(Double.parseDouble(clear_cvot(two_three_sender_money_tf.getText())) 
                            + Double.parseDouble(clear_cvot(two_three_service_money_tf.getText())))));

            two_three_balance_money_tf.setText(money_S_B_R_validate(selected_money_type_to_pro,
                    String.valueOf(Double.parseDouble(clear_cvot(two_three_service_money_tf.getText())) / 2)));
        }
    }

}
