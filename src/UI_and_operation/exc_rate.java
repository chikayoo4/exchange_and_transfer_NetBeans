/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import static UI_and_operation.UI_and_operation.current_date;
import static UI_and_operation.UI_and_operation.get_path;
import UI_and_operation.UI_and_operation.type_of_exchange;
import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

/**
 *
 * @author Chhann_chikay
 */
public class exc_rate {

    private String S_to_R_one;
    private String S_to_R_two;
    private String S_to_R_three;
    private String S_to_R_four;

    private String R_to_S_one;
    private String R_to_S_two;
    private String R_to_S_three;
    private String R_to_S_four;

    private String S_to_B_one;
    private String S_to_B_two;
    private String S_to_B_three;
    private String S_to_B_four;

    private String B_to_S_one;
    private String B_to_S_two;
    private String B_to_S_three;
    private String B_to_S_four;

    private String B_to_R_one;
    private String B_to_R_two;
    private String B_to_R_three;
    private String B_to_R_four;

    private String R_to_B_one;
    private String R_to_B_two;
    private String R_to_B_three;
    private String R_to_B_four;

    private String S_to_R;
    private String R_to_S;
    private String S_to_B;
    private String B_to_S;
    private String B_to_R;
    private String R_to_B;

    public static void end_task_ppt() {
        try {
            Runtime.getRuntime().exec("taskkill /f /im POWERPNT.EXE");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void open_exc_rate_ppt() {
        try {
            Desktop.getDesktop().open(new File(get_path() + "\\54 exchange.pptx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  int get_top_1_rate_from_db() {
        int id = -1;
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
            pst = con.prepareStatement("SELECT TOP 1 id_rate, dollar_to_rial, rial_to_dollar, dollar_to_bart, "
                    + "bart_to_dollar, bart_to_rial, rial_to_bart "
                    + "FROM exc_rate_tb ORDER BY date_rate DESC;");

            rs = pst.executeQuery();
            while (rs.next()) {
                
                id = rs.getInt("id_rate");
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
        return id;
    }

    public static void set_rate_to_excel(String S_to_R, String R_to_S, String S_to_B,
            String B_to_S, String B_to_R, String R_to_B) {

        Workbook wb;
        Sheet sh;
        FileInputStream fis;
        FileOutputStream fos;
        Row row;
        try {
            fis = new FileInputStream("./set_exching_rate.xlsx");
            wb = WorkbookFactory.create(fis);
            sh = wb.getSheet("rate");

            // Create a new font and alter it.
            Font font = wb.createFont();
//            font.setFontHeightInPoints((short) 24);
//            font.setFontName("Courier New");
            font.setItalic(true);
//            font.setStrikeout(true);
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFPalette palette = hwb.getCustomPalette();
//            HSSFColor myColor = palette.findSimilarColor(44, 19, 63);
            HSSFColor myColor = palette.findSimilarColor(112, 40, 120);
// get the palette index of that color 
            short palIndex = myColor.getIndex();
            font.setColor(palIndex);
            CellStyle style = wb.createCellStyle();
            style.setFont(font);

            String[] SSBxRBR = new String[3];
            SSBxRBR[0] = S_to_R;
            SSBxRBR[1] = S_to_B;
            SSBxRBR[2] = B_to_R;

            String[] RBRxSSB = new String[3];
            RBRxSSB[0] = R_to_S;
            RBRxSSB[1] = B_to_S;
            RBRxSSB[2] = R_to_B;

            for (int i = 0; i < 3; i++) {

                row = sh.createRow(i);
                for (int j = 0; j < 3; j++) {
                    Cell cell = row.createCell(j);
                    if (cell.getColumnIndex() == 0) {
                        cell.setCellValue(SSBxRBR[i]);
                        cell.setCellStyle(style);

                    }
                    if (cell.getColumnIndex() == 1) {
                        cell.setCellValue(RBRxSSB[i]);
                        cell.setCellStyle(style);
                    }
                }
            }

            fos = new FileOutputStream("./set_exching_rate.xlsx");
            wb.write(fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void set_rate_to_db(String S_to_R, String R_to_S, String S_to_B,
            String B_to_S, String B_to_R, String R_to_B) {

        Connection con;
        PreparedStatement pst;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );

            //write sql query to access
            pst = con.prepareStatement("insert into exc_rate_tb "
                    + "(date_rate, dollar_to_rial, rial_to_dollar, dollar_to_bart, bart_to_dollar, bart_to_rial, rial_to_bart) "
                    + "values( ?, ?, ?, ?, ?, ?, ?);");

            //set value to ?
            pst.setTimestamp(1, current_date());
            pst.setString(2, S_to_R);
            pst.setString(3, R_to_S);
            pst.setString(4, S_to_B);
            pst.setString(5, B_to_S);
            pst.setString(6, B_to_R);
            pst.setString(7, R_to_B);
            pst.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void set_each_rate(type_of_exchange type, String rate) {
        switch (type) {
            case S_to_R:
                S_to_R_one = rate.substring(0, 1);
                S_to_R_two = rate.substring(1, 2);
                S_to_R_three = rate.substring(2, 3);
                S_to_R_four = rate.substring(3, 4);
                break;
            case R_to_S:
                R_to_S_one = rate.substring(0, 1);
                R_to_S_two = rate.substring(1, 2);
                R_to_S_three = rate.substring(2, 3);
                R_to_S_four = rate.substring(3, 4);
                break;
            case S_to_B:
                S_to_B_one = rate.substring(0, 1);
                S_to_B_two = rate.substring(1, 2);
                S_to_B_three = rate.substring(3, 4);
                S_to_B_four = rate.substring(4, 5);
                break;
            case B_to_S:
                B_to_S_one = rate.substring(0, 1);
                B_to_S_two = rate.substring(1, 2);
                B_to_S_three = rate.substring(3, 4);
                B_to_S_four = rate.substring(4, 5);
                break;
            case B_to_R:
                B_to_R_one = rate.substring(0, 1);
                B_to_R_two = rate.substring(1, 2);
                B_to_R_three = rate.substring(2, 3);
                B_to_R_four = rate.substring(4, 5);
                break;
            case R_to_B:
                R_to_B_one = rate.substring(0, 1);
                R_to_B_two = rate.substring(1, 2);
                R_to_B_three = rate.substring(2, 3);
                R_to_B_four = rate.substring(4, 5);
                break;
            default:
                System.out.println("errorrrrr");
        }
    }

    public String getS_to_R() {
        return S_to_R;
    }

    public String getR_to_S() {
        return R_to_S;
    }

    public String getS_to_B() {
        return S_to_B;
    }

    public String getB_to_S() {
        return B_to_S;
    }

    public String getB_to_R() {
        return B_to_R;
    }

    public String getR_to_B() {
        return R_to_B;
    }

    public String getS_to_R_one() {
        return S_to_R_one;
    }

    public String getS_to_R_two() {
        return S_to_R_two;
    }

    public String getS_to_R_three() {
        return S_to_R_three;
    }

    public String getS_to_R_four() {
        return S_to_R_four;
    }

    public String getR_to_S_one() {
        return R_to_S_one;
    }

    public String getR_to_S_two() {
        return R_to_S_two;
    }

    public String getR_to_S_three() {
        return R_to_S_three;
    }

    public String getR_to_S_four() {
        return R_to_S_four;
    }

    public String getS_to_B_one() {
        return S_to_B_one;
    }

    public String getS_to_B_two() {
        return S_to_B_two;
    }

    public String getS_to_B_three() {
        return S_to_B_three;
    }

    public String getS_to_B_four() {
        return S_to_B_four;
    }

    public String getB_to_S_one() {
        return B_to_S_one;
    }

    public String getB_to_S_two() {
        return B_to_S_two;
    }

    public String getB_to_S_three() {
        return B_to_S_three;
    }

    public String getB_to_S_four() {
        return B_to_S_four;
    }

    public String getB_to_R_one() {
        return B_to_R_one;
    }

    public String getB_to_R_two() {
        return B_to_R_two;
    }

    public String getB_to_R_three() {
        return B_to_R_three;
    }

    public String getB_to_R_four() {
        return B_to_R_four;
    }

    public String getR_to_B_one() {
        return R_to_B_one;
    }

    public String getR_to_B_two() {
        return R_to_B_two;
    }

    public String getR_to_B_three() {
        return R_to_B_three;
    }

    public String getR_to_B_four() {
        return R_to_B_four;
    }

}
