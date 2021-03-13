/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import static UI_and_operation.UI_and_operation.current_date;
import UI_and_operation.UI_and_operation.purpose_type;
import static UI_and_operation.UI_and_operation.set_invoice_man_db;
import UI_and_operation.UI_and_operation.type_of_exchange;
import UI_and_operation.UI_and_operation.type_of_money;
import static UI_and_operation.account.get_acc_id;
import static UI_and_operation.connection_to_ms_sql.*;
import static UI_and_operation.invoice_man.delete_inv_man;
import static UI_and_operation.invoice_man.is_null_acc_id_invoice_man;
import static UI_and_operation.invoice_man.update_inv_man_money;
import static UI_and_operation.path_file.double_exc_reciept_path;
import static UI_and_operation.path_file.exc_reciept_path;
import static UI_and_operation.path_file.get_path;
import static UI_and_operation.purpose.get_id_pur_from_db;
import static UI_and_operation.reciept.print_reciept;
import static UI_and_operation.validate_value.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chhann_chikay
 */
public class exchanging {

    private String customer_money;
    private String customer_result;
    private String rate;
    private Timestamp date;
    private type_of_exchange selected_exchange_rate;

    public exchanging(String customer_money, String customer_result, String rate,
            Timestamp date, type_of_exchange selected_exchange_rate) {
        this.customer_money = customer_money;
        this.customer_result = customer_result;
        this.rate = rate;
        this.date = date;
        this.selected_exchange_rate = selected_exchange_rate;
    }

    public type_of_exchange getSelected_exchange_rate() {
        return selected_exchange_rate;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getCustomer_money() {
        return customer_money;
    }

    public String getCustomer_result() {
        return customer_result;
    }

    public String getRate() {
        return rate;
    }

    public static type_of_exchange convert_to_stan_exc_type(String type_exc) {
        switch (type_exc) {
            case "$ → ៛":
                return type_of_exchange.S_to_R;
            case "៛ → $":
                return type_of_exchange.R_to_S;
            case "$ → ฿":
                return type_of_exchange.S_to_B;
            case "฿ → $":
                return type_of_exchange.B_to_S;
            case "฿ → ៛":
                return type_of_exchange.B_to_R;
            case "៛ → ฿":
                return type_of_exchange.R_to_B;
            default:
                return type_of_exchange.not_select;
        }
    }

    public static void caret_one_two(javax.swing.JTextField one_tf_customer_money,
            javax.swing.JComboBox<String> one_two_rate_bc,
            javax.swing.JTextField one_tf_customer_result,
            javax.swing.JTextField one_tf_exchange_rate) {
        if (!one_tf_customer_money.getText().isEmpty() && !one_two_rate_bc.getSelectedItem().equals("none")) {
            double customer_money = Double.parseDouble(clear_cvot(one_tf_customer_money.getText()));
            double exchange_rate = Double.parseDouble(clear_cvot(one_tf_exchange_rate.getText()));
            String result = "";
            switch (convert_to_stan_exc_type(String.valueOf(one_two_rate_bc.getSelectedItem()))) {

                //do * operator when......
                case S_to_R:
                case B_to_R:
                    result = money_S_B_R_validate(type_of_money.Rial, String.format("%.2f", (Double) (customer_money * exchange_rate)), true);
                    break;

                case S_to_B:
                    result = money_S_B_R_validate(type_of_money.Bart, String.format("%.2f", (Double) (customer_money * exchange_rate)), true);
                    break;

                //do / operator when......
                case R_to_S:
                case B_to_S:
                    result = money_S_B_R_validate(type_of_money.Dollar, String.format("%.2f", (Double) (customer_money / exchange_rate)), true);
                    break;

                //do / operator when......
                case R_to_B:
                    result = money_S_B_R_validate(type_of_money.Bart, String.format("%.2f", (Double) (customer_money / exchange_rate)), true);
                    break;
            }
            one_tf_customer_result.setText(String.valueOf(result));
        } else {
            one_tf_customer_result.setText("");
        }
    }

    public static void set_to_exc_two_cb(javax.swing.JComboBox<String> cb,
            javax.swing.JTextField set_tf, javax.swing.JLabel lb_exc, javax.swing.JLabel lb_result) {
        exc_rate exc_rate_obj = new exc_rate();
        exc_rate_obj.get_top_1_rate_from_db();
        switch (convert_to_stan_exc_type(String.valueOf(cb.getSelectedItem()))) {

            case S_to_R:
                set_tf.setText(exc_rate_obj.getS_to_R());
                lb_exc.setText("$");
                lb_result.setText("៛");
                break;
            case R_to_S:
                set_tf.setText(exc_rate_obj.getR_to_S());
                lb_exc.setText("៛");
                lb_result.setText("$");
                break;
            case S_to_B:
                set_tf.setText(exc_rate_obj.getS_to_B());
                lb_exc.setText("$");
                lb_result.setText("฿");
                break;
            case B_to_S:
                set_tf.setText(exc_rate_obj.getB_to_S());
                lb_exc.setText("฿");
                lb_result.setText("$");
                break;
            case B_to_R:
                set_tf.setText(exc_rate_obj.getB_to_R());
                lb_exc.setText("฿");
                lb_result.setText("៛");
                break;
            case R_to_B:
                set_tf.setText(exc_rate_obj.getR_to_B());
                lb_exc.setText("៛");
                lb_result.setText("฿");
                break;
            default:
                set_tf.setText("");
                lb_exc.setText("");
                lb_result.setText("");
        }
    }

    public int insert_to_db() {
        int lastinsert_id_invoice = -1;
        Connection con;
        PreparedStatement pst;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );

            invoice_man in_man = new invoice_man();
            in_man.get_R_D_B_B_top_1_from_db();

            //write sql query to access
            pst = con.prepareStatement("insert into exc_invoice_tb(exchanging_money, result_exchanging_money, invoice_date, id_type, exchange_rate, id_acc, id_pur)"
                    + "values(?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);

            //set value to ? in query
            pst.setString(1, customer_money);
            pst.setString(2, customer_result);
            pst.setTimestamp(3, getDate());
            pst.setInt(4, get_id_type_from_db(selected_exchange_rate));
            pst.setString(5, rate_S_B_R());
            pst.setInt(6, get_acc_id());
            pst.setInt(7, get_id_pur_from_db(purpose_type.exchanging));
            pst.executeUpdate();
            ResultSet generatekey = pst.getGeneratedKeys();
            if (generatekey.next()) {
                lastinsert_id_invoice = generatekey.getInt(1);
            }

            //set value to ? in query
            switch (selected_exchange_rate) {
                case S_to_R:
                    set_invoice_man_db(rial_validation(Double.parseDouble(clear_cvot(in_man.getRial())) - Double.parseDouble(clear_cvot(customer_result))),
                            dollar_validation(Double.parseDouble(clear_cvot(in_man.getDollar())) + Double.parseDouble(clear_cvot(customer_money))),
                            bart_validation(Double.parseDouble(clear_cvot(in_man.getBart()))),
                            bart_validation(Double.parseDouble(clear_cvot(in_man.getBank_Bart()))),
                            lastinsert_id_invoice,
                            get_acc_id(),
                            purpose_type.exchanging,
                            getDate());
                    break;
                case S_to_B:
                    set_invoice_man_db(rial_validation(Double.parseDouble(clear_cvot(in_man.getRial()))),
                            dollar_validation(Double.parseDouble(clear_cvot(in_man.getDollar())) + Double.parseDouble(clear_cvot(customer_money))),
                            bart_validation(Double.parseDouble(clear_cvot(in_man.getBart())) - Double.parseDouble(clear_cvot(customer_result))),
                            bart_validation(Double.parseDouble(clear_cvot(in_man.getBank_Bart()))),
                            lastinsert_id_invoice,
                            get_acc_id(),
                            purpose_type.exchanging,
                            getDate());

                    break;
                case B_to_R:
                    set_invoice_man_db(rial_validation(Double.parseDouble(clear_cvot(in_man.getRial())) - Double.parseDouble(clear_cvot(customer_result))),
                            dollar_validation(Double.parseDouble(clear_cvot(in_man.getDollar()))),
                            bart_validation(Double.parseDouble(clear_cvot(in_man.getBart())) + Double.parseDouble(clear_cvot(customer_money))),
                            bart_validation(Double.parseDouble(clear_cvot(in_man.getBank_Bart()))),
                            lastinsert_id_invoice,
                            get_acc_id(),
                            purpose_type.exchanging,
                            getDate());
                    break;
                case R_to_S:
                    set_invoice_man_db(rial_validation(Double.parseDouble(clear_cvot(in_man.getRial())) + Double.parseDouble(clear_cvot(customer_money))),
                            dollar_validation(Double.parseDouble(clear_cvot(in_man.getDollar())) - Double.parseDouble(clear_cvot(customer_result))),
                            bart_validation(Double.parseDouble(clear_cvot(in_man.getBart()))),
                            bart_validation(Double.parseDouble(clear_cvot(in_man.getBank_Bart()))),
                            lastinsert_id_invoice,
                            get_acc_id(),
                            purpose_type.exchanging,
                            getDate());
                    break;
                case B_to_S:
                    set_invoice_man_db(rial_validation(Double.parseDouble(clear_cvot(in_man.getRial()))),
                            dollar_validation(Double.parseDouble(clear_cvot(in_man.getDollar())) - Double.parseDouble(clear_cvot(customer_result))),
                            bart_validation(Double.parseDouble(clear_cvot(in_man.getBart())) + Double.parseDouble(clear_cvot(customer_money))),
                            bart_validation(Double.parseDouble(clear_cvot(in_man.getBank_Bart()))),
                            lastinsert_id_invoice,
                            get_acc_id(),
                            purpose_type.exchanging,
                            getDate());
                    break;
                case R_to_B:
                    set_invoice_man_db(rial_validation(Double.parseDouble(clear_cvot(in_man.getRial())) + Double.parseDouble(clear_cvot(customer_money))),
                            dollar_validation(Double.parseDouble(clear_cvot(in_man.getDollar()))),
                            bart_validation(Double.parseDouble(clear_cvot(in_man.getBart())) - Double.parseDouble(clear_cvot(customer_result))),
                            bart_validation(Double.parseDouble(clear_cvot(in_man.getBank_Bart()))),
                            lastinsert_id_invoice,
                            get_acc_id(),
                            purpose_type.exchanging,
                            getDate());
                    break;
                default:
                    System.out.println("Error");
            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return lastinsert_id_invoice;
    }

    public static int insert_double_exc_to_db(javax.swing.JTextField one_tf_customer_money1, javax.swing.JTextField one_tf_customer_money2,
            javax.swing.JComboBox<String> one_two_rate_bc1, javax.swing.JComboBox<String> one_two_rate_bc2,
            javax.swing.JTextField one_tf_exchange_rate1, javax.swing.JTextField one_tf_customer_result1,
            javax.swing.JTextField one_tf_exchange_rate2, javax.swing.JTextField one_tf_customer_result2,
            UI_and_operation ui_ope) {
        int id_inv_man = -1;
        if (!one_tf_customer_money1.getText().isEmpty() && !one_tf_customer_money2.getText().isEmpty()
                && !one_two_rate_bc1.getSelectedItem().equals("none") && !one_two_rate_bc2.getSelectedItem().equals("none")) {
            String one_customer_money = cut_the_lastest_point(one_tf_customer_money1.getText());
            String one_type_exc = String.valueOf(one_two_rate_bc1.getSelectedItem());
            String one_rate_exc = one_tf_exchange_rate1.getText();
            String one_customer_result = one_tf_customer_result1.getText();
            String two_customer_money = cut_the_lastest_point(one_tf_customer_money2.getText());
            String two_type_exc = String.valueOf(one_two_rate_bc2.getSelectedItem());
            String two_rate_exc = one_tf_exchange_rate2.getText();
            String two_customer_result = one_tf_customer_result2.getText();

            int lastinsert_id_invoice = -1;
            Connection con;
            PreparedStatement pst;
            try {
                con = DriverManager.getConnection(
                        getLocal_host(),
                        getLocal_host_user_name(),
                        getLocal_host_password()
                );

                //write sql query to access
                pst = con.prepareStatement("insert into exc_invoice_two_operator_tb("
                        + "exchanging_money_one, "
                        + " result_exchanging_money_one, "
                        + "exchanging_money_two, "
                        + "result_exchanging_money_two, "
                        + "one_rate, "
                        + "two_rate, "
                        + "id_type_one, "
                        + "id_type_two, "
                        + "id_acc, id_pur)"
                        + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS);
                //set value to ? in query
                pst.setString(1, one_customer_money);
                pst.setString(2, one_customer_result);
                pst.setString(3, two_customer_money);
                pst.setString(4, two_customer_result);
                pst.setString(5, one_rate_exc);
                pst.setString(6, two_rate_exc);
                pst.setInt(7, get_id_type_from_db(convert_to_stan_exc_type(one_type_exc)));
                pst.setInt(8, get_id_type_from_db(convert_to_stan_exc_type(two_type_exc)));
                pst.setInt(9, get_acc_id());
                pst.setInt(10, get_id_pur_from_db(purpose_type.double_exchanging));
                pst.executeUpdate();
                ResultSet generatekey = pst.getGeneratedKeys();
                if (generatekey.next()) {
                    lastinsert_id_invoice = generatekey.getInt(1);
                }

                invoice_man in_man = new invoice_man();
                in_man.get_R_D_B_B_top_1_from_db();

                Double tf_one_cus = Double.parseDouble(clear_cvot(one_customer_money));
                Double tf_one_res = Double.parseDouble(clear_cvot(one_customer_result));
                Double tf_two_cus = Double.parseDouble(clear_cvot(two_customer_money));
                Double tf_two_res = Double.parseDouble(clear_cvot(two_customer_result));

                Double db_rial = Double.parseDouble(in_man.getRial());
                Double db_dollar = Double.parseDouble(in_man.getDollar());
                Double db_bart = Double.parseDouble(in_man.getBart());

//                //set value to ? in query
                switch (convert_to_stan_exc_type(one_two_rate_bc1.getSelectedItem().toString())) {
                    case S_to_R:
                        db_dollar = db_dollar + tf_one_cus;
                        db_rial = db_rial - tf_one_res;
                        break;
                    case S_to_B:
                        db_dollar = db_dollar + tf_one_cus;
                        db_bart = db_bart - tf_one_res;
                        break;
                    case B_to_R:
                        db_bart = db_bart + tf_one_cus;
                        db_rial = db_rial - tf_one_res;
                        break;
                    case R_to_S:
                        db_rial = db_rial + tf_one_cus;
                        db_dollar = db_dollar - tf_one_res;
                        break;
                    case B_to_S:
                        db_bart = db_bart + tf_one_cus;
                        db_dollar = db_dollar - tf_one_res;
                        break;
                    case R_to_B:
                        db_rial = db_rial + tf_one_cus;
                        db_bart = db_bart - tf_one_res;
                        break;
                    default:
                        System.out.println("Error");
                }
                switch (convert_to_stan_exc_type(one_two_rate_bc2.getSelectedItem().toString())) {
                    case S_to_R:
                        db_dollar = db_dollar + tf_two_cus;
                        db_rial = db_rial - tf_two_res;
                        break;
                    case S_to_B:
                        db_dollar = db_dollar + tf_two_cus;
                        db_bart = db_bart - tf_two_res;
                        break;
                    case B_to_R:
                        db_bart = db_bart + tf_two_cus;
                        db_rial = db_rial - tf_two_res;
                        break;
                    case R_to_S:
                        db_rial = db_rial + tf_two_cus;
                        db_dollar = db_dollar - tf_two_res;
                        break;
                    case B_to_S:
                        db_bart = db_bart + tf_two_cus;
                        db_dollar = db_dollar - tf_two_res;
                        break;
                    case R_to_B:
                        db_rial = db_rial + tf_two_cus;
                        db_bart = db_bart - tf_two_res;
                        break;
                    default:
                        System.out.println("Error");
                }
                id_inv_man = set_invoice_man_db(rial_validation(db_rial),
                        dollar_validation(db_dollar),
                        bart_validation(db_bart),
                        bart_validation(Double.parseDouble(in_man.getBank_Bart())),
                        lastinsert_id_invoice,
                        get_acc_id(),
                        purpose_type.double_exchanging,
                        current_date());
                one_tf_customer_money1.setText("");
                one_tf_customer_money2.setText("");
                one_two_rate_bc1.getModel().setSelectedItem("none");
                one_two_rate_bc2.getModel().setSelectedItem("none");
                ui_ope.set_history();
            } catch (SQLException ex) {
                System.err.println(ex);
            }
        }
        return id_inv_man;
    }

    public static int get_id_type_from_db(type_of_exchange selected_exc_rate) {

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
            pst = con.prepareStatement("SELECT id_type FROM exc_type_tb WHERE type_of_exchanging = '" + selected_exc_rate.toString() + "';");
            rs = pst.executeQuery();

            while (rs.next()) {
                id = rs.getInt("id_type");
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return id;
    }

    public static void delete_double_exe_from_db(int id, String acc, String pur) {

        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            String exchanging_money_one = "";
            String result_exchanging_money_one = "";
            String type_of_exchanging_one = "";
            String exchanging_money_two = "";
            String result_exchanging_money_two = "";
            String type_of_exchanging_two = "";

            //query to access
            pst = con.prepareStatement("SELECT exchanging_money_one, result_exchanging_money_one, "
                    + "exchanging_money_two, result_exchanging_money_two, "
                    + "(select  type_of_exchanging FROM exc_type_tb WHERE exc_type_tb.id_type = exc_invoice_two_operator_tb.id_type_one) AS exchange_type_one, "
                    + "(select  type_of_exchanging FROM exc_type_tb WHERE exc_type_tb.id_type = exc_invoice_two_operator_tb.id_type_two) AS exchange_type_two "
                    + "FROM exc_invoice_two_operator_tb "
                    + "where id_invoice = ? "
                    + "AND id_acc = (select  id_acc FROM account_tb WHERE account_tb.user_name = ?) "
                    + "AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?) ;");
            pst.setInt(1, id);
            pst.setString(2, acc);
            pst.setString(3, pur);
            rs = pst.executeQuery();
            while (rs.next()) {
                exchanging_money_one = clear_cvot(rs.getString("exchanging_money_one"));
                result_exchanging_money_one = clear_cvot(rs.getString("result_exchanging_money_one"));
                type_of_exchanging_one = rs.getString("exchange_type_one");
                exchanging_money_two = clear_cvot(rs.getString("exchanging_money_two"));
                result_exchanging_money_two = clear_cvot(rs.getString("result_exchanging_money_two"));
                type_of_exchanging_two = rs.getString("exchange_type_two");
            }

            switch (type_of_exchanging_one) {
                case "S_to_R":
                    update_inv_man_money(result_exchanging_money_one, "-" + exchanging_money_one, "0", "0", id, acc, pur);
                    break;
                case "S_to_B":
                    update_inv_man_money("0", "-" + exchanging_money_one, result_exchanging_money_one, "0", id, acc, pur);
                    break;
                case "B_to_R":
                    update_inv_man_money(result_exchanging_money_one, "0", "-" + exchanging_money_one, "0", id, acc, pur);
                    break;
                case "R_to_S":
                    update_inv_man_money("-" + exchanging_money_one, result_exchanging_money_one, "0", "0", id, acc, pur);
                    break;
                case "B_to_S":
                    update_inv_man_money("0", result_exchanging_money_one, "-" + exchanging_money_one, "0", id, acc, pur);
                    break;
                case "R_to_B":
                    update_inv_man_money("-" + exchanging_money_one, "0", result_exchanging_money_one, "0", id, acc, pur);
                    break;
                default:
                    System.out.println("Error");
            }
            switch (type_of_exchanging_two) {
                case "S_to_R":
                    update_inv_man_money(result_exchanging_money_two, "-" + exchanging_money_two, "0", "0", id, acc, pur);
                    break;
                case "S_to_B":
                    update_inv_man_money("0", "-" + exchanging_money_two, result_exchanging_money_two, "0", id, acc, pur);
                    break;
                case "B_to_R":
                    update_inv_man_money(result_exchanging_money_two, "0", "-" + exchanging_money_two, "0", id, acc, pur);
                    break;
                case "R_to_S":
                    update_inv_man_money("-" + exchanging_money_two, result_exchanging_money_two, "0", "0", id, acc, pur);
                    break;
                case "B_to_S":
                    update_inv_man_money("0", result_exchanging_money_two, "-" + exchanging_money_two, "0", id, acc, pur);
                    break;
                case "R_to_B":
                    update_inv_man_money("-" + exchanging_money_two, "0", result_exchanging_money_two, "0", id, acc, pur);
                    break;
                default:
                    System.out.println("Error");
            }
            //update sql query to access
            pst = con.prepareStatement("delete from exc_invoice_two_operator_tb "
                    + "where id_invoice = ? "
                    + "AND id_acc = (select  id_acc FROM account_tb WHERE account_tb.user_name = ?) "
                    + "AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?)");

            pst.setInt(1, id);
            pst.setString(2, acc);
            pst.setString(3, pur);
            pst.executeUpdate();
            //dialog when added to access is success
            //                        JOptionPane.showMessageDialog(this, "records update");

            delete_inv_man(id, acc, pur);

        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public static void delete_exe_from_db(int id, String acc, String pur) {

        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            String exchanging_money = "";
            String result_exchanging_money = "";
            String type_of_exchanging = "";

            //query to access
            pst = con.prepareStatement("SELECT exchanging_money, result_exchanging_money, type_of_exchanging "
                    + "FROM exc_invoice_tb INNER JOIN exc_type_tb ON exc_invoice_tb.id_type = exc_type_tb.id_type "
                    + "where id_invoice = ? "
                    + "AND id_acc = (select  id_acc FROM account_tb WHERE account_tb.user_name = ?) "
                    + "AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?) ;");
            pst.setInt(1, id);
            pst.setString(2, acc);
            pst.setString(3, pur);
            rs = pst.executeQuery();
            while (rs.next()) {
                exchanging_money = clear_cvot(rs.getString("exchanging_money"));
                result_exchanging_money = clear_cvot(rs.getString("result_exchanging_money"));
                type_of_exchanging = rs.getString("type_of_exchanging");
            }

            switch (type_of_exchanging) {
                case "S_to_R":
                    update_inv_man_money(result_exchanging_money, "-" + exchanging_money, "0", "0", id, acc, pur);
                    break;
                case "S_to_B":
                    update_inv_man_money("0", "-" + exchanging_money, result_exchanging_money, "0", id, acc, pur);
                    break;
                case "B_to_R":
                    update_inv_man_money(result_exchanging_money, "0", "-" + exchanging_money, "0", id, acc, pur);
                    break;
                case "R_to_S":
                    update_inv_man_money("-" + exchanging_money, result_exchanging_money, "0", "0", id, acc, pur);
                    break;
                case "B_to_S":
                    update_inv_man_money("0", result_exchanging_money, "-" + exchanging_money, "0", id, acc, pur);
                    break;
                case "R_to_B":
                    update_inv_man_money("-" + exchanging_money, "0", result_exchanging_money, "0", id, acc, pur);
                    break;
                default:
                    System.out.println("Error");
            }
            //update sql query to access
            pst = con.prepareStatement("delete from exc_invoice_tb where id_invoice = ? AND id_acc = (select  id_acc FROM account_tb WHERE account_tb.user_name = ?) AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?)");

            pst.setInt(1, id);
            pst.setString(2, acc);
            pst.setString(3, pur);
            pst.executeUpdate();
            //dialog when added to access is success
            //                        JOptionPane.showMessageDialog(this, "records update");

            delete_inv_man(id, acc, pur);

        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public String rate_S_B_R() {
        String str = "";
        switch (selected_exchange_rate) {
            case R_to_S:
            case S_to_R:
                str = S_R_validation(Double.parseDouble(rate));
                break;

            case B_to_S:
            case S_to_B:
                str = S_B_validation(Double.parseDouble(rate));
                break;

            case B_to_R:
            case R_to_B:
                str = R_B_validation(Double.parseDouble(rate));
                break;
        }
        return str;

    }

    //20 < x < 40
    //S_B 3050.0 -> incorrect
    //S_B 30.0 -> 30.50
    public static String S_B_validation(Double num) {
        String str = String.format("%.2f", num);
//        if (!(20 < Integer.parseInt(String.format("%.0f", num)) && Integer.parseInt(String.format("%.0f", num)) < 40)) {
//            str = "incorrect";
//        }
        return str;
    }

    //750 < x < 1500
    //B_R1 124.5 -> 124.50
    //S_B 1234.0 -> incorrect
    public static String R_B_validation(Double num) {
        String str = String.format("%.1f", num);
//        if (!(75 < Integer.parseInt(String.format("%.0f", num)) && Integer.parseInt(String.format("%.0f", num)) < 150)) {
//            str = "incorrect";
//        }
        return str;
    }

    //3000 < x < 5000
    //S_R 4050.0 -> 4050
    //S_R 405.0 -> incorrect
    public static String S_R_validation(Double num) {
        String str = String.format("%.0f", num);
//        if (!(3000 < Integer.parseInt(str) && Integer.parseInt(str) < 5000)) {
//            str = "incorrect";
//        }
        return str;
    }

    //function that calutation exchanging money
    //reson why don't i use variable type_of_exchange selected_exchange_rate abow because the function static but variable alow is non-static. 
    //So i have no choice i must for type_of_exchange selected_exchange_rate variable we use one_calculate() function
    public static void one_two_calculation(type_of_exchange selected_exchange_rate,
            javax.swing.JTextField one_tf_customer_money, javax.swing.JTextField one_tf_exchange_rate,
            javax.swing.JTextField one_tf_customer_result) {

        try {
            //find out that customer money and exchange rate ft is null or not
            Boolean cus_mon_is_empty = one_tf_customer_money.getText().isEmpty();
            Boolean exc_rate_is_empty = one_tf_exchange_rate.getText().isEmpty();

            //if customer money and exchange rate tf is not null, it will * or / between the 2 tf then set value to result tf
            if (!cus_mon_is_empty && !exc_rate_is_empty) {
                double customer_money = Double.parseDouble(clear_cvot(one_tf_customer_money.getText()));
                double exchange_rate = Double.parseDouble(clear_cvot(one_tf_exchange_rate.getText()));
                String result = "";
                switch (selected_exchange_rate) {

                    //do * operator when......
                    case S_to_R:
                    case B_to_R:
                        result = money_S_B_R_validate(type_of_money.Rial, String.format("%.2f", (Double) (customer_money * exchange_rate)), true);
                        break;

                    case S_to_B:
                        result = money_S_B_R_validate(type_of_money.Bart, String.format("%.2f", (Double) (customer_money * exchange_rate)), true);
                        break;

                    //do / operator when......
                    case R_to_S:
                    case B_to_S:
                        result = money_S_B_R_validate(type_of_money.Dollar, String.format("%.2f", (Double) (customer_money / exchange_rate)), true);
                        break;

                    //do / operator when......
                    case R_to_B:
                        result = money_S_B_R_validate(type_of_money.Bart, String.format("%.2f", (Double) (customer_money / exchange_rate)), true);
                        break;
                }

                //set value to result tf
                one_tf_customer_result.setText(add_cuot_to_money(result));
            } //if customer money and exchange rate tf is null, it will set value to result tf to null
            else {
                one_tf_customer_result.setText("");
            }

        } catch (Exception e) {
            System.out.println("ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        }
    }

    public static void one_set_money_type_to_lb(String cus_money,
            String result_money, javax.swing.JLabel one_money_type_lb,
            javax.swing.JLabel one_rate_type_lb, javax.swing.JLabel one_money_type_result_lb) {
        one_money_type_lb.setText("( " + cus_money + " )");
        one_rate_type_lb.setText("( " + cus_money + " → " + result_money + " )");
        one_money_type_result_lb.setText("( " + result_money + " )");
    }

    public static void exc_close_or_print(Boolean is_print, javax.swing.JTextField one_tf_customer_money,
            javax.swing.JTextField one_tf_exchange_rate, javax.swing.JTextField one_tf_customer_result,
            javax.swing.JButton one_bn_S_to_R, javax.swing.JButton one_bn_S_to_B,
            javax.swing.JButton one_bn_B_to_R, javax.swing.JButton one_bn_R_to_S,
            javax.swing.JButton one_bn_B_to_S, javax.swing.JButton one_bn_R_to_B,
            javax.swing.JLabel one_lb_operator, type_of_exchange selected_exchange_rate,
            UI_and_operation ui_ope) {

        if (selected_exchange_rate != type_of_exchange.not_select && !one_tf_customer_result.getText().isEmpty()) {
            String result = cut_the_lastest_point(one_tf_customer_money.getText());
            switch (selected_exchange_rate) {

                case R_to_S:
                case R_to_B:
                    result = money_S_B_R_validate(type_of_money.Rial, result, true);

                    break;

                case S_to_R:
                case S_to_B:
                    result = money_S_B_R_validate(type_of_money.Dollar, result, true);
                    break;

                case B_to_R:
                case B_to_S:
                    result = money_S_B_R_validate(type_of_money.Bart, result, true);
                    break;

            }
            one_tf_customer_money.setText(result);
            exchanging exchanging_obj = new exchanging(
                    one_tf_customer_money.getText(),
                    one_tf_customer_result.getText(),
                    one_tf_exchange_rate.getText(),
                    current_date(),
                    selected_exchange_rate
            );
            if (is_print) {
//                print_reciept(get_path() + "\\reciept_for_print\\exchanging.jrxml",
//                        exchanging_obj.insert_to_db());
                print_reciept(get_path() + exc_reciept_path,
                        exchanging_obj.insert_to_db());
            } else {
                exchanging_obj.insert_to_db();
            }
            one_tf_customer_money.setText("");
            one_tf_exchange_rate.setText("");
            one_tf_customer_result.setText("");
            one_bn_S_to_R.setEnabled(true);
            one_bn_S_to_B.setEnabled(true);
            one_bn_B_to_R.setEnabled(true);
            one_bn_R_to_S.setEnabled(true);
            one_bn_B_to_S.setEnabled(true);
            one_bn_R_to_B.setEnabled(true);
            one_lb_operator.setText("");
            ui_ope.set_history();
        }

    }

    public static void double_exc_close_or_print(javax.swing.JTextField one_tf_customer_money1, javax.swing.JTextField one_tf_customer_money2,
            javax.swing.JComboBox<String> one_two_rate_bc1, javax.swing.JComboBox<String> one_two_rate_bc2,
            javax.swing.JTextField one_tf_exchange_rate1, javax.swing.JTextField one_tf_customer_result1,
            javax.swing.JTextField one_tf_exchange_rate2, javax.swing.JTextField one_tf_customer_result2, Boolean is_print, UI_and_operation ui_ope) {
        if (is_print) {
            print_reciept(get_path() + double_exc_reciept_path,
                    insert_double_exc_to_db(one_tf_customer_money1, one_tf_customer_money2, one_two_rate_bc1,
                            one_two_rate_bc2, one_tf_exchange_rate1, one_tf_customer_result1,
                            one_tf_exchange_rate2, one_tf_customer_result2, ui_ope));
        } else {
            insert_double_exc_to_db(one_tf_customer_money1, one_tf_customer_money2, one_two_rate_bc1,
                    one_two_rate_bc2, one_tf_exchange_rate1, one_tf_customer_result1,
                    one_tf_exchange_rate2, one_tf_customer_result2, ui_ope);
        }

    }

    public static void get_from_pro_db_set_to_tb_double_exc(int id_invoice, ArrayList<Vector> v2) {
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
                        get_id_pur_from_db(purpose_type.double_exchanging),
                        id_invoice);

                pst = con.prepareStatement("SELECT id_invoice, "
                        + "(SELECT invoice_man_date FROM invoice_management_tb WHERE id_invoice = ? AND id_acc = " + get_acc_id() + " AND id_pur = " + get_id_pur_from_db(purpose_type.double_exchanging) + ") AS invoice_man_date,"
                        + "(SELECT id_invoice_man FROM invoice_management_tb WHERE id_invoice = ? AND id_acc = " + get_acc_id() + " AND id_pur = " + get_id_pur_from_db(purpose_type.double_exchanging) + ") AS id_invoice_man, "
                        + "(select  user_name FROM account_tb WHERE account_tb.id_acc = exc_invoice_two_operator_tb.id_acc) AS acc, "
                        + "(select  pur_type FROM purpose_tb WHERE purpose_tb.id_pur = exc_invoice_two_operator_tb.id_pur) AS pur, "
                        + "exchanging_money_one, result_exchanging_money_one, exchanging_money_two, result_exchanging_money_two, one_rate, two_rate, "
                        + "(select  type_of_exchanging FROM exc_type_tb WHERE exc_type_tb.id_type = exc_invoice_two_operator_tb.id_type_one) AS exchange_type_one, "
                        + "(select  type_of_exchanging FROM exc_type_tb WHERE exc_type_tb.id_type = exc_invoice_two_operator_tb.id_type_two) AS exchange_type_two "
                        + "FROM exc_invoice_two_operator_tb WHERE id_invoice = ?;");

//            System.out.println(id_invoice.get(i));
                pst.setInt(1, id_invoice);
                pst.setInt(2, id_invoice);
                pst.setInt(3, id_invoice);
                rs = pst.executeQuery();

                while (rs.next()) {
                    Vector v3 = new Vector();
                    String money_type_from_sql_one = rs.getString("exchange_type_one");
                    String cus_money_type_one = money_type_from_sql_one.substring(0, 1);
                    String owner_money_type_one = money_type_from_sql_one.substring(5, 6);

                    String money_type_from_sql_two = rs.getString("exchange_type_two");
                    String cus_money_type_two = money_type_from_sql_two.substring(0, 1);
                    String owner_money_type_two = money_type_from_sql_two.substring(5, 6);

                    //to get value then set in table history
                    //Date format string is passed as an argument to the Date format object
                    SimpleDateFormat objSDF = new SimpleDateFormat("yyyy-MMM-dd  a hh:mm:ss");

                    //get date of exchanging money
                    String date_history = objSDF.format(rs.getTimestamp("invoice_man_date"));

                    v3.add(date_history);
                    v3.add(String.valueOf(rs.getInt("id_invoice_man")));
                    v3.add(rs.getString("acc"));
                    v3.add(rs.getString("pur"));

                    v3.add((!(money_type_from_sql_one.equals("S_to_B") || money_type_from_sql_one.equals("B_to_S"))
                            || !(money_type_from_sql_two.equals("S_to_B") || money_type_from_sql_two.equals("B_to_S")))
                            ? money_S_B_R_validate(type_of_money.Rial, inv_man_obj.getRial(), true) : "");
                    v3.add((!(money_type_from_sql_one.equals("B_to_R") || money_type_from_sql_one.equals("R_to_B"))
                            || !(money_type_from_sql_two.equals("B_to_R") || money_type_from_sql_two.equals("R_to_B")))
                            ? money_S_B_R_validate(type_of_money.Dollar, inv_man_obj.getDollar(), true) : "");
                    v3.add((!(money_type_from_sql_one.equals("S_to_R") || money_type_from_sql_one.equals("R_to_S"))
                            || !(money_type_from_sql_two.equals("S_to_R") || money_type_from_sql_two.equals("R_to_S")))
                            ? money_S_B_R_validate(type_of_money.Bart, inv_man_obj.getBart(), true) : "");
                    v3.add("");
                    v3.add("គេ: "
                            + rs.getString("exchanging_money_one") + " " + cus_money_type_one + "  |  យើង: -"
                            + rs.getString("result_exchanging_money_one") + " " + owner_money_type_one + "  |  អត្រា: "
                            + rs.getString("one_rate") + "  |  "
                            + money_type_from_sql_one + " | គេ: "
                            + rs.getString("exchanging_money_two") + " " + cus_money_type_two + "  |  យើង: -"
                            + rs.getString("result_exchanging_money_two") + " " + owner_money_type_two + "  |  អត្រា: "
                            + rs.getString("two_rate") + "  |  "
                            + money_type_from_sql_two);
                    v2.add(v3);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void get_exe_db_set_to_tb(int id_invoice, ArrayList<Vector> v2) {

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
                        get_id_pur_from_db(purpose_type.exchanging),
                        id_invoice);

                pst = con.prepareStatement("SELECT id_invoice, "
                        + "(SELECT id_invoice_man FROM invoice_management_tb WHERE id_invoice = ? AND id_acc = " + get_acc_id() + " AND id_pur = " + get_id_pur_from_db(purpose_type.exchanging) + ") AS id_invoice_man, "
                        + "(select  user_name FROM account_tb WHERE account_tb.id_acc = exc_invoice_tb.id_acc) AS acc, "
                        + "(select  pur_type FROM purpose_tb WHERE purpose_tb.id_pur = exc_invoice_tb.id_pur) AS pur,  "
                        + "exchanging_money, result_exchanging_money, invoice_date, exchange_rate, "
                        + "(select  type_of_exchanging FROM exc_type_tb WHERE exc_type_tb.id_type = exc_invoice_tb.id_type) AS exchange_type "
                        + "FROM exc_invoice_tb WHERE id_invoice = ?;");

//            System.out.println(id_invoice.get(i));
                pst.setInt(1, id_invoice);
                pst.setInt(2, id_invoice);
                rs = pst.executeQuery();

                while (rs.next()) {

                    Vector v3 = new Vector();
                    String money_type_from_sql = rs.getString("exchange_type");
                    String cus_money_type = money_type_from_sql.substring(0, 1);
                    String owner_money_type = money_type_from_sql.substring(5, 6);
                    //to get value then set in table history
                    //Date format string is passed as an argument to the Date format object
                    SimpleDateFormat objSDF = new SimpleDateFormat("yyyy-MMM-dd  a hh:mm:ss");

                    //get date of exchanging money
                    String date_history = objSDF.format(rs.getTimestamp("invoice_date"));

                    v3.add(date_history);
                    v3.add(String.valueOf(rs.getInt("id_invoice_man")));
                    v3.add(rs.getString("acc"));
                    v3.add(rs.getString("pur"));
                    v3.add((!(money_type_from_sql.equals("S_to_B") || money_type_from_sql.equals("B_to_S"))) ? money_S_B_R_validate(type_of_money.Rial, inv_man_obj.getRial(), true) : "");
                    v3.add((!(money_type_from_sql.equals("B_to_R") || money_type_from_sql.equals("R_to_B"))) ? money_S_B_R_validate(type_of_money.Dollar, inv_man_obj.getDollar(), true) : "");
                    v3.add((!(money_type_from_sql.equals("S_to_R") || money_type_from_sql.equals("R_to_S"))) ? money_S_B_R_validate(type_of_money.Bart, inv_man_obj.getBart(), true) : "");
                    v3.add("");
                    v3.add("គេ: "
                            + rs.getString("exchanging_money") + " " + cus_money_type + "  |  យើង: -"
                            + rs.getString("result_exchanging_money") + " " + owner_money_type + "  |  អត្រា: "
                            + rs.getString("exchange_rate") + "  |  "
                            + money_type_from_sql);
                    v2.add(v3);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
