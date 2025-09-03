package DEMO_GUI;

import BUSINESSLOGIC.*;
import CONNECTIONDATA.CONNECTIONSQLSERVER;
import CONNECTIONDATA.IConnector;
import SCHEMACLASS.*;
import SCHEMAOBJECT.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

public class ThanhToanGUI extends JFrame {
    private IConnector connector;
    private String maBan, tenBan, maHoaDon;
    private GUI_DS_BAN_LAN2 parentFrame;
    private double tongTien;

    private JLabel lblTotal, lblChange;
    private JTextField txtReceived, txtName, txtPhone;
    private JButton btnConfirm, btnPrint;
    private JComboBox<String> cbPayMethod;

    private HOA_DON_BUS hoaDonBus;
    private BAN_BUS banBus;

    public ThanhToanGUI(IConnector connector, String maBan, String tenBan, String maHoaDon, GUI_DS_BAN_LAN2 parentFrame) {
        this.connector = connector;
        this.maBan = maBan;
        this.tenBan = tenBan;
        this.maHoaDon = maHoaDon;
        this.parentFrame = parentFrame;

        setTitle("Thanh Toán - " + tenBan);
        setSize(450, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        hoaDonBus = new HOA_DON_BUS(new HOA_DON_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector));
        banBus = new BAN_BUS(new BAN_DAO(connector));
        loadTongTien();
        initComponents();
    }

    private void loadTongTien() {
        try {
            HOA_DON hd = hoaDonBus.getHoaDonById(maHoaDon);
            tongTien = (hd != null) ? hd.getTongTien() : 0.0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            tongTien = 0.0;
        }
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Bàn: " + tenBan), gbc);
        gbc.gridx = 1;
        lblTotal = new JLabel(String.format("Tổng tiền: %,.2f", tongTien));
        panel.add(lblTotal, gbc);

        gbc.gridy = 1; gbc.gridx = 0;
        panel.add(new JLabel("Phương thức TT:"), gbc);
        gbc.gridx = 1;
        cbPayMethod = new JComboBox<>(new String[]{"Tiền mặt", "Thẻ tín dụng", "Chuyển khoản"});
        panel.add(cbPayMethod, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        panel.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(15);
        panel.add(txtName, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        panel.add(new JLabel("Số ĐT:"), gbc);
        gbc.gridx = 1;
        txtPhone = new JTextField(15);
        panel.add(txtPhone, gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        panel.add(new JLabel("Tiền nhận:"), gbc);
        gbc.gridx = 1;
        txtReceived = new JTextField(10);
        panel.add(txtReceived, gbc);

        gbc.gridy = 5; gbc.gridx = 0;
        panel.add(new JLabel("Tiền thừa:"), gbc);
        gbc.gridx = 1;
        lblChange = new JLabel("0.00");
        panel.add(lblChange, gbc);

        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnConfirm = new JButton("Xác nhận");
        btnPrint = new JButton("Xuất hoá đơn");
        btnPrint.setEnabled(false);
        btnPanel.add(btnConfirm);
        btnPanel.add(btnPrint);
        panel.add(btnPanel, gbc);

        add(panel);

        txtReceived.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { updateChange(); }
        });
        btnConfirm.addActionListener(e -> handleConfirm());
        btnPrint.addActionListener(e -> showInvoiceDialog());
    }

    private void updateChange() {
        try 
        {
            double received = Double.parseDouble(txtReceived.getText());
            double change = received - tongTien;
            lblChange.setText(String.format("%,.2f", change));
            boolean valid = change >= 0 && !txtName.getText().isEmpty() && !txtPhone.getText().isEmpty();
            btnConfirm.setEnabled(valid);
            btnPrint.setEnabled(valid);
        } 
        catch (Exception e) 
        {
            lblChange.setText("0.00");
            btnConfirm.setEnabled(false);
            btnPrint.setEnabled(false);
        }
    }

    private void handleConfirm() {
        updateChange();
        if (!btnConfirm.isEnabled()) 
            return;
        try {
            HOA_DON hd = hoaDonBus.getHoaDonById(maHoaDon);
            hd.setNgayBan(new Date(System.currentTimeMillis()));
            hd.setTrangThai("Đã thanh toán");
            hoaDonBus.updateHoaDon(hd);

            BAN ban = banBus.getBanById(maBan);
            if (ban != null && ban.isTrangThai())
                banBus.huyDatBan(maBan);
            if (parentFrame != null) 
            {
                parentFrame.initializeData();
                parentFrame.updateTableGrid();
                parentFrame.setVisible(true);
            }
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showInvoiceDialog() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\n======= THÔNG TIN THANH TOÁN =======\n");
        sb.append("Bàn: ").append(tenBan).append("\n");
        sb.append("Mã hóa đơn: ").append(maHoaDon).append("\n");
        sb.append("Khách hàng: ").append(txtName.getText()).append("\n");
        sb.append("Số điện thoại: ").append(txtPhone.getText()).append("\n");
        sb.append("Phương thức thanh toán: ").append(cbPayMethod.getSelectedItem()).append("\n");
        sb.append("\n--- Danh sách món đã gọi ---\n");

        try 
        {
            HOA_DON_CHI_TIET_BUS chiTietBus = new HOA_DON_CHI_TIET_BUS(new HOA_DON_CHI_TIET_DAO(connector));
            MENU_BUS menuBus = new MENU_BUS(new MENU_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector));
            List<HOA_DON_CHI_TIET> list = chiTietBus.getChiTietByHoaDon(maHoaDon);
            for (HOA_DON_CHI_TIET ct : list) 
            {
                MENU m = menuBus.getMenuById(ct.getMaSanPham_ID());
                if (m != null) 
                {
                    double tt = ct.getDonGia() * ct.getSoLuong();
                    sb.append(m.getTenSanPham())
                      .append(" x").append(ct.getSoLuong())
                      .append(" @ ").append(String.format("%,.2f", ct.getDonGia()))
                      .append(" = ").append(String.format("%,.2f", tt)).append("\n");
                }
            }
        } 
        catch (Exception e) 
        {
            sb.append("[Lỗi khi tải món]\n");
        }

        sb.append("\nTổng tiền: ").append(String.format("%,.2f", tongTien)).append("\n");
        sb.append("Tiền nhận: ").append(txtReceived.getText()).append("\n");
        sb.append("Tiền thừa: ").append(lblChange.getText()).append("\n");
        sb.append("Ngày thanh toán: ").append(new Date(System.currentTimeMillis()).toString()).append("\n");
        sb.append("===================================\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(420, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Chi tiết thanh toán", JOptionPane.INFORMATION_MESSAGE);

        try {
            HOA_DON hd = hoaDonBus.getHoaDonById(maHoaDon);
            hd.setNgayBan(new Date(System.currentTimeMillis()));
            hd.setTrangThai("Đã thanh toán");
            hoaDonBus.updateHoaDon(hd);

            BAN ban = banBus.getBanById(maBan);
            if (ban != null && ban.isTrangThai()) {
                banBus.huyDatBan(maBan);
            }

            JOptionPane.showMessageDialog(this, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            if (parentFrame != null) {
                parentFrame.initializeData();
                parentFrame.updateTableGrid();
                parentFrame.setVisible(true);
            }
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ThanhToanGUI(new CONNECTIONSQLSERVER(), "B001", "Bàn 1", "HD001", null).setVisible(true));
    }
}
