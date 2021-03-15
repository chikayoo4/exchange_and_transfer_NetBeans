/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import UI_and_operation.UI_and_operation.dialog_type_for_db_e_a;
import static UI_and_operation.UI_and_operation.is_has_history_list_db;
import static UI_and_operation.UI_and_operation.set_admin_password;
import static UI_and_operation.UI_and_operation.set_history_list_db;
import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import static UI_and_operation.view_history_list.col_sql;
import static UI_and_operation.view_history_list.tb_sql;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static UI_and_operation.validate_value.validate_keyTyped_ph_num;
import static UI_and_operation.UI_and_operation.set_cb;

/**
 *
 * @author Chhann_chikay
 */
public class input_dialog extends javax.swing.JFrame
        implements WindowListener {

    private dialog_type_for_db_e_a dialog_type;
    private view_history_list view_his_obj;
    private String default_edit_value;
    private Boolean is_allow_validate_input;
    private Boolean is_validate_ph;
    private Boolean is_input_pass;
    private UI_and_operation ui_and_ope_obj;

    private Boolean input_pass_dia_for_to_pro() {
        Boolean is_correct_pass = true;
        if (!set_admin_password.equals(JOptionPane.showInputDialog(this, "Enter password"))) {
            is_correct_pass = false;
            JOptionPane.showMessageDialog(this, "Incorrect password", "Alert", JOptionPane.WARNING_MESSAGE);
        }
        return is_correct_pass;
    }

    public void set_default_edit_value(String default_edit_value) {
        this.default_edit_value = default_edit_value;
        input_tf.setText(default_edit_value);
    }

    /**
     * Creates new form input_dialog
     */
    public input_dialog(view_history_list view_his_obj, String dia_app_bar, String dialog_title,
            String dialog_bn_name, dialog_type_for_db_e_a dialog_type, Boolean is_allow_validate,
            Boolean is_validate_ph, Boolean is_input_pass, UI_and_operation ui_and_ope_obj) {
        this.ui_and_ope_obj = ui_and_ope_obj;
        this.view_his_obj = view_his_obj;
        this.dialog_type = dialog_type;
        this.is_allow_validate_input = is_allow_validate;
        this.is_validate_ph = is_validate_ph;
        this.is_input_pass = is_input_pass;
        initComponents();
        setResizable(false);
        addWindowListener(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        title_lb.setText(dialog_title);
        agree_bn.setText(dialog_bn_name);
        setTitle(dia_app_bar);
    }

    public input_dialog() {
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

        input_tf = new javax.swing.JTextField();
        agree_bn = new javax.swing.JButton();
        title_lb = new javax.swing.JLabel();
        cancel_bn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        input_tf.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        input_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_tfActionPerformed(evt);
            }
        });
        input_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                input_tfKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                input_tfKeyTyped(evt);
            }
        });

        agree_bn.setFont(new java.awt.Font("Khmer OS Battambang", 0, 18)); // NOI18N
        agree_bn.setText("x");
        agree_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agree_bnActionPerformed(evt);
            }
        });

        title_lb.setFont(new java.awt.Font("Khmer OS Battambang", 0, 18)); // NOI18N
        title_lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title_lb.setText("x");

        cancel_bn.setFont(new java.awt.Font("Khmer OS Battambang", 0, 18)); // NOI18N
        cancel_bn.setText("cancel");
        cancel_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel_bnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(input_tf)
                            .addComponent(title_lb, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(agree_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cancel_bn)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(title_lb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(input_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(agree_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancel_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void agree_bn_fn() {
        Connection con;
        PreparedStatement pst;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            switch (dialog_type) {
                case Add:
                    if (is_has_history_list_db(input_tf.getText().trim(), col_sql, tb_sql)) {
                        JOptionPane.showMessageDialog(this, "it is already exsit", "Alert", JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (is_input_pass) {
                            if (input_pass_dia_for_to_pro()) {
                                set_history_list_db(input_tf.getText().trim(), col_sql, tb_sql);
                            }
                        } else {
                            set_history_list_db(input_tf.getText().trim(), col_sql, tb_sql);
                        }
                    }
                    break;
                case Edit:
                    if (is_input_pass) {
                        if (input_pass_dia_for_to_pro()) {
                            pst = con.prepareStatement("UPDATE " + tb_sql + " "
                                    + "SET " + col_sql + " = '" + input_tf.getText().trim() + "' "
                                    + "where " + col_sql + " = '" + default_edit_value + "'");
                            pst.executeUpdate();
                        }
                    } else {
                        pst = con.prepareStatement("UPDATE " + tb_sql + " "
                                + "SET " + col_sql + " = '" + input_tf.getText().trim() + "' "
                                + "where " + col_sql + " = '" + default_edit_value + "'");
                        pst.executeUpdate();
                    }
                    break;
                default:
                    System.out.println("Eorror");
            }
            if (ui_and_ope_obj.get_idx_transfer_pt() == 0) {
                set_cb(ui_and_ope_obj.get_to_pro_cb_from_ui_oper(), "transfer_province", "province_name_history_tb");
                ui_and_ope_obj.set_is_change_pro_true();
            } else if (ui_and_ope_obj.get_idx_transfer_pt() == 1) {
                set_cb(ui_and_ope_obj.get_from_pro_cb_from_ui_oper(), "transfer_province", "province_name_history_tb");
                ui_and_ope_obj.set_is_change_pro_true();
            } else {
                set_cb(ui_and_ope_obj.get_from_bank_thai_cb_from_ui_oper(), "bank", "to_thai_bank_name_history_tb");
            }
            view_his_obj.set_history();
            view_his_obj.setEnabled(true);
            this.setVisible(false);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(UI_and_operation.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void agree_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agree_bnActionPerformed
        if (!input_tf.getText().isEmpty()) {
            if (input_tf.getText().length() >= 9 || !is_validate_ph) {
                agree_bn_fn();

            }
        }
    }//GEN-LAST:event_agree_bnActionPerformed

    private void input_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_input_tfKeyTyped
        if (is_allow_validate_input) {
            validate_keyTyped_ph_num(evt, input_tf);
        }
    }//GEN-LAST:event_input_tfKeyTyped

    private void input_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_input_tfKeyReleased

    }//GEN-LAST:event_input_tfKeyReleased

    private void cancel_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_bnActionPerformed
        view_his_obj.setEnabled(true);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_cancel_bnActionPerformed

    private void input_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_tfActionPerformed

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
            java.util.logging.Logger.getLogger(input_dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(input_dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(input_dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(input_dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new input_dialog().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agree_bn;
    private javax.swing.JButton cancel_bn;
    private javax.swing.JTextField input_tf;
    private javax.swing.JLabel title_lb;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        view_his_obj.setEnabled(true);

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
