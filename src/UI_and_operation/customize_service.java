/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import UI_and_operation.UI_and_operation.dialog_type_for_db_e_a;
import static UI_and_operation.UI_and_operation.get_id_money_type_from_db;
import static UI_and_operation.UI_and_operation.get_id_province_name_from_db;
import static UI_and_operation.UI_and_operation.get_id_province_name_history_from_db;
import static UI_and_operation.UI_and_operation.set_admin_password;
import UI_and_operation.UI_and_operation.type_of_money;
import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import static UI_and_operation.validate_value.clear_cvot;
import static UI_and_operation.validate_value.validate_KeyReleased_money;
import static UI_and_operation.validate_value.validate_KeyTyped_money;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static UI_and_operation.UI_and_operation.set_cb;

/**
 *
 * @author Chhann_chikay
 */
public class customize_service extends javax.swing.JFrame
        implements WindowListener {

    type_of_money selected_money_type_pro_ser;
    view_service set_service_obj;
    dialog_type_for_db_e_a type_input;

    public void set_default_data(String set_service_start_money, String set_service_end_money,
            String set_service_price, String money_type, String set_service_pro_name) {
        set_service_start_money_tf.setText(set_service_start_money);
        set_service_end_money_tf.setText(set_service_end_money);
        set_service_price_tf.setText(set_service_price);
        switch (money_type) {
            case "៛":
                pro_service_rial_rb.setSelected(true);
                selected_money_type_pro_ser = type_of_money.Rial;
                break;
            case "$":
                pro_service_dollar_rb.setSelected(true);
                selected_money_type_pro_ser = type_of_money.Dollar;
                break;
            case "฿":
                pro_service_bart_rb.setSelected(true);
                selected_money_type_pro_ser = type_of_money.Bart;
                break;
            default:
                System.out.println("error");
        }
        set_service_pro_name_cb.getModel().setSelectedItem(set_service_pro_name);
    }

    /**
     * Creates new form customize_service
     */
    public customize_service() {
        initComponents();
    }

    private void short_constructor(view_service set_service_obj, String title, String app_bar, String title_bn, dialog_type_for_db_e_a type_input) {

        this.set_service_obj = set_service_obj;
        this.type_input = type_input;
        initComponents();
        setResizable(false);
        setTitle(app_bar);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(this);
        set_service_title_lb.setText(title);
        set_service_agree_bn.setText(title_bn);
        set_service_id_lb.setText("new id");
        set_cb(set_service_pro_name_cb, "transfer_province", "province_name_history_tb");
    }

//for add
    public customize_service(view_service set_service_obj, String title, String app_bar, String title_bn, dialog_type_for_db_e_a type_input) {
        short_constructor(set_service_obj, title, app_bar, title_bn, type_input);
    }

    //for edit
    public customize_service(view_service set_service_obj, String title, String app_bar, String title_bn, dialog_type_for_db_e_a type_input, String id) {
        short_constructor(set_service_obj, title, app_bar, title_bn, type_input);
        set_service_id_lb.setText(id);
    }

    public static Boolean is_has_service_db(String start_m, String end_m, String price, int id_money_type, int id_pro_name) {

        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT id_pro_service "
                    + "FROM province_service "
                    + "WHERE start_money = ? "
                    + "AND end_money = ? "
                    + "AND price = ? "
                    + "AND id_type_of_money = ? "
                    + "AND id_pro_name = ?;");
            pst.setString(1, start_m);
            pst.setString(2, end_m);
            pst.setString(3, price);
            pst.setInt(4, id_money_type);
            pst.setInt(5, id_pro_name);
            rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            System.err.println("error: two_three_bn_finish\n" + ex);
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel2 = new javax.swing.JLabel();
        pro_service_rial_rb = new javax.swing.JRadioButton();
        pro_service_dollar_rb = new javax.swing.JRadioButton();
        pro_service_bart_rb = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        set_service_start_money_tf = new javax.swing.JTextField();
        set_service_end_money_tf = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        set_service_price_tf = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        set_service_pro_name_cb = new javax.swing.JComboBox<>();
        set_service_agree_bn = new javax.swing.JButton();
        set_service_title_lb = new javax.swing.JLabel();
        set_service_cancel_bn = new javax.swing.JButton();
        set_service_id_lb = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("format : start < x <= end");

        buttonGroup1.add(pro_service_rial_rb);
        pro_service_rial_rb.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        pro_service_rial_rb.setText("៛");
        pro_service_rial_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pro_service_rial_rbActionPerformed(evt);
            }
        });

        buttonGroup1.add(pro_service_dollar_rb);
        pro_service_dollar_rb.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        pro_service_dollar_rb.setText("$");
        pro_service_dollar_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pro_service_dollar_rbActionPerformed(evt);
            }
        });

        buttonGroup1.add(pro_service_bart_rb);
        pro_service_bart_rb.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        pro_service_bart_rb.setText("฿");
        pro_service_bart_rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pro_service_bart_rbActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel3.setText(" start");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        set_service_start_money_tf.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        set_service_start_money_tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                set_service_start_money_tfActionPerformed(evt);
            }
        });
        set_service_start_money_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                set_service_start_money_tfKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                set_service_start_money_tfKeyTyped(evt);
            }
        });

        set_service_end_money_tf.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        set_service_end_money_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                set_service_end_money_tfKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                set_service_end_money_tfKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel4.setText(" end");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel6.setText("price");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        set_service_price_tf.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        set_service_price_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                set_service_price_tfKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                set_service_price_tfKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel5.setText(" name");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        set_service_pro_name_cb.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        set_service_pro_name_cb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none" }));
        set_service_pro_name_cb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                set_service_pro_name_cbActionPerformed(evt);
            }
        });

        set_service_agree_bn.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        set_service_agree_bn.setText("x");
        set_service_agree_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                set_service_agree_bnActionPerformed(evt);
            }
        });

        set_service_title_lb.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        set_service_title_lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        set_service_title_lb.setText("x");

        set_service_cancel_bn.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        set_service_cancel_bn.setText("cancel");
        set_service_cancel_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                set_service_cancel_bnActionPerformed(evt);
            }
        });

        set_service_id_lb.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("id : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(set_service_title_lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(set_service_start_money_tf)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(set_service_end_money_tf)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(set_service_price_tf)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(set_service_pro_name_cb, 0, 364, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(set_service_id_lb, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pro_service_rial_rb)
                        .addGap(18, 18, 18)
                        .addComponent(pro_service_dollar_rb)
                        .addGap(18, 18, 18)
                        .addComponent(pro_service_bart_rb)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(161, 161, 161)
                .addComponent(set_service_agree_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addComponent(set_service_cancel_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(171, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(set_service_title_lb, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pro_service_rial_rb)
                        .addComponent(pro_service_dollar_rb)
                        .addComponent(pro_service_bart_rb))
                    .addComponent(set_service_id_lb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(set_service_start_money_tf)
                    .addComponent(set_service_pro_name_cb)
                    .addComponent(set_service_end_money_tf)
                    .addComponent(set_service_price_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(set_service_agree_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(set_service_cancel_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pro_service_rial_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pro_service_rial_rbActionPerformed
        selected_money_type_pro_ser = type_of_money.Rial;
    }//GEN-LAST:event_pro_service_rial_rbActionPerformed

    private void pro_service_dollar_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pro_service_dollar_rbActionPerformed
        selected_money_type_pro_ser = type_of_money.Dollar;
    }//GEN-LAST:event_pro_service_dollar_rbActionPerformed

    private void pro_service_bart_rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pro_service_bart_rbActionPerformed
        selected_money_type_pro_ser = type_of_money.Bart;
    }//GEN-LAST:event_pro_service_bart_rbActionPerformed

    private void set_service_start_money_tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_set_service_start_money_tfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_set_service_start_money_tfActionPerformed

    private void set_service_start_money_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_set_service_start_money_tfKeyReleased
        validate_KeyReleased_money(evt, set_service_start_money_tf);
    }//GEN-LAST:event_set_service_start_money_tfKeyReleased

    private void set_service_start_money_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_set_service_start_money_tfKeyTyped
        validate_KeyTyped_money(evt, set_service_start_money_tf);
    }//GEN-LAST:event_set_service_start_money_tfKeyTyped

    private void set_service_end_money_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_set_service_end_money_tfKeyReleased
        validate_KeyReleased_money(evt, set_service_end_money_tf);
    }//GEN-LAST:event_set_service_end_money_tfKeyReleased

    private void set_service_end_money_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_set_service_end_money_tfKeyTyped
        validate_KeyTyped_money(evt, set_service_end_money_tf);
    }//GEN-LAST:event_set_service_end_money_tfKeyTyped

    private void set_service_price_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_set_service_price_tfKeyReleased
        validate_KeyReleased_money(evt, set_service_price_tf);
    }//GEN-LAST:event_set_service_price_tfKeyReleased

    private void set_service_price_tfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_set_service_price_tfKeyTyped
        validate_KeyTyped_money(evt, set_service_price_tf);
    }//GEN-LAST:event_set_service_price_tfKeyTyped

    private void set_service_agree_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_set_service_agree_bnActionPerformed
        //        System.out.println("set_service_pro_name_cb.getSelectedItem().equals(\"none\") : " + set_service_pro_name_cb.getSelectedItem().equals("none"));
        if ((!set_service_start_money_tf.getText().isEmpty() && !set_service_end_money_tf.getText().isEmpty() && !set_service_price_tf.getText().isEmpty()
                && !set_service_pro_name_cb.getSelectedItem().equals("none")) && (pro_service_rial_rb.isSelected()
                || pro_service_dollar_rb.isSelected() || pro_service_bart_rb.isSelected())) {

            Connection con;
            PreparedStatement pst;
            ResultSet rs;
            try {
                con = DriverManager.getConnection(
                        getLocal_host(),
                        getLocal_host_user_name(),
                        getLocal_host_password()
                );

                if (!is_has_service_db(clear_cvot(set_service_start_money_tf.getText()), 
                        clear_cvot(set_service_end_money_tf.getText()), 
                        clear_cvot(set_service_price_tf.getText()),
                        get_id_money_type_from_db(selected_money_type_pro_ser),
                        get_id_province_name_history_from_db(set_service_pro_name_cb.getSelectedItem().toString()))) {
                    if (set_admin_password.equals(JOptionPane.showInputDialog(this, "Enter password"))) {
                        switch (type_input) {
                            case Add:
                                pst = con.prepareStatement("INSERT INTO province_service (start_money, end_money, price, id_type_of_money, id_pro_name) "
                                        + "VALUES (?, ?, ?, ?, ?);");
                                pst.setString(1, clear_cvot(set_service_start_money_tf.getText()));
                                pst.setString(2, clear_cvot(set_service_end_money_tf.getText()));
                                pst.setString(3, clear_cvot(set_service_price_tf.getText()));
                                pst.setInt(4, get_id_money_type_from_db(selected_money_type_pro_ser));
                                pst.setInt(5, get_id_province_name_history_from_db(set_service_pro_name_cb.getSelectedItem().toString()));
                                pst.executeUpdate();
                                break;
                            case Edit:
                                pst = con.prepareStatement("UPDATE province_service "
                                        + "SET start_money = ?, "
                                        + "end_money = ?, "
                                        + "price = ?, "
                                        + "id_type_of_money = ?, "
                                        + "id_pro_name = ? "
                                        + "WHERE id_pro_service = ?;");
                                pst.setString(1, clear_cvot(set_service_start_money_tf.getText()));
                                pst.setString(2, clear_cvot(set_service_end_money_tf.getText()));
                                pst.setString(3, clear_cvot(set_service_price_tf.getText()));
                                pst.setInt(4, get_id_money_type_from_db(selected_money_type_pro_ser));
                                pst.setInt(5, get_id_province_name_history_from_db(set_service_pro_name_cb.getSelectedItem().toString()));
                                pst.setInt(6, Integer.parseInt(set_service_id_lb.getText()));
                                pst.executeUpdate();
                                break;
                            default:
                                System.out.println("errorrrrrrr");
                        }
                        set_service_obj.set_ser_to_tb();
                        set_service_obj.setEnabled(true);
                        this.setVisible(false);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect password", "Alert", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "it is already exsit", "Alert", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                System.err.println(ex);
            }
        }
    }//GEN-LAST:event_set_service_agree_bnActionPerformed

    private void set_service_pro_name_cbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_set_service_pro_name_cbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_set_service_pro_name_cbActionPerformed

    private void set_service_cancel_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_set_service_cancel_bnActionPerformed
        set_service_obj.setEnabled(true);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_set_service_cancel_bnActionPerformed

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
            java.util.logging.Logger.getLogger(customize_service.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(customize_service.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(customize_service.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(customize_service.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new customize_service().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JRadioButton pro_service_bart_rb;
    private javax.swing.JRadioButton pro_service_dollar_rb;
    private javax.swing.JRadioButton pro_service_rial_rb;
    private javax.swing.JButton set_service_agree_bn;
    private javax.swing.JButton set_service_cancel_bn;
    private javax.swing.JTextField set_service_end_money_tf;
    private javax.swing.JLabel set_service_id_lb;
    private javax.swing.JTextField set_service_price_tf;
    private javax.swing.JComboBox<String> set_service_pro_name_cb;
    private javax.swing.JTextField set_service_start_money_tf;
    private javax.swing.JLabel set_service_title_lb;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        set_service_obj.setEnabled(true);
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
