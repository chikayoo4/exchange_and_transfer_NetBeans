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
public class connection_to_ms_sql {
    private static String local_host;
    private static String local_host_user_name;
    private static String local_host_password;
    private static String wifi_host;
    private static String wifi_host_user_name;
    private static String wifi_host_password;

    public static void setLocal_host(String local_host_server_name, String local_host_db_name) {
        connection_to_ms_sql.local_host = "jdbc:sqlserver://" + local_host_server_name + "\\sqlexpress;databaseName=" + local_host_db_name;
    }

    public static String getLocal_host_user_name() {
        return local_host_user_name;
    }

    public static String getLocal_host_password() {
        return local_host_password;
    }

    public static String getWifi_host_user_name() {
        return wifi_host_user_name;
    }

    public static String getWifi_host_password() {
        return wifi_host_password;
    }

    public static void setWifi_host(String wifi_host) {
        connection_to_ms_sql.wifi_host = wifi_host;
    }

    public static String getLocal_host() {
        return local_host;
    }

    public static String getWifi_host() {
        return wifi_host;
    }

    public static void setLocal_host_user_name(String local_host_user_name) {
        connection_to_ms_sql.local_host_user_name = local_host_user_name;
    }

    public static void setLocal_host_password(String local_host_password) {
        connection_to_ms_sql.local_host_password = local_host_password;
    }

    public static void setWifi_host_user_name(String wifi_host_user_name) {
        connection_to_ms_sql.wifi_host_user_name = wifi_host_user_name;
    }

    public static void setWifi_host_password(String wifi_host_password) {
        connection_to_ms_sql.wifi_host_password = wifi_host_password;
    }

}
