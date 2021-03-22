/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Chhann_chikay
 */
public class path_file {

    public static String exc_reciept_path = "\\reciept_for_print\\exchanging.jrxml";
    public static String double_exc_reciept_path = "\\reciept_for_print\\double_exchanging.jrxml";

    public static String get_path() {
        String path = "";
        try {
            File file = new File("x.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            path = file.getAbsolutePath();
            path = path.substring(0, path.length() - 6);
        } catch (IOException e) {
            System.out.println("error");
        }
        return path;
    }

    public static void set_main_proj_path_to_db() {

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

            String path = "";
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

            pst = con.prepareStatement("UPDATE project_path SET path_name = '" + path + "';");
            pst.executeUpdate();

        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }

    }
}
