/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import UI_and_operation.UI_and_operation.type_of_exchange;

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
    public void set_each_rate(type_of_exchange type, String rate){
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
