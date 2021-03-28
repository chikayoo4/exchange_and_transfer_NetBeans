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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Chhann_chikay
 */
public class reciept {

    private static Boolean is_print = true;

    public static void print_reciept(String dir, int lastinsertid) {

        HashMap m = new HashMap();
        m.put("invoice_id", lastinsertid);
        try {
            Connection con;
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //change locate whenever you move the project
            JasperDesign jdesign = JRXmlLoader.load(dir);
            JasperReport ireport = JasperCompileManager.compileReport(jdesign);
            JasperPrint jprint = JasperFillManager.fillReport(ireport, m, con);

            if (!is_print) {
                //view report
                JasperViewer.viewReport(jprint);
            } else {
                //print report 
                JasperPrintManager.printReport(jprint, false);
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        } catch (JRException ex) {
            all_type_error_mes error_mes = new all_type_error_mes("error function reciept class: print_reciept\n" + ex);
        }
    }

    public static void print_reciept_exc(String dir, int lastinsertid, String type_exc) {

        HashMap m = new HashMap();
//             m.put("invoice_id",  exchanging_obj.insert_to_db());
        m.put("invoice_id", lastinsertid);
        m.put("type_exc", type_exc);
        try {
            Connection con;
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //change locate whenever you move the project
            JasperDesign jdesign = JRXmlLoader.load(dir);
            JasperReport ireport = JasperCompileManager.compileReport(jdesign);
            JasperPrint jprint = JasperFillManager.fillReport(ireport, m, con);

            if (!is_print) {
                //view report
                JasperViewer.viewReport(jprint);
            } else {
                //print report 
                JasperPrintManager.printReport(jprint, false);
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        } catch (JRException ex) {
            all_type_error_mes error_mes = new all_type_error_mes("error function reciept class: print_reciept\n" + ex);
        }
    }

    public static void print_reciept_double_exc(String dir, int lastinsertid, String type_exc_one, String type_exc_two) {

        HashMap m = new HashMap();
//             m.put("invoice_id",  exchanging_obj.insert_to_db());
        m.put("invoice_id", lastinsertid);
        m.put("type_exc_one", type_exc_one);
        m.put("type_exc_two", type_exc_two);
        try {
            Connection con;
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //change locate whenever you move the project
            JasperDesign jdesign = JRXmlLoader.load(dir);
            JasperReport ireport = JasperCompileManager.compileReport(jdesign);
            JasperPrint jprint = JasperFillManager.fillReport(ireport, m, con);

            if (!is_print) {
                //view report
                JasperViewer.viewReport(jprint);
            } else {
                //print report 
                JasperPrintManager.printReport(jprint, false);
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        } catch (JRException ex) {
            all_type_error_mes error_mes = new all_type_error_mes("error function reciept class: print_reciept\n" + ex);
        }
    }

    public static void print_reciept_same_double_exc(String dir, int lastinsertid, String var, String money, String type_exc_one, String type_exc_two) {

        HashMap m = new HashMap();
//             m.put("invoice_id",  exchanging_obj.insert_to_db());
        m.put("invoice_id", lastinsertid);
        m.put("type_exc_one", type_exc_one);
        m.put("type_exc_two", type_exc_two);
        m.put(var, money);
        try {
            Connection con;
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //change locate whenever you move the project
            JasperDesign jdesign = JRXmlLoader.load(dir);
            JasperReport ireport = JasperCompileManager.compileReport(jdesign);
            JasperPrint jprint = JasperFillManager.fillReport(ireport, m, con);

            if (!is_print) {
                //view report
                JasperViewer.viewReport(jprint);
            } else {
                //print report 
                JasperPrintManager.printReport(jprint, false);
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        } catch (JRException ex) {
            all_type_error_mes error_mes = new all_type_error_mes("error function reciept class: print_reciept\n" + ex);
        }
    }

    public static void print_reciept_same_same_double_exc(String dir, int lastinsertid, String exc_m, String res_m, String type_exc) {

        HashMap m = new HashMap();
//             m.put("invoice_id",  exchanging_obj.insert_to_db());
        m.put("invoice_id", lastinsertid);
        m.put("exc_m", exc_m);
        m.put("res_m", res_m);
        m.put("type_exc", type_exc);
        try {
            Connection con;
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            //change locate whenever you move the project
            JasperDesign jdesign = JRXmlLoader.load(dir);
            JasperReport ireport = JasperCompileManager.compileReport(jdesign);
            JasperPrint jprint = JasperFillManager.fillReport(ireport, m, con);

            if (!is_print) {
                //view report
                JasperViewer.viewReport(jprint);
            } else {
                //print report 
                JasperPrintManager.printReport(jprint, false);
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        } catch (JRException ex) {
            all_type_error_mes error_mes = new all_type_error_mes("error function reciept class: print_reciept\n" + ex);
        }
    }

}
