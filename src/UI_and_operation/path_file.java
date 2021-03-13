/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import java.io.File;
import java.io.IOException;

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

}
