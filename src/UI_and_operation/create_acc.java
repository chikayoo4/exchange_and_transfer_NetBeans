/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import static UI_and_operation.UI_and_operation.silivor_c;
import static UI_and_operation.UI_and_operation.sky_c;
import static UI_and_operation.UI_and_operation.white_c;
import static UI_and_operation.account.is_has_user_name;
import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Chhann_chikay
 */
public class create_acc extends javax.swing.JFrame
        implements WindowListener {

    login login_obj;

    private void set_color_with_focus_exc(Boolean user_name, Boolean password, Boolean re_password, Boolean login) {
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
        if (re_password) {
            re_password_tf.requestFocus();
            re_password_tf.setBackground(sky_c);
        } else {
            re_password_tf.setBackground(white_c);
        }
        if (login) {
            login_bn.requestFocus();
            login_bn.setBackground(sky_c);
        } else {
            login_bn.setBackground(silivor_c);
        }
    }

    /**
     * Creates new form create_acc
     */
    public create_acc(login login_obj) {
        this.login_obj = login_obj;
        setResizable(false);
        initComponents();
        setTitle("SQL");
        addWindowListener(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        set_color_with_focus_exc(true, false, false, false);
    }

    public create_acc() {
        initComponents();
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
        user_name_tf = new javax.swing.JTextField();
        password_tf = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        login_bn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        re_password_tf = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Create account");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("user name     :");

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

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("password      :");

        login_bn.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        login_bn.setText("Create");
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

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("re-password  :");

        re_password_tf.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        re_password_tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                re_password_tfFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                re_password_tfFocusLost(evt);
            }
        });
        re_password_tf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                re_password_tfMouseClicked(evt);
            }
        });
        re_password_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                re_password_tfKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(login_bn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(5, 5, 5))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(re_password_tf)
                            .addComponent(user_name_tf)
                            .addComponent(password_tf, javax.swing.GroupLayout.Alignment.TRAILING))))
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
                    .addComponent(user_name_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(password_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(re_password_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 24, Short.MAX_VALUE)
                .addComponent(login_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void login_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_login_bnActionPerformed
        if (!user_name_tf.getText().isEmpty() && !password_tf.getText().isEmpty() && !re_password_tf.getText().isEmpty()) {
            String user_name = user_name_tf.getText().trim();
            String password = password_tf.getText();
            String re_password = re_password_tf.getText();
            try {
                Connection con;
                PreparedStatement pst;
                ResultSet rs;
                con = DriverManager.getConnection(
                        getLocal_host(),
                        getLocal_host_user_name(),
                        getLocal_host_password()
                );
                if (password.equals(re_password)) {
                    //query to access
                    if (!is_has_user_name(user_name)) {
                        pst = con.prepareStatement("insert into account_tb(user_name, password) values(?, ?);");
                        pst.setString(1, user_name);
                        pst.setString(2, password);
                        pst.executeUpdate();
                        login_obj.setEnabled(true);
                        login_obj.set_user_name_tf(user_name);
                        this.setVisible(false);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "user name already exsist", "Alert", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "password not match!!!!!", "Alert", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                sql_con sql_con_obj = new sql_con(ex);
                sql_con_obj.setVisible(true);
            }
        }
    }//GEN-LAST:event_login_bnActionPerformed

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

    private void user_name_tfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_user_name_tfMouseClicked
        set_color_with_focus_exc(true, false, false, false);
    }//GEN-LAST:event_user_name_tfMouseClicked

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

    private void password_tfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_password_tfMouseClicked
        set_color_with_focus_exc(false, true, false, false);
    }//GEN-LAST:event_password_tfMouseClicked

    private void re_password_tfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_re_password_tfKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                set_color_with_focus_exc(false, true, false, false);
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_DOWN:
                set_color_with_focus_exc(false, false, false, true);
                break;
            case KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_LEFT:
                break;
        }
    }//GEN-LAST:event_re_password_tfKeyPressed

    private void re_password_tfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_re_password_tfMouseClicked
        set_color_with_focus_exc(false, false, true, false);
    }//GEN-LAST:event_re_password_tfMouseClicked

    private void login_bnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_login_bnKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                set_color_with_focus_exc(false, false, true, false);
                break;
            case KeyEvent.VK_ENTER:
                set_color_with_focus_exc(false, false, false, true);
                login_bn.doClick();
                break;
            case KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_LEFT:
                break;
        }
    }//GEN-LAST:event_login_bnKeyPressed

    private void login_bnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_login_bnMouseClicked
set_color_with_focus_exc(false, false, false, true);
    }//GEN-LAST:event_login_bnMouseClicked

    private void user_name_tfFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_user_name_tfFocusGained
                set_color_with_focus_exc(true, false, false, false);
    }//GEN-LAST:event_user_name_tfFocusGained

    private void password_tfFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_password_tfFocusGained
                set_color_with_focus_exc(false, true, false, false);
    }//GEN-LAST:event_password_tfFocusGained

    private void re_password_tfFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_re_password_tfFocusGained
                set_color_with_focus_exc(false, false, true, false);
    }//GEN-LAST:event_re_password_tfFocusGained

    private void login_bnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_login_bnFocusGained
                set_color_with_focus_exc(false, false, false, true);
    }//GEN-LAST:event_login_bnFocusGained

    private void user_name_tfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_user_name_tfFocusLost
            user_name_tf.setBackground(white_c);
    }//GEN-LAST:event_user_name_tfFocusLost

    private void password_tfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_password_tfFocusLost
            password_tf.setBackground(white_c);
    }//GEN-LAST:event_password_tfFocusLost

    private void re_password_tfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_re_password_tfFocusLost
            re_password_tf.setBackground(white_c);
    }//GEN-LAST:event_re_password_tfFocusLost

    private void login_bnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_login_bnFocusLost
            login_bn.setBackground(silivor_c);
    }//GEN-LAST:event_login_bnFocusLost

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
            java.util.logging.Logger.getLogger(create_acc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(create_acc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(create_acc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(create_acc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new create_acc().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton login_bn;
    private javax.swing.JPasswordField password_tf;
    private javax.swing.JPasswordField re_password_tf;
    private javax.swing.JTextField user_name_tf;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        login_obj.setEnabled(true);
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
