/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import static UI_and_operation.account.*;
import static UI_and_operation.add_total_money.detele_add_total_to_db;
import static UI_and_operation.add_total_money.get_add_total_db_set_to_tb;
import static UI_and_operation.connection_to_ms_sql.*;
import static UI_and_operation.exc_rate.end_task_ppt;
import static UI_and_operation.exc_rate.open_exc_rate_ppt;
import static UI_and_operation.exc_rate.set_rate_to_db;
import static UI_and_operation.exc_rate.set_rate_to_excel;
import static UI_and_operation.exchanging.R_B_validation;
import static UI_and_operation.exchanging.S_B_validation;
import static UI_and_operation.exchanging.S_R_validation;
import static UI_and_operation.exchanging.*;
import static UI_and_operation.from_province.detele_from_pro_to_db;
import static UI_and_operation.from_province.get_from_pro_db_set_to_tb;
import static UI_and_operation.from_province.two_two_cal;
import static UI_and_operation.invoice_man.get_count_id_invoice_man_from_db;
import static UI_and_operation.invoice_man.get_id_invoice;
import static UI_and_operation.invoice_man.is_null_acc_id_invoice_man;
import static UI_and_operation.path_file.double_exc_reciept_path;
import static UI_and_operation.path_file.exc_reciept_path;
import static UI_and_operation.path_file.get_path;
import static UI_and_operation.path_file.set_main_proj_path_to_db;
import static UI_and_operation.purpose.*;
import static UI_and_operation.reciept.print_reciept;
import static UI_and_operation.to_province.two_one_cal;
import static UI_and_operation.validate_value.*;
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
import static UI_and_operation.to_province.detele_to_pro_to_db;
import static UI_and_operation.to_province.get_to_pro_db_set_to_tb;
import static UI_and_operation.to_province.set_to_pro_to_db;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Chhann_chikay
 */
public class UI_and_operation extends javax.swing.JFrame {

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

    private int next_show_his = 0;
    private int num_show_his = 10;
    private int idx_transfer_pt = 0;
    public static String set_admin_password = "";
    private Boolean two_one_is_off_edit = true;
    //to store which type of exchange that user performs, by defauld selected_exchange_rate = not_select
    private type_of_exchange selected_exchange_rate = type_of_exchange.not_select;
    private type_of_money selected_money_type_exe;
    private type_of_money selected_money_type_to_pro;
    private type_of_money selected_money_type_from_pro;
    private static Boolean is_change_his = false;

    //------------------------------------------------------my function------------------------------------------------------
    public static void set_is_change_true() {
        is_change_his = true;
    }

    public int get_idx_transfer_pt() {
        return idx_transfer_pt;
    }

    public void hide_or_show_column_his(javax.swing.JCheckBox chb, int idx_col, int set_col_size, int set_max_col_size, int set_min_col_size) {
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

    public javax.swing.JComboBox<String> get_to_pro_cb_from_ui_oper() {
        return two_one_pro_name_cb;
    }

    public javax.swing.JComboBox<String> get_from_pro_cb_from_ui_oper() {
        return two_two_pro_name_cb;
    }

    public javax.swing.JComboBox<String> get_from_bank_thai_cb_from_ui_oper() {
        return two_three_bank_thai_cb;
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
            System.err.println(ex);
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
            System.err.println(ex);
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
            System.err.println(ex);
        }
        return id;
    }

    public static String convert_to_short_money_type(String money_type) {

        switch (money_type) {
            case "Rial":
                return "៛";
            case "Dollar":

                return "$";
            case "Bart":

                return "B";
            default:
                return "error";
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
                pst = con.prepareStatement("SELECT " + col + " FROM " + tb + " WHERE " + col + " LIKE '%" + value + "%';");
                rs = pst.executeQuery();
                while (rs.next()) {
                    two_one_mode.addElement(rs.getString(col));
                }
            } catch (SQLException ex) {
                System.err.println(ex);
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
                pst = con.prepareStatement("SELECT " + col_target + ", " + col_1 + ", " + col_2 + ", " + col_3
                        + " FROM " + tb
                        + " WHERE " + col_target + " LIKE '%" + value + "%';");
                rs = pst.executeQuery();
                while (rs.next()) {
                    two_one_mode.addElement(rs.getString(col_target) + " | " + rs.getString(col_1) + " | " + rs.getString(col_2) + " | " + rs.getString(col_3));
                }
            } catch (SQLException ex) {
                System.err.println(ex);
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
//                        System.out.println("set_val.charAt(j - 1) : " + set_val.charAt(j - 1));
                        if (set_val.charAt(j - 1) == ' ' && set_val.charAt(j) == '|' && set_val.charAt(j + 1) == ' ') {
                            set_on_tf2.setText(set_val.substring(i + 2, j - 1));

                            for (int k = j + 2; k < set_val.length() - 1; k++) {
//                        System.out.println("set_val.charAt(j - 1) : " + set_val.charAt(j - 1));
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
            System.err.println(ex);
        }
        return id_inv_man;
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
            System.err.println("error: two_three_bn_finish\n" + ex);
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
            System.err.println("error: two_three_bn_finish\n" + ex);
        }
        return id;

    }

    class list_pur_and_id {

        ArrayList<String> pur_type = new ArrayList<>();
        ArrayList<Integer> id_invoice = new ArrayList<>();

        list_pur_and_id() {
            pur_type = new ArrayList<>();
            id_invoice = new ArrayList<>();
        }
    }

    private void get_from_db_set_total_in_tb() {

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
            pst = con.prepareStatement("SELECT TOP 1 rial, dollar, bart, bank_bart "
                    + "FROM invoice_management_tb ORDER BY invoice_man_date DESC;");
            rs = pst.executeQuery();
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
            System.err.println("error: two_three_bn_finish\n" + ex);
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
            System.err.println("error: two_three_bn_finish\n" + ex);
        }
        return count;
    }

    private list_pur_and_id set_data_db_to_list_pur_and_id() {

        list_pur_and_id list_pur_id_obj = new list_pur_and_id();
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
            pst = con.prepareStatement("SELECT id_invoice, pur_type "
                    + "FROM invoice_management_tb INNER JOIN purpose_tb "
                    + "ON invoice_management_tb.id_pur = purpose_tb.id_pur "
                    + "WHERE id_acc = ? AND invoice_man_date >= '" + pre_7_day_from_start_date + "' "
                    + "AND invoice_man_date <= '" + start_date + "' "
                    + "ORDER BY id_invoice_man DESC "
                    + "OFFSET " + next_show_his + " ROWS "
                    + "FETCH NEXT " + num_show_his + " ROWS ONLY;");
            pst.setInt(1, get_acc_id());
            rs = pst.executeQuery();

            while (rs.next()) {
                list_pur_id_obj.pur_type.add(rs.getString("pur_type"));
                list_pur_id_obj.id_invoice.add(rs.getInt("id_invoice"));
            }

        } catch (SQLException ex) {
            System.err.println("error: two_three_bn_finish\n" + ex);
        }
        return list_pur_id_obj;
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

    public void set_history() {
//        System.out.println("count_db_to_list_pur_and_id() : " + count_db_to_list_pur_and_id());
        if (three_calendar_cld.getDate() != null) {
            three_calendar_cld.setBackground(Color.lightGray);
            list_pur_and_id list_pur_id_obj = new list_pur_and_id();
            if (!is_null_acc_id_invoice_man(get_acc_id())) {
                get_from_db_set_total_in_tb();
                list_pur_id_obj = set_data_db_to_list_pur_and_id();
            } else {
                set_to_tb("0", "0", "0", "0");
            }

            ArrayList<Vector> v2 = new ArrayList<Vector>();
            DefaultTableModel dft = (DefaultTableModel) three_tb_history.getModel();
            dft.setRowCount(0);
            for (int i = 0; i < list_pur_id_obj.id_invoice.size(); i++) {
                switch (list_pur_id_obj.pur_type.get(i)) {
                    case "exchanging":
                        get_exe_db_set_to_tb(list_pur_id_obj.id_invoice.get(i), v2);
                        break;
                    case "add_total_money":
                        get_add_total_db_set_to_tb(list_pur_id_obj.id_invoice.get(i), v2);
                        break;
                    case "to_province":
                        get_to_pro_db_set_to_tb(list_pur_id_obj.id_invoice.get(i), v2);
                        break;
                    case "from_province":
                        get_from_pro_db_set_to_tb(list_pur_id_obj.id_invoice.get(i), v2);
                        break;
                    case "double_exchanging":
                        get_from_pro_db_set_to_tb_double_exc(list_pur_id_obj.id_invoice.get(i), v2);
                        break;
                    default:
                        System.out.println("error");
                }

            }
            if (v2.size() != 0) {
                for (int i = 0; i < v2.size(); i++) {
                    dft.addRow(v2.get(i));
                }
            }
        } else {
            three_calendar_cld.setBackground(Color.red);
            JOptionPane.showMessageDialog(this, "Wrong day or month or year");
        }
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
            System.err.println("error: two_three_bn_finish\n" + ex);
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
            System.err.println("error: two_three_bn_finish\n" + ex);
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
            System.err.println("error: two_three_bn_finish\n" + ex);
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
            System.err.println("error: two_three_bn_finish\n" + ex);
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
    }

    private void set_con_db() {

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
    }

    private void add_listener_calendar_three() {

        three_calendar_cld.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                three_up.setEnabled(false);
                next_show_his = 0;
                if (count_db_to_list_pur_and_id() < num_show_his) {
                    three_down.setEnabled(false);
                } else {
                    three_down.setEnabled(true);
                }
                set_history();
            }
        });
    }

    void add_listener_pt_change_on_his() {

        zero_tp.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
//            System.out.println("Tab: " + zero_tp.getSelectedIndex());
                if (zero_tp.getSelectedIndex() == 2 && is_change_his) {
                    set_history();
                    is_change_his = false;
                }
            }
        });
    }
    
    void add_listener_pt_change_on_transfer() {

        zero_tp.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
//            System.out.println("Tab: " + zero_tp.getSelectedIndex());
                if (zero_tp.getSelectedIndex() == 2 && is_change_his) {
                    set_history();
                    is_change_his = false;
                }
            }
        });
    }
    

    //-----------------------------------------------------class call--------------------------------------------------
    exchange_rate_show exe_rate_show = new exchange_rate_show();

    /**
     * Creates new form UI_and_operation
     */
    public UI_and_operation() {

//        this.setUndecorated(false);
//        this.setAlwaysOnTop(true);
//        this.setResizable(true);
//        this.setVisible(true);
        initComponents();

        Toolkit a = Toolkit.getDefaultToolkit();
        int xSize = (int) a.getScreenSize().getWidth();
        int ySize = (int) a.getScreenSize().getHeight();
        this.setSize(xSize, ySize);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        three_calendar_cld.setDate(current_date());

        //set account user to class for waiting to use
        setAccount(five_user_name_tf.getText(), five_password_tf.getText());

        set_con_db();

        set_main_proj_path_to_db();

        setTitle("Exchange and Transfer money");

        setIconImage(Toolkit.getDefaultToolkit().getImage("logo_and_icon\\icon\\main_logo.png"));

        set_cb(two_one_pro_name_cb, "transfer_province", "province_name_history_tb");
        set_cb(two_two_pro_name_cb, "transfer_province", "province_name_history_tb");

        one_tf_customer_money.requestFocus();

        three_up.setEnabled(false);

        custom_four_rate_default();

        custom_three_tb_his_default();

        set_history();

        add_listener_calendar_three();

        add_listener_pt_change_on_his();
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
        zero_tp = new javax.swing.JTabbedPane();
        exc_pt = new javax.swing.JPanel();
        sub_exc_pt = new javax.swing.JTabbedPane();
        exc_pn = new javax.swing.JPanel();
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
        double_exc_pn = new javax.swing.JPanel();
        one_lb_customer_money1 = new javax.swing.JLabel();
        one_tf_customer_money1 = new javax.swing.JTextField();
        one_lb_customer_result1 = new javax.swing.JLabel();
        one_tf_customer_result1 = new javax.swing.JTextField();
        one_lb_exchange_rate1 = new javax.swing.JLabel();
        one_two_rate_bc1 = new javax.swing.JComboBox<>();
        one_lb_equal1 = new javax.swing.JLabel();
        one_lb_operator1 = new javax.swing.JLabel();
        one_two_bn_finished = new javax.swing.JButton();
        one_bn_print1 = new javax.swing.JButton();
        one_lb_customer_money2 = new javax.swing.JLabel();
        one_tf_customer_money2 = new javax.swing.JTextField();
        one_tf_customer_result2 = new javax.swing.JTextField();
        one_lb_customer_result2 = new javax.swing.JLabel();
        one_lb_equal2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        one_tf_exchange_rate1 = new javax.swing.JTextField();
        one_lb_exchange_rate3 = new javax.swing.JLabel();
        one_two_rate_bc2 = new javax.swing.JComboBox<>();
        one_tf_exchange_rate2 = new javax.swing.JTextField();
        one_lb_operator2 = new javax.swing.JLabel();
        one_money_type_lb1 = new javax.swing.JLabel();
        one_money_type_lb2 = new javax.swing.JLabel();
        one_money_type_lb3_ = new javax.swing.JLabel();
        one_money_type_lb4 = new javax.swing.JLabel();
        tran_pt = new javax.swing.JPanel();
        sub_tran_pt = new javax.swing.JTabbedPane();
        to_pro_pn = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        two_three_sender_money_tf = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        two_three_service_money_tf = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        two_one_total_money_tf = new javax.swing.JTextField();
        two_three_balance_money_tf = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        two_three_bn_finish = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        two_three_dollar_money_rb = new javax.swing.JRadioButton();
        two_three_rial_money_rb = new javax.swing.JRadioButton();
        two_three_bart_money_rb = new javax.swing.JRadioButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        two_three_sender_phone_no_tf = new javax.swing.JTextField();
        two_one_ph_reciever_layer_pane = new javax.swing.JLayeredPane();
        two_one_ph_reciever_list = new javax.swing.JList<>();
        two_one_ph_senter_layer_pane = new javax.swing.JLayeredPane();
        two_one_ph_senter_list = new javax.swing.JList<>();
        jLabel25 = new javax.swing.JLabel();
        two_three_receiver_phone_no_tf = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        two_one_pro_his_bn = new javax.swing.JButton();
        two_one_sender_his_bn = new javax.swing.JButton();
        two_one_reciever_his_bn = new javax.swing.JButton();
        two_one_pro_name_cb = new javax.swing.JComboBox<>();
        two_one_edit_bn = new javax.swing.JButton();
        from_pro_pn = new javax.swing.JPanel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jLabel23 = new javax.swing.JLabel();
        two_four_reciece_money_tf = new javax.swing.JTextField();
        two_four_balance_money_tf = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        two_four_total_money_tf = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        two_four_bn_finish = new javax.swing.JButton();
        two_four_rial_money_rb = new javax.swing.JRadioButton();
        two_four_dollar_money_rb = new javax.swing.JRadioButton();
        two_four_bart_money_rb = new javax.swing.JRadioButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        two_four_receiver_phone_no_tf = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        two_two_pro_name_cb = new javax.swing.JComboBox<>();
        two_two_reciever_his_bn = new javax.swing.JButton();
        two_one_pro_his_bn1 = new javax.swing.JButton();
        two_two_ph_recieve_layer_pane = new javax.swing.JLayeredPane();
        two_two_ph_recieve_list = new javax.swing.JList<>();
        to_thai_pn = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        two_one_tf_cus_money = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        two_one_tf_service_money = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        two_one_tf_total_money = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        two_one_bn_finish = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        two_one_tf_cus_no = new javax.swing.JTextField();
        two_one_tf_cus_name = new javax.swing.JTextField();
        two_three_bank_info_his_bn = new javax.swing.JButton();
        two_three_to_thai_bank_id_layer_pane = new javax.swing.JLayeredPane();
        two_three_to_thai_bank_id_list = new javax.swing.JList<>();
        jLabel19 = new javax.swing.JLabel();
        two_three_to_thai_name_layer_pane = new javax.swing.JLayeredPane();
        two_three_to_thai_name_list = new javax.swing.JList<>();
        jLabel57 = new javax.swing.JLabel();
        two_three_tf_cus_ph_no = new javax.swing.JTextField();
        two_three_to_thai_ph_no_layer_pane = new javax.swing.JLayeredPane();
        two_three_to_thai_ph_no_list = new javax.swing.JList<>();
        two_three_bank_thai_cb = new javax.swing.JComboBox<>();
        two_three_bank_his_bn = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        from_thai_pn = new javax.swing.JPanel();
        print = new javax.swing.JButton();
        two_two_bn_finish = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        two_two_reveiver_money_tf = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        two_two_service_money_tf = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        two_two_result_money_tf = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        two_two_reveiver_ph_no_tf = new javax.swing.JTextField();
        his_pt = new javax.swing.JPanel();
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
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        five_dev_permission_tf = new javax.swing.JTextField();
        five_dev_permission_bn = new javax.swing.JButton();

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

        zero_tp.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 48)); // NOI18N

        sub_exc_pt.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N

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
        one_tf_customer_result.setFocusable(false);
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

        javax.swing.GroupLayout exc_pnLayout = new javax.swing.GroupLayout(exc_pn);
        exc_pn.setLayout(exc_pnLayout);
        exc_pnLayout.setHorizontalGroup(
            exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exc_pnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(one_bn_R_to_S, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(one_bn_S_to_R, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, exc_pnLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(exc_pnLayout.createSequentialGroup()
                                .addComponent(one_bn_finished, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(one_bn_print, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(exc_pnLayout.createSequentialGroup()
                                .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(exc_pnLayout.createSequentialGroup()
                                        .addComponent(one_lb_customer_money)
                                        .addGap(18, 18, 18)
                                        .addComponent(one_money_type_lb, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                                    .addComponent(one_tf_customer_money))
                                .addGap(5, 5, 5)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(exc_pnLayout.createSequentialGroup()
                        .addComponent(one_lb_operator, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(exc_pnLayout.createSequentialGroup()
                                .addComponent(one_lb_exchange_rate)
                                .addGap(18, 18, 18)
                                .addComponent(one_rate_type_lb, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))
                            .addComponent(one_tf_exchange_rate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(one_lb_equal))
                    .addComponent(one_bn_B_to_S, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(one_bn_S_to_B, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(one_bn_B_to_R, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(one_bn_R_to_B, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(exc_pnLayout.createSequentialGroup()
                        .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(exc_pnLayout.createSequentialGroup()
                                .addComponent(one_lb_customer_result, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(one_money_type_result_lb, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
                            .addComponent(one_tf_customer_result))
                        .addGap(50, 50, 50)))
                .addContainerGap())
        );
        exc_pnLayout.setVerticalGroup(
            exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exc_pnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(one_bn_B_to_R, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(one_bn_S_to_B, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(one_bn_S_to_R, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(one_bn_R_to_S, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(one_bn_R_to_B, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(one_bn_B_to_S, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 29, Short.MAX_VALUE)
                .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(one_lb_customer_money)
                        .addComponent(one_money_type_lb, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(one_lb_exchange_rate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(one_lb_customer_result)
                        .addComponent(one_money_type_result_lb, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(one_rate_type_lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(one_tf_exchange_rate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(one_tf_customer_result, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, exc_pnLayout.createSequentialGroup()
                            .addComponent(one_lb_equal)
                            .addGap(8, 8, 8)))
                    .addComponent(one_tf_customer_money, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(one_lb_operator, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59)
                .addGroup(exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(one_bn_finished, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(one_bn_print, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(118, Short.MAX_VALUE))
        );

        sub_exc_pt.addTab("ប្តូរប្រាក់មួយមុខ", exc_pn);

        one_lb_customer_money1.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        one_lb_customer_money1.setText("ទឹកប្រាក់ទទួល");

        one_tf_customer_money1.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        one_tf_customer_money1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                one_tf_customer_money1CaretUpdate(evt);
            }
        });
        one_tf_customer_money1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_tf_customer_money1ActionPerformed(evt);
            }
        });
        one_tf_customer_money1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_tf_customer_money1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                one_tf_customer_money1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                one_tf_customer_money1KeyTyped(evt);
            }
        });

        one_lb_customer_result1.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        one_lb_customer_result1.setText("ទឹកប្រាក់ប្រគល់");

        one_tf_customer_result1.setEditable(false);
        one_tf_customer_result1.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        one_tf_customer_result1.setFocusable(false);
        one_tf_customer_result1.setRequestFocusEnabled(false);
        one_tf_customer_result1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_tf_customer_result1ActionPerformed(evt);
            }
        });

        one_lb_exchange_rate1.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        one_lb_exchange_rate1.setText("អត្រាប្តូរប្រាក់");

        one_two_rate_bc1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        one_two_rate_bc1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "$ → ៛", "៛ → $", "$ → ฿", "฿ → $", "฿ → ៛", "៛ → ฿" }));
        one_two_rate_bc1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_two_rate_bc1ActionPerformed(evt);
            }
        });

        one_lb_equal1.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        one_lb_equal1.setText("=");

        one_lb_operator1.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N

        one_two_bn_finished.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        one_two_bn_finished.setText("រួចរាល់");
        one_two_bn_finished.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_two_bn_finishedActionPerformed(evt);
            }
        });
        one_two_bn_finished.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_two_bn_finishedKeyPressed(evt);
            }
        });

        one_bn_print1.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N
        one_bn_print1.setText("ព្រីនវិក្កិយបត្រ");
        one_bn_print1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_bn_print1ActionPerformed(evt);
            }
        });
        one_bn_print1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_bn_print1KeyPressed(evt);
            }
        });

        one_lb_customer_money2.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        one_lb_customer_money2.setText("ទឹកប្រាក់ទទួល");

        one_tf_customer_money2.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        one_tf_customer_money2.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                one_tf_customer_money2CaretUpdate(evt);
            }
        });
        one_tf_customer_money2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_tf_customer_money2ActionPerformed(evt);
            }
        });
        one_tf_customer_money2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_tf_customer_money2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                one_tf_customer_money2KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                one_tf_customer_money2KeyTyped(evt);
            }
        });

        one_tf_customer_result2.setEditable(false);
        one_tf_customer_result2.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        one_tf_customer_result2.setFocusable(false);
        one_tf_customer_result2.setRequestFocusEnabled(false);
        one_tf_customer_result2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_tf_customer_result2ActionPerformed(evt);
            }
        });

        one_lb_customer_result2.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        one_lb_customer_result2.setText("ទឹកប្រាក់ប្រគល់");

        one_lb_equal2.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        one_lb_equal2.setText("=");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("2");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel2.setText("1");

        one_tf_exchange_rate1.setEditable(false);
        one_tf_exchange_rate1.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        one_tf_exchange_rate1.setFocusable(false);
        one_tf_exchange_rate1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                one_tf_exchange_rate1CaretUpdate(evt);
            }
        });
        one_tf_exchange_rate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_tf_exchange_rate1ActionPerformed(evt);
            }
        });
        one_tf_exchange_rate1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_tf_exchange_rate1KeyPressed(evt);
            }
        });

        one_lb_exchange_rate3.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        one_lb_exchange_rate3.setText("អត្រាប្តូរប្រាក់");

        one_two_rate_bc2.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        one_two_rate_bc2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "$ → ៛", "៛ → $", "$ → ฿", "฿ → $", "฿ → ៛", "៛ → ฿" }));
        one_two_rate_bc2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_two_rate_bc2ActionPerformed(evt);
            }
        });

        one_tf_exchange_rate2.setEditable(false);
        one_tf_exchange_rate2.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        one_tf_exchange_rate2.setFocusable(false);
        one_tf_exchange_rate2.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                one_tf_exchange_rate2CaretUpdate(evt);
            }
        });
        one_tf_exchange_rate2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                one_tf_exchange_rate2ActionPerformed(evt);
            }
        });
        one_tf_exchange_rate2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                one_tf_exchange_rate2KeyPressed(evt);
            }
        });

        one_lb_operator2.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N

        one_money_type_lb1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N

        one_money_type_lb2.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N

        one_money_type_lb3_.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N

        one_money_type_lb4.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N

        javax.swing.GroupLayout double_exc_pnLayout = new javax.swing.GroupLayout(double_exc_pn);
        double_exc_pn.setLayout(double_exc_pnLayout);
        double_exc_pnLayout.setHorizontalGroup(
            double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(double_exc_pnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(24, 24, 24)
                .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(double_exc_pnLayout.createSequentialGroup()
                        .addComponent(one_two_bn_finished, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(one_bn_print1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(double_exc_pnLayout.createSequentialGroup()
                        .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(double_exc_pnLayout.createSequentialGroup()
                                .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(double_exc_pnLayout.createSequentialGroup()
                                        .addComponent(one_lb_customer_money2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(one_money_type_lb3_, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
                                    .addComponent(one_tf_customer_money2, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(one_lb_operator2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, double_exc_pnLayout.createSequentialGroup()
                                .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, double_exc_pnLayout.createSequentialGroup()
                                        .addComponent(one_lb_customer_money1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(one_money_type_lb1, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))
                                    .addComponent(one_tf_customer_money1))
                                .addGap(18, 18, 18)
                                .addComponent(one_lb_operator1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)))
                        .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(double_exc_pnLayout.createSequentialGroup()
                                .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(double_exc_pnLayout.createSequentialGroup()
                                        .addComponent(one_lb_exchange_rate3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(one_two_rate_bc2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, double_exc_pnLayout.createSequentialGroup()
                                        .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(double_exc_pnLayout.createSequentialGroup()
                                                .addComponent(one_tf_exchange_rate2)
                                                .addGap(2, 2, 2))
                                            .addGroup(double_exc_pnLayout.createSequentialGroup()
                                                .addComponent(one_tf_exchange_rate1)
                                                .addGap(5, 5, 5)))
                                        .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(one_lb_equal1)
                                            .addComponent(one_lb_equal2, javax.swing.GroupLayout.Alignment.TRAILING))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(double_exc_pnLayout.createSequentialGroup()
                                .addComponent(one_lb_exchange_rate1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(one_two_rate_bc1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(138, 138, 138)))
                        .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(double_exc_pnLayout.createSequentialGroup()
                                .addComponent(one_lb_customer_result1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(one_money_type_lb4, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
                            .addGroup(double_exc_pnLayout.createSequentialGroup()
                                .addComponent(one_lb_customer_result2, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(one_money_type_lb2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(one_tf_customer_result1)
                            .addComponent(one_tf_customer_result2))
                        .addGap(75, 75, 75))))
        );
        double_exc_pnLayout.setVerticalGroup(
            double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(double_exc_pnLayout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(one_lb_customer_money1, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(one_lb_customer_result1)
                            .addComponent(one_lb_exchange_rate1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(one_two_rate_bc1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(one_money_type_lb1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(one_money_type_lb4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(one_lb_operator1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(one_tf_customer_result1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(one_lb_equal1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(one_tf_exchange_rate1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(one_tf_customer_money1))
                .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(double_exc_pnLayout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(one_lb_exchange_rate3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(one_two_rate_bc2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(361, 361, 361))
                    .addGroup(double_exc_pnLayout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(one_lb_equal2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(double_exc_pnLayout.createSequentialGroup()
                                .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(one_lb_customer_money2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(one_lb_customer_result2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(one_money_type_lb2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(one_money_type_lb3_, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(one_tf_customer_result2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(one_tf_exchange_rate2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(one_tf_customer_money2)
                                    .addComponent(one_lb_operator2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(90, 90, 90)
                        .addGroup(double_exc_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(one_two_bn_finished, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(one_bn_print1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        sub_exc_pt.addTab("ប្តូរប្រាក់ពីរមុខ", double_exc_pn);

        javax.swing.GroupLayout exc_ptLayout = new javax.swing.GroupLayout(exc_pt);
        exc_pt.setLayout(exc_ptLayout);
        exc_ptLayout.setHorizontalGroup(
            exc_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, exc_ptLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sub_exc_pt)
                .addContainerGap())
        );
        exc_ptLayout.setVerticalGroup(
            exc_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, exc_ptLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sub_exc_pt)
                .addContainerGap())
        );

        zero_tp.addTab("ប្តូរប្រាក់", exc_pt);

        sub_tran_pt.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 24)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel17.setText("ចំនួនទឹកប្រាក់");

        two_three_sender_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_three_sender_money_tf.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                two_three_sender_money_tfCaretUpdate(evt);
            }
        });
        two_three_sender_money_tf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_three_sender_money_tfMouseClicked(evt);
            }
        });
        two_three_sender_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_sender_money_tfActionPerformed(evt);
            }
        });
        two_three_sender_money_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                two_three_sender_money_tfKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                two_three_sender_money_tfKeyTyped(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel18.setText("ថ្លៃសេវា");

        two_three_service_money_tf.setEditable(false);
        two_three_service_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_three_service_money_tf.setFocusable(false);
        two_three_service_money_tf.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                two_three_service_money_tfCaretUpdate(evt);
            }
        });
        two_three_service_money_tf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_three_service_money_tfMouseClicked(evt);
            }
        });
        two_three_service_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_service_money_tfActionPerformed(evt);
            }
        });
        two_three_service_money_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                two_three_service_money_tfKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                two_three_service_money_tfKeyTyped(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel20.setText("ទឹកប្រាក់ទទួល");

        two_one_total_money_tf.setEditable(false);
        two_one_total_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_one_total_money_tf.setFocusable(false);
        two_one_total_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_one_total_money_tfActionPerformed(evt);
            }
        });
        two_one_total_money_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                two_one_total_money_tfKeyReleased(evt);
            }
        });

        two_three_balance_money_tf.setEditable(false);
        two_three_balance_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_three_balance_money_tf.setFocusable(false);
        two_three_balance_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_balance_money_tfActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
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
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

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
        two_three_rial_money_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_rial_money_rbActionPerformed(evt);
            }
        });

        buttonGroup2.add(two_three_bart_money_rb);
        two_three_bart_money_rb.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        two_three_bart_money_rb.setText("฿");
        two_three_bart_money_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_bart_money_rbActionPerformed(evt);
            }
        });

        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel24.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel24.setText("លេខទូរស័ព្ទអ្នកផ្ញើរ");
        jPanel11.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        two_three_sender_phone_no_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_three_sender_phone_no_tf.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                two_three_sender_phone_no_tfCaretUpdate(evt);
            }
        });
        two_three_sender_phone_no_tf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_three_sender_phone_no_tfMouseClicked(evt);
            }
        });
        two_three_sender_phone_no_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_sender_phone_no_tfActionPerformed(evt);
            }
        });
        two_three_sender_phone_no_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                two_three_sender_phone_no_tfKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                two_three_sender_phone_no_tfKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                two_three_sender_phone_no_tfKeyTyped(evt);
            }
        });
        jPanel11.add(two_three_sender_phone_no_tf, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 87, 690, 75));

        two_one_ph_reciever_list.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        two_one_ph_reciever_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                two_one_ph_reciever_listMousePressed(evt);
            }
        });
        two_one_ph_reciever_list.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                two_one_ph_reciever_listKeyPressed(evt);
            }
        });

        two_one_ph_reciever_layer_pane.setLayer(two_one_ph_reciever_list, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout two_one_ph_reciever_layer_paneLayout = new javax.swing.GroupLayout(two_one_ph_reciever_layer_pane);
        two_one_ph_reciever_layer_pane.setLayout(two_one_ph_reciever_layer_paneLayout);
        two_one_ph_reciever_layer_paneLayout.setHorizontalGroup(
            two_one_ph_reciever_layer_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(two_one_ph_reciever_list, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        two_one_ph_reciever_layer_paneLayout.setVerticalGroup(
            two_one_ph_reciever_layer_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(two_one_ph_reciever_layer_paneLayout.createSequentialGroup()
                .addComponent(two_one_ph_reciever_list)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel11.add(two_one_ph_reciever_layer_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 270, 230));

        two_one_ph_senter_list.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        two_one_ph_senter_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                two_one_ph_senter_listMousePressed(evt);
            }
        });
        two_one_ph_senter_list.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                two_one_ph_senter_listKeyPressed(evt);
            }
        });

        two_one_ph_senter_layer_pane.setLayer(two_one_ph_senter_list, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout two_one_ph_senter_layer_paneLayout = new javax.swing.GroupLayout(two_one_ph_senter_layer_pane);
        two_one_ph_senter_layer_pane.setLayout(two_one_ph_senter_layer_paneLayout);
        two_one_ph_senter_layer_paneLayout.setHorizontalGroup(
            two_one_ph_senter_layer_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(two_one_ph_senter_list, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        two_one_ph_senter_layer_paneLayout.setVerticalGroup(
            two_one_ph_senter_layer_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(two_one_ph_senter_layer_paneLayout.createSequentialGroup()
                .addComponent(two_one_ph_senter_list)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.add(two_one_ph_senter_layer_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 270, 230));

        jLabel25.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel25.setText("លេខទូរស័ព្ទអ្នកទទួល");
        jPanel11.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));

        two_three_receiver_phone_no_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_three_receiver_phone_no_tf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_three_receiver_phone_no_tfMouseClicked(evt);
            }
        });
        two_three_receiver_phone_no_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_receiver_phone_no_tfActionPerformed(evt);
            }
        });
        two_three_receiver_phone_no_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                two_three_receiver_phone_no_tfKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                two_three_receiver_phone_no_tfKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                two_three_receiver_phone_no_tfKeyTyped(evt);
            }
        });
        jPanel11.add(two_three_receiver_phone_no_tf, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 270, 690, 75));

        jLabel56.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel56.setText("បើកនៅ");
        jPanel11.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, -1, -1));

        two_one_pro_his_bn.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        two_one_pro_his_bn.setText("view history");
        two_one_pro_his_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_one_pro_his_bnActionPerformed(evt);
            }
        });
        jPanel11.add(two_one_pro_his_bn, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 200, 50));

        two_one_sender_his_bn.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        two_one_sender_his_bn.setText("view history");
        two_one_sender_his_bn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_one_sender_his_bnMouseClicked(evt);
            }
        });
        two_one_sender_his_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_one_sender_his_bnActionPerformed(evt);
            }
        });
        jPanel11.add(two_one_sender_his_bn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 200, 50));

        two_one_reciever_his_bn.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        two_one_reciever_his_bn.setText("view history");
        two_one_reciever_his_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_one_reciever_his_bnActionPerformed(evt);
            }
        });
        jPanel11.add(two_one_reciever_his_bn, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, 200, 50));

        two_one_pro_name_cb.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_one_pro_name_cb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none" }));
        two_one_pro_name_cb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_one_pro_name_cbMouseClicked(evt);
            }
        });
        two_one_pro_name_cb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_one_pro_name_cbActionPerformed(evt);
            }
        });
        jPanel11.add(two_one_pro_name_cb, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 690, 70));

        two_one_edit_bn.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        two_one_edit_bn.setText("auto fill");
        two_one_edit_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_one_edit_bnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout to_pro_pnLayout = new javax.swing.GroupLayout(to_pro_pn);
        to_pro_pn.setLayout(to_pro_pnLayout);
        to_pro_pnLayout.setHorizontalGroup(
            to_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, to_pro_pnLayout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 747, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(to_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(to_pro_pnLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(two_three_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(to_pro_pnLayout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addGroup(to_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(two_three_sender_money_tf, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(two_three_balance_money_tf, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(two_one_total_money_tf)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, to_pro_pnLayout.createSequentialGroup()
                                .addGroup(to_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, to_pro_pnLayout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addGap(18, 18, 18)
                                        .addComponent(two_three_rial_money_rb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(two_three_dollar_money_rb)
                                        .addGap(40, 40, 40)
                                        .addComponent(two_three_bart_money_rb))
                                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, to_pro_pnLayout.createSequentialGroup()
                                .addComponent(two_three_service_money_tf)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(two_one_edit_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(122, 122, 122))
        );
        to_pro_pnLayout.setVerticalGroup(
            to_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(to_pro_pnLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(to_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(two_three_rial_money_rb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(two_three_dollar_money_rb)
                    .addComponent(two_three_bart_money_rb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(two_three_sender_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(to_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(two_one_edit_bn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(two_three_service_money_tf, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(two_one_total_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(two_three_balance_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(to_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(two_three_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(to_pro_pnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        sub_tran_pt.addTab("ផ្ទេរប្រាក់តាមតំបន់", to_pro_pn);

        jLabel23.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel23.setText("ចំនួនទឹកប្រាក់");

        two_four_reciece_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_four_reciece_money_tf.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                two_four_reciece_money_tfCaretUpdate(evt);
            }
        });
        two_four_reciece_money_tf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_four_reciece_money_tfMouseClicked(evt);
            }
        });
        two_four_reciece_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_four_reciece_money_tfActionPerformed(evt);
            }
        });
        two_four_reciece_money_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                two_four_reciece_money_tfKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                two_four_reciece_money_tfKeyTyped(evt);
            }
        });

        two_four_balance_money_tf.setEditable(false);
        two_four_balance_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_four_balance_money_tf.setFocusable(false);

        jLabel27.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel27.setText("ទឹកប្រាក់ប្រគល់");

        two_four_total_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_four_total_money_tf.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                two_four_total_money_tfCaretUpdate(evt);
            }
        });
        two_four_total_money_tf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_four_total_money_tfMouseClicked(evt);
            }
        });
        two_four_total_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_four_total_money_tfActionPerformed(evt);
            }
        });
        two_four_total_money_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                two_four_total_money_tfKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                two_four_total_money_tfKeyTyped(evt);
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
        two_four_rial_money_rb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_four_rial_money_rbMouseClicked(evt);
            }
        });
        two_four_rial_money_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_four_rial_money_rbActionPerformed(evt);
            }
        });

        buttonGroup3.add(two_four_dollar_money_rb);
        two_four_dollar_money_rb.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        two_four_dollar_money_rb.setText("$");
        two_four_dollar_money_rb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_four_dollar_money_rbMouseClicked(evt);
            }
        });
        two_four_dollar_money_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_four_dollar_money_rbActionPerformed(evt);
            }
        });

        buttonGroup3.add(two_four_bart_money_rb);
        two_four_bart_money_rb.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        two_four_bart_money_rb.setText("฿");
        two_four_bart_money_rb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_four_bart_money_rbMouseClicked(evt);
            }
        });
        two_four_bart_money_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_four_bart_money_rbActionPerformed(evt);
            }
        });

        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel22.setText("លេខទូរស័ព្ទអ្នកទទួល");
        jPanel12.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 317, 57));

        two_four_receiver_phone_no_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_four_receiver_phone_no_tf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_four_receiver_phone_no_tfMouseClicked(evt);
            }
        });
        two_four_receiver_phone_no_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_four_receiver_phone_no_tfActionPerformed(evt);
            }
        });
        two_four_receiver_phone_no_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                two_four_receiver_phone_no_tfKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                two_four_receiver_phone_no_tfKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                two_four_receiver_phone_no_tfKeyTyped(evt);
            }
        });
        jPanel12.add(two_four_receiver_phone_no_tf, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 700, 75));

        jLabel58.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 36)); // NOI18N
        jLabel58.setText("ផ្ទេរពី");
        jPanel12.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, -1, -1));

        two_two_pro_name_cb.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_two_pro_name_cb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none" }));
        two_two_pro_name_cb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_two_pro_name_cbMouseClicked(evt);
            }
        });
        two_two_pro_name_cb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_two_pro_name_cbActionPerformed(evt);
            }
        });
        jPanel12.add(two_two_pro_name_cb, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 700, 70));

        two_two_reciever_his_bn.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        two_two_reciever_his_bn.setText("view history");
        two_two_reciever_his_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_two_reciever_his_bnActionPerformed(evt);
            }
        });
        jPanel12.add(two_two_reciever_his_bn, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 200, 50));

        two_one_pro_his_bn1.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        two_one_pro_his_bn1.setText("view history");
        two_one_pro_his_bn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_one_pro_his_bn1ActionPerformed(evt);
            }
        });
        jPanel12.add(two_one_pro_his_bn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 210, 200, 50));

        two_two_ph_recieve_list.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        two_two_ph_recieve_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                two_two_ph_recieve_listMousePressed(evt);
            }
        });
        two_two_ph_recieve_list.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                two_two_ph_recieve_listKeyPressed(evt);
            }
        });

        two_two_ph_recieve_layer_pane.setLayer(two_two_ph_recieve_list, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout two_two_ph_recieve_layer_paneLayout = new javax.swing.GroupLayout(two_two_ph_recieve_layer_pane);
        two_two_ph_recieve_layer_pane.setLayout(two_two_ph_recieve_layer_paneLayout);
        two_two_ph_recieve_layer_paneLayout.setHorizontalGroup(
            two_two_ph_recieve_layer_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(two_two_ph_recieve_list, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        two_two_ph_recieve_layer_paneLayout.setVerticalGroup(
            two_two_ph_recieve_layer_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(two_two_ph_recieve_layer_paneLayout.createSequentialGroup()
                .addComponent(two_two_ph_recieve_list)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.add(two_two_ph_recieve_layer_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 270, 230));

        javax.swing.GroupLayout from_pro_pnLayout = new javax.swing.GroupLayout(from_pro_pn);
        from_pro_pn.setLayout(from_pro_pnLayout);
        from_pro_pnLayout.setHorizontalGroup(
            from_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, from_pro_pnLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 756, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(from_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(from_pro_pnLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(two_four_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(from_pro_pnLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(filler3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(122, 122, 122)
                        .addGroup(from_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(two_four_balance_money_tf)
                            .addComponent(two_four_total_money_tf)
                            .addComponent(two_four_reciece_money_tf)
                            .addGroup(from_pro_pnLayout.createSequentialGroup()
                                .addGroup(from_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel28)
                                    .addGroup(from_pro_pnLayout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addGap(18, 18, 18)
                                        .addComponent(two_four_rial_money_rb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)
                                        .addComponent(two_four_dollar_money_rb)
                                        .addGap(41, 41, 41)
                                        .addComponent(two_four_bart_money_rb)))
                                .addGap(0, 156, Short.MAX_VALUE)))))
                .addGap(100, 100, 100))
        );
        from_pro_pnLayout.setVerticalGroup(
            from_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(from_pro_pnLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(from_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filler3, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(from_pro_pnLayout.createSequentialGroup()
                        .addGroup(from_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(two_four_rial_money_rb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(two_four_dollar_money_rb)
                            .addComponent(two_four_bart_money_rb))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(two_four_reciece_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(two_four_total_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(two_four_balance_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addGroup(from_pro_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(two_four_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60))
            .addGroup(from_pro_pnLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        sub_tran_pt.addTab("ដកប្រាក់តាមតំបន់", from_pro_pn);

        jLabel10.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel10.setText("ចំនួនទឹកប្រាក់");

        two_one_tf_cus_money.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_one_tf_cus_money.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_one_tf_cus_moneyActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel11.setText("ថ្លៃសេវា");

        two_one_tf_service_money.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel12.setText("ទឹកប្រាក់ទទួល");

        two_one_tf_total_money.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

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

        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel8.setText("លេខបញ្ជី");
        jPanel13.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        two_one_tf_cus_no.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_one_tf_cus_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_one_tf_cus_noActionPerformed(evt);
            }
        });
        two_one_tf_cus_no.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                two_one_tf_cus_noKeyReleased(evt);
            }
        });
        jPanel13.add(two_one_tf_cus_no, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 700, 78));

        two_one_tf_cus_name.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_one_tf_cus_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                two_one_tf_cus_nameKeyReleased(evt);
            }
        });
        jPanel13.add(two_one_tf_cus_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 700, 70));

        two_three_bank_info_his_bn.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        two_three_bank_info_his_bn.setText("view history");
        two_three_bank_info_his_bn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_three_bank_info_his_bnMouseClicked(evt);
            }
        });
        two_three_bank_info_his_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_bank_info_his_bnActionPerformed(evt);
            }
        });
        jPanel13.add(two_three_bank_info_his_bn, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 200, 50));

        two_three_to_thai_bank_id_list.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        two_three_to_thai_bank_id_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                two_three_to_thai_bank_id_listMousePressed(evt);
            }
        });
        two_three_to_thai_bank_id_list.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                two_three_to_thai_bank_id_listKeyPressed(evt);
            }
        });

        two_three_to_thai_bank_id_layer_pane.setLayer(two_three_to_thai_bank_id_list, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout two_three_to_thai_bank_id_layer_paneLayout = new javax.swing.GroupLayout(two_three_to_thai_bank_id_layer_pane);
        two_three_to_thai_bank_id_layer_pane.setLayout(two_three_to_thai_bank_id_layer_paneLayout);
        two_three_to_thai_bank_id_layer_paneLayout.setHorizontalGroup(
            two_three_to_thai_bank_id_layer_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(two_three_to_thai_bank_id_list, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
        );
        two_three_to_thai_bank_id_layer_paneLayout.setVerticalGroup(
            two_three_to_thai_bank_id_layer_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(two_three_to_thai_bank_id_layer_paneLayout.createSequentialGroup()
                .addComponent(two_three_to_thai_bank_id_list)
                .addContainerGap(247, Short.MAX_VALUE))
        );

        jPanel13.add(two_three_to_thai_bank_id_layer_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 580, -1));

        jLabel19.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel19.setText("ឈ្មោះ");
        jPanel13.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 90, -1));

        two_three_to_thai_name_list.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        two_three_to_thai_name_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                two_three_to_thai_name_listMousePressed(evt);
            }
        });
        two_three_to_thai_name_list.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                two_three_to_thai_name_listKeyPressed(evt);
            }
        });

        two_three_to_thai_name_layer_pane.setLayer(two_three_to_thai_name_list, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout two_three_to_thai_name_layer_paneLayout = new javax.swing.GroupLayout(two_three_to_thai_name_layer_pane);
        two_three_to_thai_name_layer_pane.setLayout(two_three_to_thai_name_layer_paneLayout);
        two_three_to_thai_name_layer_paneLayout.setHorizontalGroup(
            two_three_to_thai_name_layer_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(two_three_to_thai_name_list, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
        );
        two_three_to_thai_name_layer_paneLayout.setVerticalGroup(
            two_three_to_thai_name_layer_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(two_three_to_thai_name_layer_paneLayout.createSequentialGroup()
                .addComponent(two_three_to_thai_name_list)
                .addContainerGap(240, Short.MAX_VALUE))
        );

        jPanel13.add(two_three_to_thai_name_layer_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 590, 240));

        jLabel57.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel57.setText("លេខអ្នកផ្ញើរ");
        jPanel13.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 150, -1));

        two_three_tf_cus_ph_no.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_three_tf_cus_ph_no.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                two_three_tf_cus_ph_noKeyReleased(evt);
            }
        });
        jPanel13.add(two_three_tf_cus_ph_no, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 700, 75));

        two_three_to_thai_ph_no_list.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        two_three_to_thai_ph_no_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                two_three_to_thai_ph_no_listMousePressed(evt);
            }
        });
        two_three_to_thai_ph_no_list.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                two_three_to_thai_ph_no_listKeyPressed(evt);
            }
        });

        two_three_to_thai_ph_no_layer_pane.setLayer(two_three_to_thai_ph_no_list, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout two_three_to_thai_ph_no_layer_paneLayout = new javax.swing.GroupLayout(two_three_to_thai_ph_no_layer_pane);
        two_three_to_thai_ph_no_layer_pane.setLayout(two_three_to_thai_ph_no_layer_paneLayout);
        two_three_to_thai_ph_no_layer_paneLayout.setHorizontalGroup(
            two_three_to_thai_ph_no_layer_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(two_three_to_thai_ph_no_list, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        two_three_to_thai_ph_no_layer_paneLayout.setVerticalGroup(
            two_three_to_thai_ph_no_layer_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(two_three_to_thai_ph_no_layer_paneLayout.createSequentialGroup()
                .addComponent(two_three_to_thai_ph_no_list)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.add(two_three_to_thai_ph_no_layer_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 590, 240));

        two_three_bank_thai_cb.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_three_bank_thai_cb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none" }));
        two_three_bank_thai_cb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                two_three_bank_thai_cbMouseClicked(evt);
            }
        });
        jPanel13.add(two_three_bank_thai_cb, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 520, 700, 70));

        two_three_bank_his_bn.setFont(new java.awt.Font("Khmer OS Battambang", 0, 24)); // NOI18N
        two_three_bank_his_bn.setText("view history");
        two_three_bank_his_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_three_bank_his_bnActionPerformed(evt);
            }
        });
        jPanel13.add(two_three_bank_his_bn, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 460, 200, 50));

        jLabel29.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel29.setText("ធនាគារ");
        jPanel13.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, -1, -1));

        javax.swing.GroupLayout to_thai_pnLayout = new javax.swing.GroupLayout(to_thai_pn);
        to_thai_pn.setLayout(to_thai_pnLayout);
        to_thai_pnLayout.setHorizontalGroup(
            to_thai_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(to_thai_pnLayout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 757, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(to_thai_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(to_thai_pnLayout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addGroup(to_thai_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(two_one_tf_total_money, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                            .addComponent(two_one_tf_service_money, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                            .addGroup(to_thai_pnLayout.createSequentialGroup()
                                .addGroup(to_thai_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel11))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(two_one_tf_cus_money, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(to_thai_pnLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(two_one_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(130, 130, 130))
        );
        to_thai_pnLayout.setVerticalGroup(
            to_thai_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(to_thai_pnLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(two_one_tf_cus_money, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(two_one_tf_service_money, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(two_one_tf_total_money, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(to_thai_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(two_one_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74))
            .addGroup(to_thai_pnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
                .addContainerGap())
        );

        sub_tran_pt.addTab("ផ្ទេរប្រាក់ទៅថៃ", to_thai_pn);

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

        jLabel14.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel14.setText("ចំនួនទឹកប្រាក់");

        two_two_reveiver_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_two_reveiver_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_two_reveiver_money_tfActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel16.setText("ថ្លៃសេវា");

        two_two_service_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        two_two_service_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                two_two_service_money_tfActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel15.setText("ទឹកប្រាក់ប្រគល់");

        two_two_result_money_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

        jLabel34.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 30)); // NOI18N
        jLabel34.setText("លេខទូរស័ព្ទអ្នកទទួល");

        two_two_reveiver_ph_no_tf.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

        javax.swing.GroupLayout from_thai_pnLayout = new javax.swing.GroupLayout(from_thai_pn);
        from_thai_pn.setLayout(from_thai_pnLayout);
        from_thai_pnLayout.setHorizontalGroup(
            from_thai_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(from_thai_pnLayout.createSequentialGroup()
                .addComponent(jLabel13)
                .addGap(725, 725, 725)
                .addGroup(from_thai_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(from_thai_pnLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, from_thai_pnLayout.createSequentialGroup()
                        .addGroup(from_thai_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(two_two_service_money_tf, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(two_two_reveiver_money_tf, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(two_two_reveiver_ph_no_tf, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, from_thai_pnLayout.createSequentialGroup()
                                .addGroup(from_thai_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(two_two_result_money_tf, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(130, 130, 130))))
            .addGroup(from_thai_pnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(two_two_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        from_thai_pnLayout.setVerticalGroup(
            from_thai_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(from_thai_pnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(from_thai_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(two_two_reveiver_ph_no_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(two_two_reveiver_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(two_two_service_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(two_two_result_money_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(from_thai_pnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(two_two_bn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );

        sub_tran_pt.addTab("ដកប្រាក់ពីថៃ", from_thai_pn);

        javax.swing.GroupLayout tran_ptLayout = new javax.swing.GroupLayout(tran_pt);
        tran_pt.setLayout(tran_ptLayout);
        tran_ptLayout.setHorizontalGroup(
            tran_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tran_ptLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sub_tran_pt)
                .addContainerGap())
        );
        tran_ptLayout.setVerticalGroup(
            tran_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tran_ptLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sub_tran_pt)
                .addContainerGap())
        );

        zero_tp.addTab("ផ្ទេរប្រាក់", tran_pt);

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

        three_tb_history.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 18)); // NOI18N
        three_tb_history.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ថ្ងៃទី", "លេខប័ណ្ណ", "ចេញប័ណ្ណដោយ", "បំណង", "លុយ R", "លុយ S", "លុយ B", "លុយ B ក្នុងកុង", "detail"
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
                                        .addComponent(jLabel35)
                                        .addGap(18, 18, 18)
                                        .addComponent(three_calendar_cld, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(his_ptLayout.createSequentialGroup()
                                        .addGroup(his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(three_add_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(his_ptLayout.createSequentialGroup()
                                                .addComponent(jLabel47)
                                                .addGap(18, 18, 18)
                                                .addComponent(three_rial_rb)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(three_dollar_rb)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(three_bart_rb)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(three_add_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                        .addComponent(three_chb_m_detail)))
                                .addGap(0, 235, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, his_ptLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(date_history_lb, javax.swing.GroupLayout.PREFERRED_SIZE, 1046, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        his_ptLayout.setVerticalGroup(
            his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(his_ptLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(three_rial_rb)
                    .addComponent(three_dollar_rb)
                    .addComponent(three_bart_rb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(three_add_tf, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(three_add_bn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(his_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(date_history_lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(three_calendar_cld, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(39, 39, 39)
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
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE))
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
                .addContainerGap(237, Short.MAX_VALUE)
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
                .addContainerGap(500, Short.MAX_VALUE))
        );
        rate_ptLayout.setVerticalGroup(
            rate_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rate_ptLayout.createSequentialGroup()
                .addContainerGap(160, Short.MAX_VALUE)
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
                .addContainerGap(132, Short.MAX_VALUE))
        );

        zero_tp.addTab("អត្រាប្តូរប្រាក់", rate_pt);

        five_user_name_tf.setEditable(false);
        five_user_name_tf.setText("chi");
        five_user_name_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                five_user_name_tfActionPerformed(evt);
            }
        });

        five_password_tf.setEditable(false);
        five_password_tf.setText("1234");

        five_local_host_server_name_tf.setEditable(false);
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

        five_local_host_db_name_tf.setEditable(false);
        five_local_host_db_name_tf.setText("exchange_transfer_sm");

        jLabel48.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel48.setText("user name");

        jLabel49.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel49.setText("password");

        jLabel50.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel50.setText("host name");

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel51.setText("host user name");

        jLabel52.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel52.setText("host password");

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel53.setText("wifi host name");

        jLabel54.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel54.setText("host user name");

        jLabel55.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel55.setText("host password");

        five_dev_permission_bn.setText("Enter");
        five_dev_permission_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                five_dev_permission_bnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout db_con_ptLayout = new javax.swing.GroupLayout(db_con_pt);
        db_con_pt.setLayout(db_con_ptLayout);
        db_con_ptLayout.setHorizontalGroup(
            db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(db_con_ptLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(db_con_ptLayout.createSequentialGroup()
                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(five_wifi_host_tf))
                    .addGroup(db_con_ptLayout.createSequentialGroup()
                        .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(db_con_ptLayout.createSequentialGroup()
                                .addComponent(five_local_host_server_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(five_local_host_db_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(five_password_tf)
                            .addComponent(five_user_name_tf)))
                    .addComponent(five_create_acc_tf)
                    .addGroup(db_con_ptLayout.createSequentialGroup()
                        .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                            .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(five_local_host_password_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(five_local_host_user_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(db_con_ptLayout.createSequentialGroup()
                        .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(five_wifi_host_password_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(db_con_ptLayout.createSequentialGroup()
                        .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(five_wifi_host_user_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 314, Short.MAX_VALUE)
                .addComponent(five_dev_permission_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(five_dev_permission_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(368, Short.MAX_VALUE))
        );
        db_con_ptLayout.setVerticalGroup(
            db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(db_con_ptLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(five_user_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(five_password_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(db_con_ptLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(five_create_acc_tf))
                    .addGroup(db_con_ptLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(five_dev_permission_bn, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                            .addComponent(five_dev_permission_tf))))
                .addGap(27, 27, 27)
                .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(five_local_host_db_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(five_local_host_server_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(db_con_ptLayout.createSequentialGroup()
                        .addComponent(five_local_host_user_name_tf)
                        .addGap(2, 2, 2)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(db_con_ptLayout.createSequentialGroup()
                        .addComponent(five_local_host_password_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addGap(31, 31, 31)
                .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(five_wifi_host_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(five_wifi_host_user_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(db_con_ptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(db_con_ptLayout.createSequentialGroup()
                        .addComponent(five_wifi_host_password_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addGap(298, 298, 298))
        );

        zero_tp.addTab("connection", db_con_pt);

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

    private void five_user_name_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_five_user_name_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_five_user_name_tfActionPerformed

    private void one_bn_printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_bn_printActionPerformed
        exc_close_or_print(true, one_tf_customer_money, one_tf_exchange_rate, one_tf_customer_result,
                one_bn_S_to_R, one_bn_S_to_B, one_bn_B_to_R, one_bn_R_to_S, one_bn_B_to_S, one_bn_R_to_B,
                one_lb_operator, selected_exchange_rate, this);
    }//GEN-LAST:event_one_bn_printActionPerformed

    private void one_bn_finishedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_bn_finishedActionPerformed
        exc_close_or_print(false, one_tf_customer_money, one_tf_exchange_rate, one_tf_customer_result,
                one_bn_S_to_R, one_bn_S_to_B, one_bn_B_to_R, one_bn_R_to_S, one_bn_B_to_S, one_bn_R_to_B,
                one_lb_operator, selected_exchange_rate, this);
    }//GEN-LAST:event_one_bn_finishedActionPerformed

    private void one_tf_customer_resultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_tf_customer_resultActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_customer_resultActionPerformed

    private void one_tf_exchange_rateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_tf_exchange_rateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_exchange_rateActionPerformed

    private void one_tf_exchange_rateCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_one_tf_exchange_rateCaretUpdate
        one_two_calculation(selected_exchange_rate, one_tf_customer_money, one_tf_exchange_rate, one_tf_customer_result);
    }//GEN-LAST:event_one_tf_exchange_rateCaretUpdate

    private void one_bn_R_to_BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_bn_R_to_BActionPerformed
        one_lb_operator.setText("/");
        one_set_money_type_to_lb("៛", "฿", one_money_type_lb, one_rate_type_lb, one_money_type_result_lb);
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
        one_set_money_type_to_lb("฿", "$", one_money_type_lb, one_rate_type_lb, one_money_type_result_lb);
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
        one_set_money_type_to_lb("៛", "$", one_money_type_lb, one_rate_type_lb, one_money_type_result_lb);
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
        one_set_money_type_to_lb("฿", "៛", one_money_type_lb, one_rate_type_lb, one_money_type_result_lb);
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
        one_set_money_type_to_lb("$", "฿", one_money_type_lb, one_rate_type_lb, one_money_type_result_lb);

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
        one_set_money_type_to_lb("$", "៛", one_money_type_lb, one_rate_type_lb, one_money_type_result_lb);
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
        validate_KeyTyped_money(evt, one_tf_customer_money);
    }//GEN-LAST:event_one_tf_customer_moneyKeyTyped

    private void one_tf_customer_moneyCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_one_tf_customer_moneyCaretUpdate
        one_two_calculation(selected_exchange_rate, one_tf_customer_money, one_tf_exchange_rate, one_tf_customer_result);
    }//GEN-LAST:event_one_tf_customer_moneyCaretUpdate

    private void one_tf_customer_moneyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_customer_moneyKeyReleased
        validate_KeyReleased_money(evt, one_tf_customer_money);
    }//GEN-LAST:event_one_tf_customer_moneyKeyReleased

    private void one_tf_customer_moneyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_customer_moneyKeyPressed

        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_ENTER:
                one_bn_finished.requestFocus();
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
                one_tf_customer_money.requestFocus();
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
                one_bn_finished.requestFocus();
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
        selected_money_type_exe = type_of_money.Rial;
    }//GEN-LAST:event_three_rial_rbActionPerformed

    private void three_add_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_add_bnActionPerformed

        if ((three_rial_rb.isSelected() || three_dollar_rb.isSelected() || three_bart_rb.isSelected()) && !three_add_tf.getText().isEmpty()) {
            String admin_password = JOptionPane.showInputDialog(this, "Enter password");
            if (set_admin_password.equals(admin_password)) {
                int lastinsert_id_add = -1;
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
                    pst.setInt(3, get_id_money_type_from_db(selected_money_type_exe));
                    pst.setInt(4, get_acc_id());
                    pst.setInt(5, get_id_pur_from_db(purpose_type.add_total_money));
                    pst.executeUpdate();
                    ResultSet generatekey = pst.getGeneratedKeys();
                    if (generatekey.next()) {
                        lastinsert_id_add = generatekey.getInt(1);
                    }

                    String add_money = "0";
                    String money_type = "";

                    //query to access
                    pst = con.prepareStatement("SELECT add_money, type_of_money "
                            + "FROM add_money_history_tb INNER JOIN money_type_tb ON add_money_history_tb.id_type_of_money = money_type_tb.id_type_of_money "
                            + "where id_add = ? "
                            + "AND id_acc = ? "
                            + "AND id_pur = ?;");
                    pst.setInt(1, lastinsert_id_add);
                    pst.setInt(2, get_acc_id());
                    pst.setInt(3, get_id_pur_from_db(UI_and_operation.purpose_type.add_total_money));
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        add_money = rs.getString("add_money");
                        money_type = rs.getString("type_of_money");
                    }

                    invoice_man in_man = new invoice_man();
                    in_man.get_R_D_B_B_top_1_from_db();

                    switch (money_type) {
                        case "Rial":
                            //write sql query to access
                            pst = con.prepareStatement("INSERT INTO invoice_management_tb (rial, dollar, bart, bank_bart, id_invoice, id_acc, id_pur, invoice_man_date) "
                                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
                            pst.setString(1, rial_validation(Double.parseDouble(clear_cvot(in_man.getRial())) + Double.parseDouble(clear_cvot(add_money))));
                            pst.setString(2, dollar_validation(Double.parseDouble(clear_cvot(in_man.getDollar()))));
                            pst.setString(3, bart_validation(Double.parseDouble(clear_cvot(in_man.getBart()))));
                            pst.setString(4, bart_validation(Double.parseDouble(clear_cvot(in_man.getBank_Bart()))));
                            pst.setInt(5, lastinsert_id_add);
                            pst.setInt(6, get_acc_id());
                            pst.setInt(7, get_id_pur_from_db(UI_and_operation.purpose_type.add_total_money));
                            pst.setTimestamp(8, cur_date);
                            pst.executeUpdate();
                            break;
                        case "Dollar":
                            //write sql query to access
                            pst = con.prepareStatement("INSERT INTO invoice_management_tb (rial, dollar, bart, bank_bart, id_invoice, id_acc, id_pur, invoice_man_date) "
                                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
                            pst.setString(1, rial_validation(Double.parseDouble(clear_cvot(in_man.getRial()))));
                            pst.setString(2, dollar_validation(Double.parseDouble(clear_cvot(in_man.getDollar())) + Double.parseDouble(clear_cvot(add_money))));
                            pst.setString(3, bart_validation(Double.parseDouble(clear_cvot(in_man.getBart()))));
                            pst.setString(4, bart_validation(Double.parseDouble(clear_cvot(in_man.getBank_Bart()))));
                            pst.setInt(5, lastinsert_id_add);
                            pst.setInt(6, get_acc_id());
                            pst.setInt(7, get_id_pur_from_db(UI_and_operation.purpose_type.add_total_money));
                            pst.setTimestamp(8, cur_date);
                            pst.executeUpdate();
                            break;
                        case "Bart":
                            //write sql query to access
                            pst = con.prepareStatement("INSERT INTO invoice_management_tb (rial, dollar, bart, bank_bart, id_invoice, id_acc, id_pur, invoice_man_date) "
                                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
                            pst.setString(1, rial_validation(Double.parseDouble(clear_cvot(in_man.getRial()))));
                            pst.setString(2, dollar_validation(Double.parseDouble(clear_cvot(in_man.getDollar()))));
                            pst.setString(3, bart_validation(Double.parseDouble(clear_cvot(in_man.getBart())) + Double.parseDouble(clear_cvot(add_money))));
                            pst.setString(4, bart_validation(Double.parseDouble(clear_cvot(in_man.getBank_Bart()))));
                            pst.setInt(5, lastinsert_id_add);
                            pst.setInt(6, get_acc_id());
                            pst.setInt(7, get_id_pur_from_db(UI_and_operation.purpose_type.add_total_money));
                            pst.setTimestamp(8, cur_date);
                            pst.executeUpdate();
                            break;
                        default:
                            System.out.println("Error");
                    }
                    set_history();
                } catch (SQLException ex) {
                    System.err.println(ex);
                }
                three_add_tf.setText("");
                buttonGroup4.clearSelection();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect password", "Alert", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_three_add_bnActionPerformed

    private void three_dollar_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_dollar_rbActionPerformed
        selected_money_type_exe = type_of_money.Dollar;
    }//GEN-LAST:event_three_dollar_rbActionPerformed

    private void three_bart_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_bart_rbActionPerformed
        selected_money_type_exe = type_of_money.Bart;
    }//GEN-LAST:event_three_bart_rbActionPerformed

    private void three_add_tfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_three_add_tfKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_three_add_tfKeyPressed

    private void three_add_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_three_add_tfKeyReleased
        validate_KeyReleased_money(evt, three_add_tf);
    }//GEN-LAST:event_three_add_tfKeyReleased

    private void three_add_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_three_add_tfKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || ((c == '.' || c == '-') && !is_has_double_points(three_add_tf.getText())))) {
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

    private void three_add_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_three_add_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_three_add_tfActionPerformed

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
                        switch (pur) {
                            case "exchanging":
                                id = get_id_inv_by_id_inv_man_from_db(id);
                                print_reciept(get_path() + exc_reciept_path, id);
                                break;
                            case "double_exchanging":
                                print_reciept(get_path() + double_exc_reciept_path, id);
                                break;
                            default:
                                System.out.println("errorrrrrrrrr");
                        }
                    }
                    break;
                case Delete:

                    if (!three_tb_history.getSelectionModel().isSelectionEmpty()) {
                        if (set_admin_password.equals(JOptionPane.showInputDialog(this, "Enter password"))) {
                            //table in UI
                            DefaultTableModel model = (DefaultTableModel) three_tb_history.getModel();
                            //select index of the table row
                            int selectedIndex = three_tb_history.getSelectedRow();

                            //get id from to store inside variable id here
                            int id = -1;

                            id = get_id_invoice(Integer.parseInt(model.getValueAt(selectedIndex, 1).toString()));
                            String acc = model.getValueAt(selectedIndex, 2).toString();
                            String pur = model.getValueAt(selectedIndex, 3).toString();
                            int dialogresult = JOptionPane.showConfirmDialog(this, "Do you want to delete the record", "Warning", JOptionPane.YES_NO_OPTION);

                            if (dialogresult == JOptionPane.YES_NO_OPTION) {
                                if (get_count_id_invoice_man_from_db(id, acc, pur) <= 10000) {
                                    switch (pur) {
                                        case "exchanging":
                                            delete_exe_from_db(id, acc, pur);
                                            break;
                                        case "add_total_money":
                                            detele_add_total_to_db(id, acc, pur);
                                            break;
                                        case "to_province":
                                            detele_to_pro_to_db(id, acc, pur);
                                            break;
                                        case "from_province":
                                            detele_from_pro_to_db(id, acc, pur);
                                            break;
                                        case "double_exchanging":
                                            delete_double_exe_from_db(id, acc, pur);
                                            break;
                                        default:
                                            System.out.println("error");
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

    private void five_dev_permission_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_five_dev_permission_bnActionPerformed
        if (five_dev_permission_tf.getText().equals("Chi0123456789kay")) {
            five_wifi_host_user_name_tf.setEditable(true);
            five_user_name_tf.setEditable(true);
            five_password_tf.setEditable(true);
            five_local_host_server_name_tf.setEditable(true);
            five_local_host_db_name_tf.setEditable(true);
            five_local_host_user_name_tf.setEditable(true);
            five_local_host_password_tf.setEditable(true);
            five_wifi_host_tf.setEditable(true);
            five_wifi_host_password_tf.setEditable(true);
        } else {

            five_wifi_host_user_name_tf.setEditable(false);
            five_user_name_tf.setEditable(false);
            five_password_tf.setEditable(false);
            five_local_host_server_name_tf.setEditable(false);
            five_local_host_db_name_tf.setEditable(false);
            five_local_host_user_name_tf.setEditable(false);
            five_local_host_password_tf.setEditable(false);
            five_wifi_host_tf.setEditable(false);
            five_wifi_host_password_tf.setEditable(false);
        }
    }//GEN-LAST:event_five_dev_permission_bnActionPerformed

    private void two_four_dollar_money_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_four_dollar_money_rbActionPerformed
        selected_money_type_from_pro = type_of_money.Dollar;
        two_two_cal(selected_money_type_from_pro, two_four_reciece_money_tf,
                two_four_total_money_tf, two_four_balance_money_tf,
                two_four_rial_money_rb, two_four_dollar_money_rb,
                two_four_bart_money_rb);
    }//GEN-LAST:event_two_four_dollar_money_rbActionPerformed

    private void two_four_bn_finishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_four_bn_finishActionPerformed
        if ((!two_four_receiver_phone_no_tf.getText().isEmpty() && !two_two_pro_name_cb.getSelectedItem().equals("none")
                && !two_four_reciece_money_tf.getText().isEmpty() && !two_four_total_money_tf.getText().isEmpty()) && (two_four_rial_money_rb.isSelected()
                || two_four_dollar_money_rb.isSelected() || two_four_bart_money_rb.isSelected())) {
            int lastinsert_id_invoice = -1;
            Connection con;
            PreparedStatement pst;
            try {
                con = DriverManager.getConnection(
                        getLocal_host(),
                        getLocal_host_user_name(),
                        getLocal_host_password()
                );

                if (!is_has_history_list_db(two_two_pro_name_cb.getSelectedItem().toString(), "transfer_province", "province_name_tb")) {
                    set_history_list_db(two_two_pro_name_cb.getSelectedItem().toString(), "transfer_province", "province_name_tb");
                }

                //write sql query to access
                pst = con.prepareStatement("insert into from_province_invoice_tb "
                        + "(balance_money, "
                        + "transfering_money, total_money, "
                        + "receiver_phone_no, "
                        + "id_type_of_money, id_province, "
                        + "id_acc, id_pur) values(?, ?, ?, ?, ?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS);

                //set value to ? in query
                pst.setString(1, two_four_balance_money_tf.getText());
                pst.setString(2, two_four_reciece_money_tf.getText());
                pst.setString(3, two_four_total_money_tf.getText());
                pst.setString(4, two_four_receiver_phone_no_tf.getText().trim());
                pst.setInt(5, get_id_money_type_from_db(selected_money_type_from_pro));
                pst.setInt(6, get_id_province_name_from_db(two_two_pro_name_cb.getSelectedItem().toString()));
                pst.setInt(7, get_acc_id());
                pst.setInt(8, get_id_pur_from_db(purpose_type.from_province));
                pst.executeUpdate();
                ResultSet generatekey = pst.getGeneratedKeys();
                if (generatekey.next()) {
                    lastinsert_id_invoice = generatekey.getInt(1);
                }

                if (!is_has_history_list_db(two_four_receiver_phone_no_tf.getText().trim(), "receiver_phone_no", "to_pro_receiver_ph_no_history_tb")) {
                    set_history_list_db(two_four_receiver_phone_no_tf.getText().trim(), "receiver_phone_no", "to_pro_receiver_ph_no_history_tb");
                }

                invoice_man in_man = new invoice_man();
                in_man.get_R_D_B_B_top_1_from_db();

                switch (selected_money_type_from_pro) {
                    case Rial:
                        set_invoice_man_db(
                                String.valueOf(rial_validation(Double.parseDouble(clear_cvot(in_man.getRial())) - Double.parseDouble(clear_cvot(two_four_total_money_tf.getText())))),
                                String.valueOf(in_man.getDollar()),
                                String.valueOf(in_man.getBart()),
                                String.valueOf(in_man.getBank_Bart()),
                                lastinsert_id_invoice,
                                get_acc_id(),
                                purpose_type.from_province,
                                current_date());
                        break;
                    case Dollar:
                        set_invoice_man_db(
                                String.valueOf(in_man.getRial()),
                                String.valueOf(dollar_validation(Double.parseDouble(clear_cvot(in_man.getDollar())) - Double.parseDouble(clear_cvot(two_four_total_money_tf.getText())))),
                                String.valueOf(in_man.getBart()),
                                String.valueOf(in_man.getBank_Bart()),
                                lastinsert_id_invoice,
                                get_acc_id(),
                                purpose_type.from_province,
                                current_date());
                        break;
                    case Bart:
                        set_invoice_man_db(
                                String.valueOf(in_man.getRial()),
                                String.valueOf(in_man.getDollar()),
                                String.valueOf(bart_validation(Double.parseDouble(clear_cvot(in_man.getBart())) - Double.parseDouble(clear_cvot(two_four_total_money_tf.getText())))),
                                String.valueOf(in_man.getBank_Bart()),
                                lastinsert_id_invoice,
                                get_acc_id(),
                                purpose_type.from_province,
                                current_date());
                        break;
                }
            } catch (SQLException ex) {
                System.err.println("error: two_three_bn_finish\n" + ex);
            }

            two_four_receiver_phone_no_tf.setText("");
            two_two_pro_name_cb.getModel().setSelectedItem("none");
            two_four_reciece_money_tf.setText("");
            two_four_total_money_tf.setText("");
            buttonGroup3.clearSelection();
            remove_all_in_list(two_two_ph_recieve_list);
//            set_history();
            set_is_change_true();
        }
    }//GEN-LAST:event_two_four_bn_finishActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void two_four_total_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_four_total_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_four_total_money_tfActionPerformed

    private void two_four_reciece_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_four_reciece_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_four_reciece_money_tfActionPerformed

    private void two_four_receiver_phone_no_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_four_receiver_phone_no_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_four_receiver_phone_no_tfActionPerformed

    private void two_three_dollar_money_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_dollar_money_rbActionPerformed
        selected_money_type_to_pro = type_of_money.Dollar;
        two_one_cal(selected_money_type_to_pro, two_three_sender_money_tf,
                two_three_service_money_tf, two_one_total_money_tf,
                two_three_balance_money_tf, two_three_rial_money_rb,
                two_three_dollar_money_rb, two_three_bart_money_rb);
        if (two_one_is_off_edit) {
            if (!two_three_sender_money_tf.getText().isEmpty() && two_one_is_off_edit
                    && (two_three_rial_money_rb.isSelected() || two_three_dollar_money_rb.isSelected()
                    || two_three_bart_money_rb.isSelected())) {
                two_three_service_money_tf.setText(service_validate(money_S_B_R_validate(selected_money_type_to_pro, two_three_sender_money_tf.getText(), true), selected_money_type_to_pro));
            } else {
                two_three_service_money_tf.setText("");
            }
        }
    }//GEN-LAST:event_two_three_dollar_money_rbActionPerformed

    private void two_three_bn_finishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_bn_finishActionPerformed

        set_to_pro_to_db(two_three_sender_phone_no_tf, two_three_receiver_phone_no_tf,
                two_one_total_money_tf, two_three_sender_money_tf, two_three_balance_money_tf,
                two_three_service_money_tf, two_one_pro_name_cb,
                two_three_rial_money_rb, two_three_dollar_money_rb,
                two_three_bart_money_rb, selected_money_type_to_pro, buttonGroup2, this);
        remove_all_in_list(two_one_ph_reciever_list);
        remove_all_in_list(two_one_ph_senter_list);
    }//GEN-LAST:event_two_three_bn_finishActionPerformed

    private void two_three_balance_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_balance_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_balance_money_tfActionPerformed

    private void two_one_total_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_total_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_one_total_money_tfActionPerformed

    private void two_three_service_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_service_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_service_money_tfActionPerformed

    private void two_three_sender_phone_no_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_sender_phone_no_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_sender_phone_no_tfActionPerformed

    private void two_one_bn_finishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_bn_finishActionPerformed

    }//GEN-LAST:event_two_one_bn_finishActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void two_one_tf_cus_moneyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_tf_cus_moneyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_one_tf_cus_moneyActionPerformed

    private void two_three_sender_phone_no_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_sender_phone_no_tfKeyReleased
        search_engine_pro(two_one_ph_senter_list, two_three_sender_phone_no_tf.getText().trim(),
                "sender_phone_no", "to_pro_sender_ph_no_history_tb");
    }//GEN-LAST:event_two_three_sender_phone_no_tfKeyReleased

    private void two_three_sender_money_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_sender_money_tfKeyReleased
        validate_KeyReleased_money(evt, two_three_sender_money_tf);
    }//GEN-LAST:event_two_three_sender_money_tfKeyReleased

    private void two_three_sender_money_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_sender_money_tfKeyTyped
        validate_KeyTyped_money(evt, two_three_sender_money_tf);
    }//GEN-LAST:event_two_three_sender_money_tfKeyTyped

    private void two_three_service_money_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_service_money_tfKeyReleased
        validate_KeyReleased_money(evt, two_three_service_money_tf);
    }//GEN-LAST:event_two_three_service_money_tfKeyReleased

    private void two_three_service_money_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_service_money_tfKeyTyped
        validate_KeyTyped_money(evt, two_three_service_money_tf);
    }//GEN-LAST:event_two_three_service_money_tfKeyTyped

    private void two_one_total_money_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_one_total_money_tfKeyReleased

    }//GEN-LAST:event_two_one_total_money_tfKeyReleased

    private void two_three_rial_money_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_rial_money_rbActionPerformed
        selected_money_type_to_pro = type_of_money.Rial;
        two_one_cal(selected_money_type_to_pro, two_three_sender_money_tf,
                two_three_service_money_tf, two_one_total_money_tf,
                two_three_balance_money_tf, two_three_rial_money_rb,
                two_three_dollar_money_rb, two_three_bart_money_rb);
        if (two_one_is_off_edit) {
            if (!two_three_sender_money_tf.getText().isEmpty() && two_one_is_off_edit
                    && (two_three_rial_money_rb.isSelected() || two_three_dollar_money_rb.isSelected()
                    || two_three_bart_money_rb.isSelected())) {
                two_three_service_money_tf.setText(service_validate(money_S_B_R_validate(selected_money_type_to_pro, two_three_sender_money_tf.getText(), true), selected_money_type_to_pro));
            } else {
                two_three_service_money_tf.setText("");
            }
        }
    }//GEN-LAST:event_two_three_rial_money_rbActionPerformed

    private void two_three_bart_money_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_bart_money_rbActionPerformed
        selected_money_type_to_pro = type_of_money.Bart;
        two_one_cal(selected_money_type_to_pro, two_three_sender_money_tf,
                two_three_service_money_tf, two_one_total_money_tf,
                two_three_balance_money_tf, two_three_rial_money_rb,
                two_three_dollar_money_rb, two_three_bart_money_rb);
        if (two_one_is_off_edit) {
            if (!two_three_sender_money_tf.getText().isEmpty() && two_one_is_off_edit
                    && (two_three_rial_money_rb.isSelected() || two_three_dollar_money_rb.isSelected()
                    || two_three_bart_money_rb.isSelected())) {
                two_three_service_money_tf.setText(service_validate(money_S_B_R_validate(selected_money_type_to_pro, two_three_sender_money_tf.getText(), true), selected_money_type_to_pro));
            } else {
                two_three_service_money_tf.setText("");
            }
        }
    }//GEN-LAST:event_two_three_bart_money_rbActionPerformed

    private void two_three_sender_money_tfCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_two_three_sender_money_tfCaretUpdate
        two_one_cal(selected_money_type_to_pro, two_three_sender_money_tf,
                two_three_service_money_tf, two_one_total_money_tf,
                two_three_balance_money_tf, two_three_rial_money_rb,
                two_three_dollar_money_rb, two_three_bart_money_rb);
        if (two_one_is_off_edit) {
            if (!two_three_sender_money_tf.getText().isEmpty() && two_one_is_off_edit
                    && (two_three_rial_money_rb.isSelected() || two_three_dollar_money_rb.isSelected()
                    || two_three_bart_money_rb.isSelected())) {
                two_three_service_money_tf.setText(service_validate(money_S_B_R_validate(selected_money_type_to_pro, two_three_sender_money_tf.getText(), true), selected_money_type_to_pro));
            } else {
                two_three_service_money_tf.setText("");
            }
        }
    }//GEN-LAST:event_two_three_sender_money_tfCaretUpdate

    private void two_three_service_money_tfCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_two_three_service_money_tfCaretUpdate
        two_one_cal(selected_money_type_to_pro, two_three_sender_money_tf,
                two_three_service_money_tf, two_one_total_money_tf,
                two_three_balance_money_tf, two_three_rial_money_rb,
                two_three_dollar_money_rb, two_three_bart_money_rb);
    }//GEN-LAST:event_two_three_service_money_tfCaretUpdate

    private void two_three_sender_phone_no_tfCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_two_three_sender_phone_no_tfCaretUpdate

    }//GEN-LAST:event_two_three_sender_phone_no_tfCaretUpdate

    private void two_three_receiver_phone_no_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_receiver_phone_no_tfKeyReleased
        search_engine_pro(two_one_ph_reciever_list, two_three_receiver_phone_no_tf.getText().trim(),
                "receiver_phone_no", "to_pro_receiver_ph_no_history_tb");
    }//GEN-LAST:event_two_three_receiver_phone_no_tfKeyReleased

    private void two_one_sender_his_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_sender_his_bnActionPerformed
        view_history_list view_his_obj = new view_history_list(
                this, "sender_phone_no", "to_pro_sender_ph_no_history_tb", "view sender phone number history",
                "Sender phone number hishory", "Edit phone number", "Add new phone number",
                "edit sender phone number history", "add sender phone number history", true, true, false);
        view_his_obj.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_two_one_sender_his_bnActionPerformed

    private void two_one_reciever_his_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_reciever_his_bnActionPerformed
        view_history_list view_his_obj = new view_history_list(
                this, "receiver_phone_no", "to_pro_receiver_ph_no_history_tb",
                "view receiver phone number history", "receiver phone number hishory", "Edit phone number",
                "Add new phone number", "edit receiver phone number history", "add receiver phone number history", true, true, false);
        view_his_obj.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_two_one_reciever_his_bnActionPerformed

    private void two_one_pro_his_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_pro_his_bnActionPerformed
        view_history_list view_his_obj = new view_history_list(
                this, "transfer_province", "province_name_history_tb",
                "view transfer province history", "transfer province hishory", "Edit transfer province",
                "Add new transfer province", "edit transfer province history", "add transfer province history", false, false, true);
        view_his_obj.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_two_one_pro_his_bnActionPerformed

    private void two_three_sender_phone_no_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_sender_phone_no_tfKeyTyped
        validate_keyTyped_ph_num(evt, two_three_sender_phone_no_tf);
    }//GEN-LAST:event_two_three_sender_phone_no_tfKeyTyped

    private void two_three_receiver_phone_no_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_receiver_phone_no_tfKeyTyped
        validate_keyTyped_ph_num(evt, two_three_receiver_phone_no_tf);
    }//GEN-LAST:event_two_three_receiver_phone_no_tfKeyTyped

    private void two_one_edit_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_edit_bnActionPerformed

        if (two_one_is_off_edit) {
            if (!set_admin_password.equals(JOptionPane.showInputDialog(this, "Enter password"))) {
                JOptionPane.showMessageDialog(this, "Incorrect password", "Alert", JOptionPane.WARNING_MESSAGE);
            } else {
                two_one_edit_bn.setText("on edit");
                two_three_service_money_tf.setFocusable(true);
                two_three_service_money_tf.setEditable(true);
                two_one_is_off_edit = false;
            }
        } else {
            two_one_edit_bn.setText("auto fill");
            two_three_service_money_tf.setFocusable(false);
            two_three_service_money_tf.setEditable(false);
            two_one_is_off_edit = true;
        }
        two_one_cal(selected_money_type_to_pro, two_three_sender_money_tf,
                two_three_service_money_tf, two_one_total_money_tf,
                two_three_balance_money_tf, two_three_rial_money_rb,
                two_three_dollar_money_rb, two_three_bart_money_rb);
        if (two_one_is_off_edit) {
            if (!two_three_sender_money_tf.getText().isEmpty() && two_one_is_off_edit
                    && (two_three_rial_money_rb.isSelected() || two_three_dollar_money_rb.isSelected()
                    || two_three_bart_money_rb.isSelected())) {
                two_three_service_money_tf.setText(service_validate(money_S_B_R_validate(selected_money_type_to_pro, two_three_sender_money_tf.getText(), true), selected_money_type_to_pro));
            } else {
                two_three_service_money_tf.setText("");
            }
        }
    }//GEN-LAST:event_two_one_edit_bnActionPerformed

    private void two_one_ph_senter_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_one_ph_senter_listMousePressed
        get_pro_result_search_db(two_one_ph_senter_list, two_three_sender_phone_no_tf);
    }//GEN-LAST:event_two_one_ph_senter_listMousePressed

    private void two_three_sender_phone_no_tfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_sender_phone_no_tfKeyPressed
        if (two_one_ph_senter_list.getModel().getSize() != 0) {
            int code = evt.getKeyCode();
            switch (code) {
                case KeyEvent.VK_DOWN:
                    two_one_ph_senter_list.requestFocus();
                    break;
            }
        }
    }//GEN-LAST:event_two_three_sender_phone_no_tfKeyPressed

    private void two_one_ph_senter_listKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_one_ph_senter_listKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_ENTER:
                get_pro_result_search_db(two_one_ph_senter_list, two_three_sender_phone_no_tf);
                break;
        }
    }//GEN-LAST:event_two_one_ph_senter_listKeyPressed

    private void two_three_receiver_phone_no_tfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_receiver_phone_no_tfKeyPressed
        if (two_one_ph_reciever_list.getModel().getSize() != 0) {
            int code = evt.getKeyCode();
            switch (code) {
                case KeyEvent.VK_DOWN:
                    two_one_ph_reciever_list.requestFocus();
                    break;
            }
        }
    }//GEN-LAST:event_two_three_receiver_phone_no_tfKeyPressed

    private void two_one_ph_reciever_listKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_one_ph_reciever_listKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_ENTER:
                get_pro_result_search_db(two_one_ph_reciever_list, two_three_receiver_phone_no_tf);
                break;
        }

    }//GEN-LAST:event_two_one_ph_reciever_listKeyPressed

    private void two_one_ph_reciever_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_one_ph_reciever_listMousePressed
        get_pro_result_search_db(two_one_ph_reciever_list, two_three_receiver_phone_no_tf);
    }//GEN-LAST:event_two_one_ph_reciever_listMousePressed

    private void two_two_reciever_his_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_two_reciever_his_bnActionPerformed
        view_history_list view_his_obj = new view_history_list(
                this, "receiver_phone_no", "from_pro_receiver_ph_no_history_tb",
                "view receiver phone number history", "receiver phone number hishory", "Edit phone number",
                "Add new phone number", "edit receiver phone number history", "add receiver phone number history", true, true, false);
        view_his_obj.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_two_two_reciever_his_bnActionPerformed

    private void two_one_pro_his_bn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_pro_his_bn1ActionPerformed
        view_history_list view_his_obj = new view_history_list(
                this, "transfer_province", "province_name_history_tb",
                "view transfer province history", "transfer province hishory", "Edit transfer province",
                "Add new transfer province", "edit transfer province history", "add transfer province history", false, false, true);
        view_his_obj.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_two_one_pro_his_bn1ActionPerformed

    private void two_three_receiver_phone_no_tfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_three_receiver_phone_no_tfMouseClicked
        remove_all_in_list(two_one_ph_senter_list);
    }//GEN-LAST:event_two_three_receiver_phone_no_tfMouseClicked

    private void two_three_sender_phone_no_tfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_three_sender_phone_no_tfMouseClicked
        remove_all_in_list(two_one_ph_reciever_list);
    }//GEN-LAST:event_two_three_sender_phone_no_tfMouseClicked

    private void two_one_pro_name_cbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_one_pro_name_cbMouseClicked
        remove_all_in_list(two_one_ph_reciever_list);
        remove_all_in_list(two_one_ph_senter_list);
    }//GEN-LAST:event_two_one_pro_name_cbMouseClicked

    private void two_three_sender_money_tfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_three_sender_money_tfMouseClicked
        remove_all_in_list(two_one_ph_reciever_list);
        remove_all_in_list(two_one_ph_senter_list);
    }//GEN-LAST:event_two_three_sender_money_tfMouseClicked

    private void two_three_service_money_tfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_three_service_money_tfMouseClicked
        remove_all_in_list(two_one_ph_reciever_list);
        remove_all_in_list(two_one_ph_senter_list);
    }//GEN-LAST:event_two_three_service_money_tfMouseClicked

    private void two_one_sender_his_bnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_one_sender_his_bnMouseClicked
        remove_all_in_list(two_one_ph_reciever_list);
        remove_all_in_list(two_one_ph_senter_list);
    }//GEN-LAST:event_two_one_sender_his_bnMouseClicked

    private void two_two_ph_recieve_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_two_ph_recieve_listMousePressed

        get_pro_result_search_db(two_two_ph_recieve_list, two_four_receiver_phone_no_tf);
    }//GEN-LAST:event_two_two_ph_recieve_listMousePressed

    private void two_two_ph_recieve_listKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_two_ph_recieve_listKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_ENTER:
                get_pro_result_search_db(two_two_ph_recieve_list, two_four_receiver_phone_no_tf);
                break;
        }
    }//GEN-LAST:event_two_two_ph_recieve_listKeyPressed

    private void two_three_receiver_phone_no_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_receiver_phone_no_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_receiver_phone_no_tfActionPerformed

    private void two_four_receiver_phone_no_tfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_four_receiver_phone_no_tfKeyPressed
        if (two_two_ph_recieve_list.getModel().getSize() != 0) {
            int code = evt.getKeyCode();
            switch (code) {
                case KeyEvent.VK_DOWN:
                    two_two_ph_recieve_list.requestFocus();
                    break;
            }
        }
    }//GEN-LAST:event_two_four_receiver_phone_no_tfKeyPressed

    private void two_four_receiver_phone_no_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_four_receiver_phone_no_tfKeyReleased
        search_engine_pro(two_two_ph_recieve_list, two_four_receiver_phone_no_tf.getText().trim(),
                "receiver_phone_no", "from_pro_receiver_ph_no_history_tb");
    }//GEN-LAST:event_two_four_receiver_phone_no_tfKeyReleased

    private void two_four_receiver_phone_no_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_four_receiver_phone_no_tfKeyTyped
        validate_keyTyped_ph_num(evt, two_four_receiver_phone_no_tf);
    }//GEN-LAST:event_two_four_receiver_phone_no_tfKeyTyped

    private void two_four_receiver_phone_no_tfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_four_receiver_phone_no_tfMouseClicked

    }//GEN-LAST:event_two_four_receiver_phone_no_tfMouseClicked

    private void two_four_rial_money_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_four_rial_money_rbActionPerformed
        selected_money_type_from_pro = type_of_money.Rial;
        two_two_cal(selected_money_type_from_pro, two_four_reciece_money_tf,
                two_four_total_money_tf, two_four_balance_money_tf,
                two_four_rial_money_rb, two_four_dollar_money_rb,
                two_four_bart_money_rb);
    }//GEN-LAST:event_two_four_rial_money_rbActionPerformed

    private void two_four_bart_money_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_four_bart_money_rbActionPerformed
        selected_money_type_from_pro = type_of_money.Bart;
        two_two_cal(selected_money_type_from_pro, two_four_reciece_money_tf,
                two_four_total_money_tf, two_four_balance_money_tf,
                two_four_rial_money_rb, two_four_dollar_money_rb,
                two_four_bart_money_rb);
    }//GEN-LAST:event_two_four_bart_money_rbActionPerformed

    private void two_four_reciece_money_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_four_reciece_money_tfKeyReleased
        validate_KeyReleased_money(evt, two_four_reciece_money_tf);
    }//GEN-LAST:event_two_four_reciece_money_tfKeyReleased

    private void two_four_reciece_money_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_four_reciece_money_tfKeyTyped
        validate_KeyTyped_money(evt, two_four_reciece_money_tf);
    }//GEN-LAST:event_two_four_reciece_money_tfKeyTyped

    private void two_four_total_money_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_four_total_money_tfKeyReleased
        validate_KeyReleased_money(evt, two_four_total_money_tf);
    }//GEN-LAST:event_two_four_total_money_tfKeyReleased

    private void two_four_total_money_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_four_total_money_tfKeyTyped
        validate_KeyTyped_money(evt, two_four_total_money_tf);
    }//GEN-LAST:event_two_four_total_money_tfKeyTyped

    private void two_four_reciece_money_tfCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_two_four_reciece_money_tfCaretUpdate

        two_two_cal(selected_money_type_from_pro, two_four_reciece_money_tf,
                two_four_total_money_tf, two_four_balance_money_tf,
                two_four_rial_money_rb, two_four_dollar_money_rb,
                two_four_bart_money_rb);
    }//GEN-LAST:event_two_four_reciece_money_tfCaretUpdate

    private void two_four_total_money_tfCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_two_four_total_money_tfCaretUpdate

        two_two_cal(selected_money_type_from_pro, two_four_reciece_money_tf,
                two_four_total_money_tf, two_four_balance_money_tf,
                two_four_rial_money_rb, two_four_dollar_money_rb,
                two_four_bart_money_rb);
    }//GEN-LAST:event_two_four_total_money_tfCaretUpdate

    private void two_two_service_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_two_service_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_two_service_money_tfActionPerformed

    private void two_two_reveiver_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_two_reveiver_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_two_reveiver_money_tfActionPerformed

    private void two_two_bn_finishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_two_bn_finishActionPerformed

    }//GEN-LAST:event_two_two_bn_finishActionPerformed

    private void two_two_pro_name_cbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_two_pro_name_cbMouseClicked

        remove_all_in_list(two_two_ph_recieve_list);
    }//GEN-LAST:event_two_two_pro_name_cbMouseClicked

    private void two_four_reciece_money_tfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_four_reciece_money_tfMouseClicked

        remove_all_in_list(two_two_ph_recieve_list);
    }//GEN-LAST:event_two_four_reciece_money_tfMouseClicked

    private void two_four_rial_money_rbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_four_rial_money_rbMouseClicked

        remove_all_in_list(two_two_ph_recieve_list);
    }//GEN-LAST:event_two_four_rial_money_rbMouseClicked

    private void two_four_dollar_money_rbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_four_dollar_money_rbMouseClicked

        remove_all_in_list(two_two_ph_recieve_list);
    }//GEN-LAST:event_two_four_dollar_money_rbMouseClicked

    private void two_four_bart_money_rbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_four_bart_money_rbMouseClicked

        remove_all_in_list(two_two_ph_recieve_list);
    }//GEN-LAST:event_two_four_bart_money_rbMouseClicked

    private void two_four_total_money_tfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_four_total_money_tfMouseClicked

        remove_all_in_list(two_two_ph_recieve_list);
    }//GEN-LAST:event_two_four_total_money_tfMouseClicked

    private void two_three_sender_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_sender_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_sender_money_tfActionPerformed

    private void two_one_tf_cus_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_tf_cus_noActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_one_tf_cus_noActionPerformed

    private void two_three_bank_info_his_bnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_three_bank_info_his_bnMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_bank_info_his_bnMouseClicked

    private void two_three_bank_info_his_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_bank_info_his_bnActionPerformed
        view_history_thai view_his_thai_obj = new view_history_thai(this);
        view_his_thai_obj.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_two_three_bank_info_his_bnActionPerformed

    private void one_tf_customer_money1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_one_tf_customer_money1CaretUpdate
        caret_one_two(one_tf_customer_money1, one_two_rate_bc1,
                one_tf_customer_result1, one_tf_exchange_rate1);
    }//GEN-LAST:event_one_tf_customer_money1CaretUpdate

    private void one_tf_customer_money1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_tf_customer_money1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_customer_money1ActionPerformed

    private void one_tf_customer_money1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_customer_money1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_customer_money1KeyPressed

    private void one_tf_customer_money1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_customer_money1KeyReleased
        validate_KeyReleased_money(evt, one_tf_customer_money1);
    }//GEN-LAST:event_one_tf_customer_money1KeyReleased

    private void one_tf_customer_money1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_customer_money1KeyTyped
        validate_KeyTyped_money(evt, one_tf_customer_money1);
    }//GEN-LAST:event_one_tf_customer_money1KeyTyped

    private void one_tf_customer_result1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_tf_customer_result1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_customer_result1ActionPerformed

    private void one_two_bn_finishedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_two_bn_finishedActionPerformed
        double_exc_close_or_print(one_tf_customer_money1, one_tf_customer_money2, one_two_rate_bc1,
                one_two_rate_bc2, one_tf_exchange_rate1, one_tf_customer_result1,
                one_tf_exchange_rate2, one_tf_customer_result2, false, this);
    }//GEN-LAST:event_one_two_bn_finishedActionPerformed

    private void one_two_bn_finishedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_two_bn_finishedKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_two_bn_finishedKeyPressed

    private void one_bn_print1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_bn_print1ActionPerformed
        double_exc_close_or_print(one_tf_customer_money1, one_tf_customer_money2, one_two_rate_bc1,
                one_two_rate_bc2, one_tf_exchange_rate1, one_tf_customer_result1,
                one_tf_exchange_rate2, one_tf_customer_result2, true, this);
    }//GEN-LAST:event_one_bn_print1ActionPerformed

    private void one_bn_print1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_bn_print1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_bn_print1KeyPressed

    private void one_tf_customer_money2CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_one_tf_customer_money2CaretUpdate
        caret_one_two(one_tf_customer_money2, one_two_rate_bc2,
                one_tf_customer_result2, one_tf_exchange_rate2);
    }//GEN-LAST:event_one_tf_customer_money2CaretUpdate

    private void one_tf_customer_money2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_tf_customer_money2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_customer_money2ActionPerformed

    private void one_tf_customer_money2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_customer_money2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_customer_money2KeyPressed

    private void one_tf_customer_money2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_customer_money2KeyReleased
        validate_KeyReleased_money(evt, one_tf_customer_money2);
    }//GEN-LAST:event_one_tf_customer_money2KeyReleased

    private void one_tf_customer_money2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_customer_money2KeyTyped
        validate_KeyTyped_money(evt, one_tf_customer_money2);
    }//GEN-LAST:event_one_tf_customer_money2KeyTyped

    private void one_tf_customer_result2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_tf_customer_result2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_customer_result2ActionPerformed

    private void one_tf_exchange_rate1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_one_tf_exchange_rate1CaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_exchange_rate1CaretUpdate

    private void one_tf_exchange_rate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_tf_exchange_rate1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_exchange_rate1ActionPerformed

    private void one_tf_exchange_rate1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_exchange_rate1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_exchange_rate1KeyPressed

    private void one_tf_exchange_rate2CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_one_tf_exchange_rate2CaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_exchange_rate2CaretUpdate

    private void one_tf_exchange_rate2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_tf_exchange_rate2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_exchange_rate2ActionPerformed

    private void one_tf_exchange_rate2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_one_tf_exchange_rate2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_one_tf_exchange_rate2KeyPressed

    private void one_two_rate_bc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_two_rate_bc1ActionPerformed
        set_to_exc_two_cb(one_two_rate_bc1, one_tf_exchange_rate1, one_money_type_lb1, one_money_type_lb4);
        caret_one_two(one_tf_customer_money1, one_two_rate_bc1,
                one_tf_customer_result1, one_tf_exchange_rate1);
    }//GEN-LAST:event_one_two_rate_bc1ActionPerformed

    private void one_two_rate_bc2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_one_two_rate_bc2ActionPerformed
        set_to_exc_two_cb(one_two_rate_bc2, one_tf_exchange_rate2, one_money_type_lb3_, one_money_type_lb2);
        caret_one_two(one_tf_customer_money2, one_two_rate_bc2,
                one_tf_customer_result2, one_tf_exchange_rate2);
    }//GEN-LAST:event_one_two_rate_bc2ActionPerformed

    private void two_one_pro_name_cbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_one_pro_name_cbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_one_pro_name_cbActionPerformed

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

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void two_three_to_thai_bank_id_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_three_to_thai_bank_id_listMousePressed
        get_bank_thai_result_search_db(two_three_to_thai_bank_id_list, two_one_tf_cus_no,
                two_one_tf_cus_name, two_three_tf_cus_ph_no, two_three_bank_thai_cb);
    }//GEN-LAST:event_two_three_to_thai_bank_id_listMousePressed

    private void two_three_to_thai_bank_id_listKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_to_thai_bank_id_listKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_to_thai_bank_id_listKeyPressed

    private void two_one_tf_cus_noKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_one_tf_cus_noKeyReleased
        search_engine_bank_thai(two_three_to_thai_bank_id_list, two_one_tf_cus_no.getText().trim(),
                "bank_id", "name", "phone_no", "bank", "to_thai_history_tb");
    }//GEN-LAST:event_two_one_tf_cus_noKeyReleased

    private void two_three_to_thai_name_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_three_to_thai_name_listMousePressed
        get_bank_thai_result_search_db(two_three_to_thai_name_list, two_one_tf_cus_name,
                two_one_tf_cus_no, two_three_tf_cus_ph_no, two_three_bank_thai_cb);
    }//GEN-LAST:event_two_three_to_thai_name_listMousePressed

    private void two_three_to_thai_name_listKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_to_thai_name_listKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_to_thai_name_listKeyPressed

    private void two_three_to_thai_ph_no_listMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_three_to_thai_ph_no_listMousePressed
        get_bank_thai_result_search_db(two_three_to_thai_ph_no_list, two_three_tf_cus_ph_no,
                two_one_tf_cus_no, two_one_tf_cus_name, two_three_bank_thai_cb);
    }//GEN-LAST:event_two_three_to_thai_ph_no_listMousePressed

    private void two_three_to_thai_ph_no_listKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_to_thai_ph_no_listKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_three_to_thai_ph_no_listKeyPressed

    private void two_three_bank_thai_cbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_two_three_bank_thai_cbMouseClicked

    }//GEN-LAST:event_two_three_bank_thai_cbMouseClicked

    private void two_three_bank_his_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_three_bank_his_bnActionPerformed
        view_history_list view_his_obj = new view_history_list(
                this, "bank", "to_thai_bank_name_history_tb",
                "view thai bank", "thai bank", "Edit bank name",
                "Add new bank name", "edit bank name", "add bank name", false, false, true);
        view_his_obj.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_two_three_bank_his_bnActionPerformed

    private void two_one_tf_cus_nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_one_tf_cus_nameKeyReleased
        search_engine_bank_thai(two_three_to_thai_name_list, two_one_tf_cus_name.getText().trim(),
                "name", "bank_id", "phone_no", "bank", "to_thai_history_tb");
    }//GEN-LAST:event_two_one_tf_cus_nameKeyReleased

    private void two_three_tf_cus_ph_noKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_two_three_tf_cus_ph_noKeyReleased
        search_engine_bank_thai(two_three_to_thai_ph_no_list, two_three_tf_cus_ph_no.getText().trim(),
                "phone_no", "bank_id", "name", "bank", "to_thai_history_tb");
    }//GEN-LAST:event_two_three_tf_cus_ph_noKeyReleased

    private void two_two_pro_name_cbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_two_two_pro_name_cbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_two_two_pro_name_cbActionPerformed

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
            java.util.logging.Logger.getLogger(UI_and_operation.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UI_and_operation.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UI_and_operation.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UI_and_operation.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JPanel db_con_pt;
    private javax.swing.JPanel double_exc_pn;
    private javax.swing.JPanel exc_pn;
    private javax.swing.JPanel exc_pt;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JButton five_create_acc_tf;
    private javax.swing.JButton five_dev_permission_bn;
    private javax.swing.JTextField five_dev_permission_tf;
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
    private javax.swing.JPanel from_pro_pn;
    private javax.swing.JPanel from_thai_pn;
    private javax.swing.JPanel his_pt;
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
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel34;
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
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JButton one_bn_B_to_R;
    private javax.swing.JButton one_bn_B_to_S;
    private javax.swing.JButton one_bn_R_to_B;
    private javax.swing.JButton one_bn_R_to_S;
    private javax.swing.JButton one_bn_S_to_B;
    private javax.swing.JButton one_bn_S_to_R;
    private javax.swing.JButton one_bn_finished;
    private javax.swing.JButton one_bn_print;
    private javax.swing.JButton one_bn_print1;
    private javax.swing.JLabel one_lb_customer_money;
    private javax.swing.JLabel one_lb_customer_money1;
    private javax.swing.JLabel one_lb_customer_money2;
    private javax.swing.JLabel one_lb_customer_result;
    private javax.swing.JLabel one_lb_customer_result1;
    private javax.swing.JLabel one_lb_customer_result2;
    private javax.swing.JLabel one_lb_equal;
    private javax.swing.JLabel one_lb_equal1;
    private javax.swing.JLabel one_lb_equal2;
    private javax.swing.JLabel one_lb_exchange_rate;
    private javax.swing.JLabel one_lb_exchange_rate1;
    private javax.swing.JLabel one_lb_exchange_rate3;
    private javax.swing.JLabel one_lb_operator;
    private javax.swing.JLabel one_lb_operator1;
    private javax.swing.JLabel one_lb_operator2;
    private javax.swing.JLabel one_money_type_lb;
    private javax.swing.JLabel one_money_type_lb1;
    private javax.swing.JLabel one_money_type_lb2;
    private javax.swing.JLabel one_money_type_lb3_;
    private javax.swing.JLabel one_money_type_lb4;
    private javax.swing.JLabel one_money_type_result_lb;
    private javax.swing.JLabel one_rate_type_lb;
    private javax.swing.JTextField one_tf_customer_money;
    private javax.swing.JTextField one_tf_customer_money1;
    private javax.swing.JTextField one_tf_customer_money2;
    private javax.swing.JTextField one_tf_customer_result;
    private javax.swing.JTextField one_tf_customer_result1;
    private javax.swing.JTextField one_tf_customer_result2;
    private javax.swing.JTextField one_tf_exchange_rate;
    private javax.swing.JTextField one_tf_exchange_rate1;
    private javax.swing.JTextField one_tf_exchange_rate2;
    private javax.swing.JButton one_two_bn_finished;
    private javax.swing.JComboBox<String> one_two_rate_bc1;
    private javax.swing.JComboBox<String> one_two_rate_bc2;
    private javax.swing.JButton print;
    private javax.swing.JPanel rate_pt;
    private javax.swing.JTabbedPane sub_exc_pt;
    private javax.swing.JTabbedPane sub_tran_pt;
    private javax.swing.JButton three_add_bn;
    private javax.swing.JTextField three_add_tf;
    private javax.swing.JRadioButton three_bart_rb;
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
    private javax.swing.JRadioButton three_dollar_rb;
    private javax.swing.JButton three_down;
    private javax.swing.JRadioButton three_rial_rb;
    private javax.swing.JTable three_tb_history;
    private javax.swing.JTable three_tb_total_money;
    private javax.swing.JButton three_up;
    private javax.swing.JPanel to_pro_pn;
    private javax.swing.JPanel to_thai_pn;
    private javax.swing.JPanel tran_pt;
    private javax.swing.JTextField two_four_balance_money_tf;
    private javax.swing.JRadioButton two_four_bart_money_rb;
    private javax.swing.JButton two_four_bn_finish;
    private javax.swing.JRadioButton two_four_dollar_money_rb;
    private javax.swing.JTextField two_four_receiver_phone_no_tf;
    private javax.swing.JTextField two_four_reciece_money_tf;
    private javax.swing.JRadioButton two_four_rial_money_rb;
    private javax.swing.JTextField two_four_total_money_tf;
    private javax.swing.JButton two_one_bn_finish;
    private javax.swing.JButton two_one_edit_bn;
    private javax.swing.JLayeredPane two_one_ph_reciever_layer_pane;
    private javax.swing.JList<String> two_one_ph_reciever_list;
    private javax.swing.JLayeredPane two_one_ph_senter_layer_pane;
    private javax.swing.JList<String> two_one_ph_senter_list;
    private javax.swing.JButton two_one_pro_his_bn;
    private javax.swing.JButton two_one_pro_his_bn1;
    private javax.swing.JComboBox<String> two_one_pro_name_cb;
    private javax.swing.JButton two_one_reciever_his_bn;
    private javax.swing.JButton two_one_sender_his_bn;
    private javax.swing.JTextField two_one_tf_cus_money;
    private javax.swing.JTextField two_one_tf_cus_name;
    private javax.swing.JTextField two_one_tf_cus_no;
    private javax.swing.JTextField two_one_tf_service_money;
    private javax.swing.JTextField two_one_tf_total_money;
    private javax.swing.JTextField two_one_total_money_tf;
    private javax.swing.JTextField two_three_balance_money_tf;
    private javax.swing.JButton two_three_bank_his_bn;
    private javax.swing.JButton two_three_bank_info_his_bn;
    private javax.swing.JComboBox<String> two_three_bank_thai_cb;
    private javax.swing.JRadioButton two_three_bart_money_rb;
    private javax.swing.JButton two_three_bn_finish;
    private javax.swing.JRadioButton two_three_dollar_money_rb;
    private javax.swing.JTextField two_three_receiver_phone_no_tf;
    private javax.swing.JRadioButton two_three_rial_money_rb;
    private javax.swing.JTextField two_three_sender_money_tf;
    private javax.swing.JTextField two_three_sender_phone_no_tf;
    private javax.swing.JTextField two_three_service_money_tf;
    private javax.swing.JTextField two_three_tf_cus_ph_no;
    private javax.swing.JLayeredPane two_three_to_thai_bank_id_layer_pane;
    private javax.swing.JList<String> two_three_to_thai_bank_id_list;
    private javax.swing.JLayeredPane two_three_to_thai_name_layer_pane;
    private javax.swing.JList<String> two_three_to_thai_name_list;
    private javax.swing.JLayeredPane two_three_to_thai_ph_no_layer_pane;
    private javax.swing.JList<String> two_three_to_thai_ph_no_list;
    private javax.swing.JButton two_two_bn_finish;
    private javax.swing.JLayeredPane two_two_ph_recieve_layer_pane;
    private javax.swing.JList<String> two_two_ph_recieve_list;
    private javax.swing.JComboBox<String> two_two_pro_name_cb;
    private javax.swing.JButton two_two_reciever_his_bn;
    private javax.swing.JTextField two_two_result_money_tf;
    private javax.swing.JTextField two_two_reveiver_money_tf;
    private javax.swing.JTextField two_two_reveiver_ph_no_tf;
    private javax.swing.JTextField two_two_service_money_tf;
    private javax.swing.JTabbedPane zero_tp;
    // End of variables declaration//GEN-END:variables

}
