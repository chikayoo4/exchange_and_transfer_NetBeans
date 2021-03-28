/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_p;

import static admin_p.connection_to_ms_sql.getLocal_host;
import static admin_p.connection_to_ms_sql.getLocal_host_password;
import static admin_p.connection_to_ms_sql.getLocal_host_user_name;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Chhann_chikay
 */
public class account {

    private static String user_name;
    private static String password;

    public static void setAccount(String user_name, String password) {
        account.user_name = user_name;
        account.password = password;
    }

    public static String getUser_name() {
        return user_name;
    }

    public static String getPassword() {
        return password;
    }

    public static void setUser_name(String user_name) {
        account.user_name = user_name;
    }

    public static void setPassword(String password) {
        account.password = password;
    }

    public static String get_user_n_by_id(int id_acc) {
        String user_n = "";
        try {
            Connection con;
            PreparedStatement pst;
            ResultSet rs;
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //query to access
            pst = con.prepareStatement("SELECT user_name FROM account_tb WHERE id_acc = " + id_acc + ";");
            rs = pst.executeQuery();

            while (rs.next()) {
                user_n = rs.getString("user_name");
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return user_n;
    }

    public static String get_pass_by_id(int id_acc) {
        String user_n = "";
        try {
            Connection con;
            PreparedStatement pst;
            ResultSet rs;
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //query to access
            pst = con.prepareStatement("SELECT password FROM account_tb WHERE id_acc = " + id_acc + ";");
            rs = pst.executeQuery();

            while (rs.next()) {
                user_n = rs.getString("password");
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return user_n;
    }

    public static int get_acc_id() {
        int id = -1;
        try {
            Connection con;
            PreparedStatement pst;
            ResultSet rs;
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //query to access
            pst = con.prepareStatement("SELECT id_acc FROM account_tb WHERE user_name = '" + user_name + "' AND password = '" + password + "';");
            rs = pst.executeQuery();

            while (rs.next()) {
                id = rs.getInt("id_acc");
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return id;
    }

    public static Boolean is_has_user_name_except(String user_name, String except_user_n) {
        Boolean is_has = false;
        try {
            Connection con;
            PreparedStatement pst;
            ResultSet rs;
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            if (!user_name.equals(except_user_n)) {
                pst = con.prepareStatement("SELECT user_name "
                        + "FROM account_tb "
                        + "WHERE user_name = ?;");
                pst.setString(1, user_name);
                rs = pst.executeQuery();
                while (rs.next()) {
                    is_has = true;
                }
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return is_has;
    }

    public static Boolean is_has_user_name(String user_name) {
        Boolean is_has = false;
        try {
            Connection con;
            PreparedStatement pst;
            ResultSet rs;
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT user_name "
                    + "FROM account_tb "
                    + "WHERE user_name = ?;");
            pst.setString(1, user_name);
            rs = pst.executeQuery();
            while (rs.next()) {
                is_has = true;
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
        return is_has;
    }
}
