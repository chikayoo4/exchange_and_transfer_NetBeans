/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;


/**
 *
 * @author Chhann_chikay
 */
public class get_from_thai {
    private String am_or_pm;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private Double receiver_money;
    private Double service_money;
    private Double total_money;
    private String ph_no;
    


    public get_from_thai(  String am_or_pm, 
                                        int year, 
                                        int month, 
                                        int day, 
                                        int hour, 
                                        int minute, 
                                        Double receiver_money, 
                                        Double service_money, 
                                        Double total_money,
                                        String ph_no) {
        
        if(am_or_pm.equals("am")) {
            if(hour > 12){ 
                hour = hour - 12;
            }
        }
        else if(am_or_pm.equals("pm")) {
            if(hour < 12) {
                hour = hour + 12;
            }
        }
        
        this.am_or_pm = am_or_pm;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.receiver_money = receiver_money;
        this.service_money = service_money;
        this.total_money = total_money;
        this.ph_no = ph_no;
    }

    public String getAm_or_pm() {
        return am_or_pm;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getPh_no() {
        return ph_no;
    }

    

    public Double getReceiver_money() {
        return receiver_money;
    }

    public Double getService_money() {
        return service_money;
    }

    public Double getTotal_money() {
        return total_money;
    }
    
    
}
