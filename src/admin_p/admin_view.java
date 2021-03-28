/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_p;

import static admin_p.account.*;
import static admin_p.add_total_money.detele_add_total_to_db;
import static admin_p.add_total_money.get_add_total_db_set_to_tb;
import static admin_p.connection_to_ms_sql.*;
import static admin_p.exc_rate.end_task_ppt;
import static admin_p.exc_rate.open_exc_rate_ppt;
import static admin_p.exc_rate.set_rate_to_db;
import static admin_p.exc_rate.set_rate_to_excel;
import static admin_p.exchanging.R_B_validation;
import static admin_p.exchanging.S_B_validation;
import static admin_p.exchanging.S_R_validation;
import static admin_p.exchanging.*;
import static admin_p.from_province.detele_from_pro_to_db;
import static admin_p.from_province.get_from_pro_db_set_to_tb;
import static admin_p.from_province.two_two_cal;
import static admin_p.from_thai.detele_from_thai_to_db;
import static admin_p.from_thai.get_from_thai_db_set_to_tb;
import static admin_p.invoice_man.get_count_id_invoice_man_from_db;
import static admin_p.invoice_man.get_id_invoice;
import static admin_p.invoice_man.is_null_acc_id_invoice_man;
import static admin_p.invoice_man.set_ind_man_db;
import static admin_p.invoice_man.set_invoice_man_db;
import static admin_p.path_file.exc_reciept_path;
import static admin_p.path_file.get_path;
import static admin_p.purpose.*;
import static admin_p.reciept.print_reciept;
import static admin_p.reciept.print_reciept_exc;
import static admin_p.to_province.two_one_cal;
import static admin_p.validate_value.*;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static admin_p.to_province.detele_to_pro_to_db;
import static admin_p.to_province.get_to_pro_db_set_to_tb;
import static admin_p.to_province.set_to_pro_to_db;
import static admin_p.to_thai.detele_to_thai_to_db;
import static admin_p.to_thai.get_to_thai_db_set_to_tb;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Chhann_chikay
 */
public class admin_view extends javax.swing.JFrame {

    //------------------------------------------------------my varaible------------------------------------------------------
    //type_of_exchange is also the same value type of exchange  database except not_select
    public static enum type_of_exchange {
        S_to_R, S_to_B, B_to_R, R_to_S, B_to_S, R_to_B, not_select
    }

    public static enum purpose_type {
        exchanging, to_thai, from_thai, to_province, from_province, add_total_money, double_exchanging
    }

    public enum type_of_money {
        Rial, Dollar, Bart
    }

    private enum dialog_choose_p_d_c {
        Print, Delete, Close
    };

    public static enum dialog_choose_e_d_c {
        Edit, Delete, Close
    };

    public static enum dialog_type_for_db_e_a {
        Edit, Add
    };

    public static Color sky_c = new Color(153, 255, 255);
    public static Color silivor_c = new Color(200, 200, 200);
    public static Color white_c = new Color(255, 255, 255);
    public static Color black_c = Color.BLACK;
    private int next_show_his = 0;
    private final int num_show_his = 10;
    private int idx_transfer_pt = 0;
    private Boolean is_change_pro = false;
//    public static String set_admin_password = "";
    private Boolean two_one_is_off_edit = true;
    //to store which type of exchange that user performs, by defauld selected_exchange_rate = not_select
    private type_of_exchange selected_exchange_rate = type_of_exchange.not_select;
    private type_of_money selected_money_type_exe;
    private type_of_money selected_money_type_to_pro;
    private type_of_money selected_money_type_from_pro;
    private static Boolean is_change_his = false;

    //-----------------------------------------------------class call--------------------------------------------------
//    exchange_rate_show exe_rate_show = new exchange_rate_show();
    //------------------------------------------------------my function------------------------------------------------------
    public static void set_is_change_true() {
        is_change_his = true;
    }

    public void set_selected_exchange_rate_to_not_select() {
        selected_exchange_rate = type_of_exchange.not_select;
    }

    public int get_idx_transfer_pt() {
        return idx_transfer_pt;
    }

    public void set_is_change_pro_true() {
        is_change_pro = true;
    }

    public void hide_or_show_column_his(javax.swing.JCheckBox chb,
            int idx_col, int set_col_size, int set_max_col_size, int set_min_col_size) {
        if (chb.isSelected()) {
            three_tb_history.getColumnModel().getColumn(idx_col).setMaxWidth(set_max_col_size);
            three_tb_history.getColumnModel().getColumn(idx_col).setMinWidth(set_min_col_size);

            three_tb_history.getColumnModel().getColumn(idx_col).setPreferredWidth(set_col_size);
        } else {

            three_tb_history.getColumnModel().getColumn(idx_col).setMaxWidth(0);
            three_tb_history.getColumnModel().getColumn(idx_col).setMinWidth(0);

            three_tb_history.getColumnModel().getColumn(idx_col).setPreferredWidth(0);
        }
    }

    private void remove_all_in_list(javax.swing.JList<String> list) {
        DefaultListModel mode = new DefaultListModel();
        list.setModel(mode);
        mode.removeAllElements();
    }

    public static void get_pro_result_search_db(javax.swing.JList<String> list, javax.swing.JTextField set_on_tf) {
        if (!list.isSelectionEmpty()) {
            DefaultListModel mode = new DefaultListModel();
            String ph_no = (String) list.getSelectedValue();
            list.setModel(mode);
            set_on_tf.setText(ph_no);
            mode.removeAllElements();
        }
    }

    public static void set_cb(javax.swing.JComboBox<String> service_pro_name_cb, String col_sql, String tb_sql) {

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
            pst = con.prepareStatement("SELECT " + col_sql + " FROM " + tb_sql + " ORDER BY " + col_sql + ";");
            rs = pst.executeQuery();
            service_pro_name_cb.removeAllItems();
            service_pro_name_cb.addItem("none");
            while (rs.next()) {
                service_pro_name_cb.addItem(rs.getString(col_sql));
            }
//        set_service_pro_name_cb.addItem();
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    public static int get_id_inv_by_id_inv_man_from_db(int id_inv_man) {
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
            pst = con.prepareStatement("SELECT id_invoice FROM invoice_management_tb WHERE id_invoice_man = " + id_inv_man + ";");
            rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id_invoice");
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return id;
    }

    public static int get_id_money_type_from_db(type_of_money money_type) {
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
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return id;
    }

    public static String convert_to_short_money_type(String money_type) {

        switch (money_type) {
            case "Rial":
            case "R":
                return "៛";
            case "Dollar":
            case "S":
                return "$";
            case "Bart":
            case "B":
                return "B";
            default:
                return "";
        }
    }

    //function that get current date
    public static Timestamp current_date() {
        long millis = System.currentTimeMillis();
        Timestamp time = new Timestamp(millis);
        return time;
    }

    public void search_engine_pro(javax.swing.JList<String> two_one_ph_list,
            String value, String col, String tb) {
        DefaultListModel two_one_mode = new DefaultListModel();
        two_one_ph_list.setModel(two_one_mode);
        two_one_mode.removeAllElements();
        String two_one_ph_senter_str = value.trim();
        if (!two_one_ph_senter_str.equals("")) {
            Connection con;
            PreparedStatement pst;
            ResultSet rs;
            try {
                con = DriverManager.getConnection(
                        getLocal_host(),
                        getLocal_host_user_name(),
                        getLocal_host_password()
                );
                pst = con.prepareStatement("SELECT TOP 5 " + col + " FROM " + tb + " WHERE " + col + " LIKE '%" + value + "%' "
                        + "ORDER BY " + col + ";");
                rs = pst.executeQuery();
                while (rs.next()) {
                    two_one_mode.addElement(rs.getString(col));
                }
            } catch (SQLException ex) {
                sql_con sql_con_obj = new sql_con(ex);
                sql_con_obj.setVisible(true);
            }
        }
    }

    public void search_engine_bank_thai(javax.swing.JList<String> bank_list,
            String value, String col_target, String col_1, String col_2, String col_3, String tb) {

        DefaultListModel two_one_mode = new DefaultListModel();
        bank_list.setModel(two_one_mode);
        two_one_mode.removeAllElements();
        String two_one_ph_senter_str = value.trim();
        if (!two_one_ph_senter_str.equals("")) {
            Connection con;
            PreparedStatement pst;
            ResultSet rs;
            try {
                con = DriverManager.getConnection(
                        getLocal_host(),
                        getLocal_host_user_name(),
                        getLocal_host_password()
                );
                pst = con.prepareStatement("SELECT TOP 6 " + col_target + ", " + col_1 + ", " + col_2 + ", " + col_3
                        + " FROM " + tb
                        + " WHERE " + col_target + " LIKE '%" + value + "%' "
                        + "ORDER BY " + col_target + ", " + col_1 + ", " + col_2 + ", " + col_3 + ";");
                rs = pst.executeQuery();
                while (rs.next()) {
                    two_one_mode.addElement(rs.getString(col_target) + " | " + rs.getString(col_1) + " | " + rs.getString(col_2) + " | " + rs.getString(col_3));
                }
            } catch (SQLException ex) {
                sql_con sql_con_obj = new sql_con(ex);
                sql_con_obj.setVisible(true);
            }
        }
    }

    public static void get_bank_thai_result_search_db(javax.swing.JList<String> list, javax.swing.JTextField set_on_tf1,
            javax.swing.JTextField set_on_tf2, javax.swing.JTextField set_on_tf3, javax.swing.JComboBox<String> set_on_cb4) {
        if (!list.isSelectionEmpty()) {
            DefaultListModel mode = new DefaultListModel();
            String set_val = (String) list.getSelectedValue();
            for (int i = 1; i < set_val.length() - 1; i++) {
                if (set_val.charAt(i - 1) == ' ' && set_val.charAt(i) == '|' && set_val.charAt(i + 1) == ' ') {
                    set_on_tf1.setText(set_val.substring(0, i - 1));

                    for (int j = i + 2; j < set_val.length() - 1; j++) {
                        if (set_val.charAt(j - 1) == ' ' && set_val.charAt(j) == '|' && set_val.charAt(j + 1) == ' ') {
                            set_on_tf2.setText(set_val.substring(i + 2, j - 1));

                            for (int k = j + 2; k < set_val.length() - 1; k++) {
                                if (set_val.charAt(k - 1) == ' ' && set_val.charAt(k) == '|' && set_val.charAt(k + 1) == ' ') {
                                    set_on_tf3.setText(set_val.substring(j + 2, k - 1));
                                    set_on_cb4.getModel().setSelectedItem(set_val.substring(k + 2, set_val.length()));
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }
            list.setModel(mode);
            mode.removeAllElements();
        }
    }

    public static int get_id_province_name_from_db(String province_name) {

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
            pst = con.prepareStatement("SELECT id_province "
                    + "FROM province_name_tb "
                    + "WHERE transfer_province = '" + province_name + "';");
            rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id_province");
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return id;
    }

    public static int get_id_province_name_history_from_db(String province_name) {

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
            pst = con.prepareStatement("SELECT id_pro_name "
                    + "FROM province_name_history_tb "
                    + "WHERE transfer_province = '" + province_name + "';");
            rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id_pro_name");
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return id;

    }

    private void get_from_db_set_total_in_tb(Boolean is_ind) {

        Connection con;
        PreparedStatement pst;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //query to access
            if (is_ind) {
                pst = con.prepareStatement("SELECT TOP 1 rial, dollar, bart, bank_bart "
                        + "FROM invoice_management_tb ORDER BY invoice_man_date DESC;");
                rs = pst.executeQuery();
            } else {
                pst = con.prepareStatement("SELECT TOP 1 individual_total_money_management_tb.rial, "
                        + "individual_total_money_management_tb.dollar, "
                        + "individual_total_money_management_tb.bart, "
                        + "individual_total_money_management_tb.bank_bart "
                        + "FROM individual_total_money_management_tb INNER JOIN invoice_management_tb "
                        + "ON individual_total_money_management_tb.id_invoice_man = invoice_management_tb.id_invoice_man "
                        + "WHERE id_acc = " + get_acc_id() + " "
                        + "ORDER BY invoice_man_date DESC;");
                rs = pst.executeQuery();
            }
            while (rs.next()) {
                String Dollar = rs.getString("dollar");
                String Bart = rs.getString("bart");
                String Rial = rs.getString("rial");
                String Bart_bank = rs.getString("bank_bart");
                set_to_tb(
                        money_S_B_R_validate(type_of_money.Dollar, Dollar, true),
                        money_S_B_R_validate(type_of_money.Bart, Bart, true),
                        money_S_B_R_validate(type_of_money.Rial, Rial, true),
                        money_S_B_R_validate(type_of_money.Bart, Bart_bank, true));
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    private int count_db_to_list_pur_and_id() {

        int count = 0;
        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );

            Calendar start_cal = three_calendar_cld.getCalendar();
            Calendar start_cal2 = three_calendar_cld.getCalendar();
            Calendar pre_7_day_from_start_cal = three_calendar_cld.getCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
            start_cal.add(Calendar.DAY_OF_MONTH, 1);
            String start_date = sdf.format(start_cal.getTime());
            String start_date2 = sdf2.format(start_cal2.getTime());
            pre_7_day_from_start_cal.add(Calendar.DAY_OF_MONTH, 0);
            String pre_7_day_from_start_date = sdf.format(pre_7_day_from_start_cal.getTime());
//            String pre_7_day_from_start_date2 = sdf2.format(pre_7_day_from_start_cal.getTime());
            date_history_lb.setText(start_date2);
            pst = con.prepareStatement("SELECT COUNT(id_invoice_man) AS count "
                    + "FROM invoice_management_tb INNER JOIN purpose_tb "
                    + "ON invoice_management_tb.id_pur = purpose_tb.id_pur "
                    + "WHERE id_acc = ? AND invoice_man_date >= '" + pre_7_day_from_start_date + "' "
                    + "AND invoice_man_date <= '" + start_date + "';");
            pst.setInt(1, get_acc_id());
            rs = pst.executeQuery();

            while (rs.next()) {
                count = rs.getInt("count");
            }

        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return count;
    }

    private void set_to_tb(String S, String R, String B, String B_bank) {

        DefaultTableModel dft1 = (DefaultTableModel) three_tb_total_money.getModel();
        dft1.setRowCount(0);
        Vector v3 = new Vector();

        //set to v2 all data only 1 row
        v3.add(S);
        v3.add(R);
        v3.add(B);
        v3.add(B_bank);

        //set data to table history row
        dft1.addRow(v3);
    }

    private class start_end_date_db {

        private String start_date;
        private String end_date;
        private String dd_MM_yyyy;

        public start_end_date_db(int start_num, int end_num) {
            Calendar start_cal = three_calendar_cld.getCalendar();
            Calendar start_cal_d_m_y = three_calendar_cld.getCalendar();
            Calendar end_cal = three_calendar_cld.getCalendar();
            SimpleDateFormat sdf_y_m_d = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf_d_m_y = new SimpleDateFormat("dd-MM-yyyy");
            start_cal.add(Calendar.DAY_OF_MONTH, start_num);
            start_date = sdf_y_m_d.format(start_cal.getTime());
            dd_MM_yyyy = sdf_d_m_y.format(start_cal_d_m_y.getTime());
            end_cal.add(Calendar.DAY_OF_MONTH, end_num);
            end_date = sdf_y_m_d.format(end_cal.getTime());
        }
    }

    public void set_history() {
        if (three_calendar_cld.getDate() != null && validate_dc(three_calendar_cld)) {
            three_calendar_cld.setBackground(Color.lightGray);

            if (is_delete_last_7d()) {
                delete_not_cur_to_last_7_d_from_db();
            }

            if (!is_null_acc_id_invoice_man(get_acc_id())) {
                get_from_db_set_total_in_tb(false);
                Connection con;
                PreparedStatement pst;
                ResultSet rs;
                try {
                    con = DriverManager.getConnection(
                            getLocal_host(),
                            getLocal_host_user_name(),
                            getLocal_host_password()
                    );

                    start_end_date_db s_e_d_db_obj = new start_end_date_db(1, 0);
                    date_history_lb.setText(s_e_d_db_obj.dd_MM_yyyy);
                    pst = con.prepareStatement("SELECT id_invoice, pur_type "
                            + "FROM invoice_management_tb INNER JOIN purpose_tb "
                            + "ON invoice_management_tb.id_pur = purpose_tb.id_pur "
                            + "WHERE id_acc = " + get_acc_id() + " AND invoice_man_date >= '" + s_e_d_db_obj.end_date + "' "
                            + "AND invoice_man_date <= '" + s_e_d_db_obj.start_date + "' "
                            + "ORDER BY id_invoice_man DESC "
                            + "OFFSET " + next_show_his + " ROWS "
                            + "FETCH NEXT " + num_show_his + " ROWS ONLY;");
                    rs = pst.executeQuery();
                    DefaultTableModel dft = (DefaultTableModel) three_tb_history.getModel();
                    int row_increas = 0;
                    while (rs.next()) {
                        dft.setRowCount(row_increas);
                        Vector v3 = new Vector();
                        dft.setRowCount(row_increas);
                        int id_inv = rs.getInt("id_invoice");
                        switch (rs.getString("pur_type")) {
                            case "exchanging":
                                v3 = get_exe_db_set_to_tb(id_inv, false);
                                break;
                            case "add_total_money":
                                v3 = get_add_total_db_set_to_tb(id_inv, false);
                                break;
                            case "to_province":
                                v3 = get_to_pro_db_set_to_tb(id_inv, false);
                                break;
                            case "from_province":
                                v3 = get_from_pro_db_set_to_tb(id_inv, false);
                                break;
                            case "double_exchanging":
                                v3 = get_from_pro_db_set_to_tb_double_exc(id_inv, false);
                                break;
                            case "to_thai":
                                v3 = get_to_thai_db_set_to_tb(id_inv, false);
                                break;
                            case "from_thai":
                                v3 = get_from_thai_db_set_to_tb(id_inv, false);
                                break;
                            default:
                                JOptionPane.showMessageDialog(this, "error : set_history", "Alert", JOptionPane.WARNING_MESSAGE);
                        }
                        dft.addRow(v3);
                        row_increas++;
                    }
                    if (row_increas == 0) {
                        dft.setRowCount(0);
                    }
                } catch (SQLException ex) {
                    sql_con sql_con_obj = new sql_con(ex);
                    sql_con_obj.setVisible(true);
                }
            } else {
                set_to_tb("0", "0", "0", "0");
                DefaultTableModel dft = (DefaultTableModel) three_tb_history.getModel();
                dft.setRowCount(0);
            }
        } else {
            three_calendar_cld.setBackground(Color.red);
            JOptionPane.showMessageDialog(this, "Wrong day or month or year");
        }
    }

    public static String convert_pur_to_kh(String pur) {
        String pur_kh = "";
        switch (pur) {
            case "exchanging":
                pur_kh = "ប្តូរប្រាក់";
                break;
            case "add_total_money":
                pur_kh = "បន្ថែមប្រាក់";
                break;
            case "to_province":
                pur_kh = "ផ្ញើរប្រាក់ទៅតំបន់";
                break;
            case "from_province":
                pur_kh = "ដកប្រាក់ពីតំបន់";
                break;
            case "double_exchanging":
                pur_kh = "ប្តូរប្រាក់២ដង";
                break;
            case "to_thai":
                pur_kh = "ផ្ញើរប្រាក់ទៅថៃ";
                break;
            case "from_thai":
                pur_kh = "ដកប្រាក់ពីថៃ";
                break;
            default:
                all_type_error_mes error_mes = new all_type_error_mes("error function UI_and_operation class: convert_pur_to_kh");
        }
        return pur_kh;
    }

    public String convert_kh_to_pur(String kh) {
        String pur_kh = "";
        switch (kh) {
            case "ប្តូរប្រាក់":
                pur_kh = "exchanging";
                break;
            case "បន្ថែមប្រាក់":
                pur_kh = "add_total_money";
                break;
            case "ផ្ញើរប្រាក់ទៅតំបន់":
                pur_kh = "to_province";
                break;
            case "ដកប្រាក់ពីតំបន់":
                pur_kh = "from_province";
                break;
            case "ប្តូរប្រាក់២ដង":
                pur_kh = "double_exchanging";
                break;
            case "ផ្ញើរប្រាក់ទៅថៃ":
                pur_kh = "to_thai";
                break;
            case "ដកប្រាក់ពីថៃ":
                pur_kh = "from_thai";
                break;
            default:
                all_type_error_mes error_mes = new all_type_error_mes("error function UI_and_operation class: convert_kh_to_pur");
        }
        return pur_kh;
    }

    public static Boolean is_has_history_list_db(String value, String col, String tb) {

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
            pst = con.prepareStatement("SELECT " + col + " FROM " + tb + " WHERE " + col + " = '" + value + "';");
            rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return false;
    }

    public static void set_thai_history_db(
            String col_bank, String val_bank,
            String col_id_bank, String val_id_bank,
            String col_name, String val_name,
            String col_ph_no, String val_ph_no,
            String tb) {

        Connection con;
        PreparedStatement pst;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //write sql query to access
            pst = con.prepareStatement("insert into " + tb + " (" + col_bank + ", " + col_id_bank + ", " + col_name + "," + col_ph_no + ") "
                    + "values('" + val_bank + "', '" + val_id_bank + "', '" + val_name + "', '" + val_ph_no + "');");
            pst.executeUpdate();

        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    public static void update_thai_history_db(
            String col_bank, String val_bank,
            String col_id_bank, String val_id_bank,
            String col_name, String val_name,
            String col_ph_no, String val_ph_no,
            String tb, String id_to_thai, int id) {

        Connection con;
        PreparedStatement pst;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //write sql query to access
            pst = con.prepareStatement("UPDATE " + tb
                    + " SET " + col_bank + " = '" + val_bank + "', "
                    + col_id_bank + " = '" + val_id_bank + "', "
                    + col_name + " = '" + val_name + "', "
                    + col_ph_no + " = '" + val_ph_no + "' "
                    + "WHERE " + id_to_thai + " = " + id + ";");
            pst.executeUpdate();

        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    public static void set_history_list_db(String value, String col, String tb) {

        Connection con;
        PreparedStatement pst;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //write sql query to access
            pst = con.prepareStatement("insert into " + tb + " (" + col + ") values('" + value + "');");
            pst.executeUpdate();

        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    private void custom_three_tb_his_default() {

        //design head table  total money in balance
        three_tb_total_money.getTableHeader().setFont(new Font("Khmer OS Siemreap", Font.BOLD, 30));
        three_tb_total_money.setRowHeight(40);

        //design head table  history in balance
        three_tb_history.getTableHeader().setFont(new Font("Khmer OS Siemreap", Font.BOLD, 20));
        three_tb_history.setRowHeight(40);

        three_tb_history.setShowGrid(true);

        three_tb_history.getColumnModel().getColumn(0).setMaxWidth(300);
        three_tb_history.getColumnModel().getColumn(1).setMaxWidth(150);
        three_tb_history.getColumnModel().getColumn(2).setMaxWidth(170);
        three_tb_history.getColumnModel().getColumn(3).setMaxWidth(200);
        three_tb_history.getColumnModel().getColumn(4).setMaxWidth(200);
        three_tb_history.getColumnModel().getColumn(5).setMaxWidth(200);
        three_tb_history.getColumnModel().getColumn(6).setMaxWidth(200);
        three_tb_history.getColumnModel().getColumn(7).setMaxWidth(200);
        three_tb_history.getColumnModel().getColumn(8).setMaxWidth(1500);

        three_tb_history.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        three_tb_history.getColumnModel().getColumn(0).setPreferredWidth(230);
        three_tb_history.getColumnModel().getColumn(1).setPreferredWidth(100);
        three_tb_history.getColumnModel().getColumn(2).setPreferredWidth(150);
        three_tb_history.getColumnModel().getColumn(3).setPreferredWidth(175);
        three_tb_history.getColumnModel().getColumn(4).setPreferredWidth(130);
        three_tb_history.getColumnModel().getColumn(5).setPreferredWidth(130);
        three_tb_history.getColumnModel().getColumn(6).setPreferredWidth(130);
        three_tb_history.getColumnModel().getColumn(7).setPreferredWidth(140);
        three_tb_history.getColumnModel().getColumn(8).setPreferredWidth(1000);

        if (count_db_to_list_pur_and_id() < num_show_his) {
            three_down.setEnabled(false);
        } else {
            three_down.setEnabled(true);
        }
    }

    private void custom_four_rate_default() {

        String S_to_R = "";
        String R_to_S = "";
        String S_to_B = "";
        String B_to_S = "";
        String B_to_R = "";
        String R_to_B = "";
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
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
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
    }

    private void add_listener_calendar_three() {

        three_calendar_cld.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                three_up.setEnabled(false);
                next_show_his = 0;
                if (count_db_to_list_pur_and_id() <= num_show_his) {
                    three_down.setEnabled(false);
                } else {
                    three_down.setEnabled(true);
                }
                set_history();
            }
        });
    }

    void add_listener_pt_zero() {

        zero_tp.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {

                if (zero_tp.getSelectedIndex() == 2 && is_change_his) {
                    three_up.setEnabled(false);
                    next_show_his = 0;
                    if (count_db_to_list_pur_and_id() <= num_show_his) {
                        three_down.setEnabled(false);
                    } else {
                        three_down.setEnabled(true);
                    }
                    set_history();
                    is_change_his = false;
                }
            }
        });
    }

    private void set_listener_cb_on_click(javax.swing.JComboBox<String> bc) {
        bc.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                bc.showPopup();
            }
        });
    }

    private void delete_not_cur_to_last_7_d_from_db() {

        start_end_date_db s_e_d_db_obj = new start_end_date_db(1, -7);
        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );

            pst = con.prepareStatement("SELECT id_invoice, id_acc, id_pur "
                    + "FROM invoice_management_tb "
                    + "WHERE NOT invoice_man_date >= '" + s_e_d_db_obj.end_date + "' "
                    + "AND invoice_man_date <= '" + s_e_d_db_obj.start_date + "';");
            rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_invoice");
                String acc = get_user_n_by_id(rs.getInt("id_acc"));
                String pur = get_pur_by_id_from_db(rs.getInt("id_pur"));
                switch (pur) {
                    case "exchanging":
                        delete_exe_from_db(id, acc, pur, false);
                        break;
                    case "add_total_money":
                        detele_add_total_to_db(id, acc, pur, false);
                        break;
                    case "to_province":
                        detele_to_pro_to_db(id, acc, pur, false);
                        break;
                    case "from_province":
                        detele_from_pro_to_db(id, acc, pur, false);
                        break;
                    case "double_exchanging":
                        delete_double_exe_from_db(id, acc, pur, false);
                        break;
                    case "to_thai":
                        detele_to_thai_to_db(id, acc, pur, false);
                        break;
                    case "from_thai":
                        detele_from_thai_to_db(id, acc, pur, false);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "error function main class: delete_not_cur_to_last_7_d_from_db", "Alert", JOptionPane.WARNING_MESSAGE);

                }

            }
//            pst = con.prepareStatement("DELETE FROM invoice_management_tb "
//                    + "WHERE NOT invoice_man_date >= '" + pre_7_day_from_start_date + "' "
//                    + "AND invoice_man_date <= '" + start_date + "';");
//            pst.executeUpdate();
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    private int count_cur_to_last_7_d_from_db() {

        int count = 0;
//        num_count_each_d_perform_from_db obj = new num_count_each_d_perform_from_db();
        Calendar start_cal = three_calendar_cld.getCalendar();
        Calendar pre_7_day_from_start_cal = three_calendar_cld.getCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        start_cal.add(Calendar.DAY_OF_MONTH, 1);
        String start_date = sdf.format(start_cal.getTime());
        pre_7_day_from_start_cal.add(Calendar.DAY_OF_MONTH, -7);
        String pre_7_day_from_start_date = sdf.format(pre_7_day_from_start_cal.getTime());

        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );

            pst = con.prepareStatement("select COUNT(*) as count_each_day, dbo.fnFormatDate (invoice_man_date, 'YYYY-MM-DD') as Y_M_D "
                    + "FROM invoice_management_tb "
                    + "WHERE invoice_man_date >= '" + pre_7_day_from_start_date + "' "
                    + "AND invoice_man_date <= '" + start_date + "' "
                    + "GROUP BY dbo.fnFormatDate (invoice_man_date, 'YYYY-MM-DD');");
            rs = pst.executeQuery();

            while (rs.next()) {
//                obj.Date_str.add(rs.getString("Y_M_D"));
                count = count + rs.getInt("count_each_day");
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return count;
    }

    private Boolean is_delete_last_7d() {
        Boolean is_delete = false;
        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT is_delete FROM delete_last_7d_tb;");
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("is_delete") == 1) {
                    is_delete = true;
                }
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return is_delete;
    }

    private void set_delete_last_7d(Boolean set_delete) {
        Connection con;
        PreparedStatement pst;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("UPDATE delete_last_7d_tb "
                    + "SET is_delete = ?;");
            if (set_delete) {
                pst.setInt(1, 1);
            } else {
                pst.setInt(1, 0);
            }
            pst.executeUpdate();
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    public static String get_admin_pass_from_db() {
        String pass = "";
        try {
            Connection con;
            PreparedStatement pst;
            ResultSet rs;
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT password "
                    + "FROM admin_pass_tb;");
            rs = pst.executeQuery();
            while (rs.next()) {
                pass = rs.getString("password");
            }
        } catch (SQLException ex) {
            pass = "lasdjfiohger28394811ernca6-wedf1";
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return pass;
    }

    public static Boolean field_admin_pass() {
        Boolean correct_pass = false;
        JPasswordField pwd = new JPasswordField(10);
        int action = JOptionPane.showConfirmDialog(null, pwd, "Enter Password", JOptionPane.OK_CANCEL_OPTION);
        if (action == JOptionPane.OK_OPTION) {
            correct_pass = pwd.getText().equals(get_admin_pass_from_db());
        } else {
        }
        return correct_pass;
    }

    public static type_of_money convert_to_type_of_m(String money_type) {
        type_of_money exc_rate = null;
        switch (money_type) {
            case "R":
            case "Rial":
            case "rial":
                exc_rate = type_of_money.Rial;
                break;
            case "S":
            case "Dollar":
            case "dollar":
                exc_rate = type_of_money.Dollar;
                break;
            case "B":
            case "Bart":
            case "bart":
                exc_rate = type_of_money.Bart;
                break;
            default:
                all_type_error_mes error_mes = new all_type_error_mes("error function UI_and_operation class: convert_to_type_of_m");
        }
        return exc_rate;
    }

    private void init_component() {

        end_task_ppt();
        open_exc_rate_ppt();

        Toolkit a = Toolkit.getDefaultToolkit();
        int xSize = (int) a.getScreenSize().getWidth();
        int ySize = (int) a.getScreenSize().getHeight();
        this.setSize(xSize, ySize);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        three_calendar_cld.setDate(current_date());

        setTitle("Exchange and Transfer money");

        setIconImage(Toolkit.getDefaultToolkit().getImage("logo_and_icon\\icon\\main_logo.png"));

        custom_four_rate_default();

        custom_three_tb_his_default();

        add_listener_calendar_three();

        add_listener_pt_zero();

        three_up.setEnabled(false);

        next_show_his = 0;

        if (count_db_to_list_pur_and_id() <= num_show_his) {
            three_down.setEnabled(false);
        } else {
            three_down.setEnabled(true);
        }

        del_last_7d_cb.setSelected(is_delete_last_7d());

        set_history();
    }

    /**
     * Creates new form UI_and_operation
     */
    public admin_view() {
        initComponents();
        init_component();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg_to_pro = new javax.swing.ButtonGroup();
        bg_from_pro = new javax.swing.ButtonGroup();
        bg_add_m = new javax.swing.ButtonGroup();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        bg_from_thai = new javax.swing.ButtonGroup();
        zero_tp = new javax.swing.JTabbedPane();
        his_pt = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        three_tb_total_money = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        three_tb_history = new javax.swing.JTable();
        jLabel35 = new javax.swing.JLabel();
        three_calendar_cld = new com.toedter.calendar.JDateChooser();
        date_history_lb = new javax.swing.JLabel();
        three_up = new javax.swing.JButton();
        three_down = new javax.swing.JButton();
        three_chb_id = new javax.swing.JCheckBox();
        three_chb_date = new javax.swing.JCheckBox();
        three_chb_user = new javax.swing.JCheckBox();
        three_chb_pur = new javax.swing.JCheckBox();
        three_chb_m_r = new javax.swing.JCheckBox();
        three_chb_m_s = new javax.swing.JCheckBox();
        three_chb_m_b = new javax.swing.JCheckBox();
        three_chb_m_b_bank = new javax.swing.JCheckBox();
        three_chb_m_detail = new javax.swing.JCheckBox();
        del_last_7d_cb = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        rate_pt = new javax.swing.JPanel();
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
        db_con_pt = new javax.swing.JPanel();
        sql_lb = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        zero_tp.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 48)); // NOI18N

        three_tb_total_money.getTableHeader().setReorderingAllowed(false);
        three_tb_total_money.setFont(new java.awt.Font("Serif", 0, 36)); // NOI18N
        three_tb_total_money.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "$", "B", "៛", "លុយ B ក្នុងកុង"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        three_tb_total_money.setEnabled(false);
        three_tb_total_money.setRowHeight(40);
        three_tb_total_money.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(three_tb_total_money);

        three_tb_history.getTableHeader().setReorderingAllowed(false);
        three_tb_history.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 18)); // NOI18N
        three_tb_history.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ថ្ងៃទី", "លេខប័ណ្ណ", "ចេញប័ណ្ណដោយ", "បំណង", "លុយ R", "លុយ S", "លុយ B", "លុយ B ក្នុងកុង", "សម្រាយ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        three_tb_history.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                three_tb_historyMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(three_tb_history);

        jLabel35.setFont(new java.awt.Font("Khmer OS Battambang", 0, 36)); // NOI18N
        jLabel35.setText("ប្រវត្តិប្រតិបត្តិការ");

        three_calendar_cld.setDateFormatString("dd-MM-yyyy");
        three_calendar_cld.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        three_calendar_cld.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                three_calendar_cldKeyTyped(evt);
            }
        });

        date_history_lb.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N

        three_up.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        three_up.setText("up");
        three_up.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_upActionPerformed(evt);
            }
        });

        three_down.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        three_down.setText("down");
        three_down.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_downActionPerformed(evt);
            }
        });

        three_chb_id.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        three_chb_id.setSelected(true);
        three_chb_id.setText("លេខប័ណ្ណ");
        three_chb_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_chb_idActionPerformed(evt);
            }
        });

        three_chb_date.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        three_chb_date.setSelected(true);
        three_chb_date.setText("ថ្ងៃទី");
        three_chb_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_chb_dateActionPerformed(evt);
            }
        });

        three_chb_user.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        three_chb_user.setSelected(true);
        three_chb_user.setText("ចេញប័ណ្ណដោយ");
        three_chb_user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_chb_userActionPerformed(evt);
            }
        });

        three_chb_pur.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        three_chb_pur.setSelected(true);
        three_chb_pur.setText("បំណង");
        three_chb_pur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_chb_purActionPerformed(evt);
            }
        });

        three_chb_m_r.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        three_chb_m_r.setSelected(true);
        three_chb_m_r.setText("លុយ R");
        three_chb_m_r.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_chb_m_rActionPerformed(evt);
            }
        });

        three_chb_m_s.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        three_chb_m_s.setSelected(true);
        three_chb_m_s.setText("លុយ S");
        three_chb_m_s.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_chb_m_sActionPerformed(evt);
            }
        });

        three_chb_m_b.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        three_chb_m_b.setSelected(true);
        three_chb_m_b.setText("លុយ B");
        three_chb_m_b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_chb_m_bActionPerformed(evt);
            }
        });

        three_chb_m_b_bank.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        three_chb_m_b_bank.setSelected(true);
        three_chb_m_b_bank.setText("លុយ B ក្នុងកុង");
        three_chb_m_b_bank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_chb_m_b_bankActionPerformed(evt);
            }
        });

        three_chb_m_detail.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        three_chb_m_detail.setSelected(true);
        three_chb_m_detail.setText("detail");
        three_chb_m_detail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                three_chb_m_detailActionPerformed(evt);
            }
        });

        del_last_7d_cb.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        del_last_7d_cb.setSelected(true);
        del_last_7d_cb.setText("លុបទិន្ន័យ៧ថ្ងៃចុងក្រោយ");
        del_last_7d_cb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                del_last_7d_cbActionPerformed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("dd-MM-yyyy");

        javax.swing.GroupLayout his_ptLayout = new javax.swing.GroupLayout(his_pt);
        his_pt.setLayout(his_ptLayout);
        his_ptLayout.setHorizontalGroup(
            his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(his_ptLayout.createSequentialGroup()
                .addGroup(his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(his_ptLayout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(three_up, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(three_down)))
                    .addGroup(his_ptLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(his_ptLayout.createSequentialGroup()
                                .addGroup(his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(his_ptLayout.createSequentialGroup()
                                        .addComponent(three_chb_date)
                                        .addGap(51, 51, 51)
                                        .addComponent(three_chb_id)
                                        .addGap(31, 31, 31)
                                        .addComponent(three_chb_user)
                                        .addGap(34, 34, 34)
                                        .addComponent(three_chb_pur)
                                        .addGap(29, 29, 29)
                                        .addComponent(three_chb_m_r)
                                        .addGap(27, 27, 27)
                                        .addComponent(three_chb_m_s)
                                        .addGap(37, 37, 37)
                                        .addComponent(three_chb_m_b)
                                        .addGap(33, 33, 33)
                                        .addComponent(three_chb_m_b_bank)
                                        .addGap(34, 34, 34)
                                        .addComponent(three_chb_m_detail))
                                    .addGroup(his_ptLayout.createSequentialGroup()
                                        .addComponent(jLabel35)
                                        .addGap(18, 18, 18)
                                        .addGroup(his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(three_calendar_cld, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(51, 51, 51)
                                        .addComponent(date_history_lb, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(52, 52, 52)
                                        .addComponent(del_last_7d_cb, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 147, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        his_ptLayout.setVerticalGroup(
            his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(his_ptLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(del_last_7d_cb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(date_history_lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, his_ptLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(three_calendar_cld, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27)
                .addGroup(his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(three_chb_id, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(three_chb_date, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(three_chb_user, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(three_chb_pur, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(three_chb_m_r, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(three_chb_m_s, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(three_chb_m_b, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(three_chb_m_b_bank, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(three_chb_m_detail, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(his_ptLayout.createSequentialGroup()
                        .addComponent(three_up, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(three_down, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE))
                .addContainerGap())
        );

        zero_tp.addTab("Banlance", his_pt);

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
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
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

        javax.swing.GroupLayout rate_ptLayout = new javax.swing.GroupLayout(rate_pt);
        rate_pt.setLayout(rate_ptLayout);
        rate_ptLayout.setHorizontalGroup(
            rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rate_ptLayout.createSequentialGroup()
                .addContainerGap(230, Short.MAX_VALUE)
                .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 835, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(rate_ptLayout.createSequentialGroup()
                        .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel39)
                            .addComponent(jLabel38)
                            .addComponent(jLabel37))
                        .addGap(22, 22, 22)
                        .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(rate_ptLayout.createSequentialGroup()
                                .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(four_B_to_R_one_tf)
                                    .addComponent(four_S_to_B_one_tf, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(four_B_to_R_two_tf)
                                    .addComponent(four_S_to_B_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(rate_ptLayout.createSequentialGroup()
                                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_S_to_B_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_S_to_B_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(rate_ptLayout.createSequentialGroup()
                                        .addComponent(four_B_to_R_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_B_to_R_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(rate_ptLayout.createSequentialGroup()
                                .addComponent(four_S_to_R_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(four_S_to_R_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(four_S_to_R_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(four_S_to_R_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(97, 97, 97)
                        .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(rate_ptLayout.createSequentialGroup()
                                .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel45)
                                    .addComponent(jLabel46))
                                .addGap(31, 31, 31)
                                .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(rate_ptLayout.createSequentialGroup()
                                        .addComponent(four_R_to_S_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(four_R_to_S_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rate_ptLayout.createSequentialGroup()
                                        .addComponent(four_B_to_S_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_B_to_S_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, rate_ptLayout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(four_R_to_S_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_R_to_S_four_tf))
                                    .addGroup(rate_ptLayout.createSequentialGroup()
                                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_B_to_S_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(four_B_to_S_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(rate_ptLayout.createSequentialGroup()
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
                    .addGroup(rate_ptLayout.createSequentialGroup()
                        .addComponent(four_bn_edit_exchange_rate, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(277, 277, 277)))
                .addContainerGap(489, Short.MAX_VALUE))
        );
        rate_ptLayout.setVerticalGroup(
            rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rate_ptLayout.createSequentialGroup()
                .addContainerGap(159, Short.MAX_VALUE)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rate_ptLayout.createSequentialGroup()
                        .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(four_R_to_S_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(four_R_to_S_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(four_R_to_S_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(four_R_to_S_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46))
                        .addGap(38, 38, 38)
                        .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(four_B_to_S_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(four_B_to_S_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(four_B_to_S_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(four_B_to_S_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel45))
                            .addGroup(rate_ptLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel41))))
                    .addGroup(rate_ptLayout.createSequentialGroup()
                        .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(four_S_to_R_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(four_S_to_R_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(four_S_to_R_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(four_S_to_R_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37))
                        .addGap(38, 38, 38)
                        .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(rate_ptLayout.createSequentialGroup()
                                .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(four_S_to_B_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(four_S_to_B_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel38))
                                    .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(four_S_to_B_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(four_S_to_B_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel42))
                            .addGroup(rate_ptLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel40)
                                .addGap(41, 41, 41)
                                .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(four_B_to_R_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(four_B_to_R_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(four_B_to_R_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel39))
                                    .addComponent(four_B_to_R_four_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(four_R_to_B_four_tf, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(four_R_to_B_one_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(four_R_to_B_two_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(four_R_to_B_three_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel44)))))))
                .addGap(69, 69, 69)
                .addComponent(four_bn_edit_exchange_rate, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(133, Short.MAX_VALUE))
        );

        zero_tp.addTab("អត្រាប្តូរប្រាក់", rate_pt);

        sql_lb.setBackground(new java.awt.Color(255, 255, 255));
        sql_lb.setForeground(new java.awt.Color(0, 0, 255));
        sql_lb.setText("sql");
        sql_lb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sql_lbMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout db_con_ptLayout = new javax.swing.GroupLayout(db_con_pt);
        db_con_pt.setLayout(db_con_ptLayout);
        db_con_ptLayout.setHorizontalGroup(
            db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(db_con_ptLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(sql_lb)
                .addContainerGap(1503, Short.MAX_VALUE))
        );
        db_con_ptLayout.setVerticalGroup(
            db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(db_con_ptLayout.createSequentialGroup()
                .addContainerGap(140, Short.MAX_VALUE)
                .addComponent(sql_lb)
                .addGap(651, 651, 651))
        );

        zero_tp.addTab("គណនេយ្យ", db_con_pt);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(zero_tp)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(zero_tp, javax.swing.GroupLayout.DEFAULT_SIZE, 909, Short.MAX_VALUE)
                .addContainerGap())
        );

        zero_tp.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

        exc_rate exc_rate_obj = new exc_rate();
        exc_rate_obj.get_top_1_rate_from_db();

        String S_to_R = S_R_validation(Double.parseDouble(four_S_to_R_one_tf.getText()
                + four_S_to_R_two_tf.getText()
                + four_S_to_R_three_tf.getText()
                + four_S_to_R_four_tf.getText()));
        String R_to_S = S_R_validation(Double.parseDouble(four_R_to_S_one_tf.getText()
                + four_R_to_S_two_tf.getText()
                + four_R_to_S_three_tf.getText()
                + four_R_to_S_four_tf.getText()));
        String S_to_B = S_B_validation(Double.parseDouble(four_S_to_B_one_tf.getText() + four_S_to_B_two_tf.getText()
                + "."
                + four_S_to_B_three_tf.getText()
                + four_S_to_B_four_tf.getText()));
        String B_to_S = S_B_validation(Double.parseDouble(four_B_to_S_one_tf.getText()
                + four_B_to_S_two_tf.getText()
                + "."
                + four_B_to_S_three_tf.getText()
                + four_B_to_S_four_tf.getText()));
        String B_to_R = R_B_validation(Double.parseDouble(four_B_to_R_one_tf.getText()
                + four_B_to_R_two_tf.getText()
                + four_B_to_R_three_tf.getText()
                + "."
                + four_B_to_R_four_tf.getText()));
        String R_to_B = R_B_validation(Double.parseDouble(four_R_to_B_one_tf.getText()
                + four_R_to_B_two_tf.getText()
                + four_R_to_B_three_tf.getText()
                + "."
                + four_R_to_B_four_tf.getText()));

        if (!(exc_rate_obj.getS_to_R().equals(S_to_R)
                && exc_rate_obj.getR_to_S().equals(R_to_S)
                && exc_rate_obj.getS_to_B().equals(S_to_B)
                && exc_rate_obj.getB_to_S().equals(B_to_S)
                && exc_rate_obj.getB_to_R().equals(B_to_R)
                && exc_rate_obj.getR_to_B().equals(R_to_B))) {

            set_rate_to_db(S_to_R, R_to_S, S_to_B, B_to_S, B_to_R, R_to_B);

            end_task_ppt();
            set_rate_to_excel(S_to_R, R_to_S, S_to_B, B_to_S, B_to_R, R_to_B);
            open_exc_rate_ppt();

//                exe_rate_show.set_rate(S_to_R_tf, R_to_S_tf, S_to_B_tf, B_to_S_tf, B_to_R_tf, R_to_B_tf);
            JOptionPane.showMessageDialog(this, "Exchange rate saved.");
        } else {
            JOptionPane.showMessageDialog(this, "You change nothing.");
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

    private void three_tb_historyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_three_tb_historyMouseClicked
        boolean is_edit_tb = three_tb_history.isEditing();
        if (is_edit_tb == false) {
//            JOptionPane.showMessageDialog(this, "You can not edit this table");
            Object[] options = {dialog_choose_p_d_c.Print, dialog_choose_p_d_c.Delete, dialog_choose_p_d_c.Close};
            dialog_choose_p_d_c choose_from_dialog = dialog_choose_p_d_c.Close;
            int idx = JOptionPane.showOptionDialog(this, "what you choose?", null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            if (idx != -1) {
                choose_from_dialog = dialog_choose_p_d_c.values()[idx];
            }
            switch (choose_from_dialog) {
                case Print:

                    if (!three_tb_history.getSelectionModel().isSelectionEmpty()) {

                        //table in UI
                        DefaultTableModel model = (DefaultTableModel) three_tb_history.getModel();
                        //select index of the table row
                        int selectedIndex = three_tb_history.getSelectedRow();

                        //get id from to store inside variable id here
                        int id = Integer.parseInt(model.getValueAt(selectedIndex, 1).toString());
                        String pur = convert_kh_to_pur(model.getValueAt(selectedIndex, 3).toString());
                        switch (pur) {
                            case "exchanging":
//                                id = get_id_inv_by_id_inv_man_from_db(id);
                                print_reciept_exc(get_path() + exc_reciept_path, get_id_inv_by_id_inv_man_from_db(id), get_type_exc(id));
                                break;
                            case "double_exchanging":
                                print_2_exc(id);
                                break;
                            default:
                                JOptionPane.showMessageDialog(this, "error function main class: three_tb_historyMouseClicked", "Alert", JOptionPane.WARNING_MESSAGE);

                        }
                    }
                    break;
                case Delete:

                    if (!three_tb_history.getSelectionModel().isSelectionEmpty()) {
                        if (field_admin_pass()) {
                            //table in UI
                            DefaultTableModel model = (DefaultTableModel) three_tb_history.getModel();
                            //select index of the table row
                            int selectedIndex = three_tb_history.getSelectedRow();

                            //get id from to store inside variable id here
                            int id = -1;

                            id = get_id_invoice(Integer.parseInt(model.getValueAt(selectedIndex, 1).toString()));
                            String acc = model.getValueAt(selectedIndex, 2).toString();
                            String pur = convert_kh_to_pur(model.getValueAt(selectedIndex, 3).toString());
                            int dialogresult = JOptionPane.showConfirmDialog(this, "Do you want to delete the record", "Warning", JOptionPane.YES_NO_OPTION);

                            if (dialogresult == JOptionPane.YES_NO_OPTION) {
                                if (get_count_id_invoice_man_from_db(id, acc, pur) <= 10000) {
                                    switch (pur) {
                                        case "exchanging":
                                            delete_exe_from_db(id, acc, pur, true);
                                            break;
                                        case "add_total_money":
                                            detele_add_total_to_db(id, acc, pur, true);
                                            break;
                                        case "to_province":
                                            detele_to_pro_to_db(id, acc, pur, true);
                                            break;
                                        case "from_province":
                                            detele_from_pro_to_db(id, acc, pur, true);
                                            break;
                                        case "double_exchanging":
                                            delete_double_exe_from_db(id, acc, pur, true);
                                            break;
                                        case "to_thai":
                                            detele_to_thai_to_db(id, acc, pur, true);
                                            break;
                                        case "from_thai":
                                            detele_from_thai_to_db(id, acc, pur, true);
                                            break;
                                        default:
                                            JOptionPane.showMessageDialog(this, "error function main class: three_tb_historyMouseClicked", "Alert", JOptionPane.WARNING_MESSAGE);

                                    }
                                    if (count_db_to_list_pur_and_id() <= num_show_his) {
                                        three_up.setEnabled(false);
                                        next_show_his = 0;
                                        three_down.setEnabled(false);
                                    } else {
                                        three_down.setEnabled(true);
                                    }
                                    set_history();
                                } else {
                                    JOptionPane.showMessageDialog(this, "your data is more than 10000 when count from the top. So you can't delete it", "Alert", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Incorrect password", "Alert", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    break;
            }
        }
    }//GEN-LAST:event_three_tb_historyMouseClicked

    private void three_upActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_upActionPerformed

        if ((next_show_his - num_show_his) >= 0) {
            next_show_his = next_show_his - num_show_his;
            three_down.setEnabled(true);
            if (!((next_show_his - num_show_his) >= 0)) {
                three_up.setEnabled(false);
            }
        }
        set_history();
    }//GEN-LAST:event_three_upActionPerformed

    private void three_downActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_downActionPerformed
        if ((next_show_his + num_show_his) <= count_db_to_list_pur_and_id()) {
            next_show_his = next_show_his + num_show_his;
            three_up.setEnabled(true);
            if (!((next_show_his + num_show_his) <= count_db_to_list_pur_and_id())) {
                three_down.setEnabled(false);
            }
        }
        set_history();
    }//GEN-LAST:event_three_downActionPerformed

    private void three_chb_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_chb_idActionPerformed
        hide_or_show_column_his(three_chb_id, 1, 100, 150, 10);
    }//GEN-LAST:event_three_chb_idActionPerformed

    private void three_chb_userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_chb_userActionPerformed
        hide_or_show_column_his(three_chb_user, 2, 150, 170, 10);
    }//GEN-LAST:event_three_chb_userActionPerformed

    private void three_chb_purActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_chb_purActionPerformed
        hide_or_show_column_his(three_chb_pur, 3, 175, 200, 10);
    }//GEN-LAST:event_three_chb_purActionPerformed

    private void three_chb_m_rActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_chb_m_rActionPerformed
        hide_or_show_column_his(three_chb_m_r, 4, 130, 200, 10);
    }//GEN-LAST:event_three_chb_m_rActionPerformed

    private void three_chb_m_sActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_chb_m_sActionPerformed
        hide_or_show_column_his(three_chb_m_s, 5, 130, 200, 10);
    }//GEN-LAST:event_three_chb_m_sActionPerformed

    private void three_chb_m_bActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_chb_m_bActionPerformed
        hide_or_show_column_his(three_chb_m_b, 6, 130, 200, 10);
    }//GEN-LAST:event_three_chb_m_bActionPerformed

    private void three_chb_m_b_bankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_chb_m_b_bankActionPerformed
        hide_or_show_column_his(three_chb_m_b_bank, 7, 140, 200, 10);
    }//GEN-LAST:event_three_chb_m_b_bankActionPerformed

    private void three_chb_m_detailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_chb_m_detailActionPerformed
        hide_or_show_column_his(three_chb_m_detail, 8, 1000, 1500, 10);
    }//GEN-LAST:event_three_chb_m_detailActionPerformed

    private void three_chb_dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_chb_dateActionPerformed
        hide_or_show_column_his(three_chb_date, 0, 230, 300, 10);
    }//GEN-LAST:event_three_chb_dateActionPerformed

    private void three_calendar_cldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_three_calendar_cldKeyTyped

    }//GEN-LAST:event_three_calendar_cldKeyTyped

    private void del_last_7d_cbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_del_last_7d_cbActionPerformed
        if (del_last_7d_cb.isSelected()) {
            set_delete_last_7d(true);
            set_history();
        } else {
            set_delete_last_7d(false);
        }
    }//GEN-LAST:event_del_last_7d_cbActionPerformed

    private void sql_lbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sql_lbMouseClicked
        sql_con sql_con_obj = new sql_con(this);
        sql_con_obj.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_sql_lbMouseClicked

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
            java.util.logging.Logger.getLogger(admin_view.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(admin_view.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(admin_view.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(admin_view.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new admin_view().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bg_add_m;
    private javax.swing.ButtonGroup bg_from_pro;
    private javax.swing.ButtonGroup bg_from_thai;
    private javax.swing.ButtonGroup bg_to_pro;
    private javax.swing.JLabel date_history_lb;
    private javax.swing.JPanel db_con_pt;
    private javax.swing.JCheckBox del_last_7d_cb;
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
    private javax.swing.JPanel his_pt;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel rate_pt;
    private javax.swing.JLabel sql_lb;
    private com.toedter.calendar.JDateChooser three_calendar_cld;
    private javax.swing.JCheckBox three_chb_date;
    private javax.swing.JCheckBox three_chb_id;
    private javax.swing.JCheckBox three_chb_m_b;
    private javax.swing.JCheckBox three_chb_m_b_bank;
    private javax.swing.JCheckBox three_chb_m_detail;
    private javax.swing.JCheckBox three_chb_m_r;
    private javax.swing.JCheckBox three_chb_m_s;
    private javax.swing.JCheckBox three_chb_pur;
    private javax.swing.JCheckBox three_chb_user;
    private javax.swing.JButton three_down;
    private javax.swing.JTable three_tb_history;
    private javax.swing.JTable three_tb_total_money;
    private javax.swing.JButton three_up;
    private javax.swing.JTabbedPane zero_tp;
    // End of variables declaration//GEN-END:variables
}
