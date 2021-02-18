/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import static UI_and_operation.account.*;
import static UI_and_operation.connection_to_ms_sql.*;
import static UI_and_operation.exchanging.R_B_validation;
import static UI_and_operation.exchanging.S_B_validation;
import static UI_and_operation.exchanging.S_R_validation;
import static UI_and_operation.purpose.*;
import static UI_and_operation.reciept.print_reciept;
import static UI_and_operation.validate_double_value.*;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Chhann_chikay
 */
public class UI_and_operation extends javax.swing.JFrame {

    //------------------------------------------------------my varaible------------------------------------------------------
//    Connection con_;
//    PreparedStatement pst_;
//    ResultSet rs_;
//type_of_exchange is also the same value type of exchange  database except not_select
    public static enum type_of_exchange {
        S_to_R, S_to_B, B_to_R, R_to_S, B_to_S, R_to_B, not_select
    }

    public static enum purpose_type {
        exchanging, to_thai, from_thai, to_province, from_province, add_total_money
    }

    public enum type_of_money {
        Rial, Dollar, Bart
    }

    private enum dialog_choose {
        Print, Delete, Close
    };

    //to store which type of exchange that user performs, by defauld selected_exchange_rate = not_select
    type_of_exchange selected_exchange_rate = type_of_exchange.not_select;
    type_of_money selected_money_type;

    //------------------------------------------------------my function------------------------------------------------------
    public int get_id_money_type_from_db(type_of_money money_type) {
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
            pst = con.prepareStatement("SELECT id_type_of_money FROM money_type_tb WHERE type_of_money = '" + money_type.toString() + "'");
            rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id_type_of_money");
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return id;
    }

    private void one_set_money_type_to_lb(String cus_money, String result_money) {
        one_money_type_lb.setText("( " + cus_money + " )");
        one_rate_type_lb.setText("( " + cus_money + " → " + result_money + " )");
        one_money_type_result_lb.setText("( " + result_money + " )");
    }

    //function that calutation exchanging money
    private void one_calculation() {

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
                        result = money_S_B_R_validate(type_of_money.Rial, String.format("%.2f", (Double) (customer_money * exchange_rate)));

                        break;

                    case S_to_B:
                        result = money_S_B_R_validate(type_of_money.Bart, String.format("%.2f", (Double) (customer_money * exchange_rate)));
                        break;

                    //do / operator when......
                    case R_to_S:
                    case B_to_S:
                        result = money_S_B_R_validate(type_of_money.Dollar, String.format("%.2f", (Double) (customer_money / exchange_rate)));
                        break;

                    //do / operator when......
                    case R_to_B:
                        result = money_S_B_R_validate(type_of_money.Bart, String.format("%.2f", (Double) (customer_money / exchange_rate)));
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

    //function that get current date
    public static Timestamp current_date() {
        long millis = System.currentTimeMillis();
        Timestamp time = new Timestamp(millis);
        return time;
    }

    private void set_history() {
        if (three_calendar_cld.getDate() != null) {
            three_calendar_cld.setBackground(Color.lightGray);
            Connection con;
            PreparedStatement pst;
            ResultSet rs;
            ArrayList<String> pur_type = new ArrayList<String>();
            ArrayList<Integer> id_invoice = new ArrayList<Integer>();
            try {
                con = DriverManager.getConnection(
                        getLocal_host(),
                        getLocal_host_user_name(),
                        getLocal_host_password()
                );
                pst = con.prepareStatement("SELECT id_invoice "
                        + "FROM invoice_management_tb "
                        + "WHERE id_acc = ?");
                pst.setInt(1, get_acc_id());
                rs = pst.executeQuery();

                if (rs.next()) {

                    //query to access
                    pst = con.prepareStatement("SELECT rial, dollar, bart FROM total_money_tb");
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        DefaultTableModel dft1 = (DefaultTableModel) three_tb_total_money.getModel();
                        dft1.setRowCount(0);
                        Vector v3 = new Vector();

                        //set to v2 all data only 1 row
                        v3.add(rs.getString("dollar"));
                        v3.add(rs.getString("bart"));
                        v3.add(rs.getString("rial"));

                        //set data to table history row
                        dft1.addRow(v3);
                    }
                    Calendar start_cal = three_calendar_cld.getCalendar();
                    Calendar start_cal2 = three_calendar_cld.getCalendar();
                    Calendar pre_7_day_from_start_cal = three_calendar_cld.getCalendar();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
                    start_cal.add(Calendar.DAY_OF_MONTH, 1);
                    String start_date = sdf.format(start_cal.getTime());
                    String start_date2 = sdf2.format(start_cal2.getTime());
                    pre_7_day_from_start_cal.add(Calendar.DAY_OF_MONTH, -7);
                    String pre_7_day_from_start_date = sdf.format(pre_7_day_from_start_cal.getTime());
                    String pre_7_day_from_start_date2 = sdf2.format(pre_7_day_from_start_cal.getTime());

                    date_history_lb.setText(start_date2 + "  to  " + pre_7_day_from_start_date2);

//                    System.out.println("start_date : " + start_date);
//                    System.out.println("pre_7_day_from_start_date : " + pre_7_day_from_start_date);
                    //query to access
                    pst = con.prepareStatement("SELECT id_invoice, pur_type "
                            + "FROM invoice_management_tb INNER JOIN purpose_tb "
                            + "ON invoice_management_tb.id_pur = purpose_tb.id_pur "
                            + "WHERE id_acc = ? AND invoice_man_date >= '" + pre_7_day_from_start_date + "' "
                            + "AND invoice_man_date <= '" + start_date + "' "
                            + "ORDER BY id_invoice_man DESC;");
                    pst.setInt(1, get_acc_id());
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        pur_type.add(rs.getString("pur_type"));
                        id_invoice.add(rs.getInt("id_invoice"));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, ex);
            }

            ArrayList<Vector> v2 = new ArrayList<Vector>();
            DefaultTableModel dft = (DefaultTableModel) three_tb_history.getModel();
            for (int i = 0; i < id_invoice.size(); i++) {

                if (pur_type.get(i).equals("exchanging")) {

                    try {
                        con = DriverManager.getConnection(
                                getLocal_host(),
                                getLocal_host_user_name(),
                                getLocal_host_password()
                        );
                        pst = con.prepareStatement("SELECT id_invoice "
                                + "FROM invoice_management_tb "
                                + "WHERE id_acc = ?");
                        pst.setInt(1, get_acc_id());
                        rs = pst.executeQuery();

                        if (rs.next()) {
                            //query to access
                            pst = con.prepareStatement("SELECT id_invoice, (select  user_name FROM account_tb WHERE account_tb.id_acc = exc_invoice_tb.id_acc) AS acc , "
                                    + "(select  pur_type FROM purpose_tb WHERE purpose_tb.id_pur = exc_invoice_tb.id_pur) AS pur,  exchanging_money, result_exchanging_money, invoice_date, exchange_rate, "
                                    + "(select  type_of_exchanging FROM exc_type_tb WHERE exc_type_tb.id_type = exc_invoice_tb.id_type) AS exchange_type "
                                    + "FROM exc_invoice_tb WHERE id_invoice = ?;");

//            System.out.println(id_invoice.get(i));
                            pst.setInt(1, id_invoice.get(i));
                            rs = pst.executeQuery();

                            //table in UI
                            //reset selected in table
                            dft.setRowCount(0);

                            while (rs.next()) {

                                Vector v3 = new Vector();
                                //to get value then set in table history

                                //Date format string is passed as an argument to the Date format object
                                SimpleDateFormat objSDF = new SimpleDateFormat("yyyy-MMM-dd  a hh:mm:ss");

                                //get date of exchanging money
                                String date_history = objSDF.format(rs.getTimestamp("invoice_date"));

                                //set to v2 all data only 1 row
                                v3.add(date_history);
                                v3.add(String.valueOf(rs.getInt("id_invoice")));
                                v3.add(rs.getString("acc"));
                                v3.add(rs.getString("pur"));
                                v3.add("គេ: "
                                        + rs.getString("exchanging_money") + " | យើង: -"
                                        + rs.getString("result_exchanging_money") + " | អត្រា: "
                                        + rs.getString("exchange_rate") + " | "
                                        + rs.getString("exchange_type"));
                                v2.add(v3);
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(this, ex);
                    }
                }
                if (pur_type.get(i).equals("add_total_money")) {

                    try {
                        con = DriverManager.getConnection(
                                getLocal_host(),
                                getLocal_host_user_name(),
                                getLocal_host_password()
                        );
                        pst = con.prepareStatement("SELECT id_invoice "
                                + "FROM invoice_management_tb "
                                + "WHERE id_acc = ?");
                        pst.setInt(1, get_acc_id());
                        rs = pst.executeQuery();

                        if (rs.next()) {
                            pst = con.prepareStatement("SELECT  id_add, (select  user_name FROM account_tb WHERE account_tb.id_acc = add_money_history_tb.id_acc) AS acc , "
                                    + "(select  pur_type FROM purpose_tb WHERE purpose_tb.id_pur = add_money_history_tb.id_pur) AS pur, add_date, add_money, type_of_money "
                                    + "FROM add_money_history_tb INNER JOIN money_type_tb "
                                    + "ON add_money_history_tb.id_type_of_money = money_type_tb.id_type_of_money"
                                    + " WHERE id_add = ? ");
                            pst.setInt(1, id_invoice.get(i));
                            rs = pst.executeQuery();

                            //table in UI
                            //reset selected in table
                            dft.setRowCount(0);

                            while (rs.next()) {

                                Vector v3 = new Vector();
                                //to get value then set in table history

                                //Date format string is passed as an argument to the Date format object
                                SimpleDateFormat objSDF = new SimpleDateFormat("yyyy-MMM-dd  a hh:mm:ss");

                                //get date of exchanging money
                                String date_history = objSDF.format(rs.getTimestamp("add_date"));

                                //set to v2 all data only 1 row
                                v3.add(date_history);
                                v3.add(String.valueOf(rs.getInt("id_add")));
                                v3.add(rs.getString("acc"));
                                v3.add(rs.getString("pur"));
                                v3.add("បន្ថែម: " + rs.getString("add_money") + " " + rs.getString("type_of_money"));
//                                    System.out.println(v3);
                                v2.add(v3);
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(this, ex);
                    }
                }

            }
            if (v2.size() != 0) {
                for (int i = 0; i < v2.size(); i++) {
                    dft.addRow(v2.get(i));
                }
            } else {
                dft.setRowCount(0);
            }
        } else {
            three_calendar_cld.setBackground(Color.red);
            JOptionPane.showMessageDialog(this, "Wrong day or month or year");
        }
    }

    private void exc_perform(Boolean is_print) {

        if (selected_exchange_rate != type_of_exchange.not_select && !one_tf_customer_result.getText().isEmpty()) {
            String result = cut_the_lastest_point(one_tf_customer_money.getText());
            switch (selected_exchange_rate) {

                case R_to_S:
                case R_to_B:
                    result = money_S_B_R_validate(type_of_money.Rial, result);

                    break;

                case S_to_R:
                case S_to_B:
                    result = money_S_B_R_validate(type_of_money.Dollar, result);
                    break;

                case B_to_R:
                case B_to_S:
                    result = money_S_B_R_validate(type_of_money.Bart, result);
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
            if (is_print) {String path = "";
                            try {
                                File file = new File("x.txt");
                                if (!file.exists()) {
                                    file.createNewFile();
                                }
                                path = file.getAbsolutePath();
                                path = path.substring(0, path.length() - 6);
//                                System.out.println("path : " + path);
                            } catch (IOException e) {
                                System.out.println("error");
                            }
                            
                print_reciept(path + "\\reciept_for_print\\exchanging.jrxml",
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

        }

    }

    //-----------------------------------------------------class call--------------------------------------------------
    exchange_rate_show exe_rate_show = new exchange_rate_show();

    /**
     * Creates new form UI_and_operation
     */
    public UI_and_operation() {
        this.setUndecorated(false);
        this.setAlwaysOnTop(true);
        this.setResizable(true);
        this.setVisible(true);
        initComponents();
        Toolkit a = Toolkit.getDefaultToolkit();
        int xSize = (int) a.getScreenSize().getWidth();
        int ySize = (int) a.getScreenSize().getHeight();
        this.setSize(xSize, ySize);

        three_calendar_cld.setDate(current_date());

        //set account user to class for waiting to use
        setAccount(five_user_name_tf.getText(), five_password_tf.getText());

        String local_host_server_name = five_local_host_server_name_tf.getText();
        String local_host_db_name = five_local_host_db_name_tf.getText();
        String local_user_name = five_local_host_user_name_tf.getText();
        String local_password_tf = five_local_host_password_tf.getText();
        setLocal_host(local_host_server_name, local_host_db_name);
        setLocal_host_user_name(local_user_name);
        setLocal_host_password(local_password_tf);

        String wifi_host = five_wifi_host_tf.getText();
        String wifi_user_name = five_wifi_host_user_name_tf.getText();
        String wifi_password_tf = five_wifi_host_password_tf.getText();
        setWifi_host(wifi_host);
        setWifi_host_user_name(wifi_user_name);
        setWifi_host_password(wifi_password_tf);

        String S_to_R = "";
        String R_to_S = "";
        String S_to_B = "";
        String B_to_S = "";
        String B_to_R = "";
        String R_to_B = "";
        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        //        System.out.println(five_local_host_tf.getText());
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //write sql query to access
            pst = con.prepareStatement("SELECT TOP 1 dollar_to_rial, rial_to_dollar, dollar_to_bart, "
                    + "bart_to_dollar, bart_to_rial, rial_to_bart "
                    + "FROM exc_rate_tb ORDER BY date_rate DESC;");

            rs = pst.executeQuery();
            while (rs.next()) {
                S_to_R = rs.getString("dollar_to_rial");
                R_to_S = rs.getString("rial_to_dollar");
                S_to_B = rs.getString("dollar_to_bart");
                B_to_S = rs.getString("bart_to_dollar");
                B_to_R = rs.getString("bart_to_rial");
                R_to_B = rs.getString("rial_to_bart");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        exc_rate exc_ra = new exc_rate();
        exc_ra.set_each_rate(type_of_exchange.S_to_R, S_to_R);
        exc_ra.set_each_rate(type_of_exchange.R_to_S, R_to_S);
        exc_ra.set_each_rate(type_of_exchange.S_to_B, S_to_B);
        exc_ra.set_each_rate(type_of_exchange.B_to_S, B_to_S);
        exc_ra.set_each_rate(type_of_exchange.B_to_R, B_to_R);
        exc_ra.set_each_rate(type_of_exchange.R_to_B, R_to_B);
        four_S_to_R_one_tf.setText(exc_ra.getS_to_R_one());
        four_S_to_R_two_tf.setText(exc_ra.getS_to_R_two());
        four_S_to_R_three_tf.setText(exc_ra.getS_to_R_three());
        four_S_to_R_four_tf.setText(exc_ra.getS_to_R_four());

        four_R_to_S_one_tf.setText(exc_ra.getR_to_S_one());
        four_R_to_S_two_tf.setText(exc_ra.getR_to_S_two());
        four_R_to_S_three_tf.setText(exc_ra.getR_to_S_three());
        four_R_to_S_four_tf.setText(exc_ra.getR_to_S_four());

        four_S_to_B_one_tf.setText(exc_ra.getS_to_B_one());
        four_S_to_B_two_tf.setText(exc_ra.getS_to_B_two());
        four_S_to_B_three_tf.setText(exc_ra.getS_to_B_three());
        four_S_to_B_four_tf.setText(exc_ra.getS_to_B_four());

        four_B_to_S_one_tf.setText(exc_ra.getB_to_S_one());
        four_B_to_S_two_tf.setText(exc_ra.getB_to_S_two());
        four_B_to_S_three_tf.setText(exc_ra.getB_to_S_three());
        four_B_to_S_four_tf.setText(exc_ra.getB_to_S_four());

        four_B_to_R_one_tf.setText(exc_ra.getB_to_R_one());
        four_B_to_R_two_tf.setText(exc_ra.getB_to_R_two());
        four_B_to_R_three_tf.setText(exc_ra.getB_to_R_three());
        four_B_to_R_four_tf.setText(exc_ra.getB_to_R_four());

        four_R_to_B_one_tf.setText(exc_ra.getR_to_B_one());
        four_R_to_B_two_tf.setText(exc_ra.getR_to_B_two());
        four_R_to_B_three_tf.setText(exc_ra.getR_to_B_three());
        four_R_to_B_four_tf.setText(exc_ra.getR_to_B_four());
//        System.out.println(exc_ra.getS_to_R_one()+ "  " + exc_ra.getS_to_R_two() + "  " + exc_ra.getS_to_R_three() + "  " + exc_ra.getS_to_R_four());

        exe_rate_show.set_rate(S_to_R, R_to_S, S_to_B, B_to_S, B_to_R, R_to_B);
        exe_rate_show.setVisible(true);

        //set name of the window next to logo
        setTitle("Exchange and Transfer money");

        //design head table  total money in balance
        three_tb_total_money.getTableHeader().setFont(new Font("Khmer OS Siemreap", Font.BOLD, 30));
        three_tb_total_money.setRowHeight(40);

        //design head table  history in balance
        three_tb_history.getTableHeader().setFont(new Font("Khmer OS Siemreap", Font.BOLD, 30));
        three_tb_history.setRowHeight(60);
        set_history();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        one_bn_S_to_R = new javax.swing.JButton();
        one_bn_S_to_B = new javax.swing.JButton();
        one_bn_B_to_R = new javax.swing.JButton();
        one_bn_R_to_S = new javax.swing.JButton();
        one_bn_B_to_S = new javax.swing.JButton();
        one_bn_R_to_B = new javax.swing.JButton();
        one_lb_operator = new javax.swing.JLabel();
        one_tf_exchange_rate = new javax.swing.JTextField();
        one_lb_equal = new javax.swing.JLabel();
        one_tf_customer_result = new javax.swing.JTextField();
        one_lb_customer_money = new javax.swing.JLabel();
        one_lb_exchange_rate = new javax.swing.JLabel();
        one_lb_customer_result = new javax.swing.JLabel();
        one_bn_finished = new javax.swing.JButton();
        one_bn_print = new javax.swing.JButton();
        one_money_type_lb = new javax.swing.JLabel();
        one_rate_type_lb = new javax.swing.JLabel();
        one_money_type_result_lb = new javax.swing.JLabel();
        one_tf_customer_money = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        two_one_tf_cus_ph_no = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        two_one_tf_cus_no = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        two_one_tf_cus_money = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        two_one_tf_service_money = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        two_one_tf_total_money = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        two_one_tf_bank = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        two_one_bn_finish = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        two_one_tf_cus_name = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        print = new javax.swing.JButton();
        two_two_bn_finish = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        two_two_year_tf = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        two_two_month_tf = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        two_two_day_tf = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        two_two_hour_tf = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        two_two_minute_tf = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        two_two_reveiver_money_tf = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        two_two_service_money_tf = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        two_two_result_money_tf = new javax.swing.JTextField();
        two_two_AM_rb = new javax.swing.JRadioButton();
        two_two_PM_rb = new javax.swing.JRadioButton();
        jLabel34 = new javax.swing.JLabel();
        two_two_reveiver_ph_no_tf = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        two_three_sender_phone_no_tf = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        two_three_receiver_phone_no_tf = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        two_three_sender_money_tf = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        two_three_service_money_tf = new javax.swing.JTextField();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jLabel19 = new javax.swing.JLabel();
        two_three_province_name_tf = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        two_three_total_money_tf = new javax.swing.JTextField();
        two_three_balance_money_tf = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        two_three_bn_finish = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        two_three_dollar_money_rb = new javax.swing.JRadioButton();
        two_three_rial_money_rb = new javax.swing.JRadioButton();
        two_three_bart_money_rb = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        two_four_receiver_phone_no_tf = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        two_four_province_name_tf = new javax.swing.JTextField();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jLabel23 = new javax.swing.JLabel();
        two_four_sender_money_tf = new javax.swing.JTextField();
        two_four_balance_money_tf = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        two_four_total_money_tf = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        two_four_bn_finish = new javax.swing.JButton();
        two_four_rial_money_rb = new javax.swing.JRadioButton();
        two_four_dollar_money_rb = new javax.swing.JRadioButton();
        two_four_bart_money_rb = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        three_tb_total_money = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        three_tb_history = new javax.swing.JTable();
        three_add_tf = new javax.swing.JTextField();
        three_rial_rb = new javax.swing.JRadioButton();
        three_dollar_rb = new javax.swing.JRadioButton();
        three_bart_rb = new javax.swing.JRadioButton();
        three_add_bn = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        three_calendar_cld = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        date_history_lb = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        four_bn_edit_exchange_rate = new javax.swing.JButton();
        four_S_to_R_four_tf = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        four_S_to_R_one_tf = new javax.swing.JTextField();
        four_S_to_R_two_tf = new javax.swing.JTextField();
        four_S_to_R_three_tf = new javax.swing.JTextField();
        four_R_to_S_one_tf = new javax.swing.JTextField();
        four_R_to_S_two_tf = new javax.swing.JTextField();
        four_R_to_S_three_tf = new javax.swing.JTextField();
        four_R_to_S_four_tf = new javax.swing.JTextField();
        four_S_to_B_one_tf = new javax.swing.JTextField();
        four_S_to_B_two_tf = new javax.swing.JTextField();
        four_S_to_B_three_tf = new javax.swing.JTextField();
        four_S_to_B_four_tf = new javax.swing.JTextField();
        four_B_to_S_one_tf = new javax.swing.JTextField();
        four_B_to_S_two_tf = new javax.swing.JTextField();
        four_B_to_S_three_tf = new javax.swing.JTextField();
        four_B_to_S_four_tf = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        four_B_to_R_one_tf = new javax.swing.JTextField();
        four_B_to_R_two_tf = new javax.swing.JTextField();
        four_B_to_R_three_tf = new javax.swing.JTextField();
        four_B_to_R_four_tf = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        four_R_to_B_four_tf = new javax.swing.JTextField();
        four_R_to_B_three_tf = new javax.swing.JTextField();
        four_R_to_B_two_tf = new javax.swing.JTextField();
        four_R_to_B_one_tf = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        five_user_name_tf = new javax.swing.JTextField();
        five_password_tf = new javax.swing.JTextField();
        five_local_host_server_name_tf = new javax.swing.JTextField();
        five_local_host_user_name_tf = new javax.swing.JTextField();
        five_local_host_password_tf = new javax.swing.JTextField();
        five_wifi_host_tf = new javax.swing.JTextField();
        five_wifi_host_user_name_tf = new javax.swing.JTextField();
        five_wifi_host_password_tf = new javax.swing.JTextField();
        five_create_acc_tf = new javax.swing.JButton();
        five_local_host_db_name_tf = new javax.swing.JTextField();

        jLabel6.setText("jLabel6");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(658, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(394, 394, 394))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(265, 265, 265)
                .addComponent(jLabel6)
                .addContainerGap(320, Short.MAX_VALUE))
        );

        jTextField4.setText("jTextField4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 48)); // NOI18N

        one_bn_S_to_R.setFont(new java.awt.Font("Tahoma", 0, 90)); // NOI18N
        one_bn_S_to_R.setText("$ → ៛");
        one_bn_S_to_R.setMaximumSize(new java.awt.Dimension(500, 200));
        one_bn_S_to_R.setMinimumSize(new java.awt.Dimension(300, 150));
        one_bn_S_to_R.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_bn_S_to_RActionPerformed(evt);
            }
        });
        one_bn_S_to_R.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_bn_S_to_RKeyPressed(evt);
            }
        });

        one_bn_S_to_B.setFont(new java.awt.Font("Tahoma", 0, 90)); // NOI18N
        one_bn_S_to_B.setText("$ → ฿");
        one_bn_S_to_B.setMaximumSize(new java.awt.Dimension(500, 200));
        one_bn_S_to_B.setMinimumSize(new java.awt.Dimension(300, 150));
        one_bn_S_to_B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_bn_S_to_BActionPerformed(evt);
            }
        });
        one_bn_S_to_B.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_bn_S_to_BKeyPressed(evt);
            }
        });

        one_bn_B_to_R.setFont(new java.awt.Font("Tahoma", 0, 90)); // NOI18N
        one_bn_B_to_R.setText("฿ → ៛");
        one_bn_B_to_R.setMaximumSize(new java.awt.Dimension(500, 200));
        one_bn_B_to_R.setMinimumSize(new java.awt.Dimension(300, 150));
        one_bn_B_to_R.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_bn_B_to_RActionPerformed(evt);
            }
        });
        one_bn_B_to_R.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_bn_B_to_RKeyPressed(evt);
            }
        });

        one_bn_R_to_S.setFont(new java.awt.Font("Tahoma", 0, 90)); // NOI18N
        one_bn_R_to_S.setText("៛ → $");
        one_bn_R_to_S.setMaximumSize(new java.awt.Dimension(500, 200));
        one_bn_R_to_S.setMinimumSize(new java.awt.Dimension(300, 150));
        one_bn_R_to_S.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_bn_R_to_SActionPerformed(evt);
            }
        });
        one_bn_R_to_S.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_bn_R_to_SKeyPressed(evt);
            }
        });

        one_bn_B_to_S.setFont(new java.awt.Font("Tahoma", 0, 90)); // NOI18N
        one_bn_B_to_S.setText("฿ → $");
        one_bn_B_to_S.setMaximumSize(new java.awt.Dimension(500, 200));
        one_bn_B_to_S.setMinimumSize(new java.awt.Dimension(300, 150));
        one_bn_B_to_S.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_bn_B_to_SActionPerformed(evt);
            }
        });
        one_bn_B_to_S.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_bn_B_to_SKeyPressed(evt);
            }
        });

        one_bn_R_to_B.setFont(new java.awt.Font("Tahoma", 0, 90)); // NOI18N
        one_bn_R_to_B.setText("៛ → ฿");
        one_bn_R_to_B.setMaximumSize(new java.awt.Dimension(500, 200));
        one_bn_R_to_B.setMinimumSize(new java.awt.Dimension(300, 150));
        one_bn_R_to_B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_bn_R_to_BActionPerformed(evt);
            }
        });
        one_bn_R_to_B.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_bn_R_to_BKeyPressed(evt);
            }
        });

        one_lb_operator.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N

        one_tf_exchange_rate.setEditable(false);
        one_tf_exchange_rate.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        one_tf_exchange_rate.setFocusable(false);
        one_tf_exchange_rate.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                one_tf_exchange_rateCaretUpdate(evt);
            }
        });
        one_tf_exchange_rate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_tf_exchange_rateActionPerformed(evt);
            }
        });
        one_tf_exchange_rate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_tf_exchange_rateKeyPressed(evt);
            }
        });

        one_lb_equal.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        one_lb_equal.setText("=");

        one_tf_customer_result.setEditable(false);
        one_tf_customer_result.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        one_tf_customer_result.setRequestFocusEnabled(false);
        one_tf_customer_result.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_tf_customer_resultActionPerformed(evt);
            }
        });

        one_lb_customer_money.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        one_lb_customer_money.setText("ទឹកប្រាក់ទទួល");

        one_lb_exchange_rate.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        one_lb_exchange_rate.setText("អត្រាប្តូរប្រាក់");

        one_lb_customer_result.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        one_lb_customer_result.setText("ទឹកប្រាក់ប្រគល់");

        one_bn_finished.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        one_bn_finished.setText("រួចរាល់");
        one_bn_finished.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_bn_finishedActionPerformed(evt);
            }
        });
        one_bn_finished.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_bn_finishedKeyPressed(evt);
            }
        });

        one_bn_print.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        one_bn_print.setText("ព្រីនវិក្កិយបត្រ");
        one_bn_print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_bn_printActionPerformed(evt);
            }
        });
        one_bn_print.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_bn_printKeyPressed(evt);
            }
        });

        one_money_type_lb.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N

        one_rate_type_lb.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N

        one_money_type_result_lb.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N

        one_tf_customer_money.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        one_tf_customer_money.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                one_tf_customer_moneyCaretUpdate(evt);
            }
        });
        one_tf_customer_money.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_tf_customer_moneyActionPerformed(evt);
            }
        });
        one_tf_customer_money.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_tf_customer_moneyKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                one_tf_customer_moneyKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                one_tf_customer_moneyKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(one_bn_R_to_S, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(one_bn_S_to_R, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(one_lb_customer_money)
                                .addGap(18, 18, 18)
                                .addComponent(one_money_type_lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(one_tf_customer_money))
                        .addGap(5, 5, 5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(one_lb_operator, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(one_lb_exchange_rate)
                                .addGap(18, 18, 18)
                                .addComponent(one_rate_type_lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(one_tf_exchange_rate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(one_lb_equal))
                    .addComponent(one_bn_B_to_S, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(one_bn_S_to_B, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(one_bn_B_to_R, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(one_bn_R_to_B, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(one_lb_customer_result)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(one_money_type_result_lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(one_tf_customer_result))
                        .addGap(50, 50, 50)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(one_bn_finished, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(one_bn_print, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(one_bn_B_to_R, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(one_bn_S_to_B, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(one_bn_S_to_R, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(one_bn_R_to_S, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(one_bn_R_to_B, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(one_bn_B_to_S, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(one_lb_customer_money)
                        .addComponent(one_money_type_lb, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(one_lb_exchange_rate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(one_lb_customer_result)
                        .addComponent(one_money_type_result_lb, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(one_rate_type_lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(one_tf_exchange_rate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(one_tf_customer_result, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(one_lb_equal)
                                .addGap(8, 8, 8)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(one_tf_customer_money, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(one_lb_operator, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(one_bn_finished, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(one_bn_print, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(145, 145, 145))
        );

        jTabbedPane1.addTab("ប្តូរប្រាក់", jPanel2);

        jTabbedPane2.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel7.setText("ឈ្មោះ");

        two_one_tf_cus_ph_no.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel8.setText("លេខបញ្ជី");

        two_one_tf_cus_no.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_one_tf_cus_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_one_tf_cus_noActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel10.setText("ចំនួនទឹកប្រាក់");

        two_one_tf_cus_money.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_one_tf_cus_money.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_one_tf_cus_moneyActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel11.setText("ថ្លៃសេវា");

        two_one_tf_service_money.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel12.setText("ទឹកប្រាក់ទទួល");

        two_one_tf_total_money.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel9.setText("ធនាគារ");

        two_one_tf_bank.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_one_tf_bank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_one_tf_bankActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        jButton9.setText("ព្រីនវិក្កិយបត្រ");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        two_one_bn_finish.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        two_one_bn_finish.setText("រួចរាល់");
        two_one_bn_finish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_one_bn_finishActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel33.setText("លេខអ្នកផ្ញើរ");

        two_one_tf_cus_name.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(two_one_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel33)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(two_one_tf_bank)
                    .addComponent(two_one_tf_cus_no, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
                    .addComponent(two_one_tf_cus_name, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(two_one_tf_cus_ph_no, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(248, 248, 248)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(two_one_tf_cus_money))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(two_one_tf_service_money)
                            .addComponent(two_one_tf_total_money, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel10))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(two_one_tf_cus_money, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(two_one_tf_bank, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9)))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(two_one_tf_cus_no, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(two_one_tf_service_money, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(two_one_tf_total_money, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel33)
                                    .addComponent(two_one_tf_cus_ph_no, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(50, 50, 50)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(two_one_tf_cus_name, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(two_one_bn_finish, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(216, 216, 216))
        );

        jTabbedPane2.addTab("ផ្ទេរប្រាក់ទៅថៃ", jPanel4);

        print.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        print.setText("ព្រីនវិក្កិយបត្រ");

        two_two_bn_finish.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        two_two_bn_finish.setText("រួចរាល់");
        two_two_bn_finish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_two_bn_finishActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel13.setText("ថ្ងៃវេរចូល");

        two_two_year_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_two_year_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_two_year_tfActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Year");

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        jLabel31.setText("-");

        two_two_month_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Month");

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        jLabel32.setText("-");

        two_two_day_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_two_day_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_two_day_tfActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        jLabel2.setText("/");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Day");

        two_two_hour_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_two_hour_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_two_hour_tfActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        jLabel1.setText(":");

        two_two_minute_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_two_minute_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_two_minute_tfActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel29.setText("Hour");

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel30.setText("Minute");

        jLabel14.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel14.setText("ចំនួនទឹកប្រាក់");

        two_two_reveiver_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_two_reveiver_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_two_reveiver_money_tfActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel16.setText("ថ្លៃសេវា");

        two_two_service_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_two_service_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_two_service_money_tfActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel15.setText("ទឹកប្រាក់ប្រគល់");

        two_two_result_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

        buttonGroup1.add(two_two_AM_rb);
        two_two_AM_rb.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        two_two_AM_rb.setText("AM");
        two_two_AM_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_two_AM_rbActionPerformed(evt);
            }
        });

        buttonGroup1.add(two_two_PM_rb);
        two_two_PM_rb.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        two_two_PM_rb.setText("PM");

        jLabel34.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel34.setText("លេខទូរស័ព្ទអ្នកទទួល");

        two_two_reveiver_ph_no_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(two_two_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(two_two_year_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel31))
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(two_two_month_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel32))
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(two_two_day_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2))
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(two_two_hour_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1))
                            .addComponent(jLabel29))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(two_two_minute_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(two_two_AM_rb)
                                    .addComponent(two_two_PM_rb)))
                            .addComponent(jLabel30))))
                .addGap(312, 312, 312)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(two_two_service_money_tf)
                    .addComponent(two_two_result_money_tf)
                    .addComponent(two_two_reveiver_money_tf)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel14)
                            .addComponent(jLabel34)
                            .addComponent(two_two_reveiver_ph_no_tf))
                        .addContainerGap())))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(two_two_year_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31)
                            .addComponent(two_two_month_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32)
                            .addComponent(two_two_day_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(two_two_hour_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(two_two_minute_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(two_two_AM_rb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(two_two_PM_rb)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(two_two_reveiver_ph_no_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(two_two_reveiver_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(two_two_service_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(two_two_result_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(two_two_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(221, 221, 221))))
        );

        jTabbedPane2.addTab("ដកប្រាក់ពីថៃ", jPanel5);

        jLabel24.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel24.setText("លេខទូរស័ព្ទអ្នកផ្ញើរ");

        two_three_sender_phone_no_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_three_sender_phone_no_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_sender_phone_no_tfActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel25.setText("លេខទូរស័ព្ទអ្នកទទួល");

        two_three_receiver_phone_no_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_three_receiver_phone_no_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_receiver_phone_no_tfActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        jLabel17.setText("ចំនួនទឹកប្រាក់");

        two_three_sender_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        jLabel18.setText("ថ្លៃសេវា");

        two_three_service_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_three_service_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_service_money_tfActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel19.setText("បើកនៅ");

        two_three_province_name_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_three_province_name_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_province_name_tfActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        jLabel20.setText("ទឹកប្រាក់ទទួល");

        two_three_total_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_three_total_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_total_money_tfActionPerformed(evt);
            }
        });

        two_three_balance_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_three_balance_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_balance_money_tfActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        jLabel21.setText("​Balance");

        two_three_bn_finish.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        two_three_bn_finish.setText("រួចរាល់");
        two_three_bn_finish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_bn_finishActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        jButton13.setText("ព្រីនវិក្កិយបត្រ");

        buttonGroup2.add(two_three_dollar_money_rb);
        two_three_dollar_money_rb.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        two_three_dollar_money_rb.setText("$");
        two_three_dollar_money_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_dollar_money_rbActionPerformed(evt);
            }
        });

        buttonGroup2.add(two_three_rial_money_rb);
        two_three_rial_money_rb.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        two_three_rial_money_rb.setText("៛");
        two_three_rial_money_rb.setPreferredSize(new java.awt.Dimension(60, 53));

        buttonGroup2.add(two_three_bart_money_rb);
        two_three_bart_money_rb.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        two_three_bart_money_rb.setText("฿");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(two_three_receiver_phone_no_tf, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(two_three_sender_phone_no_tf, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(two_three_province_name_tf)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(jLabel25)
                            .addComponent(jLabel19))
                        .addGap(0, 313, Short.MAX_VALUE)))
                .addGap(67, 67, 67)
                .addComponent(filler2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(two_three_sender_money_tf)
                            .addComponent(two_three_service_money_tf, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                            .addComponent(two_three_total_money_tf, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                            .addComponent(two_three_balance_money_tf, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addGap(18, 18, 18)
                                        .addComponent(two_three_rial_money_rb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(two_three_dollar_money_rb)
                                        .addGap(40, 40, 40)
                                        .addComponent(two_three_bart_money_rb))
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel21))
                                .addGap(0, 270, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(two_three_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(filler2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(two_three_sender_phone_no_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(two_three_receiver_phone_no_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(two_three_province_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel17)
                                    .addComponent(two_three_rial_money_rb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(two_three_dollar_money_rb)
                                    .addComponent(two_three_bart_money_rb))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(two_three_sender_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(two_three_service_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(two_three_total_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel21)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(two_three_balance_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(two_three_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(249, 249, 249))))
        );

        jTabbedPane2.addTab("ផ្ទេរប្រាក់តាមតំបន់", jPanel7);

        jLabel22.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel22.setText("លេខទូរស័ព្ទអ្នកទទួល");

        two_four_receiver_phone_no_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_four_receiver_phone_no_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_four_receiver_phone_no_tfActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel26.setText("ផ្ទេរពី");

        two_four_province_name_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_four_province_name_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_four_province_name_tfActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel23.setText("ចំនួនទឹកប្រាក់");

        two_four_sender_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_four_sender_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_four_sender_money_tfActionPerformed(evt);
            }
        });

        two_four_balance_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel27.setText("ទឹកប្រាក់ប្រគល់");

        two_four_total_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_four_total_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_four_total_money_tfActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel28.setText("Balance");

        jButton15.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        jButton15.setText("ព្រីនវិក្កិយបត្រ");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        two_four_bn_finish.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        two_four_bn_finish.setText("រួចរាល់");
        two_four_bn_finish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_four_bn_finishActionPerformed(evt);
            }
        });

        buttonGroup3.add(two_four_rial_money_rb);
        two_four_rial_money_rb.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        two_four_rial_money_rb.setText("៛");
        two_four_rial_money_rb.setPreferredSize(new java.awt.Dimension(60, 53));

        buttonGroup3.add(two_four_dollar_money_rb);
        two_four_dollar_money_rb.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        two_four_dollar_money_rb.setText("$");
        two_four_dollar_money_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_four_dollar_money_rbActionPerformed(evt);
            }
        });

        buttonGroup3.add(two_four_bart_money_rb);
        two_four_bart_money_rb.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        two_four_bart_money_rb.setText("฿");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(two_four_province_name_tf, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
                    .addComponent(two_four_receiver_phone_no_tf))
                .addGap(40, 40, 40)
                .addComponent(filler3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(187, 187, 187)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(two_four_balance_money_tf, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                    .addComponent(two_four_total_money_tf, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                    .addComponent(two_four_sender_money_tf)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addGap(18, 18, 18)
                                .addComponent(two_four_rial_money_rb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(two_four_dollar_money_rb)
                                .addGap(41, 41, 41)
                                .addComponent(two_four_bart_money_rb)))
                        .addGap(0, 144, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(two_four_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(two_four_receiver_phone_no_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(two_four_province_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(filler3, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(two_four_rial_money_rb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(two_four_dollar_money_rb)
                            .addComponent(two_four_bart_money_rb))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(two_four_sender_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(two_four_total_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel28)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(two_four_balance_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(two_four_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("ដកប្រាក់តាមតំបន់", jPanel6);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 914, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("ផ្ទេរប្រាក់", jPanel1);

        three_tb_total_money.setFont(new java.awt.Font("Serif", 0, 36)); // NOI18N
        three_tb_total_money.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "$", "B", "៛"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        three_tb_total_money.setEnabled(false);
        three_tb_total_money.setRowHeight(40);
        three_tb_total_money.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(three_tb_total_money);

        three_tb_history.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        three_tb_history.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ថ្ងៃទី", "លេខប័ណ្ណ", "ចេញប័ណ្ណដោយ", "បំណង", "detail"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        three_tb_history.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                three_tb_historyMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(three_tb_history);

        three_add_tf.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        three_add_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_add_tfActionPerformed(evt);
            }
        });
        three_add_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                three_add_tfKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                three_add_tfKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                three_add_tfKeyTyped(evt);
            }
        });

        buttonGroup4.add(three_rial_rb);
        three_rial_rb.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        three_rial_rb.setText("៛");
        three_rial_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_rial_rbActionPerformed(evt);
            }
        });

        buttonGroup4.add(three_dollar_rb);
        three_dollar_rb.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        three_dollar_rb.setText("$");
        three_dollar_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_dollar_rbActionPerformed(evt);
            }
        });

        buttonGroup4.add(three_bart_rb);
        three_bart_rb.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        three_bart_rb.setText("฿");
        three_bart_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_bart_rbActionPerformed(evt);
            }
        });

        three_add_bn.setText("ADD");
        three_add_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_add_bnActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Khmer OS Battambang", 0, 36)); // NOI18N
        jLabel35.setText("ប្រវត្តិប្រតិបត្តិការ");

        jLabel47.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel47.setText("Add to total");

        three_calendar_cld.setDateFormatString("d-M-yyyy");
        three_calendar_cld.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton1.setText("Refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        date_history_lb.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(three_add_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jLabel47)
                                        .addGap(18, 18, 18)
                                        .addComponent(three_rial_rb)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(three_dollar_rb)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(three_bart_rb)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(three_add_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addGap(18, 18, 18)
                                .addComponent(three_calendar_cld, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addGap(60, 60, 60)
                                .addComponent(date_history_lb, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE))))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(three_rial_rb)
                    .addComponent(three_dollar_rb)
                    .addComponent(three_bart_rb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(three_add_tf, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(three_add_bn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(date_history_lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(three_calendar_cld, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Banlance", jPanel8);

        four_bn_edit_exchange_rate.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        four_bn_edit_exchange_rate.setText("save");
        four_bn_edit_exchange_rate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_bn_edit_exchange_rateActionPerformed(evt);
            }
        });

        four_S_to_R_four_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_S_to_R_four_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_S_to_R_four_tf.setText("0");
        four_S_to_R_four_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_S_to_R_four_tfActionPerformed(evt);
            }
        });
        four_S_to_R_four_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_S_to_R_four_tfKeyTyped(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel40.setText(".");

        four_S_to_R_one_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_S_to_R_one_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_S_to_R_one_tf.setText("4");
        four_S_to_R_one_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_S_to_R_one_tfActionPerformed(evt);
            }
        });
        four_S_to_R_one_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_S_to_R_one_tfKeyTyped(evt);
            }
        });

        four_S_to_R_two_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_S_to_R_two_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_S_to_R_two_tf.setText("0");
        four_S_to_R_two_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_S_to_R_two_tfActionPerformed(evt);
            }
        });
        four_S_to_R_two_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_S_to_R_two_tfKeyTyped(evt);
            }
        });

        four_S_to_R_three_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_S_to_R_three_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_S_to_R_three_tf.setText("0");
        four_S_to_R_three_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_S_to_R_three_tfActionPerformed(evt);
            }
        });
        four_S_to_R_three_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_S_to_R_three_tfKeyTyped(evt);
            }
        });

        four_R_to_S_one_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_R_to_S_one_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_R_to_S_one_tf.setText("4");
        four_R_to_S_one_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_R_to_S_one_tfActionPerformed(evt);
            }
        });
        four_R_to_S_one_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_R_to_S_one_tfKeyTyped(evt);
            }
        });

        four_R_to_S_two_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_R_to_S_two_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_R_to_S_two_tf.setText("0");
        four_R_to_S_two_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_R_to_S_two_tfActionPerformed(evt);
            }
        });
        four_R_to_S_two_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_R_to_S_two_tfKeyTyped(evt);
            }
        });

        four_R_to_S_three_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_R_to_S_three_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_R_to_S_three_tf.setText("1");
        four_R_to_S_three_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_R_to_S_three_tfActionPerformed(evt);
            }
        });
        four_R_to_S_three_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_R_to_S_three_tfKeyTyped(evt);
            }
        });

        four_R_to_S_four_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_R_to_S_four_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_R_to_S_four_tf.setText("0");
        four_R_to_S_four_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_R_to_S_four_tfActionPerformed(evt);
            }
        });
        four_R_to_S_four_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_R_to_S_four_tfKeyTyped(evt);
            }
        });

        four_S_to_B_one_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_S_to_B_one_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_S_to_B_one_tf.setText("3");
        four_S_to_B_one_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_S_to_B_one_tfActionPerformed(evt);
            }
        });
        four_S_to_B_one_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_S_to_B_one_tfKeyTyped(evt);
            }
        });

        four_S_to_B_two_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_S_to_B_two_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_S_to_B_two_tf.setText("0");
        four_S_to_B_two_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_S_to_B_two_tfActionPerformed(evt);
            }
        });
        four_S_to_B_two_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_S_to_B_two_tfKeyTyped(evt);
            }
        });

        four_S_to_B_three_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_S_to_B_three_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_S_to_B_three_tf.setText("0");
        four_S_to_B_three_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_S_to_B_three_tfActionPerformed(evt);
            }
        });
        four_S_to_B_three_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_S_to_B_three_tfKeyTyped(evt);
            }
        });

        four_S_to_B_four_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_S_to_B_four_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_S_to_B_four_tf.setText("0");
        four_S_to_B_four_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_S_to_B_four_tfActionPerformed(evt);
            }
        });
        four_S_to_B_four_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_S_to_B_four_tfKeyTyped(evt);
            }
        });

        four_B_to_S_one_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_B_to_S_one_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_B_to_S_one_tf.setText("3");
        four_B_to_S_one_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_B_to_S_one_tfActionPerformed(evt);
            }
        });
        four_B_to_S_one_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_B_to_S_one_tfKeyTyped(evt);
            }
        });

        four_B_to_S_two_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_B_to_S_two_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_B_to_S_two_tf.setText("0");
        four_B_to_S_two_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_B_to_S_two_tfActionPerformed(evt);
            }
        });
        four_B_to_S_two_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_B_to_S_two_tfKeyTyped(evt);
            }
        });

        four_B_to_S_three_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_B_to_S_three_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_B_to_S_three_tf.setText("1");
        four_B_to_S_three_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_B_to_S_three_tfActionPerformed(evt);
            }
        });
        four_B_to_S_three_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_B_to_S_three_tfKeyTyped(evt);
            }
        });

        four_B_to_S_four_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_B_to_S_four_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_B_to_S_four_tf.setText("0");
        four_B_to_S_four_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_B_to_S_four_tfActionPerformed(evt);
            }
        });
        four_B_to_S_four_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_B_to_S_four_tfKeyTyped(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel41.setText(".");

        four_B_to_R_one_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_B_to_R_one_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_B_to_R_one_tf.setText("1");
        four_B_to_R_one_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_B_to_R_one_tfActionPerformed(evt);
            }
        });
        four_B_to_R_one_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_B_to_R_one_tfKeyTyped(evt);
            }
        });

        four_B_to_R_two_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_B_to_R_two_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_B_to_R_two_tf.setText("3");
        four_B_to_R_two_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_B_to_R_two_tfActionPerformed(evt);
            }
        });
        four_B_to_R_two_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_B_to_R_two_tfKeyTyped(evt);
            }
        });

        four_B_to_R_three_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_B_to_R_three_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_B_to_R_three_tf.setText("0");
        four_B_to_R_three_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_B_to_R_three_tfActionPerformed(evt);
            }
        });
        four_B_to_R_three_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_B_to_R_three_tfKeyTyped(evt);
            }
        });

        four_B_to_R_four_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_B_to_R_four_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_B_to_R_four_tf.setText("0");
        four_B_to_R_four_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_B_to_R_four_tfActionPerformed(evt);
            }
        });
        four_B_to_R_four_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_B_to_R_four_tfKeyTyped(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel42.setText(".");

        four_R_to_B_four_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_R_to_B_four_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_R_to_B_four_tf.setText("0");
        four_R_to_B_four_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_R_to_B_four_tfActionPerformed(evt);
            }
        });
        four_R_to_B_four_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_R_to_B_four_tfKeyTyped(evt);
            }
        });

        four_R_to_B_three_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_R_to_B_three_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_R_to_B_three_tf.setText("1");
        four_R_to_B_three_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_R_to_B_three_tfActionPerformed(evt);
            }
        });
        four_R_to_B_three_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_R_to_B_three_tfKeyTyped(evt);
            }
        });

        four_R_to_B_two_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_R_to_B_two_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_R_to_B_two_tf.setText("3");
        four_R_to_B_two_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_R_to_B_two_tfActionPerformed(evt);
            }
        });
        four_R_to_B_two_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_R_to_B_two_tfKeyTyped(evt);
            }
        });

        four_R_to_B_one_tf.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        four_R_to_B_one_tf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        four_R_to_B_one_tf.setText("1");
        four_R_to_B_one_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                four_R_to_B_one_tfActionPerformed(evt);
            }
        });
        four_R_to_B_one_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                four_R_to_B_one_tfKeyTyped(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel43.setText(".");

        jLabel36.setFont(new java.awt.Font("Khmer OS Battambang", 1, 24)); // NOI18N
        jLabel36.setText("អត្រាប្តូរប្រាក់");

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel37.setText("$ → ៛");

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel38.setText("$ → ฿");

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel39.setText("฿ → ៛");

        jLabel44.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel44.setText("៛ → ฿");

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel45.setText("฿ → $");

        jLabel46.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel46.setText("៛ → $");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(424, 424, 424)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel39)
                            .addComponent(jLabel38)
                            .addComponent(jLabel37))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(four_B_to_R_one_tf)
                                    .addComponent(four_S_to_B_one_tf, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(four_B_to_R_two_tf)
                                    .addComponent(four_S_to_B_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_S_to_B_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_S_to_B_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(four_B_to_R_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_B_to_R_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(four_S_to_R_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(four_S_to_R_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(four_S_to_R_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(four_S_to_R_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(97, 97, 97)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel45)
                                    .addComponent(jLabel46))
                                .addGap(31, 31, 31)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(four_R_to_S_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(four_R_to_S_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                        .addComponent(four_B_to_S_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_B_to_S_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(four_R_to_S_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_R_to_S_four_tf))
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_B_to_S_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_B_to_S_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel44)
                                .addGap(31, 31, 31)
                                .addComponent(four_R_to_B_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(four_R_to_B_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(four_R_to_B_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(four_R_to_B_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(782, 782, 782)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(four_bn_edit_exchange_rate, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(277, 277, 277)))
                .addContainerGap(145, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(206, 206, 206)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(four_R_to_S_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(four_R_to_S_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(four_R_to_S_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(four_R_to_S_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(four_B_to_S_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(four_B_to_S_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(four_B_to_S_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(four_B_to_S_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel45))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel41))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(four_S_to_R_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(four_S_to_R_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(four_S_to_R_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(four_S_to_R_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(four_S_to_B_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(four_S_to_B_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel38))
                                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(four_S_to_B_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(four_S_to_B_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel42))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel40)
                                .addGap(41, 41, 41)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(four_B_to_R_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(four_B_to_R_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(four_B_to_R_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel39))
                                    .addComponent(four_B_to_R_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(four_R_to_B_four_tf, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(four_R_to_B_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(four_R_to_B_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(four_R_to_B_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel44)))))))
                .addGap(69, 69, 69)
                .addComponent(four_bn_edit_exchange_rate, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(196, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("អត្រាប្តូរប្រាក់", jPanel9);

        five_user_name_tf.setEditable(false);
        five_user_name_tf.setText("chi");
        five_user_name_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                five_user_name_tfActionPerformed(evt);
            }
        });

        five_password_tf.setEditable(false);
        five_password_tf.setText("1234");

        five_local_host_server_name_tf.setText("kay");
        five_local_host_server_name_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                five_local_host_server_name_tfActionPerformed(evt);
            }
        });

        five_local_host_user_name_tf.setEditable(false);
        five_local_host_user_name_tf.setText("admin");
        five_local_host_user_name_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                five_local_host_user_name_tfActionPerformed(evt);
            }
        });

        five_local_host_password_tf.setEditable(false);
        five_local_host_password_tf.setText("1234");

        five_wifi_host_tf.setEditable(false);

        five_wifi_host_user_name_tf.setEditable(false);
        five_wifi_host_user_name_tf.setText("ChhannChikay");
        five_wifi_host_user_name_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                five_wifi_host_user_name_tfActionPerformed(evt);
            }
        });

        five_wifi_host_password_tf.setEditable(false);
        five_wifi_host_password_tf.setText("1234");

        five_create_acc_tf.setText("create account");
        five_create_acc_tf.setEnabled(false);
        five_create_acc_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                five_create_acc_tfActionPerformed(evt);
            }
        });

        five_local_host_db_name_tf.setText("exchange_transfer_sm");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(431, 431, 431)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(five_create_acc_tf)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(five_wifi_host_password_tf)
                        .addComponent(five_wifi_host_user_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(five_password_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(five_user_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(five_local_host_password_tf)
                        .addComponent(five_local_host_user_name_tf, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                        .addComponent(five_wifi_host_tf)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addComponent(five_local_host_server_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(five_local_host_db_name_tf))))
                .addContainerGap(596, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addComponent(five_user_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(five_password_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(five_create_acc_tf)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(five_local_host_server_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(five_local_host_db_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(five_local_host_user_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(five_local_host_password_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88)
                .addComponent(five_wifi_host_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(five_wifi_host_user_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(five_wifi_host_password_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(411, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("connection", jPanel10);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 904, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void five_user_name_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_five_user_name_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_five_user_name_tfActionPerformed

    private void two_four_dollar_money_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_four_dollar_money_rbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_four_dollar_money_rbActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void two_four_total_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_four_total_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_four_total_money_tfActionPerformed

    private void two_four_sender_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_four_sender_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_four_sender_money_tfActionPerformed

    private void two_four_province_name_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_four_province_name_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_four_province_name_tfActionPerformed

    private void two_four_receiver_phone_no_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_four_receiver_phone_no_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_four_receiver_phone_no_tfActionPerformed

    private void two_three_dollar_money_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_dollar_money_rbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_dollar_money_rbActionPerformed

    private void two_three_bn_finishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_bn_finishActionPerformed

    }//GEN-LAST:event_two_three_bn_finishActionPerformed

    private void two_three_balance_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_balance_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_balance_money_tfActionPerformed

    private void two_three_total_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_total_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_total_money_tfActionPerformed

    private void two_three_province_name_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_province_name_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_province_name_tfActionPerformed

    private void two_three_service_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_service_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_service_money_tfActionPerformed

    private void two_three_receiver_phone_no_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_receiver_phone_no_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_receiver_phone_no_tfActionPerformed

    private void two_three_sender_phone_no_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_sender_phone_no_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_sender_phone_no_tfActionPerformed

    private void two_two_AM_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_two_AM_rbActionPerformed

    }//GEN-LAST:event_two_two_AM_rbActionPerformed

    private void two_two_service_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_two_service_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_two_service_money_tfActionPerformed

    private void two_two_reveiver_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_two_reveiver_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_two_reveiver_money_tfActionPerformed

    private void two_two_minute_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_two_minute_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_two_minute_tfActionPerformed

    private void two_two_hour_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_two_hour_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_two_hour_tfActionPerformed

    private void two_two_day_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_two_day_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_two_day_tfActionPerformed

    private void two_two_year_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_two_year_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_two_year_tfActionPerformed

    private void two_two_bn_finishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_two_bn_finishActionPerformed

    }//GEN-LAST:event_two_two_bn_finishActionPerformed

    private void two_one_bn_finishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_bn_finishActionPerformed

    }//GEN-LAST:event_two_one_bn_finishActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void two_one_tf_bankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_tf_bankActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_one_tf_bankActionPerformed

    private void two_one_tf_cus_moneyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_tf_cus_moneyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_one_tf_cus_moneyActionPerformed

    private void two_one_tf_cus_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_tf_cus_noActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_one_tf_cus_noActionPerformed

    private void one_bn_printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_bn_printActionPerformed
        exc_perform(true);
        set_history();
    }//GEN-LAST:event_one_bn_printActionPerformed

    private void one_bn_finishedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_bn_finishedActionPerformed
        exc_perform(false);
        set_history();
    }//GEN-LAST:event_one_bn_finishedActionPerformed

    private void one_tf_customer_resultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_tf_customer_resultActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_customer_resultActionPerformed

    private void one_tf_exchange_rateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_tf_exchange_rateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_exchange_rateActionPerformed

    private void one_tf_exchange_rateCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_one_tf_exchange_rateCaretUpdate
        one_calculation();
    }//GEN-LAST:event_one_tf_exchange_rateCaretUpdate

    private void one_bn_R_to_BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_bn_R_to_BActionPerformed
        one_lb_operator.setText("/");
        one_set_money_type_to_lb("៛", "฿");
        //store user choose type of exchange in selected_exchange_rate
        selected_exchange_rate = type_of_exchange.R_to_B;

        //get exchange rate from fourth tobbet and set in exchange rate tf in first tobbet
        one_tf_exchange_rate.setText(four_R_to_B_one_tf.getText() + four_R_to_B_two_tf.getText() + four_R_to_B_three_tf.getText() + "." + four_R_to_B_four_tf.getText());
        one_bn_S_to_R.setEnabled(true);
        one_bn_S_to_B.setEnabled(true);
        one_bn_B_to_R.setEnabled(true);
        one_bn_R_to_S.setEnabled(true);
        one_bn_B_to_S.setEnabled(true);
        one_bn_R_to_B.setEnabled(false);
    }//GEN-LAST:event_one_bn_R_to_BActionPerformed

    private void one_bn_B_to_SActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_bn_B_to_SActionPerformed
        one_lb_operator.setText("/");
        one_set_money_type_to_lb("฿", "$");
        //store user choose type of exchange in selected_exchange_rate
        selected_exchange_rate = type_of_exchange.B_to_S;

        //get exchange rate from fourth tobbet and set in exchange rate tf in first tobbet
        one_tf_exchange_rate.setText(four_B_to_S_one_tf.getText() + four_B_to_S_two_tf.getText() + "." + four_B_to_S_three_tf.getText() + four_B_to_S_four_tf.getText());
        one_bn_S_to_R.setEnabled(true);
        one_bn_S_to_B.setEnabled(true);
        one_bn_B_to_R.setEnabled(true);
        one_bn_R_to_S.setEnabled(true);
        one_bn_B_to_S.setEnabled(false);
        one_bn_R_to_B.setEnabled(true);
    }//GEN-LAST:event_one_bn_B_to_SActionPerformed

    private void one_bn_R_to_SActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_bn_R_to_SActionPerformed
        one_lb_operator.setText("/");
        one_set_money_type_to_lb("៛", "$");
        //store user choose type of exchange in selected_exchange_rate
        selected_exchange_rate = type_of_exchange.R_to_S;

        //get exchange rate from fourth tobbet and set in exchange rate tf in first tobbet
        one_tf_exchange_rate.setText(four_R_to_S_one_tf.getText() + four_R_to_S_two_tf.getText() + four_R_to_S_three_tf.getText() + four_R_to_S_four_tf.getText());
        one_bn_S_to_R.setEnabled(true);
        one_bn_S_to_B.setEnabled(true);
        one_bn_B_to_R.setEnabled(true);
        one_bn_R_to_S.setEnabled(false);
        one_bn_B_to_S.setEnabled(true);
        one_bn_R_to_B.setEnabled(true);
    }//GEN-LAST:event_one_bn_R_to_SActionPerformed

    private void one_bn_B_to_RActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_bn_B_to_RActionPerformed
        one_lb_operator.setText("x");
        one_set_money_type_to_lb("฿", "៛");
        //store user choose type of exchange in selected_exchange_rate
        selected_exchange_rate = type_of_exchange.B_to_R;

        //get exchange rate from fourth tobbet and set in exchange rate tf in first tobbet
        one_tf_exchange_rate.setText(four_B_to_R_one_tf.getText() + four_B_to_R_two_tf.getText() + four_B_to_R_three_tf.getText() + "." + four_B_to_R_four_tf.getText());

        one_bn_S_to_R.setEnabled(true);
        one_bn_S_to_B.setEnabled(true);
        one_bn_B_to_R.setEnabled(false);
        one_bn_R_to_S.setEnabled(true);
        one_bn_B_to_S.setEnabled(true);
        one_bn_R_to_B.setEnabled(true);
    }//GEN-LAST:event_one_bn_B_to_RActionPerformed

    private void one_bn_S_to_BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_bn_S_to_BActionPerformed

        one_lb_operator.setText("x");
        one_set_money_type_to_lb("$", "฿");

        //store user choose type of exchange in selected_exchange_rate
        selected_exchange_rate = type_of_exchange.S_to_B;

        //get exchange rate from fourth tobbet and set in exchange rate tf in first tobbet
        one_tf_exchange_rate.setText(four_S_to_B_one_tf.getText() + four_S_to_B_two_tf.getText() + "." + four_S_to_B_three_tf.getText() + four_S_to_B_four_tf.getText());
        one_bn_S_to_R.setEnabled(true);
        one_bn_S_to_B.setEnabled(false);
        one_bn_B_to_R.setEnabled(true);
        one_bn_R_to_S.setEnabled(true);
        one_bn_B_to_S.setEnabled(true);
        one_bn_R_to_B.setEnabled(true);
    }//GEN-LAST:event_one_bn_S_to_BActionPerformed

    private void one_bn_S_to_RActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_bn_S_to_RActionPerformed

        one_lb_operator.setText("x");
        one_set_money_type_to_lb("$", "៛");
        //store user choose type of exchange in selected_exchange_rate
        selected_exchange_rate = type_of_exchange.S_to_R;

        //get exchange rate from fourth tobbet and set in exchange rate tf in first tobbet
        one_tf_exchange_rate.setText(four_S_to_R_one_tf.getText() + four_S_to_R_two_tf.getText() + four_S_to_R_three_tf.getText() + four_S_to_R_four_tf.getText());
        one_bn_S_to_R.setEnabled(false);
        one_bn_S_to_B.setEnabled(true);
        one_bn_B_to_R.setEnabled(true);
        one_bn_R_to_S.setEnabled(true);
        one_bn_B_to_S.setEnabled(true);
        one_bn_R_to_B.setEnabled(true);
    }//GEN-LAST:event_one_bn_S_to_RActionPerformed

    private void two_four_bn_finishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_four_bn_finishActionPerformed

    }//GEN-LAST:event_two_four_bn_finishActionPerformed


    private void five_local_host_user_name_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_five_local_host_user_name_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_five_local_host_user_name_tfActionPerformed

    private void five_wifi_host_user_name_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_five_wifi_host_user_name_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_five_wifi_host_user_name_tfActionPerformed

    private void five_create_acc_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_five_create_acc_tfActionPerformed
        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //write sql query to access
            pst = con.prepareStatement("insert into account_tb (user_name, password) "
                    + "values( ?, ?);");

            //set value to ?
            pst.setString(1, "chi");
            pst.setString(2, "1234");
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_five_create_acc_tfActionPerformed

    private void five_local_host_server_name_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_five_local_host_server_name_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_five_local_host_server_name_tfActionPerformed

    private void one_tf_customer_moneyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_tf_customer_moneyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_customer_moneyActionPerformed

    private void one_tf_customer_moneyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_customer_moneyKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(one_tf_customer_money.getText())))) {
            evt.consume();
        }
    }//GEN-LAST:event_one_tf_customer_moneyKeyTyped

    private void one_tf_customer_moneyCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_one_tf_customer_moneyCaretUpdate
        one_calculation();
    }//GEN-LAST:event_one_tf_customer_moneyCaretUpdate

    private void one_tf_customer_moneyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_customer_moneyKeyReleased

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(one_tf_customer_money.getText())))) {
            evt.consume();
        } else {
            if (!one_tf_customer_money.getText().isEmpty()) {
                one_tf_customer_money.setFocusable(false);
                String str = one_tf_customer_money.getText();
                one_tf_customer_money.setText("");
                one_tf_customer_money.setText(add_cuot_to_money(add_zero_in_front_of_point(str)));
                one_tf_customer_money.setFocusable(true);
                one_tf_customer_money.requestFocus();
            }
        }
    }//GEN-LAST:event_one_tf_customer_moneyKeyReleased

    private void one_tf_customer_moneyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_customer_moneyKeyPressed

        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_ENTER:
                one_tf_exchange_rate.requestFocus();
                break;
            case KeyEvent.VK_UP:
                one_bn_R_to_S.requestFocus();
                break;
        }
    }//GEN-LAST:event_one_tf_customer_moneyKeyPressed

    private void one_tf_exchange_rateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_exchange_rateKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                one_bn_B_to_S.requestFocus();
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_ENTER:
                one_bn_finished.requestFocus();
                break;
            case KeyEvent.VK_LEFT:
                one_tf_customer_money.requestFocus();
                break;
            case KeyEvent.VK_DOWN:
                break;
        }
    }//GEN-LAST:event_one_tf_exchange_rateKeyPressed

    private void one_bn_finishedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_bn_finishedKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                one_bn_R_to_B.requestFocus();
                break;
            case KeyEvent.VK_ENTER:
                one_bn_finished.doClick();
                break;
            case KeyEvent.VK_RIGHT:
                one_bn_print.requestFocus();
                break;
            case KeyEvent.VK_LEFT:
                one_tf_exchange_rate.requestFocus();
                break;
            case KeyEvent.VK_DOWN:
                break;
        }
    }//GEN-LAST:event_one_bn_finishedKeyPressed

    private void one_bn_printKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_bn_printKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                one_bn_R_to_B.requestFocus();
                break;
            case KeyEvent.VK_ENTER:
                one_bn_print.doClick();
                break;
            case KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_LEFT:
                one_bn_finished.requestFocus();
                break;
            case KeyEvent.VK_DOWN:
                break;
        }
    }//GEN-LAST:event_one_bn_printKeyPressed

    private void one_bn_R_to_SKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_bn_R_to_SKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                one_bn_S_to_R.requestFocus();
                break;
            case KeyEvent.VK_ENTER:
                one_bn_R_to_S.doClick();
                break;
            case KeyEvent.VK_RIGHT:
                one_bn_B_to_S.requestFocus();
                break;
            case KeyEvent.VK_LEFT:
                one_bn_B_to_S.requestFocus();
                break;
            case KeyEvent.VK_DOWN:
                one_tf_customer_money.requestFocus();
                break;
        }
    }//GEN-LAST:event_one_bn_R_to_SKeyPressed

    private void one_bn_B_to_SKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_bn_B_to_SKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                one_bn_S_to_B.requestFocus();
                break;
            case KeyEvent.VK_ENTER:
                one_bn_B_to_S.doClick();
                break;
            case KeyEvent.VK_RIGHT:
                one_bn_R_to_B.requestFocus();
                break;
            case KeyEvent.VK_LEFT:
                one_bn_R_to_S.requestFocus();
                break;
            case KeyEvent.VK_DOWN:
                one_tf_exchange_rate.requestFocus();
                break;
        }
    }//GEN-LAST:event_one_bn_B_to_SKeyPressed

    private void one_bn_R_to_BKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_bn_R_to_BKeyPressed

        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                one_bn_B_to_R.requestFocus();
                break;
            case KeyEvent.VK_ENTER:
                one_bn_R_to_B.doClick();
                break;
            case KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_LEFT:
                one_bn_B_to_S.requestFocus();
                break;
            case KeyEvent.VK_DOWN:
                one_bn_finished.requestFocus();
                break;
        }
    }//GEN-LAST:event_one_bn_R_to_BKeyPressed

    private void one_bn_S_to_RKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_bn_S_to_RKeyPressed

        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                break;
            case KeyEvent.VK_ENTER:
                one_bn_S_to_R.doClick();
                break;
            case KeyEvent.VK_RIGHT:
                one_bn_S_to_B.requestFocus();
                break;
            case KeyEvent.VK_LEFT:
                one_bn_finished.requestFocus();
                break;
            case KeyEvent.VK_DOWN:
                one_bn_R_to_S.requestFocus();
                break;
        }

    }//GEN-LAST:event_one_bn_S_to_RKeyPressed

    private void one_bn_S_to_BKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_bn_S_to_BKeyPressed

        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                break;
            case KeyEvent.VK_ENTER:
                one_bn_S_to_B.doClick();
                break;
            case KeyEvent.VK_RIGHT:
                one_bn_B_to_R.requestFocus();
                break;
            case KeyEvent.VK_LEFT:
                one_bn_S_to_R.requestFocus();
                break;
            case KeyEvent.VK_DOWN:
                one_bn_B_to_S.requestFocus();
                break;
        }

    }//GEN-LAST:event_one_bn_S_to_BKeyPressed

    private void one_bn_B_to_RKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_bn_B_to_RKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                break;
            case KeyEvent.VK_ENTER:
                one_bn_B_to_R.doClick();
                break;
            case KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_LEFT:
                one_bn_S_to_B.requestFocus();
                break;
            case KeyEvent.VK_DOWN:
                one_bn_R_to_B.requestFocus();
                break;
        }

    }//GEN-LAST:event_one_bn_B_to_RKeyPressed

    private void three_rial_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_rial_rbActionPerformed
        selected_money_type = type_of_money.Rial;
    }//GEN-LAST:event_three_rial_rbActionPerformed

    private void three_add_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_add_bnActionPerformed
        if ((three_rial_rb.isSelected() || three_dollar_rb.isSelected() || three_bart_rb.isSelected()) && !three_add_tf.getText().isEmpty()) {

            int lastinsertid = -1;
            Connection con;
            PreparedStatement pst;
            ResultSet rs;
            try {
                con = DriverManager.getConnection(
                        getLocal_host(),
                        getLocal_host_user_name(),
                        getLocal_host_password()
                );

                //write sql query to access
                pst = con.prepareStatement("insert into add_money_history_tb (add_date, add_money, id_type_of_money, id_acc, id_pur)"
                        + "values(?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);

                Timestamp cur_date = current_date();

                //set value to ? in query
                pst.setTimestamp(1, cur_date);
                pst.setString(2, three_add_tf.getText());
                pst.setInt(3, get_id_money_type_from_db(selected_money_type));
                pst.setInt(4, get_acc_id());
                pst.setInt(5, get_id_pur_from_db(purpose_type.add_total_money.toString()));
                pst.executeUpdate();
                ResultSet generatekey = pst.getGeneratedKeys();
                if (generatekey.next()) {
                    lastinsertid = generatekey.getInt(1);
                }

                //write sql query to access
                pst = con.prepareStatement("insert into invoice_management_tb (id_invoice, id_acc, id_pur, invoice_man_date)"
                        + "values(? , ?, ?, ?);");

                //set value to ? in query
                pst.setInt(1, lastinsertid);
                pst.setInt(2, get_acc_id());
                pst.setInt(3, get_id_pur_from_db(UI_and_operation.purpose_type.add_total_money.toString()));
                pst.setTimestamp(4, cur_date);
                pst.executeUpdate();

                String add_money = "";
                String money_type = "";

                //query to access
                pst = con.prepareStatement("SELECT add_money, type_of_money "
                        + "FROM add_money_history_tb INNER JOIN money_type_tb ON add_money_history_tb.id_type_of_money = money_type_tb.id_type_of_money "
                        + "where id_add = ? "
                        + "AND id_acc = ? "
                        + "AND id_pur = ?;");
                pst.setInt(1, lastinsertid);
                pst.setInt(2, get_acc_id());
                pst.setInt(3, get_id_pur_from_db(UI_and_operation.purpose_type.add_total_money.toString()));
                rs = pst.executeQuery();
                while (rs.next()) {
                    add_money = rs.getString("add_money");
                    money_type = rs.getString("type_of_money");
                }

                String Rial = "";
                String Dollar = "";
                String Bart = "";
                pst = con.prepareStatement("SELECT rial, dollar, bart FROM total_money_tb");
                rs = pst.executeQuery();
                while (rs.next()) {
                    //set to v2 all data only 1 row
                    Rial = rs.getString("rial");
                    Dollar = rs.getString("dollar");
                    Bart = rs.getString("bart");
                }
                switch (money_type) {
                    case "Rial":
                        pst = con.prepareStatement("UPDATE total_money_tb SET rial = ? WHERE id_acc = ?;");
                        pst.setString(1, money_S_B_R_validate(type_of_money.Rial,
                                String.valueOf(Double.parseDouble(clear_cvot(Rial)) + Double.parseDouble(clear_cvot(add_money)))));
                        pst.setInt(2, get_acc_id());
                        pst.executeUpdate();
                        break;
                    case "Dollar":
                        //write sql query to access
                        pst = con.prepareStatement("UPDATE total_money_tb SET dollar = ? WHERE id_acc = ?;");
                        pst.setString(1, money_S_B_R_validate(type_of_money.Dollar,
                                String.valueOf(Double.parseDouble(clear_cvot(Dollar)) + Double.parseDouble(clear_cvot(add_money)))));
                        pst.setInt(2, get_acc_id());
                        pst.executeUpdate();
                        break;
                    case "Bart":
                        //write sql query to access
                        pst = con.prepareStatement("UPDATE total_money_tb SET bart = ? WHERE id_acc = ?;");
                        pst.setString(1, money_S_B_R_validate(type_of_money.Bart,
                                String.valueOf(Double.parseDouble(clear_cvot(Bart)) + Double.parseDouble(clear_cvot(add_money)))));
                        pst.setInt(2, get_acc_id());
                        pst.executeUpdate();
                        break;
                    default:
                        System.out.println("Error");
                }
            } catch (SQLException ex) {
                System.err.println(ex);
            }
            three_add_tf.setText("");
            buttonGroup4.clearSelection();
            set_history();
        }
    }//GEN-LAST:event_three_add_bnActionPerformed

    private void three_dollar_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_dollar_rbActionPerformed
        selected_money_type = type_of_money.Dollar;
    }//GEN-LAST:event_three_dollar_rbActionPerformed

    private void three_bart_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_bart_rbActionPerformed
        selected_money_type = type_of_money.Bart;
    }//GEN-LAST:event_three_bart_rbActionPerformed

    private void three_add_tfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_three_add_tfKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_three_add_tfKeyPressed

    private void three_add_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_three_add_tfKeyReleased
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(three_add_tf.getText())))) {
            evt.consume();
        } else {
            three_add_tf.setFocusable(false);
            String str = three_add_tf.getText();
//            System.out.println(one_tf_customer_money.getText());
            three_add_tf.setText("");
            three_add_tf.setText(add_cuot_to_money(add_zero_in_front_of_point(str)));

//            one_tf_customer_money.setText(add_cuot_to_money(str));
//            one_tf_customer_money.setText(str);
//            System.out.println(one_tf_customer_money.getText());
            three_add_tf.setFocusable(true);
            three_add_tf.requestFocus();
        }

    }//GEN-LAST:event_three_add_tfKeyReleased

    private void three_add_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_three_add_tfKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(three_add_tf.getText())))) {
            evt.consume();
        }
    }//GEN-LAST:event_three_add_tfKeyTyped

    private void four_bn_edit_exchange_rateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_bn_edit_exchange_rateActionPerformed

        four_S_to_R_one_tf.setText((!four_S_to_R_one_tf.getText().isEmpty() ? four_S_to_R_one_tf.getText() : "0"));
        four_S_to_R_two_tf.setText((!four_S_to_R_two_tf.getText().isEmpty() ? four_S_to_R_two_tf.getText() : "0"));
        four_S_to_R_three_tf.setText((!four_S_to_R_three_tf.getText().isEmpty() ? four_S_to_R_three_tf.getText() : "0"));
        four_S_to_R_four_tf.setText((!four_S_to_R_four_tf.getText().isEmpty() ? four_S_to_R_four_tf.getText() : "0"));
        four_R_to_S_one_tf.setText((!four_R_to_S_one_tf.getText().isEmpty() ? four_R_to_S_one_tf.getText() : "0"));
        four_R_to_S_two_tf.setText((!four_R_to_S_two_tf.getText().isEmpty() ? four_R_to_S_two_tf.getText() : "0"));
        four_R_to_S_three_tf.setText((!four_R_to_S_three_tf.getText().isEmpty() ? four_R_to_S_three_tf.getText() : "0"));
        four_R_to_S_four_tf.setText((!four_R_to_S_four_tf.getText().isEmpty() ? four_R_to_S_four_tf.getText() : "0"));
        four_S_to_B_one_tf.setText((!four_S_to_B_one_tf.getText().isEmpty() ? four_S_to_B_one_tf.getText() : "0"));
        four_S_to_B_two_tf.setText((!four_S_to_B_two_tf.getText().isEmpty() ? four_S_to_B_two_tf.getText() : "0"));
        four_S_to_B_three_tf.setText((!four_S_to_B_three_tf.getText().isEmpty() ? four_S_to_B_three_tf.getText() : "0"));
        four_S_to_B_four_tf.setText((!four_S_to_B_four_tf.getText().isEmpty() ? four_S_to_B_four_tf.getText() : "0"));
        four_B_to_S_one_tf.setText((!four_B_to_S_one_tf.getText().isEmpty() ? four_B_to_S_one_tf.getText() : "0"));
        four_B_to_S_two_tf.setText((!four_B_to_S_two_tf.getText().isEmpty() ? four_B_to_S_two_tf.getText() : "0"));
        four_B_to_S_three_tf.setText((!four_B_to_S_three_tf.getText().isEmpty() ? four_B_to_S_three_tf.getText() : "0"));
        four_B_to_S_four_tf.setText((!four_B_to_S_four_tf.getText().isEmpty() ? four_B_to_S_four_tf.getText() : "0"));
        four_B_to_R_one_tf.setText((!four_B_to_R_one_tf.getText().isEmpty() ? four_B_to_R_one_tf.getText() : "0"));
        four_B_to_R_two_tf.setText((!four_B_to_R_two_tf.getText().isEmpty() ? four_B_to_R_two_tf.getText() : "0"));
        four_B_to_R_three_tf.setText((!four_B_to_R_three_tf.getText().isEmpty() ? four_B_to_R_three_tf.getText() : "0"));
        four_B_to_R_four_tf.setText((!four_B_to_R_four_tf.getText().isEmpty() ? four_B_to_R_four_tf.getText() : "0"));
        four_R_to_B_one_tf.setText((!four_R_to_B_one_tf.getText().isEmpty() ? four_R_to_B_one_tf.getText() : "0"));
        four_R_to_B_two_tf.setText((!four_R_to_B_two_tf.getText().isEmpty() ? four_R_to_B_two_tf.getText() : "0"));
        four_R_to_B_three_tf.setText((!four_R_to_B_three_tf.getText().isEmpty() ? four_R_to_B_three_tf.getText() : "0"));
        four_R_to_B_four_tf.setText((!four_R_to_B_four_tf.getText().isEmpty() ? four_R_to_B_four_tf.getText() : "0"));
        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        //        System.out.println(five_local_host_tf.getText());
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );

            String S_to_R = "";
            String R_to_S = "";
            String S_to_B = "";
            String B_to_S = "";
            String B_to_R = "";
            String R_to_B = "";
            //write sql query to access
            pst = con.prepareStatement("SELECT TOP 1 dollar_to_rial, rial_to_dollar, dollar_to_bart, "
                    + "bart_to_dollar, bart_to_rial, rial_to_bart "
                    + "FROM exc_rate_tb ORDER BY date_rate DESC;");

            rs = pst.executeQuery();
            while (rs.next()) {
                S_to_R = rs.getString("dollar_to_rial");
                R_to_S = rs.getString("rial_to_dollar");
                S_to_B = rs.getString("dollar_to_bart");
                B_to_S = rs.getString("bart_to_dollar");
                B_to_R = rs.getString("bart_to_rial");
                R_to_B = rs.getString("rial_to_bart");
            }

            String S_to_R_tf = S_R_validation(Double.parseDouble(four_S_to_R_one_tf.getText()
                    + four_S_to_R_two_tf.getText()
                    + four_S_to_R_three_tf.getText()
                    + four_S_to_R_four_tf.getText()));
            String R_to_S_tf = S_R_validation(Double.parseDouble(four_R_to_S_one_tf.getText()
                    + four_R_to_S_two_tf.getText()
                    + four_R_to_S_three_tf.getText()
                    + four_R_to_S_four_tf.getText()));
            String S_to_B_tf = S_B_validation(Double.parseDouble(four_S_to_B_one_tf.getText() + four_S_to_B_two_tf.getText()
                    + "."
                    + four_S_to_B_three_tf.getText()
                    + four_S_to_B_four_tf.getText()));
            String B_to_S_tf = S_B_validation(Double.parseDouble(four_B_to_S_one_tf.getText()
                    + four_B_to_S_two_tf.getText()
                    + "."
                    + four_B_to_S_three_tf.getText()
                    + four_B_to_S_four_tf.getText()));
            String B_to_R_tf = R_B_validation(Double.parseDouble(four_B_to_R_one_tf.getText()
                    + four_B_to_R_two_tf.getText()
                    + four_B_to_R_three_tf.getText()
                    + "."
                    + four_B_to_R_four_tf.getText()));
            String R_to_B_tf = R_B_validation(Double.parseDouble(four_R_to_B_one_tf.getText()
                    + four_R_to_B_two_tf.getText()
                    + four_R_to_B_three_tf.getText()
                    + "."
                    + four_R_to_B_four_tf.getText()));
            if (!(S_to_R.equals(S_to_R_tf)
                    && R_to_S.equals(R_to_S_tf)
                    && S_to_B.equals(S_to_B_tf)
                    && B_to_S.equals(B_to_S_tf)
                    && B_to_R.equals(B_to_R_tf)
                    && R_to_B.equals(R_to_B_tf))) {
                //write sql query to access
                pst = con.prepareStatement("insert into exc_rate_tb "
                        + "(date_rate, dollar_to_rial, rial_to_dollar, dollar_to_bart, bart_to_dollar, bart_to_rial, rial_to_bart) "
                        + "values( ?, ?, ?, ?, ?, ?, ?);");

                //set value to ?
                pst.setTimestamp(1, current_date());
                pst.setString(2, S_to_R_tf);
                pst.setString(3, R_to_S_tf);
                pst.setString(4, S_to_B_tf);
                pst.setString(5, B_to_S_tf);
                pst.setString(6, B_to_R_tf);
                pst.setString(7, R_to_B_tf);
                pst.executeUpdate();
                exe_rate_show.set_rate(S_to_R_tf, R_to_S_tf, S_to_B_tf, B_to_S_tf, B_to_R_tf, R_to_B_tf);
                JOptionPane.showMessageDialog(this, "Exchange rate saved.");
            } else {
                JOptionPane.showMessageDialog(this, "You change nothing.");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_four_bn_edit_exchange_rateActionPerformed

    private void four_S_to_R_four_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_S_to_R_four_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_S_to_R_four_tfActionPerformed

    private void four_S_to_R_one_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_S_to_R_one_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_S_to_R_one_tfActionPerformed

    private void four_S_to_R_two_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_S_to_R_two_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_S_to_R_two_tfActionPerformed

    private void four_S_to_R_three_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_S_to_R_three_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_S_to_R_three_tfActionPerformed

    private void four_R_to_S_one_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_R_to_S_one_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_R_to_S_one_tfActionPerformed

    private void four_R_to_S_two_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_R_to_S_two_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_R_to_S_two_tfActionPerformed

    private void four_R_to_S_three_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_R_to_S_three_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_R_to_S_three_tfActionPerformed

    private void four_R_to_S_four_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_R_to_S_four_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_R_to_S_four_tfActionPerformed

    private void four_S_to_B_one_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_S_to_B_one_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_S_to_B_one_tfActionPerformed

    private void four_S_to_B_two_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_S_to_B_two_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_S_to_B_two_tfActionPerformed

    private void four_S_to_B_three_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_S_to_B_three_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_S_to_B_three_tfActionPerformed

    private void four_S_to_B_four_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_S_to_B_four_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_S_to_B_four_tfActionPerformed

    private void four_B_to_S_one_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_B_to_S_one_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_B_to_S_one_tfActionPerformed

    private void four_B_to_S_two_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_B_to_S_two_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_B_to_S_two_tfActionPerformed

    private void four_B_to_S_three_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_B_to_S_three_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_B_to_S_three_tfActionPerformed

    private void four_B_to_S_four_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_B_to_S_four_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_B_to_S_four_tfActionPerformed

    private void four_B_to_R_one_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_B_to_R_one_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_B_to_R_one_tfActionPerformed

    private void four_B_to_R_two_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_B_to_R_two_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_B_to_R_two_tfActionPerformed

    private void four_B_to_R_three_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_B_to_R_three_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_B_to_R_three_tfActionPerformed

    private void four_B_to_R_four_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_B_to_R_four_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_B_to_R_four_tfActionPerformed

    private void four_R_to_B_four_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_R_to_B_four_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_R_to_B_four_tfActionPerformed

    private void four_R_to_B_three_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_R_to_B_three_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_R_to_B_three_tfActionPerformed

    private void four_R_to_B_two_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_R_to_B_two_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_R_to_B_two_tfActionPerformed

    private void four_R_to_B_one_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_four_R_to_B_one_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_four_R_to_B_one_tfActionPerformed

    private void four_S_to_R_one_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_S_to_R_one_tfKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_S_to_R_one_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_S_to_R_one_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_S_to_R_one_tfKeyTyped

    private void four_S_to_R_two_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_S_to_R_two_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_S_to_R_two_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_S_to_R_two_tf.getText().isEmpty()) {
                evt.consume();
            }
        }


    }//GEN-LAST:event_four_S_to_R_two_tfKeyTyped

    private void four_S_to_R_three_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_S_to_R_three_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_S_to_R_three_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_S_to_R_three_tf.getText().isEmpty()) {
                evt.consume();
            }
        }

    }//GEN-LAST:event_four_S_to_R_three_tfKeyTyped

    private void four_S_to_R_four_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_S_to_R_four_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_S_to_R_four_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_S_to_R_four_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_S_to_R_four_tfKeyTyped

    private void four_R_to_S_one_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_R_to_S_one_tfKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_R_to_S_one_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_R_to_S_one_tf.getText().isEmpty()) {
                evt.consume();
            }
        }

    }//GEN-LAST:event_four_R_to_S_one_tfKeyTyped

    private void four_R_to_S_two_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_R_to_S_two_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_R_to_S_two_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_R_to_S_two_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_R_to_S_two_tfKeyTyped

    private void four_R_to_S_three_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_R_to_S_three_tfKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_R_to_S_three_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_R_to_S_three_tf.getText().isEmpty()) {
                evt.consume();
            }
        }

    }//GEN-LAST:event_four_R_to_S_three_tfKeyTyped

    private void four_R_to_S_four_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_R_to_S_four_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_R_to_S_four_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_R_to_S_four_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_R_to_S_four_tfKeyTyped

    private void four_S_to_B_one_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_S_to_B_one_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_S_to_B_one_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_S_to_B_one_tf.getText().isEmpty()) {
                evt.consume();
            }
        }


    }//GEN-LAST:event_four_S_to_B_one_tfKeyTyped

    private void four_S_to_B_two_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_S_to_B_two_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_S_to_B_two_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_S_to_B_two_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_S_to_B_two_tfKeyTyped

    private void four_S_to_B_three_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_S_to_B_three_tfKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_S_to_B_three_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_S_to_B_three_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_S_to_B_three_tfKeyTyped

    private void four_S_to_B_four_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_S_to_B_four_tfKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_S_to_B_four_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_S_to_B_four_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_S_to_B_four_tfKeyTyped

    private void four_B_to_S_one_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_B_to_S_one_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_B_to_S_one_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_B_to_S_one_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_B_to_S_one_tfKeyTyped

    private void four_B_to_S_two_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_B_to_S_two_tfKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_B_to_S_two_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_B_to_S_two_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_B_to_S_two_tfKeyTyped

    private void four_B_to_S_three_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_B_to_S_three_tfKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_B_to_S_three_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_B_to_S_three_tf.getText().isEmpty()) {
                evt.consume();
            }
        }

    }//GEN-LAST:event_four_B_to_S_three_tfKeyTyped

    private void four_B_to_S_four_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_B_to_S_four_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_B_to_S_four_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_B_to_S_four_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_B_to_S_four_tfKeyTyped

    private void four_B_to_R_one_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_B_to_R_one_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_B_to_R_one_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_B_to_R_one_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_B_to_R_one_tfKeyTyped

    private void four_B_to_R_two_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_B_to_R_two_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_B_to_R_two_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_B_to_R_two_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_B_to_R_two_tfKeyTyped

    private void four_B_to_R_three_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_B_to_R_three_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_B_to_R_three_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_B_to_R_three_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_B_to_R_three_tfKeyTyped

    private void four_B_to_R_four_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_B_to_R_four_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_B_to_R_four_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_B_to_R_four_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_B_to_R_four_tfKeyTyped

    private void four_R_to_B_one_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_R_to_B_one_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_R_to_B_one_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_R_to_B_one_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_R_to_B_one_tfKeyTyped

    private void four_R_to_B_two_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_R_to_B_two_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_R_to_B_two_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_R_to_B_two_tf.getText().isEmpty()) {
                evt.consume();
            }
        }

    }//GEN-LAST:event_four_R_to_B_two_tfKeyTyped

    private void four_R_to_B_three_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_R_to_B_three_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_R_to_B_three_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_R_to_B_three_tf.getText().isEmpty()) {
                evt.consume();
            }
        }

    }//GEN-LAST:event_four_R_to_B_three_tfKeyTyped

    private void four_R_to_B_four_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_four_R_to_B_four_tfKeyTyped

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(four_R_to_B_four_tf.getText())))) {
            evt.consume();
        } else {
            if (!four_R_to_B_four_tf.getText().isEmpty()) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_four_R_to_B_four_tfKeyTyped

    private void three_add_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_add_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_three_add_tfActionPerformed

    private void three_tb_historyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_three_tb_historyMouseClicked
        boolean is_edit_tb = three_tb_history.isEditing();
        if (is_edit_tb == false) {
//            JOptionPane.showMessageDialog(this, "You can not edit this table");
            Object[] options = {"Print", "Delete", "Close"};
            dialog_choose choose_from_dialog = dialog_choose.Close;
            int idx = JOptionPane.showOptionDialog(this, "Would you like to save it", null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            if (idx != -1) {
                choose_from_dialog = dialog_choose.values()[idx];
            }
//            System.out.println(choose_from_dialog);
            switch (choose_from_dialog) {
                case Print:

                    if (!three_tb_history.getSelectionModel().isSelectionEmpty()) {

                        //table in UI
                        DefaultTableModel model = (DefaultTableModel) three_tb_history.getModel();
                        //select index of the table row
                        int selectedIndex = three_tb_history.getSelectedRow();

                        //get id from to store inside variable id here
                        int id = Integer.parseInt(model.getValueAt(selectedIndex, 1).toString());
                        String pur = model.getValueAt(selectedIndex, 3).toString();
                        if (pur.equals("exchanging")) {

                            Connection con;
                            PreparedStatement pst;
                            try {
                                con = DriverManager.getConnection(
                                        getLocal_host(),
                                        getLocal_host_user_name(),
                                        getLocal_host_password()
                                );
                            } catch (SQLException ex) {
                                Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
                                JOptionPane.showMessageDialog(this, ex);

                            }
                            print_reciept("C:\\Users\\Chhann_chikay\\Desktop\\my_documents\\project\\java_project\\exchange_and_tranfer_money\\exchange_and_transfer_money_netbean\\exchange_and_transfer\\src\\UI_and_operation\\reciept\\exchanging.jrxml",
                                    id);
                        }
                    }
                    break;
                case Delete:

                    if (!three_tb_history.getSelectionModel().isSelectionEmpty()) {

                        //table in UI
                        DefaultTableModel model = (DefaultTableModel) three_tb_history.getModel();
                        //select index of the table row
                        int selectedIndex = three_tb_history.getSelectedRow();

                        //get id from to store inside variable id here
                        int id = Integer.parseInt(model.getValueAt(selectedIndex, 1).toString());
                        String acc = model.getValueAt(selectedIndex, 2).toString();
                        String pur = model.getValueAt(selectedIndex, 3).toString();
                        int dialogresult = JOptionPane.showConfirmDialog(this, "Do you want to delete the record", "Warning", JOptionPane.YES_NO_OPTION);

                        if (dialogresult == JOptionPane.YES_NO_OPTION) {
                            if (pur.equals("exchanging")) {

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
                                        exchanging_money = rs.getString("exchanging_money");
                                        result_exchanging_money = rs.getString("result_exchanging_money");
                                        type_of_exchanging = rs.getString("type_of_exchanging");
                                    }

                                    String Rial = "";
                                    String Dollar = "";
                                    String Bart = "";
                                    pst = con.prepareStatement("SELECT rial, dollar, bart FROM total_money_tb");
                                    rs = pst.executeQuery();
                                    while (rs.next()) {
                                        //set to v2 all data only 1 row
                                        Rial = rs.getString("rial");
                                        Dollar = rs.getString("dollar");
                                        Bart = rs.getString("bart");
                                    }
                                    switch (type_of_exchanging) {
                                        case "S_to_R":
                                            pst = con.prepareStatement("UPDATE total_money_tb SET dollar = ?, rial = ? WHERE id_acc = ?;");
                                            pst.setString(1, money_S_B_R_validate(type_of_money.Dollar,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Dollar)) - Double.parseDouble(clear_cvot(exchanging_money)))));
                                            pst.setString(2, money_S_B_R_validate(type_of_money.Rial,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Rial)) + Double.parseDouble(clear_cvot(result_exchanging_money)))));
                                            pst.setInt(3, get_acc_id());
                                            pst.executeUpdate();
                                            break;
                                        case "S_to_B":
                                            //write sql query to access
                                            pst = con.prepareStatement("UPDATE total_money_tb SET dollar = ?, bart = ? WHERE id_acc = ?;");
                                            pst.setString(1, money_S_B_R_validate(type_of_money.Dollar,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Dollar)) - Double.parseDouble(clear_cvot(exchanging_money)))));
                                            pst.setString(2, money_S_B_R_validate(type_of_money.Bart,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Bart)) + Double.parseDouble(clear_cvot(result_exchanging_money)))));
                                            pst.setInt(3, get_acc_id());
                                            pst.executeUpdate();
                                            break;
                                        case "B_to_R":
                                            //write sql query to access
                                            pst = con.prepareStatement("UPDATE total_money_tb SET bart = ?, rial = ? WHERE id_acc = ?;");
                                            pst.setString(1, money_S_B_R_validate(type_of_money.Bart,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Bart)) - Double.parseDouble(clear_cvot(exchanging_money)))));
                                            pst.setString(2, money_S_B_R_validate(type_of_money.Rial,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Rial)) + Double.parseDouble(clear_cvot(result_exchanging_money)))));
                                            pst.setInt(3, get_acc_id());
                                            pst.executeUpdate();
                                            break;
                                        case "R_to_S":
                                            //write sql query to access
                                            pst = con.prepareStatement("UPDATE total_money_tb SET rial = ?, dollar = ? WHERE id_acc = ?;");
                                            pst.setString(1, money_S_B_R_validate(type_of_money.Rial,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Rial)) - Double.parseDouble(clear_cvot(exchanging_money)))));
                                            pst.setString(2, money_S_B_R_validate(type_of_money.Dollar,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Dollar)) + Double.parseDouble(clear_cvot(result_exchanging_money)))));
                                            pst.setInt(3, get_acc_id());
                                            pst.executeUpdate();
                                            break;
                                        case "B_to_S":
                                            //write sql query to access
                                            pst = con.prepareStatement("UPDATE total_money_tb SET bart = ?, dollar = ? WHERE id_acc = ?;");
                                            pst.setString(1, money_S_B_R_validate(type_of_money.Bart,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Bart)) - Double.parseDouble(clear_cvot(exchanging_money)))));
                                            pst.setString(2, money_S_B_R_validate(type_of_money.Dollar,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Dollar)) + Double.parseDouble(clear_cvot(result_exchanging_money)))));
                                            pst.setInt(3, get_acc_id());
                                            pst.executeUpdate();
                                            break;
                                        case "R_to_B":
                                            //write sql query to access
                                            pst = con.prepareStatement("UPDATE total_money_tb SET rial = ?, bart = ? WHERE id_acc = ?;");
                                            pst.setString(1, money_S_B_R_validate(type_of_money.Rial,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Rial)) - Double.parseDouble(clear_cvot(exchanging_money)))));
                                            pst.setString(2, money_S_B_R_validate(type_of_money.Bart,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Bart)) + Double.parseDouble(clear_cvot(result_exchanging_money)))));
                                            pst.setInt(3, get_acc_id());
                                            pst.executeUpdate();
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
                                    set_history();
                                    //dialog when added to access is success
                                    //                        JOptionPane.showMessageDialog(this, "records update");

                                    //update sql query to access
                                    pst = con.prepareStatement("delete from invoice_management_tb where id_invoice = ? AND id_acc =(select  id_acc FROM account_tb WHERE account_tb.user_name = ?) AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?);");

                                    pst.setInt(1, id);
                                    pst.setString(2, acc);
                                    pst.setString(3, pur);
                                    pst.executeUpdate();
                                    set_history();

                                } catch (SQLException ex) {
                                    Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
                                    JOptionPane.showMessageDialog(this, ex);

                                }
                            } else if (pur.equals("add_total_money")) {

                                Connection con;
                                PreparedStatement pst;
                                ResultSet rs;
                                try {
                                    con = DriverManager.getConnection(
                                            getLocal_host(),
                                            getLocal_host_user_name(),
                                            getLocal_host_password()
                                    );

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
                                        add_money = rs.getString("add_money");
                                        money_type = rs.getString("type_of_money");
                                    }

                                    String Rial = "";
                                    String Dollar = "";
                                    String Bart = "";
                                    pst = con.prepareStatement("SELECT rial, dollar, bart FROM total_money_tb");
                                    rs = pst.executeQuery();
                                    while (rs.next()) {
                                        //set to v2 all data only 1 row
                                        Rial = rs.getString("rial");
                                        Dollar = rs.getString("dollar");
                                        Bart = rs.getString("bart");
                                    }
                                    switch (money_type) {
                                        case "Rial":
                                            pst = con.prepareStatement("UPDATE total_money_tb SET rial = ? WHERE id_acc = ?;");
                                            pst.setString(1, money_S_B_R_validate(type_of_money.Rial,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Rial)) - Double.parseDouble(clear_cvot(add_money)))));
                                            pst.setInt(2, get_acc_id());
                                            pst.executeUpdate();
                                            break;
                                        case "Dollar":
                                            //write sql query to access
                                            pst = con.prepareStatement("UPDATE total_money_tb SET dollar = ? WHERE id_acc = ?;");
                                            pst.setString(1, money_S_B_R_validate(type_of_money.Dollar,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Dollar)) - Double.parseDouble(clear_cvot(add_money)))));
                                            pst.setInt(2, get_acc_id());
                                            pst.executeUpdate();
                                            break;
                                        case "Bart":
                                            //write sql query to access
                                            pst = con.prepareStatement("UPDATE total_money_tb SET bart = ? WHERE id_acc = ?;");
                                            pst.setString(1, money_S_B_R_validate(type_of_money.Bart,
                                                    String.valueOf(Double.parseDouble(clear_cvot(Bart)) - Double.parseDouble(clear_cvot(add_money)))));
                                            pst.setInt(2, get_acc_id());
                                            pst.executeUpdate();
                                            break;
                                        default:
                                            System.out.println("Error");
                                    }
                                    //update sql query to access
                                    pst = con.prepareStatement("delete from add_money_history_tb where id_add = ? AND id_acc = (select  id_acc FROM account_tb WHERE account_tb.user_name = ?) AND id_pur = (select  id_pur FROM purpose_tb WHERE purpose_tb.pur_type = ?)");

                                    pst.setInt(1, id);
                                    pst.setString(2, acc);
                                    pst.setString(3, pur);
                                    pst.executeUpdate();
                                    set_history();

                                    //dialog when added to access is success
                                    //                        JOptionPane.showMessageDialog(this, "records update");
                                } catch (SQLException ex) {
                                    Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
                                    JOptionPane.showMessageDialog(this, ex);

                                }
                            }
                        }
                        set_history();
                    }
                    break;
            }
        }
    }//GEN-LAST:event_three_tb_historyMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        set_history();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UI_and_operation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UI_and_operation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UI_and_operation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UI_and_operation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UI_and_operation().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JLabel date_history_lb;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JButton five_create_acc_tf;
    private javax.swing.JTextField five_local_host_db_name_tf;
    private javax.swing.JTextField five_local_host_password_tf;
    private javax.swing.JTextField five_local_host_server_name_tf;
    private javax.swing.JTextField five_local_host_user_name_tf;
    private javax.swing.JTextField five_password_tf;
    private javax.swing.JTextField five_user_name_tf;
    private javax.swing.JTextField five_wifi_host_password_tf;
    private javax.swing.JTextField five_wifi_host_tf;
    private javax.swing.JTextField five_wifi_host_user_name_tf;
    private javax.swing.JTextField four_B_to_R_four_tf;
    private javax.swing.JTextField four_B_to_R_one_tf;
    private javax.swing.JTextField four_B_to_R_three_tf;
    private javax.swing.JTextField four_B_to_R_two_tf;
    private javax.swing.JTextField four_B_to_S_four_tf;
    private javax.swing.JTextField four_B_to_S_one_tf;
    private javax.swing.JTextField four_B_to_S_three_tf;
    private javax.swing.JTextField four_B_to_S_two_tf;
    private javax.swing.JTextField four_R_to_B_four_tf;
    private javax.swing.JTextField four_R_to_B_one_tf;
    private javax.swing.JTextField four_R_to_B_three_tf;
    private javax.swing.JTextField four_R_to_B_two_tf;
    private javax.swing.JTextField four_R_to_S_four_tf;
    private javax.swing.JTextField four_R_to_S_one_tf;
    private javax.swing.JTextField four_R_to_S_three_tf;
    private javax.swing.JTextField four_R_to_S_two_tf;
    private javax.swing.JTextField four_S_to_B_four_tf;
    private javax.swing.JTextField four_S_to_B_one_tf;
    private javax.swing.JTextField four_S_to_B_three_tf;
    private javax.swing.JTextField four_S_to_B_two_tf;
    private javax.swing.JTextField four_S_to_R_four_tf;
    private javax.swing.JTextField four_S_to_R_one_tf;
    private javax.swing.JTextField four_S_to_R_three_tf;
    private javax.swing.JTextField four_S_to_R_two_tf;
    private javax.swing.JButton four_bn_edit_exchange_rate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton9;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JButton one_bn_B_to_R;
    private javax.swing.JButton one_bn_B_to_S;
    private javax.swing.JButton one_bn_R_to_B;
    private javax.swing.JButton one_bn_R_to_S;
    private javax.swing.JButton one_bn_S_to_B;
    private javax.swing.JButton one_bn_S_to_R;
    private javax.swing.JButton one_bn_finished;
    private javax.swing.JButton one_bn_print;
    private javax.swing.JLabel one_lb_customer_money;
    private javax.swing.JLabel one_lb_customer_result;
    private javax.swing.JLabel one_lb_equal;
    private javax.swing.JLabel one_lb_exchange_rate;
    private javax.swing.JLabel one_lb_operator;
    private javax.swing.JLabel one_money_type_lb;
    private javax.swing.JLabel one_money_type_result_lb;
    private javax.swing.JLabel one_rate_type_lb;
    private javax.swing.JTextField one_tf_customer_money;
    private javax.swing.JTextField one_tf_customer_result;
    private javax.swing.JTextField one_tf_exchange_rate;
    private javax.swing.JButton print;
    private javax.swing.JButton three_add_bn;
    private javax.swing.JTextField three_add_tf;
    private javax.swing.JRadioButton three_bart_rb;
    private com.toedter.calendar.JDateChooser three_calendar_cld;
    private javax.swing.JRadioButton three_dollar_rb;
    private javax.swing.JRadioButton three_rial_rb;
    private javax.swing.JTable three_tb_history;
    private javax.swing.JTable three_tb_total_money;
    private javax.swing.JTextField two_four_balance_money_tf;
    private javax.swing.JRadioButton two_four_bart_money_rb;
    private javax.swing.JButton two_four_bn_finish;
    private javax.swing.JRadioButton two_four_dollar_money_rb;
    private javax.swing.JTextField two_four_province_name_tf;
    private javax.swing.JTextField two_four_receiver_phone_no_tf;
    private javax.swing.JRadioButton two_four_rial_money_rb;
    private javax.swing.JTextField two_four_sender_money_tf;
    private javax.swing.JTextField two_four_total_money_tf;
    private javax.swing.JButton two_one_bn_finish;
    private javax.swing.JTextField two_one_tf_bank;
    private javax.swing.JTextField two_one_tf_cus_money;
    private javax.swing.JTextField two_one_tf_cus_name;
    private javax.swing.JTextField two_one_tf_cus_no;
    private javax.swing.JTextField two_one_tf_cus_ph_no;
    private javax.swing.JTextField two_one_tf_service_money;
    private javax.swing.JTextField two_one_tf_total_money;
    private javax.swing.JTextField two_three_balance_money_tf;
    private javax.swing.JRadioButton two_three_bart_money_rb;
    private javax.swing.JButton two_three_bn_finish;
    private javax.swing.JRadioButton two_three_dollar_money_rb;
    private javax.swing.JTextField two_three_province_name_tf;
    private javax.swing.JTextField two_three_receiver_phone_no_tf;
    private javax.swing.JRadioButton two_three_rial_money_rb;
    private javax.swing.JTextField two_three_sender_money_tf;
    private javax.swing.JTextField two_three_sender_phone_no_tf;
    private javax.swing.JTextField two_three_service_money_tf;
    private javax.swing.JTextField two_three_total_money_tf;
    private javax.swing.JRadioButton two_two_AM_rb;
    private javax.swing.JRadioButton two_two_PM_rb;
    private javax.swing.JButton two_two_bn_finish;
    private javax.swing.JTextField two_two_day_tf;
    private javax.swing.JTextField two_two_hour_tf;
    private javax.swing.JTextField two_two_minute_tf;
    private javax.swing.JTextField two_two_month_tf;
    private javax.swing.JTextField two_two_result_money_tf;
    private javax.swing.JTextField two_two_reveiver_money_tf;
    private javax.swing.JTextField two_two_reveiver_ph_no_tf;
    private javax.swing.JTextField two_two_service_money_tf;
    private javax.swing.JTextField two_two_year_tf;
    // End of variables declaration//GEN-END:variables

}
