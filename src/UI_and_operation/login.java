/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import UI_and_operation.UI_and_operation.dialog_type_for_db_e_a;
import static UI_and_operation.UI_and_operation.field_admin_pass;
import static UI_and_operation.UI_and_operation.silivor_c;
import static UI_and_operation.UI_and_operation.sky_c;
import static UI_and_operation.UI_and_operation.white_c;
import static UI_and_operation.account.setAccount;
import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import static UI_and_operation.connection_to_ms_sql.setLocal_host;
import static UI_and_operation.connection_to_ms_sql.setLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.setLocal_host_user_name;
import static UI_and_operation.connection_to_ms_sql.setWifi_host;
import static UI_and_operation.connection_to_ms_sql.setWifi_host_user_name;
import static UI_and_operation.path_file.set_main_proj_path_to_db;
import static UI_and_operation.view_history_list.col_sql;
import static UI_and_operation.view_history_list.tb_sql;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Chhann_chikay
 */
public class login extends javax.swing.JFrame
        implements WindowListener {

    private UI_and_operation UI_and_ope_obj;
    public static final String sql_con_file_name = "sql_con.txt";
    public static final String acc_user_file_name = "acc_user_name.txt";

    private void set_color_with_focus_exc(Boolean user_name, Boolean password, Boolean login, Boolean create_acc) {

        if (user_name) {
            user_name_tf.requestFocus();
            user_name_tf.setBackground(sky_c);
        } else {
            user_name_tf.setBackground(white_c);
        }
        if (password) {
            password_tf.requestFocus();
            password_tf.setBackground(sky_c);
        } else {
            password_tf.setBackground(white_c);
        }
        if (login) {
            login_bn.requestFocus();
            login_bn.setBackground(sky_c);
        } else {
            login_bn.setBackground(silivor_c);
        }
        if (create_acc) {
            create_acc_bn.requestFocus();
            create_acc_bn.setBackground(sky_c);
        } else {
            create_acc_bn.setBackground(silivor_c);
        }
    }

    private void set_default_for_sql_file() {
        try {
            File file = new File(sql_con_file_name);
            if (!file.exists()) {
                file.createNewFile();
                FileWriter fw = new FileWriter(file, false);
                PrintWriter pw = new PrintWriter(fw);
                pw.println("kay");
                pw.println("exchange_transfer_sm");
                pw.println("admin");
                pw.println("1234");
                pw.println("1.1.1.1");
                pw.println("admin");
                pw.println("1234");
                pw.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "error function login class: set_default_for_sql_file\n" + e, "Alert", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void get_from_sql_file_to_tf() {
        try {
            Scanner sc_file = new Scanner(new File(sql_con_file_name));
            setLocal_host(sc_file.nextLine(), sc_file.nextLine());
            setLocal_host_user_name(sc_file.nextLine());
            setLocal_host_password(sc_file.nextLine());
            setWifi_host(sc_file.nextLine());
            setWifi_host_user_name(sc_file.nextLine());
            setWifi_host_user_name(sc_file.nextLine());
        } catch (FileNotFoundException e) {
            all_type_error_mes error_mes = new all_type_error_mes("error function login class: get_from_sql_file_to_tf\n" + e);
        }
    }

    private void set_acc_file(String acc_user_name) {
        try {
            File file = new File(acc_user_file_name);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, false);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(acc_user_name);
            pw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "error function login class: set_acc_file\n" + e, "Alert", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void set_user_name_tf(String user_name) {
        user_name_tf.setText(user_name);
    }

    private void init_components() {
        setTitle("log in form");
        setResizable(false);
        set_default_for_sql_file();
        get_from_sql_file_to_tf();
        set_main_proj_path_to_db();
        try {
            File file = new File(acc_user_file_name);
            if (file.exists()) {
                Scanner sc_file = new Scanner(file);
                user_name_tf.setText(sc_file.nextLine());
                set_color_with_focus_exc(false, true, false, false);
            } else {
                set_color_with_focus_exc(true, false, false, false);
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "error function login class: init_components\n" + e, "Alert", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Creates new form login
     */
    public login(UI_and_operation UI_and_ope_obj) {
        this.UI_and_ope_obj = UI_and_ope_obj;
        initComponents();
        init_components();
        addWindowListener(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public login() {
        initComponents();
        init_components();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        password_tf = new javax.swing.JPasswordField();
        login_bn = new javax.swing.JButton();
        create_acc_bn = new javax.swing.JButton();
        sql_lb = new javax.swing.JLabel();
        user_name_tf = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Log in");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("user name :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("password  :");

        password_tf.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        password_tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                password_tfFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                password_tfFocusLost(evt);
            }
        });
        password_tf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                password_tfMouseClicked(evt);
            }
        });
        password_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                password_tfKeyPressed(evt);
            }
        });

        login_bn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        login_bn.setText("Log in ");
        login_bn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                login_bnFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                login_bnFocusLost(evt);
            }
        });
        login_bn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                login_bnMouseClicked(evt);
            }
        });
        login_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                login_bnActionPerformed(evt);
            }
        });
        login_bn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                login_bnKeyPressed(evt);
            }
        });

        create_acc_bn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        create_acc_bn.setText("create new account");
        create_acc_bn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                create_acc_bnFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                create_acc_bnFocusLost(evt);
            }
        });
        create_acc_bn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                create_acc_bnMouseClicked(evt);
            }
        });
        create_acc_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_acc_bnActionPerformed(evt);
            }
        });
        create_acc_bn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                create_acc_bnKeyPressed(evt);
            }
        });

        sql_lb.setBackground(new java.awt.Color(255, 255, 255));
        sql_lb.setForeground(new java.awt.Color(0, 0, 255));
        sql_lb.setText("sql");
        sql_lb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sql_lbMouseClicked(evt);
            }
        });

        user_name_tf.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        user_name_tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                user_name_tfFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                user_name_tfFocusLost(evt);
            }
        });
        user_name_tf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                user_name_tfMouseClicked(evt);
            }
        });
        user_name_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                user_name_tfKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(password_tf, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                            .addComponent(user_name_tf)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(sql_lb))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(login_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(create_acc_bn)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(user_name_tf, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(password_tf, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(create_acc_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(login_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sql_lb)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void login_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_login_bnActionPerformed
        if (!user_name_tf.getText().isEmpty() && !password_tf.getText().isEmpty()) {
            Connection con;
            PreparedStatement pst;
            ResultSet rs;
            try {
                con = DriverManager.getConnection(
                        getLocal_host(),
                        getLocal_host_user_name(),
                        getLocal_host_password()
                );
                pst = con.prepareStatement("SELECT user_name, password "
                        + "FROM account_tb "
                        + "WHERE user_name = ? "
                        + "AND password = ?;");
                pst.setString(1, user_name_tf.getText().trim());
                pst.setString(2, password_tf.getText().trim());
                rs = pst.executeQuery();
                if (rs.next()) {
                    String user_name = rs.getString("user_name");
                    String password = rs.getString("password");
                    set_acc_file(user_name);
                    setAccount(user_name, password);
                    if (UI_and_ope_obj == null) {
                        UI_and_operation UI_oper_obj = new UI_and_operation();
                        UI_oper_obj.setVisible(true);
                    } else {
                        UI_and_ope_obj.set_history();
                        UI_and_ope_obj.setEnabled(true);
                        UI_and_ope_obj.set_history();
                        UI_and_ope_obj.set_lb_user_name(user_name);
                    }
                    this.setVisible(false);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect user name or password", "Alert", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                sql_con sql_con_obj = new sql_con(ex);
                sql_con_obj.setVisible(true);
            }
        }
    }//GEN-LAST:event_login_bnActionPerformed

    private void sql_lbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sql_lbMouseClicked
        sql_con sql_con_obj = new sql_con(this);
        sql_con_obj.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_sql_lbMouseClicked

    private void create_acc_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_acc_bnActionPerformed
        create_or_edit_acc create_acc_obj = new create_or_edit_acc(this, "Create account", "Create new", dialog_type_for_db_e_a.Add);
        create_acc_obj.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_create_acc_bnActionPerformed

    private void user_name_tfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_user_name_tfMouseClicked
        set_color_with_focus_exc(true, false, false, false);
    }//GEN-LAST:event_user_name_tfMouseClicked

    private void user_name_tfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_user_name_tfKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_DOWN:
                set_color_with_focus_exc(false, true, false, false);
                break;
            case KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_LEFT:
                break;
        }
    }//GEN-LAST:event_user_name_tfKeyPressed

    private void password_tfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_password_tfKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                set_color_with_focus_exc(true, false, false, false);
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_DOWN:
                set_color_with_focus_exc(false, false, true, false);
                break;
            case KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_LEFT:
                break;
        }
    }//GEN-LAST:event_password_tfKeyPressed

    private void login_bnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_login_bnMouseClicked
        set_color_with_focus_exc(false, false, true, false);
    }//GEN-LAST:event_login_bnMouseClicked

    private void password_tfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_password_tfMouseClicked
        set_color_with_focus_exc(false, true, false, false);
    }//GEN-LAST:event_password_tfMouseClicked

    private void create_acc_bnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_create_acc_bnKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                set_color_with_focus_exc(false, true, false, false);
                break;
            case KeyEvent.VK_ENTER:
                create_acc_bn.doClick();
                break;
            case KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_LEFT:
                set_color_with_focus_exc(false, false, true, false);
                break;
        }
    }//GEN-LAST:event_create_acc_bnKeyPressed

    private void login_bnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_login_bnKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                set_color_with_focus_exc(false, true, false, false);
                break;
            case KeyEvent.VK_ENTER:
                login_bn.doClick();
                break;
            case KeyEvent.VK_RIGHT:
                set_color_with_focus_exc(false, false, false, true);
                break;
            case KeyEvent.VK_LEFT:
                break;
        }
    }//GEN-LAST:event_login_bnKeyPressed

    private void create_acc_bnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_acc_bnMouseClicked
        set_color_with_focus_exc(false, false, false, true);
    }//GEN-LAST:event_create_acc_bnMouseClicked

    private void user_name_tfFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_user_name_tfFocusGained
        set_color_with_focus_exc(true, false, false, false);
    }//GEN-LAST:event_user_name_tfFocusGained

    private void password_tfFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_password_tfFocusGained
        set_color_with_focus_exc(false, true, false, false);
    }//GEN-LAST:event_password_tfFocusGained

    private void login_bnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_login_bnFocusGained
        set_color_with_focus_exc(false, false, true, false);
    }//GEN-LAST:event_login_bnFocusGained

    private void create_acc_bnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_create_acc_bnFocusGained
        set_color_with_focus_exc(false, false, false, true);
    }//GEN-LAST:event_create_acc_bnFocusGained

    private void user_name_tfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_user_name_tfFocusLost
        user_name_tf.setBackground(white_c);
    }//GEN-LAST:event_user_name_tfFocusLost

    private void password_tfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_password_tfFocusLost
        password_tf.setBackground(white_c);
    }//GEN-LAST:event_password_tfFocusLost

    private void login_bnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_login_bnFocusLost
        login_bn.setBackground(silivor_c);
    }//GEN-LAST:event_login_bnFocusLost

    private void create_acc_bnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_create_acc_bnFocusLost
        create_acc_bn.setBackground(silivor_c);
    }//GEN-LAST:event_create_acc_bnFocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton create_acc_bn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton login_bn;
    private javax.swing.JPasswordField password_tf;
    private javax.swing.JLabel sql_lb;
    private javax.swing.JTextField user_name_tf;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        UI_and_ope_obj.setEnabled(true);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
