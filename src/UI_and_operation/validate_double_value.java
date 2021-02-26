/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import UI_and_operation.UI_and_operation.type_of_money;
import com.sun.glass.events.KeyEvent;


/**
 *
 * @author Chhann_chikay
 */
public class validate_double_value {
    
    
    //dollar 1.0 -> 1
    //dollar 1.055 -> 1.05
    //dollar 1.054 -> 1.05
    public static String dollar_validation(Double num) {
        String str = String.format("%.3f", num);
        if(str.charAt(str.length() - 3) == '0' && str.charAt(str.length() - 2) == '0') {
            if(str.length() > 4){
                str = str.substring(0,str.length() - 4);
            }
        return str;
        }
            str = str.substring(0 ,str.length() - 1);
        return str;
    }
    
    //rial_bart 1000.5 -> 1000
    //rial_bart 1000.4 -> 1000
    static String bart_validation(Double num) {
        String str = String.format("%.1f", num);
        if(str.length() > 2){
            str = str.substring(0,str.length() - 2);
        }
        return str;
    }
    
    //4080 -> 4000
    static String rial_validation(Double num) {
        String str = String.format("%.0f", num);
        if(str.length() > 2){
            str = str.substring(0,str.length() - 2);
            str = str + "00";
        }
        return str;
    }
    
    //1. ->1
    public static String cut_the_lastest_point(String money){
        if(money.charAt(money.length() - 1) == '.'){
            money = money.substring(0, money.length() - 1);
        }
        return money;
    }
    
    //1. ->1.00
    public static String add_zero_behind_point(String money){
        return String.format("%.2f",Double.parseDouble(money));
    }
    
    //clear '
    public static String clear_cvot(String money){
        
        if(money.length() > 3){
            
        String str = "";
            int scan = money.length() - 1;
            int point = money.length();
            while(scan >= 0){
                if(money.charAt(scan) == '\''){
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
    public static String add_cuot_to_money(String money){
        
            String minus_or_not = "";
            if(money.charAt(0) == '-') {
                    money = money.substring(1, money.length());
                    minus_or_not = "-";
            }
        if(money.length() > 3){
        String temp = clear_cvot(money);
            String str = "";
            int tail = temp.length();
//            int head = ((temp.charAt(temp.length() - 3) == '.') ? (temp.length() - 3) : (temp.length()));
            int head = 0;
            if(is_has_double_points(temp)){
                while(head >= 0){
                    head++;
                    if(temp.charAt(head) == '.'){
                        break;
                    }
                }
            }
            else{
                head = temp.length();
            }
            str = temp.substring(head, tail);
                tail = head;
                head = head - 4;
            while(head >= 0){
                if(temp.charAt(head) != '\''){
                    head++;
                    while(head > 0){
                        str = "'" + temp.substring(head, tail) + str;
                        tail = head;
                        head = head - 3;
                    }
                    str = temp.substring(0, tail) + str;
                    return minus_or_not + str;
                }
                else{
                    str = temp.substring(head, tail) + str;
                }
                tail = head;
                head = head - 4;
            }
        }
        return minus_or_not + clear_cvot(money);
    }
    
    
    
    public static String money_S_B_R_validate(type_of_money selected_exchange_rate, String money) {
        String str = "";
        money = clear_cvot(money);
        switch(selected_exchange_rate){
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
        return add_cuot_to_money(str);
                 
    }
    
    public static Boolean is_has_double_points(String money){
         Boolean is_has = false;
        is_has = money.contains(".");
        return is_has;
    }
    
    public static String add_zero_in_front_of_point(String money) {
        if(money.length() == 1){
            if(money.charAt(0) == '.'){
                return "0" + money;
            }
        }
        return money;
                 
    }
    
    public static void validate_keyboard(java.awt.event.KeyEvent evt,  javax.swing.JTextField one_tf_customer_money){
    
        if (!((Character.isDigit(evt.getKeyChar()) || (evt.getKeyChar() == KeyEvent.VK_BACKSPACE) || (evt.getKeyChar() == KeyEvent.VK_DELETE)) || (evt.getKeyChar() == '.' && !is_has_double_points(one_tf_customer_money.getText())))) {
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
}
}
