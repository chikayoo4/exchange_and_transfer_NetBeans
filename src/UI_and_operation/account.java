/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
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

    public static String get_acc_by_id(int id_acc) {
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
            System.err.println(ex);
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
            System.err.println(ex);
        }
        return id;
    }

}
