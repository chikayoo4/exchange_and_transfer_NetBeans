/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_p;
import admin_p.admin_view.type_of_money;
import com.sun.glass.events.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextField;

/**
 *
 * @author Chhann_chikay
 */
public class validate_value {

    private static char point_at_3_str = ',';
    private static String print_at_3_str = ",";
    
    //500 / 1000 = 0.5 => 1
    //1000.6 / 1000 = 1.0006 => 2
    //1000 / 1000 = 1.0 => 1
    //3000 / 1000 = 3.0 => 3
    public static String service_validate(String num_str, type_of_money money_type) {
        try {
            num_str = clear_cvot(num_str);
            Double num = 0.0;
            int ser = 0;
            switch (money_type) {
                case Rial:
                    num = Double.parseDouble(num_str) / 4000000;
                    ser = (int) Math.ceil(num);
                    return String.valueOf(ser * 4000);
                case Dollar:
                    num = Double.parseDouble(num_str) / 1000;
                    ser = (int) Math.ceil(num);
                    return String.valueOf(ser);
                case Bart:
                    num = Double.parseDouble(num_str) / 30000;
                    ser = (int) Math.ceil(num);
                    return String.valueOf(ser * 30);
                default:
all_type_error_mes error_mes = new all_type_error_mes("error function validate_value class: service_validate");
            }
        } catch (Exception e) {
all_type_error_mes error_mes = new all_type_error_mes("error function validate_value class: service_validate\n" + e);
            return "";
        }
        return "";
    }

    //dollar 1.0 -> 1
    //dollar 1.055 -> 1.05
    //dollar 1.054 -> 1.05
    public static String dollar_validation(Double num) {
        String str = String.format("%.3f", num);
        if (str.charAt(str.length() - 3) == '0' && str.charAt(str.length() - 2) == '0') {
            if (str.length() > 4) {
                str = str.substring(0, str.length() - 4);
            }
            return str;
        }
        str = str.substring(0, str.length() - 1);
        return str;
    }

    //rial_bart 1000.5 -> 1000
    //rial_bart 1000.4 -> 1000
    static String bart_validation(Double num) {
        String str = String.format("%.1f", num);
        if (str.length() > 2) {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }

    //4080 -> 4000
    static String rial_validation(Double num) {
        String str = String.format("%.0f", num);
        if (str.length() > 2) {
            str = str.substring(0, str.length() - 2);
            str = str + "00";
        } else {
            str = "0";
        }
        return str;
    }

    //1. ->1
    public static String cut_the_lastest_point(String money) {
        if (money.charAt(money.length() - 1) == '.') {
            money = money.substring(0, money.length() - 1);
        }
        return money;
    }

    //1. ->1.00
    public static String add_zero_behind_point(String money) {
        return String.format("%.2f", Double.parseDouble(money));
    }

    //clear '
    public static String clear_cvot(String money) {

        if (money.length() > 3) {

            String str = "";
            int scan = money.length() - 1;
            int point = money.length();
            while (scan >= 0) {
                if (money.charAt(scan) == point_at_3_str) {
                    str = money.substring(scan + 1, point) + str;
                    point = scan;
                }
                scan--;
            }
            str = money.substring(0, point) + str;
            return str;
        }
        return money;

    }

//1000000.05 -> 1'000'000.05
//1000000 -> 1'000'000
//1'000'000 -> 1'000'000
//1'000'000.05 -> 1'000'000.05
//100 -> 100
//1000'000.05 -> 1'000'000.05
//1000'000 -> 1'000'000
    //'1'000''00'0 -> 1'000'000
    //1000.005 -> 1'000.005
    public static String add_cuot_to_money(String money) {

        String minus_or_not = "";
        if (money.charAt(0) == '-') {
            money = money.substring(1, money.length());
            minus_or_not = "-";
        }
        if (money.length() > 3) {
            String temp = clear_cvot(money);
            String str = "";
            int tail = temp.length();
//            int head = ((temp.charAt(temp.length() - 3) == '.') ? (temp.length() - 3) : (temp.length()));
            int head = 0;
            if (is_has_double_points(temp)) {
                while (head >= 0) {
                    head++;
                    if (temp.charAt(head) == '.') {
                        break;
                    }
                }
            } else {
                head = temp.length();
            }
            str = temp.substring(head, tail);
            tail = head;
            head = head - 4;
            while (head >= 0) {
                if (temp.charAt(head) != point_at_3_str) {
                    head++;
                    while (head > 0) {
                        str = print_at_3_str + temp.substring(head, tail) + str;
                        tail = head;
                        head = head - 3;
                    }
                    str = temp.substring(0, tail) + str;
                    return minus_or_not + str;
                } else {
                    str = temp.substring(head, tail) + str;
                }
                tail = head;
                head = head - 4;
            }
        }
        return minus_or_not + clear_cvot(money);
    }

    public static String money_S_B_R_validate(type_of_money selected_exchange_rate, String money, Boolean is_add_cvot) {
        String str = "";
        money = clear_cvot(money);
        switch (selected_exchange_rate) {
            case Dollar:
                str = dollar_validation(Double.parseDouble(money));
                break;

            case Rial:
                str = rial_validation(Double.parseDouble(money));
                break;
            case Bart:
                str = bart_validation(Double.parseDouble(money));
                break;
        }
        if (is_add_cvot) {
            return add_cuot_to_money(str);
        } else {
            return str;
        }

    }

    public static Boolean is_has_double_points(String money) {
        Boolean is_has = false;
        is_has = money.contains(".");
        return is_has;
    }

    public static String add_zero_in_front_of_point(String money) {
        if (money.length() == 1) {
            if (money.charAt(0) == '.') {
                return "0" + money;
            }
        }
        return money;

    }

    public static void validate_KeyReleased_money(java.awt.event.KeyEvent evt, javax.swing.JTextField one_tf_customer_money) {

        if (!one_tf_customer_money.getText().isEmpty()) {
            one_tf_customer_money.setFocusable(false);
            String str = one_tf_customer_money.getText();
            one_tf_customer_money.setText("");
            one_tf_customer_money.setText(add_cuot_to_money(add_zero_in_front_of_point(str)));
            one_tf_customer_money.setFocusable(true);
            one_tf_customer_money.requestFocus();
        }
    }

    public static void validate_keyTyped_ph_num(java.awt.event.KeyEvent evt, javax.swing.JTextField input_tf) {
        if (!((Character.isDigit(evt.getKeyChar()) || (evt.getKeyChar() == KeyEvent.VK_BACKSPACE) || (evt.getKeyChar() == KeyEvent.VK_DELETE)))) {
            evt.consume();
        } else {
            if (!(input_tf.getText().length() < 10)) {
                evt.consume();
            }
        }
    }

    public static void validate_KeyTyped_money(java.awt.event.KeyEvent evt, javax.swing.JTextField input_tf) {

        char c = evt.getKeyChar();
        if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACKSPACE) || (c == KeyEvent.VK_DELETE)) || (c == '.' && !is_has_double_points(input_tf.getText())))) {
            evt.consume();
        }
    }

    public static Boolean validate_dc(com.toedter.calendar.JDateChooser dc) {
        Boolean is_valid = true;
        try {
            String sDate1 = ((JTextField) dc.getDateEditor().getUiComponent()).getText();
            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(sDate1);
        } catch (Exception e) {
            is_valid = false;
        }
        return is_valid;
    }
}
