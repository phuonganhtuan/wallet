package view;

import api.AuthAPI;
import api.DebtAPI;
import api.ExpenditureAPI;
import api.UsersAPI;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utils.StringUtils;
import utils.TokenTool;
import static view.xemthongtindulieu.me;

/**
 * Form chính của chương trình
 *
 * @author PAT
 */
public class xemthongtindulieu extends javax.swing.JFrame {

    //Đối tượng toàn cục lưu chính class này
    public static xemthongtindulieu me;

    //Mẫu hiển thị mặc định cho bảng dữ liệu
    public DefaultTableModel tableModel = new DefaultTableModel();

    //Biến lưu dòng đang chọn trong bảng dữ liệu - mặc định là -1
    int currentIndex = -1;

    //Thứ tự các bản ghi hiển thị trên bảng ( dùng cho chức năng demo half - code )
    int order = 1;

    //Định danh loại bảng đang hiển thị -  true = so chi tieu dang hien thi, false = so no dang hien thi.
    boolean currentTable = true;

    //Định danh người dùng
    private String userid = "";

    //Ngày hiện tại
    Date ngayhomnay = new Date(System.currentTimeMillis());

    //Định dạng nnnn-tt-nn
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Thông tin văn bản về ngày hiện tại
     *
     * @return ngày hiện tại
     */
    public String thongtinngayhientai() {

        String thongtin = timeFormat.format(ngayhomnay.getTime());
        return thongtin;
    }

    /**
     * Cập nhật thông tin cho các nhãn
     */
    public void capnhatthongtin() {

        String sotien = "";
        String tennguoidung = "";
        String userid = "";

        if (AuthAPI.isLoggedIn()) {

            try {
                userid = TokenTool.getToken().get("userid");
                HashMap<String, String> userInfo = UsersAPI.getUserInfo(userid);
                sotien = userInfo.get("totalCash");
                tennguoidung = userInfo.get("fullName");
            } catch (Exception e) {
            }

        }

        tienLabel.setText(StringUtils.threeDigitsSeparate(" ", sotien));
        tenLabel.setText(tennguoidung);

    }

    /**
     * Creates new form xemchitieu
     */
    public xemthongtindulieu() {
        initComponents();

        //Tham chiếu đến chính nó ( gán thành đối tượng toàn cục cụ thể, các frame khác có thể tương tác)
        me = this;

        //Định danh bảng hiển thị là bảng chi tiêu
        currentTable = true;
        sochitieuButton.setBackground(Color.white);
        sochitieuButton.setBorderPainted(true);
        sonoButton.setBackground(new Color(230, 230, 230));
        sonoButton.setBorderPainted(false);

        //Cấu hình cho bảng hiển thị
        tableModel = (DefaultTableModel) walletTable.getModel();
        walletTable.setAutoResizeMode(walletTable.AUTO_RESIZE_OFF);
        walletTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        walletTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        walletTable.getColumnModel().getColumn(2).setPreferredWidth(430);
        walletTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        walletTable.getColumnModel().getColumn(4).setPreferredWidth(90);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        walletTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        walletTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        walletTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        tableModel = (DefaultTableModel) walletTable.getModel();

        if (AuthAPI.isLoggedIn()) {
            HashMap<String, String> token = TokenTool.getToken();
            ArrayList<HashMap<String, String>> listExpenditureRecords = null;
            if (token != null) {
                this.userid = token.get("userid");
                try {
                    listExpenditureRecords = ExpenditureAPI.getListRecord(userid);
                } catch (Exception ex) {
                    listExpenditureRecords = new ArrayList<>();
                    Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                listExpenditureRecords = new ArrayList<>();
            }

            //Hiển thị dữ liệu
            HienThiThuChi(listExpenditureRecords);

        }

    }

    /**
     * Hiển thi thông tin chi tiêu
     */
    public void HienThiThuChi(ArrayList<HashMap<String, String>> listRecords) {

        //Đặt thứ tự các bản ghi bắt đầu = 1
        order = 1;

        tableModel.setRowCount(0);

        String ngay = "";
        String noidung = "";
        String sotien = "";
        String kieu = "";

        for (HashMap<String, String> record : listRecords) {
            ngay = record.get("onDate");
            noidung = record.get("content");
            sotien = record.get("amount");
            kieu = record.get("positive").trim().equals("0") ? "Chi" : "Thu";
            sotien = StringUtils.threeDigitsSeparate(" ", sotien);
            Object[] row = new Object[]{
                order, ngay, noidung, sotien, kieu
            };

            tableModel.addRow(row);
            order++;
        }
        capnhatthongtin();


        /*
        Hiển thị dữ liệu thống kê ra màn hình
         */
    }

    /**
     * Hiển thị dữ liệu vay nợ
     */
    public void HienThiSoNo(ArrayList<HashMap<String, String>> listRecords) {

        //Đặt thứ tự các bản ghi bắt đầu = 1
        order = 1;

        tableModel.setRowCount(0);

        String ngay = "";
        String noidung = "";
        String sotien = "";
        String kieu = "";
        String trangthai = "";

        for (HashMap<String, String> record : listRecords) {
            ngay = record.get("onDate");
            noidung = record.get("content");
            sotien = record.get("amount");
            kieu = record.get("positive").trim().equals("0") ? "Cho vay" : "Đi vay";
            trangthai = record.get("paid").trim().equals("0") ? "Chưa trả" : "Đã trả";
            sotien = StringUtils.threeDigitsSeparate(" ", sotien);
            Object[] row = new Object[]{
                order, ngay, noidung, sotien, kieu, "Chưa trả"
            };

            tableModel.addRow(row);
            order++;
        }
        capnhatthongtin();
        /*
        Hiển thị dữ liệu thống kê ra màn hình
         */
    }

    /**
     * Thêm 1 bản ghi chi tiêu
     *
     * @param addedRow đối tượng chứa dữ liệu
     */
    public void ThemBanGhiChiTieu(Object[] addedRow, HashMap<String, String> addedRecord) {
        if (AuthAPI.isLoggedIn()) {
            try {
                ExpenditureAPI.insert(addedRecord);
                tableModel.addRow(addedRow);
                order++;
                capnhatthongtin();
            } catch (Exception ex) {
                Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Thêm 1 bản ghi nợ
     *
     * @param addedRow đối tượng chứa dữ liệu
     */
    public void ThemBanGhiNo(Object[] addedRow, HashMap<String, String> addedRecord) {
        if (AuthAPI.isLoggedIn()) {
            try {
                DebtAPI.insert(addedRecord);
                tableModel.addRow(addedRow);
                order++;
                capnhatthongtin();
            } catch (Exception ex) {
                Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Xóa 1 bản ghi chi tiêu
     */
    public void XoaBanGhiChiTieu() {
        if (AuthAPI.isLoggedIn()) {
            HashMap<String, String> token = TokenTool.getToken();
            ArrayList<HashMap<String, String>> listExpenditureRecords = null;
            if (token != null) {
                this.userid = token.get("userid");
                try {
                    listExpenditureRecords = ExpenditureAPI.getListRecord(userid);
                } catch (Exception ex) {
                    listExpenditureRecords = new ArrayList<>();
                    Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                listExpenditureRecords = new ArrayList<>();
            }
            String id = listExpenditureRecords.get(currentIndex).get("id");
            try {
                ExpenditureAPI.delete(id);
                tableModel.removeRow(currentIndex);
                order--;
                capnhatthongtin();
            } catch (Exception ex) {
                Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * Xóa 1 bản ghi nợ
     */
    public void XoaBanGhiNo() {
        if (AuthAPI.isLoggedIn()) {
            HashMap<String, String> token = TokenTool.getToken();
            ArrayList<HashMap<String, String>> listDebtRecords = null;
            if (token != null) {
                this.userid = token.get("userid");
                try {
                    listDebtRecords = DebtAPI.getListRecord(userid);
                } catch (Exception ex) {
                    listDebtRecords = new ArrayList<>();
                    Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                listDebtRecords = new ArrayList<>();
            }
            String id = listDebtRecords.get(currentIndex).get("id");
            try {
                DebtAPI.delete(id);
                tableModel.removeRow(currentIndex);
                order--;
                capnhatthongtin();
            } catch (Exception ex) {
                Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * Sửa 1 bản ghi chi tiêu
     *
     * @param editedRow đối tượng chứa dữ liệu
     */
    public void SuaBanGhiChiTieu(Object[] editedRow, HashMap<String, String> editedRecord) {
        if (AuthAPI.isLoggedIn()) {
            HashMap<String, String> token = TokenTool.getToken();
            ArrayList<HashMap<String, String>> listExpenditureRecords = null;
            if (token != null) {
                this.userid = token.get("userid");
                try {
                    listExpenditureRecords = ExpenditureAPI.getListRecord(userid);
                } catch (Exception ex) {
                    listExpenditureRecords = new ArrayList<>();
                    Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                listExpenditureRecords = new ArrayList<>();
            }
            try {
                String id = listExpenditureRecords.get(currentIndex).get("id");
                ExpenditureAPI.modify(id, editedRecord);
                tableModel.removeRow(currentIndex);
                tableModel.insertRow(currentIndex, editedRow);
                capnhatthongtin();
            } catch (Exception ex) {
                Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * Sửa 1 bản ghi nợ
     *
     * @param editedRow đối tượng chứa dữ liệu
     */
    public void SuaBanGhiNo(Object[] editedRow, HashMap<String, String> editedRecord) {

        if (AuthAPI.isLoggedIn()) {
            HashMap<String, String> token = TokenTool.getToken();
            ArrayList<HashMap<String, String>> listDebtRecords = null;
            if (token != null) {
                this.userid = token.get("userid");
                try {
                    listDebtRecords = DebtAPI.getListRecord(userid);
                } catch (Exception ex) {
                    listDebtRecords = new ArrayList<>();
                    Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                listDebtRecords = new ArrayList<>();
            }
            try {
                String id = listDebtRecords.get(currentIndex).get("id");
                DebtAPI.modify(id, editedRecord);
                tableModel.removeRow(currentIndex);
                tableModel.insertRow(currentIndex, editedRow);
                capnhatthongtin();
            } catch (Exception ex) {
                Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        thutuComboBox = new javax.swing.JComboBox<>();
        sapxepIcon = new javax.swing.JLabel();
        sapxepComboBox = new javax.swing.JComboBox<>();
        timText = new javax.swing.JTextField();
        timkiemIcon = new javax.swing.JLabel();
        dxButton = new javax.swing.JButton();
        tenLabel = new javax.swing.JLabel();
        taikhoanIconLabel = new javax.swing.JLabel();
        tienLabel = new javax.swing.JLabel();
        viLabel = new javax.swing.JLabel();
        tienIconLabel = new javax.swing.JLabel();
        maunenLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        walletTable = new javax.swing.JTable();
        sonoButton = new javax.swing.JButton();
        sochitieuButton = new javax.swing.JButton();
        thongkeIconLabel = new javax.swing.JLabel();
        thongkeLabel = new javax.swing.JLabel();
        themButton = new javax.swing.JButton();
        suaButton = new javax.swing.JButton();
        xoaButton = new javax.swing.JButton();
        nenLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(550, 100));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        thutuComboBox.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        thutuComboBox.setForeground(new java.awt.Color(51, 204, 0));
        thutuComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tăng", "Giảm" }));
        thutuComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thutuComboBoxActionPerformed(evt);
            }
        });
        getContentPane().add(thutuComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 160, 80, 30));

        sapxepIcon.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        sapxepIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sapxepIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/image/icons8_Sorting_27px_1.png"))); // NOI18N
        getContentPane().add(sapxepIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 160, 40, 30));

        sapxepComboBox.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        sapxepComboBox.setForeground(new java.awt.Color(0, 102, 255));
        sapxepComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ngày", "Nội dung", "Số tiền", "Kiểu" }));
        sapxepComboBox.setBorder(null);
        sapxepComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sapxepComboBoxActionPerformed(evt);
            }
        });
        getContentPane().add(sapxepComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 160, -1, 30));

        timText.setBackground(new java.awt.Color(223, 223, 223));
        timText.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        timText.setForeground(new java.awt.Color(51, 51, 51));
        timText.setText("Key Word");
        timText.setBorder(null);
        timText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                timTextMouseClicked(evt);
            }
        });
        timText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timTextActionPerformed(evt);
            }
        });
        getContentPane().add(timText, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 430, 30));

        timkiemIcon.setFont(new java.awt.Font("Corbel", 1, 18)); // NOI18N
        timkiemIcon.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        timkiemIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/image/icons8_Search_27px.png"))); // NOI18N
        getContentPane().add(timkiemIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 80, 30));

        dxButton.setBackground(new java.awt.Color(8, 107, 206));
        dxButton.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        dxButton.setForeground(new java.awt.Color(255, 255, 255));
        dxButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/image/icons8_Sign_Out_24px.png"))); // NOI18N
        dxButton.setText("Đăng xuất");
        dxButton.setFocusPainted(false);
        dxButton.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                dxButtonMouseMoved(evt);
            }
        });
        dxButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dxButtonActionPerformed(evt);
            }
        });
        getContentPane().add(dxButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 90, 140, 30));

        tenLabel.setFont(new java.awt.Font("Corbel", 0, 23)); // NOI18N
        tenLabel.setForeground(new java.awt.Color(255, 255, 255));
        tenLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tenLabel.setText("aaa");
        getContentPane().add(tenLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 30, 300, 40));

        taikhoanIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/image/person-male.png"))); // NOI18N
        getContentPane().add(taikhoanIconLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 23, -1, 120));

        tienLabel.setFont(new java.awt.Font("Corbel", 1, 32)); // NOI18N
        tienLabel.setForeground(new java.awt.Color(255, 255, 51));
        tienLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tienLabel.setText("0");
        getContentPane().add(tienLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 560, 50));

        viLabel.setFont(new java.awt.Font("Corbel", 0, 22)); // NOI18N
        viLabel.setForeground(new java.awt.Color(255, 255, 255));
        viLabel.setText("Số tiền trong ví :");
        getContentPane().add(viLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 240, -1));

        tienIconLabel.setBackground(new java.awt.Color(35, 122, 251));
        tienIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/image/2000px-Money_Flat_Icon.svg.png"))); // NOI18N
        tienIconLabel.setRequestFocusEnabled(false);
        getContentPane().add(tienIconLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 110, 100));

        maunenLabel.setBackground(new java.awt.Color(51, 153, 255));
        maunenLabel.setOpaque(true);
        getContentPane().add(maunenLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1210, 151));

        walletTable.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        walletTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Ngày", "Nội dung", "Số tiền", "Kiểu"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        walletTable.setAlignmentX(1.0F);
        walletTable.setAlignmentY(1.0F);
        walletTable.setGridColor(new java.awt.Color(207, 207, 207));
        walletTable.setRowHeight(30);
        walletTable.setSelectionBackground(new java.awt.Color(221, 221, 221));
        jScrollPane1.setViewportView(walletTable);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 200, 850, 500));

        sonoButton.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        sonoButton.setText("SỔ NỢ");
        sonoButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        sonoButton.setFocusPainted(false);
        sonoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sonoButtonActionPerformed(evt);
            }
        });
        getContentPane().add(sonoButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 100, 250));

        sochitieuButton.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        sochitieuButton.setText("SỔ CHI TIÊU");
        sochitieuButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        sochitieuButton.setFocusPainted(false);
        sochitieuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sochitieuButtonActionPerformed(evt);
            }
        });
        getContentPane().add(sochitieuButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 100, 240));

        thongkeIconLabel.setBackground(new java.awt.Color(0, 102, 0));
        thongkeIconLabel.setFont(new java.awt.Font("Corbel", 0, 24)); // NOI18N
        thongkeIconLabel.setForeground(new java.awt.Color(102, 102, 102));
        thongkeIconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        thongkeIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/image/icons8_Statistics_32px.png"))); // NOI18N
        thongkeIconLabel.setText("THỐNG KÊ");
        thongkeIconLabel.setPreferredSize(new java.awt.Dimension(91, 22));
        getContentPane().add(thongkeIconLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 160, 170, 40));

        thongkeLabel.setBackground(new java.awt.Color(248, 248, 248));
        thongkeLabel.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        thongkeLabel.setForeground(new java.awt.Color(51, 102, 0));
        thongkeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        thongkeLabel.setText("Demo chưa hỗ trợ");
        thongkeLabel.setOpaque(true);
        getContentPane().add(thongkeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 150, 250, 370));

        themButton.setBackground(new java.awt.Color(51, 153, 255));
        themButton.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        themButton.setForeground(new java.awt.Color(255, 255, 255));
        themButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/image/icons8_Plus_Math_24px.png"))); // NOI18N
        themButton.setText("Thêm bản ghi");
        themButton.setFocusPainted(false);
        themButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themButtonActionPerformed(evt);
            }
        });
        getContentPane().add(themButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 550, 170, 30));

        suaButton.setBackground(new java.awt.Color(51, 204, 0));
        suaButton.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        suaButton.setForeground(new java.awt.Color(255, 255, 255));
        suaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/image/icons8_Edit_24px_1.png"))); // NOI18N
        suaButton.setText("Sửa");
        suaButton.setFocusPainted(false);
        suaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suaButtonActionPerformed(evt);
            }
        });
        getContentPane().add(suaButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 610, 170, 30));

        xoaButton.setBackground(new java.awt.Color(204, 0, 0));
        xoaButton.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        xoaButton.setForeground(new java.awt.Color(255, 255, 255));
        xoaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/image/icons8_Delete_24px.png"))); // NOI18N
        xoaButton.setText("Xóa");
        xoaButton.setFocusPainted(false);
        xoaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xoaButtonActionPerformed(evt);
            }
        });
        getContentPane().add(xoaButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 670, 170, 30));

        nenLabel.setBackground(new java.awt.Color(245, 245, 245));
        nenLabel.setOpaque(true);
        getContentPane().add(nenLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1210, 710));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dxButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dxButtonActionPerformed
        if (JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc chắn muốn đăng xuất khỏi tài khoản hiện tại?") == 0) {
            //Làm mới toàn bộ dữ liệu, xóa sạch dữ liệu hiện tại sau khi cập nhật hoàn tất cơ sở dữ liệu, sẵn sàng cho phiên làm việc mới
            if (AuthAPI.isLoggedIn()) {
                try {
                    AuthAPI.logout();
                } catch (Exception ex) {
                    Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.dispose();
            new dangnhap().setVisible(true);
        }
    }//GEN-LAST:event_dxButtonActionPerformed

    private void sochitieuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sochitieuButtonActionPerformed

        //Định danh loại sổ cho bảng dữ liệu
        currentTable = true;
        sonoButton.setBackground(new Color(230, 230, 230));
        sonoButton.setBorderPainted(false);
        sochitieuButton.setBackground(Color.white);
        sochitieuButton.setBorderPainted(true);
        //Cấu hình lại bảng dữ liệu
        walletTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "STT", "Ngày", "Nội dung", "Số tiền", "Kiểu"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        walletTable.setAutoResizeMode(walletTable.AUTO_RESIZE_OFF);
        walletTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        walletTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        walletTable.getColumnModel().getColumn(2).setPreferredWidth(430);
        walletTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        walletTable.getColumnModel().getColumn(4).setPreferredWidth(90);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        walletTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        walletTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        walletTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableModel = (DefaultTableModel) walletTable.getModel();

        if (AuthAPI.isLoggedIn()) {
            HashMap<String, String> token = TokenTool.getToken();
            ArrayList<HashMap<String, String>> listExpenditureRecords = null;

            if (token != null) {
                this.userid = token.get("userid");
                try {
                    listExpenditureRecords = ExpenditureAPI.getListRecord(userid);
                } catch (Exception ex) {
                    listExpenditureRecords = new ArrayList<>();
                    Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                listExpenditureRecords = new ArrayList<>();
            }

            //Hiển thị dữ liệu
            HienThiThuChi(listExpenditureRecords);
        }
    }//GEN-LAST:event_sochitieuButtonActionPerformed

    private void themButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themButtonActionPerformed
        if (currentTable == true) {
            new themchitieu().setVisible(true);
        } else {
            new themkhoanno().setVisible(true);
        }
    }//GEN-LAST:event_themButtonActionPerformed

    private void suaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suaButtonActionPerformed

        //Trường hợp sửa chi tiêu
        if (currentTable == true) {
            currentIndex = -1;
            currentIndex = walletTable.getSelectedRow();
            if (currentIndex != -1) {
                String ngay = (String) tableModel.getValueAt(currentIndex, 1);
                String noidung = (String) tableModel.getValueAt(currentIndex, 2);
                String sotien = tableModel.getValueAt(currentIndex, 3).toString();
                String kieu = (String) tableModel.getValueAt(currentIndex, 4);
                new suachitieu(ngay, noidung, sotien, kieu).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn bản ghi muốn sửa.");
                return;
            }
        } else {

            //Trường hợp sửa sổ nợ
            currentIndex = -1;
            currentIndex = walletTable.getSelectedRow();
            if (currentIndex != -1) {
                String ngay = (String) tableModel.getValueAt(currentIndex, 1);
                String noidung = (String) tableModel.getValueAt(currentIndex, 2);
                String sotien = tableModel.getValueAt(currentIndex, 3).toString();
                String kieu = (String) tableModel.getValueAt(currentIndex, 4);
                String trangthai = (String) tableModel.getValueAt(currentIndex, 5);
                new suakhoanno(ngay, noidung, sotien, kieu, trangthai).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn bản ghi muốn sửa.");
                return;
            }
        }
    }//GEN-LAST:event_suaButtonActionPerformed

    private void xoaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xoaButtonActionPerformed

        //Trường hợp xóa chi tiêu
        if (currentTable == true) {
            currentIndex = -1;
            currentIndex = walletTable.getSelectedRow();
            if (currentIndex == -1) {
                JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn bản ghi muốn xóa.");
                return;
            }
            if (JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc chắn xóa bản ghi hiện tại?") == 0) {
                XoaBanGhiChiTieu();
                JOptionPane.showMessageDialog(rootPane, "Đã xóa!");
            }
        } else {
            currentIndex = -1;
            currentIndex = walletTable.getSelectedRow();
            if (currentIndex == -1) {
                JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn bản ghi muốn xóa.");
                return;
            }
            if (JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc chắn xóa bản ghi hiện tại?") == 0) {
                XoaBanGhiNo();
                JOptionPane.showMessageDialog(rootPane, "Đã xóa!");
            }
        }
    }//GEN-LAST:event_xoaButtonActionPerformed

    private void sonoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sonoButtonActionPerformed

        //Định danh loại sổ cho bảng dữ liệu
        currentTable = false;
        sochitieuButton.setBackground(new Color(230, 230, 230));
        sochitieuButton.setBorderPainted(false);
        sonoButton.setBackground(Color.white);
        sonoButton.setBorderPainted(true);

        //Cấu hình lại bảng dữ liệu
        walletTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "STT", "Ngày", "Nội dung", "Số tiền", "Kiểu", "Trạng thái"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        walletTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        walletTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        walletTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        walletTable.getColumnModel().getColumn(2).setPreferredWidth(350);
        walletTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        walletTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        walletTable.getColumnModel().getColumn(5).setPreferredWidth(90);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        walletTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        walletTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        walletTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        walletTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        tableModel = (DefaultTableModel) walletTable.getModel();

        if (AuthAPI.isLoggedIn()) {
            HashMap<String, String> token = TokenTool.getToken();
            ArrayList<HashMap<String, String>> listDebtRecords = null;

            if (token != null) {
                this.userid = token.get("userid");
                try {
                    listDebtRecords = DebtAPI.getListRecord(userid);
                } catch (Exception ex) {
                    listDebtRecords = new ArrayList<>();
                    Logger.getLogger(xemthongtindulieu.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                listDebtRecords = new ArrayList<>();
            }

            //Hiển thị dữ liệu
            HienThiSoNo(listDebtRecords);
        }

    }//GEN-LAST:event_sonoButtonActionPerformed

    private void timTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timTextActionPerformed
        JOptionPane.showMessageDialog(rootPane, "Bản demo hiện tại chưa hỗ trợ chức năng này!");
    }//GEN-LAST:event_timTextActionPerformed

    private void sapxepComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sapxepComboBoxActionPerformed
        JOptionPane.showMessageDialog(rootPane, "Chức năng chưa được xây dựng");
//        if (sapxepComboBox.getSelectedItem().toString().compareTo("Ngày") == 0) {
//            if (thutuComboBox.getSelectedItem().toString().compareTo("Tăng") == 0) {
//
//            } else {
//
//            }
//        } else if (sapxepComboBox.getSelectedItem().toString().compareTo("Nội dung") == 0) {
//            if (thutuComboBox.getSelectedItem().toString().compareTo("Tăng") == 0) {
//
//            } else {
//
//            }
//        } else if (sapxepComboBox.getSelectedItem().toString().compareTo("Số tiền") == 0) {
//            if (thutuComboBox.getSelectedItem().toString().compareTo("Tăng") == 0) {
//
//            } else {
//
//            }
//        } else {
//            if (thutuComboBox.getSelectedItem().toString().compareTo("Tăng") == 0) {
//
//            } else {
//
//            }
//        }
    }//GEN-LAST:event_sapxepComboBoxActionPerformed

    private void timTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timTextMouseClicked
        JOptionPane.showMessageDialog(rootPane, "Bản demo hiện tại chưa hỗ trợ chức năng này!");
    }//GEN-LAST:event_timTextMouseClicked

    private void thutuComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thutuComboBoxActionPerformed
        JOptionPane.showMessageDialog(rootPane, "Chức năng chưa được xây dựng");

//        if (thutuComboBox.getSelectedItem().toString().compareTo("Tăng") == 0) {
//            if (sapxepComboBox.getSelectedItem().toString().compareTo("Ngày") == 0) {
//
//            } else if (sapxepComboBox.getSelectedItem().toString().compareTo("Nội dung") == 0) {
//
//            } else if (sapxepComboBox.getSelectedItem().toString().compareTo("Số tiền") == 0) {
//
//            } else {
//
//            }
//        } else {
//            if (sapxepComboBox.getSelectedItem().toString().compareTo("Ngày") == 0) {
//
//            } else if (sapxepComboBox.getSelectedItem().toString().compareTo("Nội dung") == 0) {
//
//            } else if (sapxepComboBox.getSelectedItem().toString().compareTo("Số tiền") == 0) {
//
//            } else {
//
//    }
//    }
    }//GEN-LAST:event_thutuComboBoxActionPerformed

    private void dxButtonMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dxButtonMouseMoved

        // JOptionPane.showMessageDialog(rootPane, "Đang demo đừng đăng xuất!");
// TODO add your handling code here:
    }//GEN-LAST:event_dxButtonMouseMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton dxButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel maunenLabel;
    private javax.swing.JLabel nenLabel;
    private javax.swing.JComboBox<String> sapxepComboBox;
    private javax.swing.JLabel sapxepIcon;
    private javax.swing.JButton sochitieuButton;
    private javax.swing.JButton sonoButton;
    private javax.swing.JButton suaButton;
    private javax.swing.JLabel taikhoanIconLabel;
    private javax.swing.JLabel tenLabel;
    private javax.swing.JButton themButton;
    private javax.swing.JLabel thongkeIconLabel;
    private javax.swing.JLabel thongkeLabel;
    private javax.swing.JComboBox<String> thutuComboBox;
    private javax.swing.JLabel tienIconLabel;
    private javax.swing.JLabel tienLabel;
    private javax.swing.JTextField timText;
    private javax.swing.JLabel timkiemIcon;
    private javax.swing.JLabel viLabel;
    private javax.swing.JTable walletTable;
    private javax.swing.JButton xoaButton;
    // End of variables declaration//GEN-END:variables

}
