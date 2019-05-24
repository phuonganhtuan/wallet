/*
Form đăng nhập
 */
package view;

import api.AuthAPI;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.MySQL;

/**
 * Form hiển thị frame đăng nhập
 */
public class dangnhap extends javax.swing.JFrame {

    public static dangnhap me;

    /**
     * Khởi tạo
     */
    public dangnhap() {
        initComponents();
        me = this;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginLabel = new javax.swing.JLabel();
        accText = new javax.swing.JTextField();
        passText = new javax.swing.JPasswordField();
        loginButton = new javax.swing.JButton();
        regisButton = new javax.swing.JButton();
        forgetButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QUẢN LÝ CHI TIÊU CÁ NHÂN");
        setLocation(new java.awt.Point(600, 300));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        loginLabel.setBackground(new java.awt.Color(51, 153, 255));
        loginLabel.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N
        loginLabel.setForeground(new java.awt.Color(255, 255, 255));
        loginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginLabel.setText("ĐĂNG NHẬP");
        loginLabel.setToolTipText("");
        loginLabel.setOpaque(true);
        getContentPane().add(loginLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 120));

        accText.setBackground(new java.awt.Color(223, 223, 223));
        accText.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        accText.setAutoscrolls(false);
        accText.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        getContentPane().add(accText, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 290, 41));

        passText.setBackground(new java.awt.Color(223, 223, 223));
        passText.setAutoscrolls(false);
        passText.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        passText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passTextActionPerformed(evt);
            }
        });
        getContentPane().add(passText, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 290, 41));

        loginButton.setBackground(new java.awt.Color(51, 204, 0));
        loginButton.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("ĐĂNG NHẬP");
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        getContentPane().add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 310, 140, 35));

        regisButton.setBackground(new java.awt.Color(51, 153, 255));
        regisButton.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        regisButton.setForeground(new java.awt.Color(255, 255, 255));
        regisButton.setText("ĐĂNG KÝ");
        regisButton.setFocusPainted(false);
        regisButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regisButtonActionPerformed(evt);
            }
        });
        getContentPane().add(regisButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 310, 140, 35));

        forgetButton.setBackground(new java.awt.Color(204, 0, 0));
        forgetButton.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        forgetButton.setForeground(new java.awt.Color(255, 255, 255));
        forgetButton.setText("QUÊN MẬT KHẨU?");
        forgetButton.setFocusPainted(false);
        forgetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forgetButtonActionPerformed(evt);
            }
        });
        getContentPane().add(forgetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 390, 290, 38));

        jLabel2.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Tên đăng nhập:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, 41));

        jLabel3.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Mật khẩu:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 127, 41));

        jLabel4.setBackground(new java.awt.Color(246, 246, 246));
        jLabel4.setOpaque(true);
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 480));

        jLabel5.setText("_____________________________________________");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, 340, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void passTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passTextActionPerformed

    }//GEN-LAST:event_passTextActionPerformed


    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed

        //Kiểm tra trường văn bản trống
        if (accText.getText().length() == 0 || passText.getPassword().toString().length() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập đầy đủ thông tin đăng nhập!");
            return;
        }

        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("username", accText.getText());
        userInfo.put("password", String.valueOf(passText.getPassword()));

        try {
            //Kiểm tra thông tin đăng nhập
            if (AuthAPI.login(userInfo)) {
                this.dispose();
                new xemthongtindulieu().setVisible(true);
            }
        } catch (Exception ex) {
            Logger.getLogger(dangnhap.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Đăng nhập không thành công: Sai Tên tài khoản hoặc Mật khẩu!");
            return;
        }

    }//GEN-LAST:event_loginButtonActionPerformed

    private void regisButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regisButtonActionPerformed
        new dangkytaikhoan().setVisible(true);
    }//GEN-LAST:event_regisButtonActionPerformed

    private void forgetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forgetButtonActionPerformed
        JOptionPane.showMessageDialog(rootPane, "Bản demo chưa thêm chức năng này!");
    }//GEN-LAST:event_forgetButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (!MySQL.isConnected()) {
                    if (MySQL.connect("localhost:3306", "walletjava", "root", "12345678")) {
                        System.out.println("Database is connected");
                    }
                }

                if (!AuthAPI.isLoggedIn()) {
                    new dangnhap().setVisible(true);

                } else {
                    new xemthongtindulieu().setVisible(true);
                }
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField accText;
    private javax.swing.JButton forgetButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel loginLabel;
    private javax.swing.JPasswordField passText;
    private javax.swing.JButton regisButton;
    // End of variables declaration//GEN-END:variables

}