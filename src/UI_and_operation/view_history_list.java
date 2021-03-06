/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import UI_and_operation.UI_and_operation.dialog_choose_e_d_c;
import UI_and_operation.UI_and_operation.dialog_type_for_db_e_a;
import static UI_and_operation.UI_and_operation.field_admin_pass;
import static UI_and_operation.UI_and_operation.set_cb;
import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Chhann_chikay
 */
public class view_history_list extends javax.swing.JFrame
        implements WindowListener {

    private UI_and_operation ui_and_ope_obj;
    public static String col_sql;
    public static String tb_sql;
    private String dia_edit_title;
    private String dia_add_title;
    private String dia_edit_app_bar;
    private String dia_add_app_bar;
    private Boolean is_allow_validate;
    private Boolean is_validate_ph;
    private Boolean is_input_pass;
    private final int num_show_his = 10;
    private int next_show_his = num_show_his;
    private int row_num = num_show_his;

    private void set_see_more_bn() {
        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT " + col_sql + " FROM " + tb_sql + ";");
            rs = pst.executeQuery();
            if (rs.next()) {
                set_sub_see_more_bn();
            } else {
                bn_1_col.setEnabled(false);
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    private void set_sub_see_more_bn() {

        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT " + col_sql + " "
                    + "FROM " + tb_sql + " "
                    + "ORDER BY " + col_sql + " "
                    + "OFFSET " + next_show_his + " ROWS "
                    + "FETCH NEXT " + num_show_his + " ROWS ONLY;");
            rs = pst.executeQuery();
            if (!rs.next()) {
                bn_1_col.setEnabled(false);
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    public void set_history() {
        bn_1_col.setEnabled(true);
        next_show_his = num_show_his;
        row_num = num_show_his;
        Connection con;
        PreparedStatement pst;
        ResultSet rs;

        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT " + col_sql + " FROM " + tb_sql + ";");
            rs = pst.executeQuery();
            if (rs.next()) {
                DefaultTableModel dft = (DefaultTableModel) history_tb.getModel();
                dft.setRowCount(0);
                pst = con.prepareStatement("SELECT TOP " + num_show_his + " " + col_sql + " FROM " + tb_sql + " ORDER BY " + col_sql + ";");
                rs = pst.executeQuery();
                while (rs.next()) {
                    Vector v3 = new Vector();
                    v3.add(rs.getString(col_sql));
                    dft.addRow(v3);
                }
                set_sub_see_more_bn();
            } else {
                bn_1_col.setEnabled(false);
                DefaultTableModel dft = (DefaultTableModel) history_tb.getModel();
                dft.setRowCount(0);
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }
    }

    /**
     * Creates new form to_pro_sender_ph_history
     */
    public view_history_list(UI_and_operation ui_and_ope_obj, String col_sql, String tb_sql,
            String history_app_bar, String his_title, String dia_edit_title, String dia_add_title,
            String dia_edit_app_bar, String dia_add_app_bar, Boolean is_allow_validate,
            Boolean is_validate_ph, Boolean is_input_pass) {
        this.col_sql = col_sql;
        this.tb_sql = tb_sql;
        this.dia_edit_title = dia_edit_title;
        this.dia_add_title = dia_add_title;
        this.dia_edit_app_bar = dia_edit_app_bar;
        this.dia_add_app_bar = dia_add_app_bar;
        this.ui_and_ope_obj = ui_and_ope_obj;
        this.is_allow_validate = is_allow_validate;
        this.is_validate_ph = is_validate_ph;
        this.is_input_pass = is_input_pass;
        initComponents();
//        setTitle("To Province Sender Phone number");
        setTitle(history_app_bar);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //design head table  total money in balance
        history_tb.getTableHeader().setFont(new Font("Khmer OS Siemreap", Font.BOLD, 30));
        history_tb.setRowHeight(40);
        histroy_lb.setText(his_title);
        addWindowListener(this);
        set_history();
        set_see_more_bn();
    }

    public view_history_list() {
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

        history_add_bn = new javax.swing.JButton();
        histroy_lb = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        history_tb = new javax.swing.JTable();
        bn_1_col = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        history_add_bn.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        history_add_bn.setText("ADD");
        history_add_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                history_add_bnActionPerformed(evt);
            }
        });

        histroy_lb.setFont(new java.awt.Font("Khmer OS Battambang", 1, 40)); // NOI18N
        histroy_lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        histroy_lb.setText("x");

        history_tb.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        history_tb.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "History"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        history_tb.setRequestFocusEnabled(false);
        history_tb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                history_tbMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(history_tb);

        bn_1_col.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        bn_1_col.setText("see more...");
        bn_1_col.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bn_1_colActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(histroy_lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(history_add_bn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 681, Short.MAX_VALUE)
                    .addComponent(bn_1_col, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(histroy_lb, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(history_add_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bn_1_col, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void history_tbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_history_tbMouseClicked
        boolean is_edit_tb = history_tb.isEditing();
        if (is_edit_tb == false) {
            //table in UI
            DefaultTableModel model = (DefaultTableModel) history_tb.getModel();
            //select index of the table row
            int selectedIndex = history_tb.getSelectedRow();
            String sender_ph_no = model.getValueAt(selectedIndex, 0).toString();

//            JOptionPane.showMessageDialog(this, "You can not edit this table");
            Object[] options = {dialog_choose_e_d_c.Edit, dialog_choose_e_d_c.Delete, dialog_choose_e_d_c.Close};
            dialog_choose_e_d_c choose_from_dialog = dialog_choose_e_d_c.Close;
            int idx = JOptionPane.showOptionDialog(this, "what you choose?", null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            if (idx != -1) {
                choose_from_dialog = dialog_choose_e_d_c.values()[idx];
            }

            switch (choose_from_dialog) {
                case Edit:
                    input_dialog in_di = new input_dialog(this, dia_edit_app_bar, dia_edit_title,
                            "Save", dialog_type_for_db_e_a.Edit, is_allow_validate, is_validate_ph, is_input_pass, ui_and_ope_obj);
                    in_di.set_default_edit_value(sender_ph_no);
                    in_di.setVisible(true);
                    this.setEnabled(false);
                    break;
                case Delete:

                    Boolean is_correct_pass = true;
                    if (is_input_pass) {
                        if (!field_admin_pass()) {
                            is_correct_pass = false;
                            JOptionPane.showMessageDialog(this, "Incorrect password", "Alert", JOptionPane.WARNING_MESSAGE);
                        }
                    }

                    if (is_correct_pass) {
                        Connection con;
                        PreparedStatement pst;
                        try {
                            con = DriverManager.getConnection(
                                    getLocal_host(),
                                    getLocal_host_user_name(),
                                    getLocal_host_password()
                            );
                            //update sql query to access
                            pst = con.prepareStatement("delete from " + tb_sql + " "
                                    + "where " + col_sql + " = '" + sender_ph_no + "'");
                            pst.executeUpdate();
                        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
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
                        set_history();
                    }
                    break;
            }
        }
    }//GEN-LAST:event_history_tbMouseClicked

    private void history_add_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_history_add_bnActionPerformed
        input_dialog in_di = new input_dialog(this, dia_add_app_bar, dia_add_title,
                "Add", dialog_type_for_db_e_a.Add, is_allow_validate, is_validate_ph, is_input_pass, ui_and_ope_obj);
        in_di.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_history_add_bnActionPerformed

    private void bn_1_colActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bn_1_colActionPerformed
        DefaultTableModel dft = (DefaultTableModel) history_tb.getModel();
        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(
                    getLocal_host(),
                    getLocal_host_user_name(),
                    getLocal_host_password()
            );
            pst = con.prepareStatement("SELECT " + col_sql + " FROM " + tb_sql + ";");
            rs = pst.executeQuery();
            if (rs.next()) {

                pst = con.prepareStatement("SELECT " + col_sql + " "
                        + "FROM " + tb_sql + " "
                        + "ORDER BY " + col_sql + " "
                        + "OFFSET " + next_show_his + " ROWS "
                        + "FETCH NEXT " + num_show_his + " ROWS ONLY;");
                rs = pst.executeQuery();
                if (rs.next()) {
                    pst = con.prepareStatement("SELECT " + col_sql + " "
                            + "FROM " + tb_sql + " "
                            + "ORDER BY " + col_sql + " "
                            + "OFFSET " + next_show_his + " ROWS "
                            + "FETCH NEXT " + num_show_his + " ROWS ONLY;");
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        dft.setRowCount(row_num);
                        row_num++;
                        Vector v3 = new Vector();
                        //set to v2 all data only 1 row
                        v3.add(rs.getString(col_sql));
                        //set data to table history row
                        dft.addRow(v3);
                    }
                    next_show_his = next_show_his + num_show_his;
                    set_sub_see_more_bn();
                } else {
                    bn_1_col.setEnabled(false);
                }
            }
        } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
        }

    }//GEN-LAST:event_bn_1_colActionPerformed

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
            java.util.logging.Logger.getLogger(view_history_list.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(view_history_list.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(view_history_list.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(view_history_list.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new view_history_list().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bn_1_col;
    private javax.swing.JButton history_add_bn;
    private javax.swing.JTable history_tb;
    private javax.swing.JLabel histroy_lb;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        ui_and_ope_obj.setEnabled(true);
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
