/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_and_operation;

import UI_and_operation.UI_and_operation.dialog_type_for_db_e_a;
import static UI_and_operation.connection_to_ms_sql.getLocal_host;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_password;
import static UI_and_operation.connection_to_ms_sql.getLocal_host_user_name;
import static UI_and_operation.view_history_list.col_sql;
import static UI_and_operation.view_history_list.tb_sql;
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
public class view_history_thai extends javax.swing.JFrame
        implements WindowListener {

    private UI_and_operation ui_and_ope_obj;
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
            pst = con.prepareStatement("SELECT * FROM to_thai_history_tb;");
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
            pst = con.prepareStatement("SELECT id_to_thai, bank, bank_id, name, phone_no "
                    + "FROM to_thai_history_tb "
                    + "ORDER BY name, bank_id, phone_no, bank "
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
            pst = con.prepareStatement("SELECT id_to_thai FROM to_thai_history_tb;");
            rs = pst.executeQuery();
            if (rs.next()) {
                DefaultTableModel dft = (DefaultTableModel) history_tb.getModel();
                dft.setRowCount(0);
                pst = con.prepareStatement("SELECT TOP " + num_show_his + " id_to_thai, bank, bank_id, name, phone_no "
                        + "FROM to_thai_history_tb "
                        + "ORDER BY name, bank_id, phone_no, bank;");
                rs = pst.executeQuery();
                while (rs.next()) {

                    Vector v3 = new Vector();

                    //set to v2 all data only 1 row
                    v3.add(rs.getString("id_to_thai"));
                    v3.add(rs.getString("name"));
                    v3.add(rs.getString("bank_id"));
                    v3.add(rs.getString("phone_no"));
                    v3.add(rs.getString("bank"));

                    //set data to table history row
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
     * Creates new form view_history_thai
     */
    public view_history_thai(UI_and_operation ui_and_ope_obj) {
        this.ui_and_ope_obj = ui_and_ope_obj;
        initComponents();
        setTitle("thai bank information");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //design head table  total money in balance
        history_tb.getTableHeader().setFont(new Font("Khmer OS Siemreap", Font.BOLD, 30));
        history_tb.setRowHeight(40);
        histroy_lb.setText("thai bank information");
        addWindowListener(this);
        set_history();
        set_see_more_bn();

        history_tb.getColumnModel().getColumn(0).setMaxWidth(0);
        history_tb.getColumnModel().getColumn(0).setMinWidth(0);
        history_tb.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public view_history_thai() {
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

        histroy_lb = new javax.swing.JLabel();
        history_add_bn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        history_tb = new javax.swing.JTable();
        bn_1_col = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        histroy_lb.setFont(new java.awt.Font("Khmer OS Battambang", 1, 40)); // NOI18N
        histroy_lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        histroy_lb.setText("to province infor");

        history_add_bn.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        history_add_bn.setText("ADD");
        history_add_bn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                history_add_bnActionPerformed(evt);
            }
        });

        history_tb.getTableHeader().setReorderingAllowed(false);
        history_tb.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        history_tb.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "name", "code", "phone number", "bank"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
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
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1347, Short.MAX_VALUE)
                    .addComponent(bn_1_col, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(histroy_lb, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(history_add_bn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bn_1_col, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void history_add_bnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_history_add_bnActionPerformed
        to_thai_info to_thai_info_obj = new to_thai_info("Add", dialog_type_for_db_e_a.Add, this);
        to_thai_info_obj.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_history_add_bnActionPerformed

    private void history_tbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_history_tbMouseClicked
        boolean is_edit_tb = history_tb.isEditing();
        if (is_edit_tb == false) {
            //table in UI
            DefaultTableModel model = (DefaultTableModel) history_tb.getModel();
            //select index of the table row
            int selectedIndex = history_tb.getSelectedRow();
            String id = model.getValueAt(selectedIndex, 0).toString();

//            JOptionPane.showMessageDialog(this, "You can not edit this table");
            Object[] options = {UI_and_operation.dialog_choose_e_d_c.Edit, UI_and_operation.dialog_choose_e_d_c.Delete, UI_and_operation.dialog_choose_e_d_c.Close};
            UI_and_operation.dialog_choose_e_d_c choose_from_dialog = UI_and_operation.dialog_choose_e_d_c.Close;
            int idx = JOptionPane.showOptionDialog(this, "what you choose?", null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            if (idx != -1) {
                choose_from_dialog = UI_and_operation.dialog_choose_e_d_c.values()[idx];
            }

            Connection con;
            PreparedStatement pst;
            try {
                con = DriverManager.getConnection(
                        getLocal_host(),
                        getLocal_host_user_name(),
                        getLocal_host_password()
                );

                switch (choose_from_dialog) {
                    case Edit:
                        to_thai_info to_thai_info_obj = new to_thai_info("Save", dialog_type_for_db_e_a.Edit, this);
                        to_thai_info_obj.set_tf_from_sql(Integer.parseInt(id));
                        to_thai_info_obj.setVisible(true);
                        this.setEnabled(false);
                        break;
                    case Delete:

                        //update sql query to access
                        pst = con.prepareStatement("delete from to_thai_history_tb where id_to_thai = " + id + ";");
                        pst.executeUpdate();
                        set_history();
                        break;
                }
            } catch (SQLException ex) {
            sql_con sql_con_obj = new sql_con(ex);
            sql_con_obj.setVisible(true);
            }

        }
    }//GEN-LAST:event_history_tbMouseClicked

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

            pst = con.prepareStatement("SELECT * FROM to_thai_history_tb;");
            rs = pst.executeQuery();
            if (rs.next()) {

                pst = con.prepareStatement("SELECT id_to_thai, bank, bank_id, name, phone_no "
                        + "FROM to_thai_history_tb "
                        + "ORDER BY name, bank_id, phone_no, bank "
                        + "OFFSET " + next_show_his + " ROWS "
                        + "FETCH NEXT " + num_show_his + " ROWS ONLY;");
                rs = pst.executeQuery();
                if (rs.next()) {
                    pst = con.prepareStatement("SELECT id_to_thai, bank, bank_id, name, phone_no "
                            + "FROM to_thai_history_tb "
                            + "ORDER BY name, bank_id, phone_no, bank "
                            + "OFFSET " + next_show_his + " ROWS "
                            + "FETCH NEXT " + num_show_his + " ROWS ONLY;");
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        dft.setRowCount(row_num);
                        row_num++;
                        Vector v3 = new Vector();
                        //set to v2 all data only 1 row
                        v3.add(rs.getString("id_to_thai"));
                        v3.add(rs.getString("name"));
                        v3.add(rs.getString("bank_id"));
                        v3.add(rs.getString("phone_no"));
                        v3.add(rs.getString("bank"));
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
            java.util.logging.Logger.getLogger(view_history_thai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(view_history_thai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(view_history_thai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(view_history_thai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new view_history_thai().setVisible(true);
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
