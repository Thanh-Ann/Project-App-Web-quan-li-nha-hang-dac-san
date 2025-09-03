package DEMO_GUI;

import BUSINESSLOGIC.*;
import CONNECTIONDATA.CONNECTIONSQLSERVER;
import CONNECTIONDATA.IConnector;
import SCHEMACLASS.HOA_DON_CHI_TIET;
import SCHEMACLASS.MENU;
import SCHEMAOBJECT.HOA_DON_CHI_TIET_DAO;
import SCHEMAOBJECT.HOA_DON_DAO;
import SCHEMAOBJECT.MENU_DAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GUI_XemBan extends JFrame {
    private IConnector connector;
    private String maBan, tenBan;
    private boolean trangThai;
    private DefaultTableModel tableModel;
    private HOA_DON_BUS hoaDonBus;
    private HOA_DON_CHI_TIET_BUS chiTietBus;
    private MENU_BUS menuBus;
    private GUI_DS_BAN_LAN2 parentFrame;
    private JLabel lblTitle, lblTotal;

    public GUI_XemBan(IConnector connector, String maBan, String tenBan, boolean trangThai, GUI_DS_BAN_LAN2 parentFrame) {
        this.connector = connector;
        this.maBan = maBan;
        this.tenBan = tenBan;
        this.trangThai = trangThai;
        this.parentFrame = parentFrame;

        setTitle("Chi tiết bàn: " + tenBan);
        setSize(650, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadDanhSachMon();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // Title
        lblTitle = new JLabel();
        lblTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setText(tenBan + " - " + (trangThai ? "Có khách" : "Trống"));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Table
        String[] columns = {"Tên Món", "SL", "Đơn Giá", "Thành Tiền"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        lblTotal = new JLabel("Tổng: 0.0");
        lblTotal.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        infoPanel.add(lblTotal);
        bottomPanel.add(infoPanel, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton btnThanhToan = new JButton("Thanh Toán");
        btnThanhToan.setEnabled(trangThai);
        btnThanhToan.addActionListener(e -> handleThanhToan());
        btnThanhToan.setFocusPainted(false);
        btnPanel.add(btnThanhToan);
        bottomPanel.add(btnPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Init BUS
        hoaDonBus = new HOA_DON_BUS(new HOA_DON_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector));
        chiTietBus = new HOA_DON_CHI_TIET_BUS(new HOA_DON_CHI_TIET_DAO(connector));
        menuBus = new MENU_BUS(new MENU_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector));
    }

    private void loadDanhSachMon() {
        tableModel.setRowCount(0);
        double total = 0;
        try {
            String maHD = hoaDonBus.getLatestMaHoaDonFromMaBan(maBan);
            if (maHD != null) {
                List<HOA_DON_CHI_TIET> chiTietList = chiTietBus.getChiTietByHoaDon(maHD);
                for (HOA_DON_CHI_TIET ct : chiTietList) {
                    MENU m = menuBus.getMenuById(ct.getMaSanPham_ID());
                    if (m != null) {
                        double thanhTien = ct.getDonGia() * ct.getSoLuong();
                        tableModel.addRow(new Object[]{m.getTenSanPham(), ct.getSoLuong(), ct.getDonGia(), thanhTien});
                        total += thanhTien;
                    }
                }
                lblTotal.setText("Tổng: " + String.format("%,.2f", total));
            } else {
                JOptionPane.showMessageDialog(this, "Bàn chưa có hoá đơn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                lblTotal.setText("Tổng: 0.0");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải món: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleThanhToan() {
        try {
            String maHD = hoaDonBus.getLatestMaHoaDonFromMaBan(maBan);
            if (maHD != null) {
                new ThanhToanGUI(connector, maBan, tenBan, maHD, parentFrame).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Không có hoá đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI_XemBan(new CONNECTIONSQLSERVER(), "B001", "Bàn 1", true, null).setVisible(true));
    }
}
