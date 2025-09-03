/*
 * GUI for managing data input/output for all tables in QUAN_LY_NHA_HANG_DAC_SAN_VIET_NAM
 */
package DEMO_GUI;

import BUSINESSLOGIC.*;
import CONNECTIONDATA.CONNECTIONSQLSERVER;
import SCHEMACLASS.*;
import SCHEMAOBJECT.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.List;
import CONNECTIONDATA.IConnector;
import javax.swing.border.EmptyBorder;

public class GUI_Nhap_Xuat extends JFrame {
    private JComboBox<String> tableComboBox;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private Map<String, Object> busInstances;
    private IConnector connector;

    public GUI_Nhap_Xuat(IConnector connector) {
        this.connector = connector;
        setTitle("Quản Lý Nhà Hàng - Thêm/Sửa/Xuất");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 850);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.add(new JLabel("Bảng dữ liệu: "));
        tableComboBox = new JComboBox<>(new String[]{
            "BAN", "HOA_DON", "HOA_DON_CHI_TIET", "KHU_VUC", "LOAI_BAN",
            "MENU", "PHUONG_XA", "BAN_SAN_PHAM", "LOAI_SAN_PHAM", "TANG"
        });
        tableComboBox.addActionListener(e -> updateTableData());
        toolBar.add(tableComboBox);
        mainPanel.add(toolBar, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel);
        dataTable.setRowHeight(24);
        dataTable.setShowGrid(true);
        dataTable.setGridColor(Color.LIGHT_GRAY);
        dataTable.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(dataTable);
        scroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        addButton("Thêm", e -> handleAdd(), buttonPanel);
        addButton("Sửa", e -> handleEdit(), buttonPanel);
        addButton("Xóa", e -> handleDelete(), buttonPanel);
        addButton("Xuất CSV", e -> handleExport(), buttonPanel);
        addButton("Làm mới", e -> updateTableData(), buttonPanel);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        initializeBusInstances();
        updateTableData();
    }

    private void addButton(String text, ActionListener action, JPanel panel) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.addActionListener(action);
        panel.add(btn);
    }

    private void initializeBusInstances() {
        busInstances = new HashMap<>();
        busInstances.put("BAN", new BAN_BUS(new BAN_DAO(connector)));
        busInstances.put("HOA_DON", new HOA_DON_BUS(new HOA_DON_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector)));
        busInstances.put("HOA_DON_CHI_TIET", new HOA_DON_CHI_TIET_BUS(new HOA_DON_CHI_TIET_DAO(connector)));
        busInstances.put("KHU_VUC", new KHU_VUC_BUS(new KHU_VUC_DAO(connector), new TANG_DAO(connector)));
        busInstances.put("LOAI_BAN", new LOAI_BAN_BUS(new LOAI_BAN_DAO(connector)));
        busInstances.put("MENU", new MENU_BUS(new MENU_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector)));
        busInstances.put("PHUONG_XA", new PHUONG_XA_BUS(new PHUONG_XA_DAO(connector), new MENU_DAO(connector)));
        busInstances.put("BAN_SAN_PHAM", new BAN_SAN_PHAM_BUS(new BAN_SAN_PHAM_DAO(connector)));
        busInstances.put("LOAI_SAN_PHAM", new LOAI_SAN_PHAM_BUS(new LOAI_SAN_PHAM_DAO(connector)));
        busInstances.put("TANG", new TANG_BUS(new TANG_DAO(connector), new KHU_VUC_DAO(connector)));
    }

    private void updateTableData() {
        String selected = (String) tableComboBox.getSelectedItem();
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(getColumnNames(selected));
        try {
            List<?> list = switch (selected) {
                case "BAN" -> ((BAN_BUS) busInstances.get(selected)).getAllBan();
                case "HOA_DON" -> ((HOA_DON_BUS) busInstances.get(selected)).getAllHoaDon();
                case "HOA_DON_CHI_TIET" -> ((HOA_DON_CHI_TIET_BUS) busInstances.get(selected)).getAllChiTiet();
                case "KHU_VUC" -> ((KHU_VUC_BUS) busInstances.get(selected)).getAllKhuVuc();
                case "LOAI_BAN" -> ((LOAI_BAN_BUS) busInstances.get(selected)).getAllLoaiBan();
                case "MENU" -> ((MENU_BUS) busInstances.get(selected)).getAllMenu();
                case "PHUONG_XA" -> ((PHUONG_XA_BUS) busInstances.get(selected)).getAllPhuongXa();
                case "BAN_SAN_PHAM" -> ((BAN_SAN_PHAM_BUS) busInstances.get(selected)).getAllBanSanPham();
                case "LOAI_SAN_PHAM" -> ((LOAI_SAN_PHAM_BUS) busInstances.get(selected)).getAllLoaiSanPham();
                case "TANG" -> ((TANG_BUS) busInstances.get(selected)).getAllTANG();
                default -> Collections.emptyList();
            };
            for (Object obj : list) {
                tableModel.addRow(toRowArray(obj));
            }
        } catch (SQLException e) {
            showError("Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }

    private Object[] toRowArray(Object obj) {
        if (obj instanceof BAN ban) {
            return new Object[]{ban.getMaBan_ID(), ban.getMaLoaiBan_ID(), ban.getMaKhuVuc_ID(), ban.getTenBan(), ban.isTrangThai()};
        } else if (obj instanceof HOA_DON hd) {
            return new Object[]{hd.getMaHoaDon_ID(), hd.getMaBan_ID(), hd.getTongTien(), hd.getNgayBan(), hd.getTrangThai()};
        } else if (obj instanceof HOA_DON_CHI_TIET ct) {
            return new Object[]{ct.getMaHDCT_ID(), ct.getMaHoaDon_ID(), ct.getMaSanPham_ID(), ct.getDonGia(), ct.getSoLuong()};
        } else if (obj instanceof KHU_VUC kv) {
            return new Object[]{kv.getMaKhuVuc_ID(), kv.getMaTang_ID(), kv.getTenKhuVuc(), kv.getMoTa()};
        } else if (obj instanceof LOAI_BAN lb) {
            return new Object[]{lb.getMaLoaiBan_ID(), lb.getTenLoaiBan(), lb.getMoTa()};
        } else if (obj instanceof MENU menu) {
            return new Object[]{menu.getMaSanPham_ID(), menu.getTenSanPham(), menu.getMaLoaiSanPham_ID(), menu.getMaPhuongXa_ID(), menu.getDonGia(), menu.isTrangThai()};
        } else if (obj instanceof PHUONG_XA px) {
            return new Object[]{px.getMaPhuongXa_ID(), px.getTenPhuongXa(), px.getMoTa()};
        } else if (obj instanceof BAN_SAN_PHAM bsp) {
            return new Object[]{bsp.getMaBan_ID(), bsp.getMaSanPham_ID()};
        } else if (obj instanceof LOAI_SAN_PHAM lsp) {
            return new Object[]{lsp.getMaLoaiSanPham_ID(), lsp.getTenLoaiSanPham()};
        } else if (obj instanceof TANG tang) {
            return new Object[]{tang.getMaTang_ID(), tang.getTenTang(), tang.getMoTa()};
        }
        return new Object[]{};
    }

    private String[] getColumnNames(String table) {
        return switch (table) {
            case "BAN" -> new String[]{"Mã Bàn", "Loại Bàn", "Khu Vực", "Tên Bàn", "Trạng Thái"};
            case "HOA_DON" -> new String[]{"Mã HĐ", "Mã Bàn", "Tổng Tiền", "Ngày Bán", "Trạng Thái"};
            case "HOA_DON_CHI_TIET" -> new String[]{"Mã CT", "Mã HĐ", "Mã SP", "Đơn Giá", "SL"};
            case "KHU_VUC" -> new String[]{"Mã KV", "Mã Tầng", "Tên KV", "Mô Tả"};
            case "LOAI_BAN" -> new String[]{"Mã LB", "Tên LB", "Mô Tả"};
            case "MENU" -> new String[]{"Mã SP", "Tên SP", "Loại SP", "Phường/Xã", "Đơn Giá", "TT"};
            case "PHUONG_XA" -> new String[]{"Mã PX", "Tên PX", "Mô Tả"};
            case "BAN_SAN_PHAM" -> new String[]{"Mã Bàn", "Mã SP"};
            case "LOAI_SAN_PHAM" -> new String[]{"Mã LSP", "Tên LSP"};
            case "TANG" -> new String[]{"Mã Tầng", "Tên Tầng", "Mô Tả"};
            default -> new String[]{};
        };
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void handleAdd() {
        String selectedTable = (String) tableComboBox.getSelectedItem();
        try {
            new AddDialog(this, selectedTable).setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu thêm mới: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEdit() {
        String selectedTable = (String) tableComboBox.getSelectedItem();
        int selectedRow = dataTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bản ghi để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedTable.equals("BAN_SAN_PHAM")) {
            JOptionPane.showMessageDialog(this, "Bảng BAN_SAN_PHAM không hỗ trợ sửa. Vui lòng xóa và thêm lại bản ghi mới!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            new EditDialog(this, selectedTable, selectedRow).setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu chỉnh sửa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        String selectedTable = (String) tableComboBox.getSelectedItem();
        int selectedRow = dataTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bản ghi để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa bản ghi này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            switch (selectedTable) {
                case "BAN":
                    String maBanID = tableModel.getValueAt(selectedRow, 0).toString();
                    ((BAN_BUS) busInstances.get("BAN")).deleteBan(maBanID);
                    break;
                case "HOA_DON":
                    String maHoaDonID = tableModel.getValueAt(selectedRow, 0).toString();
                    ((HOA_DON_BUS) busInstances.get("HOA_DON")).deleteHoaDon(maHoaDonID);
                    break;
                case "HOA_DON_CHI_TIET":
                    String maHDCTID = tableModel.getValueAt(selectedRow, 0).toString();
                    ((HOA_DON_CHI_TIET_BUS) busInstances.get("HOA_DON_CHI_TIET")).deleteChiTiet(maHDCTID);
                    break;
                case "KHU_VUC":
                    String maKhuVucID = tableModel.getValueAt(selectedRow, 0).toString();
                    ((KHU_VUC_BUS) busInstances.get("KHU_VUC")).deleteKhuVuc(maKhuVucID);
                    break;
                case "LOAI_BAN":
                    String maLoaiBanID = tableModel.getValueAt(selectedRow, 0).toString();
                    ((LOAI_BAN_BUS) busInstances.get("LOAI_BAN")).deleteLoaiBan(maLoaiBanID);
                    break;
                case "MENU":
                    String maSanPhamID = tableModel.getValueAt(selectedRow, 0).toString();
                    ((MENU_BUS) busInstances.get("MENU")).deleteMenu(maSanPhamID);
                    break;
                case "PHUONG_XA":
                    String maPhuongXaID = tableModel.getValueAt(selectedRow, 0).toString();
                    ((PHUONG_XA_BUS) busInstances.get("PHUONG_XA")).deletePhuongXa(maPhuongXaID);
                    break;
                case "BAN_SAN_PHAM":
                    String maBanIDForBSP = tableModel.getValueAt(selectedRow, 0).toString();
                    String maSanPhamIDForBSP = tableModel.getValueAt(selectedRow, 1).toString();
                    ((BAN_SAN_PHAM_BUS) busInstances.get("BAN_SAN_PHAM")).deleteBanSanPham(maBanIDForBSP, maSanPhamIDForBSP);
                    break;
                case "LOAI_SAN_PHAM":
                    String maLoaiSanPhamID = tableModel.getValueAt(selectedRow, 0).toString();
                    ((LOAI_SAN_PHAM_BUS) busInstances.get("LOAI_SAN_PHAM")).deleteLoaiSanPham(maLoaiSanPhamID);
                    break;
                case "TANG":
                    String maTangID = tableModel.getValueAt(selectedRow, 0).toString();
                    ((TANG_BUS) busInstances.get("TANG")).deleteTANG(maTangID);
                    break;
            }
            JOptionPane.showMessageDialog(this, "Xóa bản ghi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            updateTableData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi không xác định: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleExport() {
        String selectedTable = (String) tableComboBox.getSelectedItem();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(selectedTable + ".csv"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                // Ghi header
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.write(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();

                // Ghi dữ liệu
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        writer.write(tableModel.getValueAt(i, j).toString());
                        if (j < tableModel.getColumnCount() - 1) {
                            writer.write(",");
                        }
                    }
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(this, "Xuất file CSV thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất CSV: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void validateInput(List<JComponent> inputFields, String table) throws IllegalArgumentException, SQLException {
        switch (table) {
            case "BAN":
                String tenBan = ((JTextField) inputFields.get(7)).getText().trim();
                if (tenBan.isEmpty()) {
                    throw new IllegalArgumentException("Tên bàn không được để trống");
                }
                String trangThai = ((JTextField) inputFields.get(9)).getText().trim().toLowerCase();
                if (!trangThai.equals("true") && !trangThai.equals("false")) {
                    throw new IllegalArgumentException("Trạng thái phải là true hoặc false");
                }
                String maLoaiBan = getInputValue(inputFields, 3);
                if (((LOAI_BAN_BUS) busInstances.get("LOAI_BAN")).getLoaiBanById(maLoaiBan) == null) {
                    throw new IllegalArgumentException("Mã loại bàn không tồn tại");
                }
                String maKhuVuc = getInputValue(inputFields, 5);
                if (((KHU_VUC_BUS) busInstances.get("KHU_VUC")).getKhuVucById(maKhuVuc) == null) {
                    throw new IllegalArgumentException("Mã khu vực không tồn tại");
                }
                break;
            case "HOA_DON":
                String tongTien = ((JTextField) inputFields.get(5)).getText().trim();
                try {
                    double tongTienValue = Double.parseDouble(tongTien);
                    if (tongTienValue < 0) {
                        throw new IllegalArgumentException("Tổng tiền không được âm");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Tổng tiền phải là số hợp lệ");
                }
                String maBan = getInputValue(inputFields, 3);
                if (((BAN_BUS) busInstances.get("BAN")).getBanById(maBan) == null) {
                    throw new IllegalArgumentException("Mã bàn không tồn tại");
                }
                String trThai = ((JTextField) inputFields.get(9)).getText().trim();
                if (!trThai.equals("Chưa thanh toán") && !trThai.equals("Đã thanh toán")) {
                    throw new IllegalArgumentException("Trạng thái phải là 'Chưa thanh toán' hoặc 'Đã thanh toán'");
                }
                break;
            case "HOA_DON_CHI_TIET":
                String donGiaCT = ((JTextField) inputFields.get(7)).getText().trim();
                try {
                    double donGiaValue = Double.parseDouble(donGiaCT);
                    if (donGiaValue <= 0) {
                        throw new IllegalArgumentException("Đơn giá phải lớn hơn 0");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Đơn giá phải là số hợp lệ");
                }
                String soLuong = ((JTextField) inputFields.get(9)).getText().trim();
                try {
                    int soLuongValue = Integer.parseInt(soLuong);
                    if (soLuongValue <= 0) {
                        throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Số lượng phải là số nguyên hợp lệ");
                }
                String maHoaDon = getInputValue(inputFields, 3);
                if (((HOA_DON_BUS) busInstances.get("HOA_DON")).getHoaDonById(maHoaDon) == null) {
                    throw new IllegalArgumentException("Mã hóa đơn không tồn tại");
                }
                String maSanPham = getInputValue(inputFields, 5);
                if (((MENU_BUS) busInstances.get("MENU")).getMenuById(maSanPham) == null) {
                    throw new IllegalArgumentException("Mã sản phẩm không tồn tại");
                }
                break;
            case "MENU":
                String donGia = ((JTextField) inputFields.get(9)).getText().trim();
                try {
                    double donGiaValue = Double.parseDouble(donGia);
                    if (donGiaValue <= 0) {
                        throw new IllegalArgumentException("Đơn giá phải lớn hơn 0");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Đơn giá phải là số hợp lệ");
                }
                String tenSanPham = ((JTextField) inputFields.get(3)).getText().trim();
                if (tenSanPham.isEmpty()) {
                    throw new IllegalArgumentException("Tên sản phẩm không được để trống");
                }
                String trangThaiMenu = ((JTextField) inputFields.get(11)).getText().trim().toLowerCase();
                if (!trangThaiMenu.equals("true") && !trangThaiMenu.equals("false")) {
                    throw new IllegalArgumentException("Trạng thái phải là true hoặc false");
                }
                String maLoaiSanPham = getInputValue(inputFields, 5);
                if (((LOAI_SAN_PHAM_BUS) busInstances.get("LOAI_SAN_PHAM")).getLoaiSanPhamById(maLoaiSanPham) == null) {
                    throw new IllegalArgumentException("Mã loại sản phẩm không tồn tại");
                }
                String maPhuongXa = getInputValue(inputFields, 7);
                if (((PHUONG_XA_BUS) busInstances.get("PHUONG_XA")).getPhuongXaById(maPhuongXa) == null) {
                    throw new IllegalArgumentException("Mã phường/xã không tồn tại");
                }
                break;
            case "KHU_VUC":
                String tenKhuVuc = ((JTextField) inputFields.get(5)).getText().trim();
                if (tenKhuVuc.isEmpty()) {
                    throw new IllegalArgumentException("Tên khu vực không được để trống");
                }
                String maTang = getInputValue(inputFields, 3);
                if (((TANG_BUS) busInstances.get("TANG")).getTANGById(maTang) == null) {
                    throw new IllegalArgumentException("Mã tầng không tồn tại");
                }
                break;
            case "LOAI_BAN":
                String tenLoaiBan = ((JTextField) inputFields.get(3)).getText().trim();
                if (tenLoaiBan.isEmpty()) {
                    throw new IllegalArgumentException("Tên loại bàn không được để trống");
                }
                break;
            case "PHUONG_XA":
                String tenPhuongXa = ((JTextField) inputFields.get(3)).getText().trim();
                if (tenPhuongXa.isEmpty()) {
                    throw new IllegalArgumentException("Tên phường/xã không được để trống");
                }
                break;
            case "LOAI_SAN_PHAM":
                String tenLoaiSanPham = ((JTextField) inputFields.get(3)).getText().trim();
                if (tenLoaiSanPham.isEmpty()) {
                    throw new IllegalArgumentException("Tên loại sản phẩm không được để trống");
                }
                break;
            case "TANG":
                String tenTang = ((JTextField) inputFields.get(3)).getText().trim();
                if (tenTang.isEmpty()) {
                    throw new IllegalArgumentException("Tên tầng không được để trống");
                }
                break;
            case "BAN_SAN_PHAM":
                String maBanBSP = getInputValue(inputFields, 1);
                if (((BAN_BUS) busInstances.get("BAN")).getBanById(maBanBSP) == null) {
                    throw new IllegalArgumentException("Mã bàn không tồn tại");
                }
                String maSanPhamBSP = getInputValue(inputFields, 3);
                if (((MENU_BUS) busInstances.get("MENU")).getMenuById(maSanPhamBSP) == null) {
                    throw new IllegalArgumentException("Mã sản phẩm không tồn tại");
                }
                break;
        }
    }

    private String getInputValue(List<JComponent> inputFields, int index) {
        JComponent field = inputFields.get(index);
        if (field instanceof JComboBox) {
            return (String) ((JComboBox<?>) field).getSelectedItem();
        } else {
            return ((JTextField) field).getText().trim();
        }
    }

    private List<JComponent> createInputFields(String[] labels, String[] hints) throws SQLException {
        List<JComponent> fields = new ArrayList<>();
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i] + ":");
            JComponent field;
            if (labels[i].equals("MaLoaiBan_ID") || labels[i].equals("MaKhuVuc_ID") || labels[i].equals("MaTang_ID") ||
                labels[i].equals("MaPhuongXa_ID") || labels[i].equals("MaLoaiSanPham_ID") || labels[i].equals("MaBan_ID") ||
                labels[i].equals("MaHoaDon_ID") || labels[i].equals("MaSanPham_ID")) {
                List<String> ids = getForeignKeyValues(labels[i]);
                field = new JComboBox<>(ids.isEmpty() ? new String[]{""} : ids.toArray(new String[0]));
            } else {
                field = new JTextField(15);
                ((JTextField) field).setToolTipText(hints[i]);
            }
            fields.add(label);
            fields.add(field);
        }
        return fields;
    }

    private List<String> getForeignKeyValues(String field) throws SQLException {
        switch (field) {
            case "MaLoaiBan_ID":
                return ((LOAI_BAN_BUS) busInstances.get("LOAI_BAN")).getAllLoaiBan()
                    .stream().map(LOAI_BAN::getMaLoaiBan_ID).toList();
            case "MaKhuVuc_ID":
                return ((KHU_VUC_BUS) busInstances.get("KHU_VUC")).getAllKhuVuc()
                    .stream().map(KHU_VUC::getMaKhuVuc_ID).toList();
            case "MaTang_ID":
                return ((TANG_BUS) busInstances.get("TANG")).getAllTANG()
                    .stream().map(TANG::getMaTang_ID).toList();
            case "MaPhuongXa_ID":
                return ((PHUONG_XA_BUS) busInstances.get("PHUONG_XA")).getAllPhuongXa()
                    .stream().map(PHUONG_XA::getMaPhuongXa_ID).toList();
            case "MaLoaiSanPham_ID":
                return ((LOAI_SAN_PHAM_BUS) busInstances.get("LOAI_SAN_PHAM")).getAllLoaiSanPham()
                    .stream().map(LOAI_SAN_PHAM::getMaLoaiSanPham_ID).toList();
            case "MaBan_ID":
                return ((BAN_BUS) busInstances.get("BAN")).getAllBan()
                    .stream().map(BAN::getMaBan_ID).toList();
            case "MaHoaDon_ID":
                return ((HOA_DON_BUS) busInstances.get("HOA_DON")).getAllHoaDon()
                    .stream().map(HOA_DON::getMaHoaDon_ID).toList();
            case "MaSanPham_ID":
                return ((MENU_BUS) busInstances.get("MENU")).getAllMenu()
                    .stream().map(MENU::getMaSanPham_ID).toList();
            default:
                return new ArrayList<>();
        }
    }

    private void updateTableRow(int selectedRow, Object[] newData) {
        for (int i = 0; i < newData.length; i++) {
            tableModel.setValueAt(newData[i], selectedRow, i);
        }
    }

    private class AddDialog extends JDialog {
        private List<JComponent> inputFields;
        private String table;

        public AddDialog(JFrame parent, String table) throws SQLException {
            super(parent, "Thêm mới " + table, true);
            this.table = table;
            setSize(400, 300);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());

            // Panel nhập liệu
            JPanel addPanel = new JPanel();
            addPanel.setLayout(new GridLayout(0, 2, 5, 5));
            inputFields = new ArrayList<>();

            // Tạo các trường nhập liệu dựa trên bảng
            String[] labels = getColumnNames(table);
            String[] hints = getHints(table);
            inputFields.addAll(createInputFields(labels, hints));

            // Vô hiệu hóa trường khóa chính
            JComponent primaryKeyField = inputFields.get(1); // Trường khóa chính (index 1)
            primaryKeyField.setEnabled(false);
            if (primaryKeyField instanceof JTextField) {
                ((JTextField) primaryKeyField).setToolTipText("Khóa chính tự động tăng");
            }
            
            if (table.equals("HOA_DON")) {
                JTextField trangThaiField = (JTextField) inputFields.get(9);
                trangThaiField.setText("Chưa thanh toán");
            }

            for (JComponent field : inputFields) {
                addPanel.add(field);
            }

            // Panel nút
            JPanel buttonPanel = new JPanel();
            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Cancel");
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);

            // Sự kiện nút
            okButton.addActionListener(e -> handleOk());
            cancelButton.addActionListener(e -> dispose());

            add(addPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private String[] getHints(String table) {
            switch (table) {
                case "BAN":
                    return new String[]{"Tự động tăng", "", "", "", "true/false"};
                case "HOA_DON":
                    return new String[]{"Tự động tăng", "", "0.0", "YYYY-MM-DD", "'Chưa thanh toán' hoặc 'Đã thanh toán'"};
                case "HOA_DON_CHI_TIET":
                    return new String[]{"Tự động tăng", "", "", "0.0", "0"};
                case "KHU_VUC":
                    return new String[]{"Tự động tăng", "", "", ""};
                case "LOAI_BAN":
                    return new String[]{"Tự động tăng", "", ""};
                case "MENU":
                    return new String[]{"Tự động tăng", "", "", "", "0.0", "true/false"};
                case "PHUONG_XA":
                    return new String[]{"Tự động tăng", "", ""};
                case "BAN_SAN_PHAM":
                    return new String[]{"", ""};
                case "LOAI_SAN_PHAM":
                    return new String[]{"Tự động tăng", ""};
                case "TANG":
                    return new String[]{"Tự động tăng", "", ""};
                default:
                    return new String[]{};
            }
        }

        private void handleOk() {
            try {
                validateInput(inputFields, table);
                switch (table) {
                    case "BAN":
                        BAN ban = new BAN(
                            null, // Khóa chính tự động tăng
                            getInputValue(inputFields, 3), // MaLoaiBan_ID
                            getInputValue(inputFields, 5), // MaKhuVuc_ID
                            getInputValue(inputFields, 7), // TenBan
                            Boolean.parseBoolean(getInputValue(inputFields, 9)) // TrangThai
                        );
                        ((BAN_BUS) busInstances.get("BAN")).addBan(ban);
                        break;
                    case "HOA_DON":
                        String dateInput = getInputValue(inputFields, 7);
                        if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", dateInput)) {
                            throw new IllegalArgumentException("Ngày bán phải có định dạng YYYY-MM-DD (ví dụ: 2025-04-18)");
                        }
                        HOA_DON hoaDon = new HOA_DON(
                            null,
                            getInputValue(inputFields, 3),
                            Double.parseDouble(getInputValue(inputFields, 5)),
                            java.sql.Date.valueOf(dateInput),
                            getInputValue(inputFields, 9)
                        );
                        ((HOA_DON_BUS) busInstances.get("HOA_DON")).addHoaDon(hoaDon, new ArrayList<>());
                        break;
                    case "HOA_DON_CHI_TIET":
                        HOA_DON_CHI_TIET chiTiet = new HOA_DON_CHI_TIET(
                            null, // Khóa chính tự động tăng
                            getInputValue(inputFields, 3), // MaHoaDon_ID
                            getInputValue(inputFields, 5), // MaSanPham_ID
                            Double.parseDouble(getInputValue(inputFields, 7)), // DonGia
                            Integer.parseInt(getInputValue(inputFields, 9)) // SoLuong
                        );
                        ((HOA_DON_CHI_TIET_BUS) busInstances.get("HOA_DON_CHI_TIET")).addChiTiet(chiTiet);
                        break;
                    case "KHU_VUC":
                        KHU_VUC khuVuc = new KHU_VUC(
                            null, // Khóa chính tự động tăng
                            getInputValue(inputFields, 3), // MaTang_ID
                            getInputValue(inputFields, 5), // TenKhuVuc
                            getInputValue(inputFields, 7) // MoTa
                        );
                        ((KHU_VUC_BUS) busInstances.get("KHU_VUC")).addKhuVuc(khuVuc);
                        break;
                    case "LOAI_BAN":
                        LOAI_BAN loaiBan = new LOAI_BAN(
                            null, // Khóa chính tự động tăng
                            getInputValue(inputFields, 3), // TenLoaiBan
                            getInputValue(inputFields, 5) // MoTa
                        );
                        ((LOAI_BAN_BUS) busInstances.get("LOAI_BAN")).addLoaiBan(loaiBan);
                        break;
                    case "MENU":
                        MENU menu = new MENU(
                            null, // Khóa chính tự động tăng
                            getInputValue(inputFields, 3), // TenSanPham
                            getInputValue(inputFields, 5), // MaLoaiSanPham_ID
                            getInputValue(inputFields, 7), // MaPhuongXa_ID
                            Double.parseDouble(getInputValue(inputFields, 9)), // DonGia
                            Boolean.parseBoolean(getInputValue(inputFields, 11)) // TrangThai
                        );
                        ((MENU_BUS) busInstances.get("MENU")).addMenu(menu);
                        break;
                    case "PHUONG_XA":
                        PHUONG_XA phuongXa = new PHUONG_XA(
                            null, // Khóa chính tự động tăng
                            getInputValue(inputFields, 3), // TenPhuongXa
                            getInputValue(inputFields, 5) // MoTa
                        );
                        ((PHUONG_XA_BUS) busInstances.get("PHUONG_XA")).addPhuongXa(phuongXa);
                        break;
                    case "BAN_SAN_PHAM":
                        BAN_SAN_PHAM banSanPham = new BAN_SAN_PHAM(
                            getInputValue(inputFields, 1), // MaBan_ID
                            getInputValue(inputFields, 3) // MaSanPham_ID
                        );
                        ((BAN_SAN_PHAM_BUS) busInstances.get("BAN_SAN_PHAM")).addBanSanPham(banSanPham);
                        break;
                    case "LOAI_SAN_PHAM":
                        LOAI_SAN_PHAM loaiSanPham = new LOAI_SAN_PHAM(
                            null, // Khóa chính tự động tăng
                            getInputValue(inputFields, 3) // TenLoaiSanPham
                        );
                        ((LOAI_SAN_PHAM_BUS) busInstances.get("LOAI_SAN_PHAM")).addLoaiSanPham(loaiSanPham);
                        break;
                    case "TANG":
                        TANG tang = new TANG(
                            null, // Khóa chính tự động tăng
                            getInputValue(inputFields, 3), // TenTang
                            getInputValue(inputFields, 5) // MoTa
                        );
                        ((TANG_BUS) busInstances.get("TANG")).addTANG(tang);
                        break;
                }
                JOptionPane.showMessageDialog(this, "Thêm bản ghi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                updateTableData();
                dispose();
            } catch (SQLException e) {
                String message = e.getMessage().contains("không tồn tại")
                    ? "Dữ liệu không hợp lệ: " + e.getMessage()
                    : "Lỗi cơ sở dữ liệu: " + e.getMessage();
                JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi không xác định: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class EditDialog extends JDialog {
        private List<JComponent> inputFields;
        private String table;
        private int row;

        public EditDialog(JFrame parent, String table, int row) throws SQLException {
            super(parent, "Chỉnh sửa " + table, true);
            this.table = table;
            this.row = row;
            setSize(400, 300);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());

            // Panel nhập liệu
            JPanel editPanel = new JPanel();
            editPanel.setLayout(new GridLayout(0, 2, 5, 5));
            inputFields = new ArrayList<>();

            // Tạo các trường nhập liệu dựa trên bảng
            String[] labels = getColumnNames(table);
            String[] hints = getHints(table);
            inputFields.addAll(createInputFields(labels, hints));

            // Điền dữ liệu từ hàng được chọn
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                Object value = tableModel.getValueAt(row, i);
                JComponent field = inputFields.get(i * 2 + 1);
                if (field instanceof JTextField) {
                    ((JTextField) field).setText(value != null ? value.toString() : "");
                } else if (field instanceof JComboBox) {
                    ((JComboBox<?>) field).setSelectedItem(value != null ? value.toString() : "");
                }
                if (i == 0) {
                    field.setEnabled(false); // Vô hiệu hóa khóa chính
                }
            }

            for (JComponent field : inputFields) {
                editPanel.add(field);
            }

            // Panel nút
            JPanel buttonPanel = new JPanel();
            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Cancel");
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);

            // Sự kiện nút
            okButton.addActionListener(e -> handleOk());
            cancelButton.addActionListener(e -> dispose());

            add(editPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private String[] getHints(String table) {
            switch (table) {
                case "BAN":
                    return new String[]{"", "", "", "", "true/false"};
                case "HOA_DON":
                    return new String[]{"", "", "0.0", "YYYY-MM-DD", "'Chưa thanh toán' hoặc 'Đã thanh toán'"};
                case "HOA_DON_CHI_TIET":
                    return new String[]{"", "", "", "0.0", "0"};
                case "KHU_VUC":
                    return new String[]{"", "", "", ""};
                case "LOAI_BAN":
                    return new String[]{"", "", ""};
                case "MENU":
                    return new String[]{"", "", "", "", "0.0", "true/false"};
                case "PHUONG_XA":
                    return new String[]{"", "", ""};
                case "LOAI_SAN_PHAM":
                    return new String[]{"", ""};
                case "TANG":
                    return new String[]{"", "", ""};
                default:
                    return new String[]{};
            }
        }

        private void handleOk() {
            // Kiểm tra thay đổi dữ liệu
            boolean hasChanges = false;
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                String newValue = getInputValue(inputFields, i * 2 + 1);
                String oldValue = tableModel.getValueAt(row, i) != null ? tableModel.getValueAt(row, i).toString() : "";
                if (!newValue.equals(oldValue)) {
                    hasChanges = true;
                    break;
                }
            }
            if (!hasChanges) {
                JOptionPane.showMessageDialog(this, "Không có thay đổi để cập nhật!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                return;
            }

            try {
                validateInput(inputFields, table);
                switch (table) {
                    case "BAN":
                        BAN ban = new BAN(
                            getInputValue(inputFields, 1),
                            getInputValue(inputFields, 3),
                            getInputValue(inputFields, 5),
                            ((JTextField) inputFields.get(7)).getText(),
                            Boolean.parseBoolean(((JTextField) inputFields.get(9)).getText())
                        );
                        ((BAN_BUS) busInstances.get("BAN")).updateBan(ban);
                        updateTableRow(row, new Object[]{
                            ban.getMaBan_ID(), ban.getMaLoaiBan_ID(), ban.getMaKhuVuc_ID(),
                            ban.getTenBan(), ban.isTrangThai()
                        });
                        break;
                    case "HOA_DON":
                        String dateInput = getInputValue(inputFields, 7);
                        if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", dateInput)) {
                            throw new IllegalArgumentException("Ngày bán phải có định dạng YYYY-MM-DD (ví dụ: 2025-04-18)");
                        }
                        HOA_DON hoaDon = new HOA_DON(
                            getInputValue(inputFields, 1),
                            getInputValue(inputFields, 3),
                            Double.parseDouble(getInputValue(inputFields, 5)),
                            java.sql.Date.valueOf(dateInput),
                            getInputValue(inputFields, 9)
                        );
                        ((HOA_DON_BUS) busInstances.get("HOA_DON")).updateHoaDon(hoaDon);
                        updateTableRow(row, new Object[]{
                            hoaDon.getMaHoaDon_ID(), hoaDon.getMaBan_ID(), hoaDon.getTongTien(), 
                            hoaDon.getNgayBan(), hoaDon.getTrangThai()
                        });
                        break;
                    case "HOA_DON_CHI_TIET":
                        HOA_DON_CHI_TIET chiTiet = new HOA_DON_CHI_TIET(
                            getInputValue(inputFields, 1),
                            getInputValue(inputFields, 3),
                            getInputValue(inputFields, 5),
                            Double.parseDouble(((JTextField) inputFields.get(7)).getText()),
                            Integer.parseInt(((JTextField) inputFields.get(9)).getText())
                        );
                        ((HOA_DON_CHI_TIET_BUS) busInstances.get("HOA_DON_CHI_TIET")).updateChiTiet(chiTiet);
                        updateTableRow(row, new Object[]{
                            chiTiet.getMaHDCT_ID(), chiTiet.getMaHoaDon_ID(), chiTiet.getMaSanPham_ID(),
                            chiTiet.getDonGia(), chiTiet.getSoLuong()
                        });
                        break;
                    case "KHU_VUC":
                        KHU_VUC khuVuc = new KHU_VUC(
                            getInputValue(inputFields, 1),
                            getInputValue(inputFields, 3),
                            ((JTextField) inputFields.get(5)).getText(),
                            ((JTextField) inputFields.get(7)).getText()
                        );
                        ((KHU_VUC_BUS) busInstances.get("KHU_VUC")).updateKhuVuc(khuVuc);
                        updateTableRow(row, new Object[]{
                            khuVuc.getMaKhuVuc_ID(), khuVuc.getMaTang_ID(), khuVuc.getTenKhuVuc(), khuVuc.getMoTa()
                        });
                        break;
                    case "LOAI_BAN":
                        LOAI_BAN loaiBan = new LOAI_BAN(
                            getInputValue(inputFields, 1),
                            ((JTextField) inputFields.get(3)).getText(),
                            ((JTextField) inputFields.get(5)).getText()
                        );
                        ((LOAI_BAN_BUS) busInstances.get("LOAI_BAN")).updateLoaiBan(loaiBan);
                        updateTableRow(row, new Object[]{
                            loaiBan.getMaLoaiBan_ID(), loaiBan.getTenLoaiBan(), loaiBan.getMoTa()
                        });
                        break;
                    case "MENU":
                        MENU menu = new MENU(
                            getInputValue(inputFields, 1),
                            ((JTextField) inputFields.get(3)).getText(),
                            getInputValue(inputFields, 5),
                            getInputValue(inputFields, 7),
                            Double.parseDouble(((JTextField) inputFields.get(9)).getText()),
                            Boolean.parseBoolean(((JTextField) inputFields.get(11)).getText())
                        );
                        ((MENU_BUS) busInstances.get("MENU")).updateMenu(menu);
                        updateTableRow(row, new Object[]{
                            menu.getMaSanPham_ID(), menu.getTenSanPham(), menu.getMaLoaiSanPham_ID(),
                            menu.getMaPhuongXa_ID(), menu.getDonGia(), menu.isTrangThai()
                        });
                        break;
                    case "PHUONG_XA":
                        PHUONG_XA phuongXa = new PHUONG_XA(
                            getInputValue(inputFields, 1),
                            ((JTextField) inputFields.get(3)).getText(),
                            ((JTextField) inputFields.get(5)).getText()
                        );
                        ((PHUONG_XA_BUS) busInstances.get("PHUONG_XA")).updatePhuongXa(phuongXa);
                        updateTableRow(row, new Object[]{
                            phuongXa.getMaPhuongXa_ID(), phuongXa.getTenPhuongXa(), phuongXa.getMoTa()
                        });
                        break;
                    case "LOAI_SAN_PHAM":
                        LOAI_SAN_PHAM loaiSanPham = new LOAI_SAN_PHAM(
                            getInputValue(inputFields, 1),
                            ((JTextField) inputFields.get(3)).getText()
                        );
                        ((LOAI_SAN_PHAM_BUS) busInstances.get("LOAI_SAN_PHAM")).updateLoaiSanPham(loaiSanPham);
                        updateTableRow(row, new Object[]{
                            loaiSanPham.getMaLoaiSanPham_ID(), loaiSanPham.getTenLoaiSanPham()
                        });
                        break;
                    case "TANG":
                        TANG tang = new TANG(
                            getInputValue(inputFields, 1),
                            ((JTextField) inputFields.get(3)).getText(),
                            ((JTextField) inputFields.get(5)).getText()
                        );
                        ((TANG_BUS) busInstances.get("TANG")).updateTANG(tang);
                        updateTableRow(row, new Object[]{
                            tang.getMaTang_ID(), tang.getTenTang(), tang.getMoTa()
                        });
                        break;
                }
                JOptionPane.showMessageDialog(this, "Sửa bản ghi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (SQLException e) {
                String message = e.getMessage().contains("không tồn tại")
                    ? "Dữ liệu không hợp lệ: " + e.getMessage()
                    : "Lỗi cơ sở dữ liệu: " + e.getMessage();
                JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi không xác định: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        CONNECTIONSQLSERVER connector = new CONNECTIONSQLSERVER();
        SwingUtilities.invokeLater(() -> new GUI_Nhap_Xuat(connector).setVisible(true));
    }
}