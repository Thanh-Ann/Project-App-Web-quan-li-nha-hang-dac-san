package DEMO_GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import BUSINESSLOGIC.*;
import CONNECTIONDATA.*;
import SCHEMACLASS.*;
import SCHEMAOBJECT.*;
import javax.swing.JTextField;

public class GUI_DS_BAN_LAN2 extends JFrame {
    private JPanel tableGridPanel;
    private JButton addButton, editButton, deleteButton, exportButton, refreshButton;
    private List<BAN> banList;
    private IConnector connector;
    private BAN_BUS banBus;
    private HOA_DON_BUS hoaDonBus;
    private HOA_DON_CHI_TIET_BUS chiTietBus;
    private MENU_BUS menuBus;

    public GUI_DS_BAN_LAN2(IConnector connector) {
        this.connector = connector;
        setTitle("Danh Sách Bàn");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Khởi tạo BUS
        banBus = new BAN_BUS(new BAN_DAO(connector));
        hoaDonBus = new HOA_DON_BUS(new HOA_DON_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector));
        chiTietBus = new HOA_DON_CHI_TIET_BUS(new HOA_DON_CHI_TIET_DAO(connector));
        menuBus = new MENU_BUS(new MENU_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector));

        // Khởi tạo dữ liệu
        initializeData();

        // Giao diện
        JPanel mainPanel = new JPanel(new BorderLayout());
        tableGridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPane = new JScrollPane(tableGridPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel chứa các nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Thêm");
        editButton = new JButton("Sửa");
        deleteButton = new JButton("Xóa");
        exportButton = new JButton("Xuất CSV");
        refreshButton = new JButton("Làm mới");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(refreshButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Sự kiện nút
        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
        exportButton.addActionListener(e -> handleExportCSV());
        refreshButton.addActionListener(e -> {
            initializeData();
            updateTableGrid();
        });

        // Cập nhật lưới bàn
        updateTableGrid();
    }

    public void initializeData() {
        try {
            banList = banBus.getAllBan();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách bàn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateTableGrid() {
        tableGridPanel.removeAll();
        for (BAN ban : banList) {
            JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
            tablePanel.setPreferredSize(new Dimension(200, 150));
            tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    ban.getTenBan(),
                    TitledBorder.CENTER,
                    TitledBorder.TOP,
                    new Font("SanSerif", Font.BOLD, 14),
                    Color.BLACK
                ),
                new EmptyBorder(10, 10, 10, 10)
            ));
            tablePanel.setBackground(ban.isTrangThai() ? new Color(255, 204, 204) : new Color(204, 255, 204));

            // Thêm panel chứa các nút
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            buttonPanel.setBackground(tablePanel.getBackground());

            // Nút Xem Bàn (chỉ hiển thị nếu bàn có khách)
            if (ban.isTrangThai()) {
                JButton viewButton = createSmallStyledButton("Xem", new Color(0, 188, 212), true);
                viewButton.addActionListener(e -> {
                    ArrayList<Object[]> danhSachMon = loadDanhSachMon(ban.getMaBan_ID());
                    new GUI_XemBan(connector, ban.getMaBan_ID(), ban.getTenBan(), ban.isTrangThai(), this).setVisible(true);
                });
                buttonPanel.add(viewButton);
            }

            // Nút Đổi Bàn
            JButton switchButton = createSmallStyledButton("Đổi", new Color(111, 66, 193), ban.isTrangThai());
            switchButton.addActionListener(e -> handleSwitchTable(ban.getMaBan_ID()));
            buttonPanel.add(switchButton);

            // Nút Gọi Món
            JButton orderButton = createSmallStyledButton("Gọi món", new Color(40, 167, 69), true);
            orderButton.addActionListener(e -> {
                new GUI_Goi_Mon(connector, ban.getMaBan_ID(), ban.getTenBan(), this).setVisible(true);
            });
            buttonPanel.add(orderButton);

            // Thêm khoảng trống để đẩy nút xuống dưới
            tablePanel.add(Box.createVerticalStrut(30), BorderLayout.CENTER); // Thêm khoảng trống 30px
            tablePanel.add(buttonPanel, BorderLayout.SOUTH);

            tableGridPanel.add(tablePanel);
        }
        tableGridPanel.revalidate();
        tableGridPanel.repaint();
    }

    private JButton createSmallStyledButton(String text, Color bgColor, boolean enabled) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(60, 25));
        button.setFont(new Font("SanSerif", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setEnabled(enabled);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(bgColor.brighter());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(bgColor);
                }
            }
        });
        return button;
    }

    private void handleAdd() {
        JTextField maBanField = new JTextField(10);
        JTextField tenBanField = new JTextField(10);
        JTextField maLoaiBanField = new JTextField(10);
        JTextField maKhuVucField = new JTextField(10);
        JTextField trangThaiField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Mã Bàn:"));
        panel.add(maBanField);
        panel.add(new JLabel("Tên Bàn:"));
        panel.add(tenBanField);
        panel.add(new JLabel("Mã Loại Bàn:"));
        panel.add(maLoaiBanField);
        panel.add(new JLabel("Mã Khu Vực:"));
        panel.add(maKhuVucField);
        panel.add(new JLabel("Trạng Thái (true/false):"));
        panel.add(trangThaiField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Thêm Bàn", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String maBan = maBanField.getText().trim();
                String tenBan = tenBanField.getText().trim();
                String maLoaiBan = maLoaiBanField.getText().trim();
                String maKhuVuc = maKhuVucField.getText().trim();
                String trangThai = trangThaiField.getText().trim();

                if (maBan.isEmpty() || tenBan.isEmpty() || maLoaiBan.isEmpty() || maKhuVuc.isEmpty() || trangThai.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean trangThaiBool;
                try {
                    trangThaiBool = Boolean.parseBoolean(trangThai);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Trạng thái phải là true hoặc false!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                BAN ban = new BAN(maBan, maLoaiBan, maKhuVuc, tenBan, trangThaiBool);
                banBus.addBan(ban);
                initializeData();
                updateTableGrid();
                JOptionPane.showMessageDialog(this, "Thêm bàn thành công!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm bàn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEdit() {
        JTextField maBanField = new JTextField(10);
        JTextField tenBanField = new JTextField(10);
        JTextField maLoaiBanField = new JTextField(10);
        JTextField maKhuVucField = new JTextField(10);
        JTextField trangThaiField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Mã Bàn:"));
        panel.add(maBanField);
        panel.add(new JLabel("Tên Bàn:"));
        panel.add(tenBanField);
        panel.add(new JLabel("Mã Loại Bàn:"));
        panel.add(maLoaiBanField);
        panel.add(new JLabel("Mã Khu Vực:"));
        panel.add(maKhuVucField);
        panel.add(new JLabel("Trạng Thái (true/false):"));
        panel.add(trangThaiField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Sửa Bàn", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String maBan = maBanField.getText().trim();
                String tenBan = tenBanField.getText().trim();
                String maLoaiBan = maLoaiBanField.getText().trim();
                String maKhuVuc = maKhuVucField.getText().trim();
                String trangThai = trangThaiField.getText().trim();

                if (maBan.isEmpty() || tenBan.isEmpty() || maLoaiBan.isEmpty() || maKhuVuc.isEmpty() || trangThai.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean trangThaiBool;
                try {
                    trangThaiBool = Boolean.parseBoolean(trangThai);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Trạng thái phải là true hoặc false!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                BAN ban = new BAN(maBan, maLoaiBan, maKhuVuc, tenBan, trangThaiBool);
                banBus.updateBan(ban);
                initializeData();
                updateTableGrid();
                JOptionPane.showMessageDialog(this, "Sửa bàn thành công!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi sửa bàn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleDelete() {
        JTextField maBanField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Mã Bàn:"));
        panel.add(maBanField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Xóa Bàn", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String maBan = maBanField.getText().trim();
                if (maBan.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập mã bàn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                banBus.deleteBan(maBan);
                initializeData();
                updateTableGrid();
                JOptionPane.showMessageDialog(this, "Xóa bàn thành công!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa bàn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleExportCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file CSV");
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Mã Bàn,Tên Bàn,Mã Loại Bàn,Mã Khu Vực,Trạng Thái\n");
                for (BAN ban : banList) {
                    writer.write(String.format("%s,%s,%s,%s,%b\n",
                        ban.getMaBan_ID(),
                        ban.getTenBan(),
                        ban.getMaLoaiBan_ID(),
                        ban.getMaKhuVuc_ID(),
                        ban.isTrangThai()));
                }
                JOptionPane.showMessageDialog(this, "Xuất CSV thành công!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất CSV: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleSwitchTable(String maBanFrom) {
        try {
            List<BAN> banTrong = getBanTrong();
            if (banTrong.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có bàn trống để đổi!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] tenBanTrong = banTrong.stream().map(BAN::getTenBan).toArray(String[]::new);
            String selectedTenBan = (String) JOptionPane.showInputDialog(
                this,
                "Chọn bàn đích:",
                "Đổi Bàn",
                JOptionPane.PLAIN_MESSAGE,
                null,
                tenBanTrong,
                tenBanTrong[0]
            );

            if (selectedTenBan == null) return;

            BAN banTo = banTrong.stream()
                .filter(b -> b.getTenBan().equals(selectedTenBan))
                .findFirst()
                .orElse(null);

            if (banTo == null) {
                JOptionPane.showMessageDialog(this, "Bàn đích không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String maBanTo = banTo.getMaBan_ID();
            String maHoaDon = hoaDonBus.getMaHoaDonFromMaBan(maBanFrom);
            if (maHoaDon == null) {
                JOptionPane.showMessageDialog(this, "Bàn " + maBanFrom + " không có hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (banTo.isTrangThai()) {
                JOptionPane.showMessageDialog(this, "Bàn đích đang có khách!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            HOA_DON hoaDon = hoaDonBus.getHoaDonById(maHoaDon);
            hoaDon.setMaBan_ID(maBanTo);
            hoaDonBus.updateHoaDon(hoaDon);
            banBus.huyDatBan(maBanFrom);
            banBus.datBan(maBanTo);
            initializeData();
            updateTableGrid();
            JOptionPane.showMessageDialog(this, "Đổi bàn thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi đổi bàn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ArrayList<Object[]> loadDanhSachMon(String maBan) {
        ArrayList<Object[]> danhSachMon = new ArrayList<>();
        try {
            String maHoaDon = hoaDonBus.getMaHoaDonFromMaBan(maBan);
            if (maHoaDon != null) {
                List<HOA_DON_CHI_TIET> chiTietList = chiTietBus.getChiTietByHoaDon(maHoaDon);
                for (HOA_DON_CHI_TIET chiTiet : chiTietList) {
                    MENU menu = menuBus.getMenuById(chiTiet.getMaSanPham_ID());
                    if (menu != null) {
                        danhSachMon.add(new Object[]{
                            menu.getTenSanPham(),
                            chiTiet.getSoLuong(),
                            chiTiet.getDonGia(),
                            chiTiet.getSoLuong() * chiTiet.getDonGia()
                        });
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách món: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return danhSachMon;
    }

    private List<BAN> getBanTrong() throws SQLException {
        List<BAN> banTrong = new ArrayList<>();
        for (BAN ban : banList) {
            if (!ban.isTrangThai()) {
                banTrong.add(ban);
            }
        }
        return banTrong;
    }

    public static void main(String[] args) {
        CONNECTIONSQLSERVER connector = new CONNECTIONSQLSERVER();
        SwingUtilities.invokeLater(() -> new GUI_DS_BAN_LAN2(connector).setVisible(true));
    }
}