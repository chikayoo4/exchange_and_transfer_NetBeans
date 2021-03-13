/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import UI_and_operation.UI_and_operation.dialog_type_for_db_e_a;
import static UI_and_operation.UI_and_operation.set_thai_history_db;
import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import static UI_and_operation.validate_value.validate_keyTyped_ph_num;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;

/**
 *
 * @author Chhann_chikay
 */
public class to_thai_info extends javax.swing.JFrame
        implements WindowListener {

    dialog_type_for_db_e_a dialog_type;
    view_history_thai view_his_thai_obj;

    public void set_tf_from_sql(int id) {

        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT bank, bank_id, name, phone_no "
                    + "FROM to_thai_history_tb "
                    + "where id_to_thai = "+ id +";");
            
//            pst.setInt(1, id);
            rs = pst.executeQuery();
            
                while (rs.next()) {
                    //set to v2 all data only 1 row
                    tf_bank.setText(rs.getString("bank"));
                    tf_cus_no.setText(rs.getString("bank_id"));
                    tf_cus_name.setText(rs.getString("name"));
                    tf_cus_ph_no.setText(rs.getString("phone_no"));

                }

        } catch (SQLException ex) {
            System.err.println("error: two_three_bn_finish\n" + ex);
        }
    }

    
    public void update_tf_from_sql(int id) {

        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT bank, bank_id, name, phone_no "
                    + "FROM to_thai_history_tb "
                    + "where id_to_thai = "+ id +";");
            
//            pst.setInt(1, id);
            rs = pst.executeQuery();
            
                while (rs.next()) {
                    //set to v2 all data only 1 row
                    tf_bank.setText(rs.getString("bank"));
                    tf_cus_no.setText(rs.getString("bank_id"));
                    tf_cus_name.setText(rs.getString("name"));
                    tf_cus_ph_no.setText(rs.getString("phone_no"));

                }

        } catch (SQLException ex) {
            System.err.println("error: two_three_bn_finishsssssssssssssssssssssssssssss\n" + ex);
        }
    }
    /**
     * Creates new form to_thai_info
     */
    public to_thai_info(String bn_name, dialog_type_for_db_e_a dialog_type, view_history_thai view_his_thai_obj) {
        this.dialog_type = dialog_type;
        this.view_his_thai_obj = view_his_thai_obj;
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        agree_bn.setText(bn_name);
        setTitle("custumize thai bank information");
        addWindowListener(this);
    }

    public to_thai_info() {
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
        jLabel9 = new javax.swing.JLabel();
        tf_bank = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tf_cus_no = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tf_cus_name = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        tf_cus_ph_no = new javax.swing.JTextField();
        agree_bn = new javax.swing.JButton();
        cancel_bn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("to thai information");

        jLabel9.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 25)); // NOI18N
        jLabel9.setText("ធនាគារ");
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        tf_bank.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        tf_bank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_bankActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 25)); // NOI18N
        jLabel8.setText("លេខបញ្ជី");
        jLabel8.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        tf_cus_no.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        tf_cus_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_cus_noActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 25)); // NOI18N
        jLabel7.setText("ឈ្មោះ");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        tf_cus_name.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N

        jLabel33.setFont(new java.awt.Font("Khmer OS Siemreap", 0, 25)); // NOI18N
        jLabel33.setText("លេខអ្នកផ្ញើរ");
        jLabel33.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        tf_cus_ph_no.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        tf_cus_ph_no.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_cus_ph_noKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tf_cus_ph_noKeyTyped(evt);
            }
        });

        agree_bn.setFont(new java.awt.Font("Khmer OS Battambang", 0, 18)); // NOI18N
        agree_bn.setText("x");
        agree_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agree_bnActionPerformed(evt);
            }
        });

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
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tf_cus_ph_no)
                    .addComponent(tf_cus_name)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tf_bank)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
                    .addComponent(tf_cus_no, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(36, 36, 36))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(agree_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cancel_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_bank, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_cus_no, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_cus_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_cus_ph_no, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(agree_bn, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(cancel_bn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tf_bankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_bankActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_bankActionPerformed

    private void tf_cus_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_cus_noActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_cus_noActionPerformed

    private void agree_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agree_bnActionPerformed
        if (!tf_bank.getText().isEmpty() || !tf_cus_no.getText().isEmpty() || !tf_cus_name.getText().isEmpty() || !tf_cus_ph_no.getText().isEmpty()) {
            switch (dialog_type) {
                case Add:
                    set_thai_history_db("bank", tf_bank.getText().trim(),
                            "bank_id", tf_cus_no.getText().trim(),
                            "name", tf_cus_name.getText().trim(),
                            "phone_no", tf_cus_ph_no.getText().trim(),
                            "to_thai_history_tb");
                    break;
                case Edit:
                        
                    break;
                default:
                    System.out.println("Eorror");
            }
//                view_his_obj.set_history();
            view_his_thai_obj.set_history();
            view_his_thai_obj.setEnabled(true);
            this.setVisible(false);
            this.dispose();
        }
    }//GEN-LAST:event_agree_bnActionPerformed

    private void cancel_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_bnActionPerformed
//        view_his_obj.setEnabled(true);
//        this.setVisible(false);
//        this.dispose();
    }//GEN-LAST:event_cancel_bnActionPerformed

    private void tf_cus_ph_noKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_cus_ph_noKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_cus_ph_noKeyReleased

    private void tf_cus_ph_noKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_cus_ph_noKeyTyped
        validate_keyTyped_ph_num(evt, tf_cus_ph_no);
    }//GEN-LAST:event_tf_cus_ph_noKeyTyped

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
            java.util.logging.Logger.getLogger(to_thai_info.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(to_thai_info.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(to_thai_info.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(to_thai_info.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new to_thai_info().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agree_bn;
    private javax.swing.JButton cancel_bn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField tf_bank;
    private javax.swing.JTextField tf_cus_name;
    private javax.swing.JTextField tf_cus_no;
    private javax.swing.JTextField tf_cus_ph_no;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        view_his_thai_obj.setEnabled(true);
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