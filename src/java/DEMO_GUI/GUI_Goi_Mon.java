/*
 * GUI for ordering food and drinks
 */
package DEMO_GUI;

import BUSINESSLOGIC.*;
import CONNECTIONDATA.CONNECTIONSQLSERVER;
import CONNECTIONDATA.IConnector;
import SCHEMACLASS.*;
import SCHEMAOBJECT.BAN_DAO;
import SCHEMAOBJECT.HOA_DON_CHI_TIET_DAO;
import SCHEMAOBJECT.HOA_DON_DAO;
import SCHEMAOBJECT.MENU_DAO;
import SCHEMAOBJECT.PHUONG_XA_DAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;


public class GUI_Goi_Mon  extends JFrame{
    private JTabbedPane tabbedPane;
    private JPanel selectedItemsPanel;
    private DefaultListModel<String> selectedItemsModel;
    private JList<String> selectedItemsList;
    private JPanel dacSanPanel, nuocUongPanel;
    private JComboBox<String> phuongXaComboBox;
    private Map<String, MENU> menuItems;
    private Map<String, Integer> itemQuantities;
    private List<MENU> selectedMenus;
    private IConnector connector;
    private MENU_BUS menuBus;
    private PHUONG_XA_BUS phuongXaBus;
    private HOA_DON_BUS hoaDonBus;
    private HOA_DON_CHI_TIET_BUS chiTietBus;
    private BAN_BUS banBus;
    private String maBan;
    private String tenBan;
    private GUI_DS_BAN_LAN2 parentFrame;
    private String imagePathDrink;
    private String imagePathFood;
    private Map<String, String> imageMapping;
    private List<PHUONG_XA> phuongXas;

    public GUI_Goi_Mon(IConnector connector, String maBan, String tenBan, GUI_DS_BAN_LAN2 parentFrame) {
        this.connector = connector;
        this.maBan = maBan;
        this.tenBan = tenBan;
        this.parentFrame = parentFrame;
        setTitle("Gọi Món - Bàn: " + tenBan);
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

        // Khởi tạo BUS
        menuBus = new MENU_BUS(new MENU_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector));
        phuongXaBus = new PHUONG_XA_BUS(new PHUONG_XA_DAO(connector), new MENU_DAO(connector));
        hoaDonBus = new HOA_DON_BUS(new HOA_DON_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector));
        chiTietBus = new HOA_DON_CHI_TIET_BUS(new HOA_DON_CHI_TIET_DAO(connector));
        banBus = new BAN_BUS(new BAN_DAO(connector));
        menuItems = new HashMap<>();
        itemQuantities = new HashMap<>();
        selectedMenus = new ArrayList<>();

        // Định nghĩa imagePath
        imagePathDrink = "images/drinks/";
        imagePathFood = "images/food/";
        imageMapping = new HashMap<>();
imageMapping.put("SP001", "Cocacola.png");
imageMapping.put("SP002", "Pepsi.png");
imageMapping.put("SP003", "Fanta.png");
imageMapping.put("SP004", "Sprite.png");
imageMapping.put("SP005", "Mirinda.png");
imageMapping.put("SP006", "RedBull.png");
imageMapping.put("SP007", "Heineken.png");
imageMapping.put("SP008", "Tiger.png");
imageMapping.put("SP009", "Saigon.png");
imageMapping.put("SP010", "Budweiser.png");
imageMapping.put("SP011", "Corona.png");
imageMapping.put("SP012", "LauMamChauDoc.png");
imageMapping.put("SP013", "HaoLongSon.png");
imageMapping.put("SP014", "VitLangGiang.png");
imageMapping.put("SP015", "RauDonBacKan.png");
imageMapping.put("SP016", "BunNuocLeo.png");
imageMapping.put("SP017", "NemBui.png");
imageMapping.put("SP018", "BanhXeoOcGao.png");
imageMapping.put("SP019", "BunChaCaQuyNhon.png");
imageMapping.put("SP020", "GoiGaMangCut.png");
imageMapping.put("SP021", "CaLangNuongSongBe.png");
imageMapping.put("SP022", "BanhCanPhanThiet.png");
imageMapping.put("SP023", "TomTichNuong.png");
imageMapping.put("SP024", "BanhCongCanTho.png");
imageMapping.put("SP025", "KhauNhucCaoBang.png");
imageMapping.put("SP026", "MiQuang.png");
imageMapping.put("SP027", "GaNuongBanDon.png");
imageMapping.put("SP028", "CaSuoiKhoRieng.png");
imageMapping.put("SP029", "MangDang.png");
imageMapping.put("SP030", "GoiCaBienHoa.png");
imageMapping.put("SP031", "LauCaLinhBongDienDien.png");
imageMapping.put("SP032", "PhoKhoGiaLai.png");
imageMapping.put("SP033", "ChaoAuTau.png");
imageMapping.put("SP034", "BanhCuonChaNuong.png");
imageMapping.put("SP035", "BunThang.png");
imageMapping.put("SP036", "BanhBeoHaTinh.png");
imageMapping.put("SP037", "BanhDucHaiDuong.png");
imageMapping.put("SP038", "BanhDaCua.png");
imageMapping.put("SP039", "LauMamHauGiang.png");
imageMapping.put("SP040", "ComLam.png");
imageMapping.put("SP041", "NhongOngRung.png");
imageMapping.put("SP042", "BanhCanNhaTrang.png");
imageMapping.put("SP043", "CaNhamNuong.png");
imageMapping.put("SP044", "GoiLaKonTum.png");
imageMapping.put("SP045", "ReuDaNuong.png");
imageMapping.put("SP046", "DauDaLatTronKem.png");
imageMapping.put("SP047", "VitQuayLangSon.png");
imageMapping.put("SP048", "ThaoKe.png");
imageMapping.put("SP049", "LauMamLongAn.png");
imageMapping.put("SP050", "CheKho.png");
imageMapping.put("SP051", "ChaoLuonVinh.png");
imageMapping.put("SP052", "ComChayNinhBinh.png");
imageMapping.put("SP053", "NhoNinhThuan.png");
imageMapping.put("SP054", "BanhTai.png");
imageMapping.put("SP055", "ChaDong.png");
imageMapping.put("SP056", "BanhLoc.png");
imageMapping.put("SP057", "CaoLauHoiAn.png");
imageMapping.put("SP058", "DonQuangNgai.png");
imageMapping.put("SP059", "ChaMucHaLong.png");
imageMapping.put("SP060", "BunHen.png");
imageMapping.put("SP061", "BanhPiaSocTrang.png");
imageMapping.put("SP062", "CaHoiMocChau.png");
imageMapping.put("SP063", "BanhTrangPhoiSuong.png");
imageMapping.put("SP064", "BunCaRo.png");
imageMapping.put("SP065", "CheKhoaiDeo.png");
imageMapping.put("SP066", "NemChuaThanhHoa.png");
imageMapping.put("SP067", "ComHen.png");
imageMapping.put("SP068", "HuTieuMyTho.png");
imageMapping.put("SP069", "ComTamSaiGon.png");
imageMapping.put("SP070", "BunNuocLeoTraVinh.png");
imageMapping.put("SP071", "CaChienSongLo.png");
imageMapping.put("SP072", "BanhXeoVinhLong.png");
imageMapping.put("SP073", "SuSuTamDaoXaoToi.png");
imageMapping.put("SP074", "MangVauYenBai.png");

        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(33, 150, 243));
        JLabel titleLabel = new JLabel("GỌI MÓN - " + tenBan);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel bên trái: Danh sách món đã chọn
        selectedItemsPanel = new JPanel(new BorderLayout());
        selectedItemsPanel.setBackground(Color.WHITE);
        selectedItemsPanel.setPreferredSize(new Dimension(300, 0));
        selectedItemsPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JLabel selectedTitle = new JLabel("MÓN ĐÃ CHỌN");
        selectedTitle.setFont(new Font("Arial", Font.BOLD, 16));
        selectedTitle.setHorizontalAlignment(SwingConstants.CENTER);
        selectedTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        selectedItemsModel = new DefaultListModel<>();
        selectedItemsList = new JList<>(selectedItemsModel);
        selectedItemsList.setFont(new Font("Arial", Font.PLAIN, 14));
        selectedItemsPanel.add(selectedTitle, BorderLayout.NORTH);
        selectedItemsPanel.add(new JScrollPane(selectedItemsList), BorderLayout.CENTER);

        // Panel nút điều khiển
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.setBackground(Color.WHITE);
        JButton minusButton = new JButton("-");
        minusButton.setFont(new Font("Arial", Font.BOLD, 14));
        JButton plusButton = new JButton("+");
        plusButton.setFont(new Font("Arial", Font.BOLD, 14));
        JButton removeButton = new JButton("Xóa");
        removeButton.setFont(new Font("Arial", Font.BOLD, 14));
        removeButton.setBackground(new Color(244, 67, 54));
        removeButton.setForeground(Color.WHITE);
        JButton confirmButton = new JButton("Xác nhận");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(76, 175, 80));
        confirmButton.setForeground(Color.WHITE);
        JButton cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(158, 158, 158));
        cancelButton.setForeground(Color.WHITE);
        controlPanel.add(minusButton);
        controlPanel.add(plusButton);
        controlPanel.add(removeButton);
        controlPanel.add(confirmButton);
        controlPanel.add(cancelButton);
        selectedItemsPanel.add(controlPanel, BorderLayout.SOUTH);

        // Panel bên phải: TabbedPane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 16));
        tabbedPane.setBackground(new Color(240, 240, 240));
        dacSanPanel = new JPanel(new BorderLayout());
        dacSanPanel.setBackground(Color.WHITE);
        nuocUongPanel = new JPanel(new BorderLayout());
        nuocUongPanel.setBackground(Color.WHITE);
        tabbedPane.addTab("ĐẶC SẢN", new JScrollPane(dacSanPanel));
        tabbedPane.addTab("NƯỚC UỐNG", new JScrollPane(nuocUongPanel));

        mainPanel.add(selectedItemsPanel, BorderLayout.WEST);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Khởi tạo danh sách phường/xã
        try {
            phuongXas = phuongXaBus.getAllPhuongXa();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải phường/xã: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        // Khởi tạo giao diện
        updateDacSanPanel();
        updateNuocUongPanel();

        // Sự kiện nút
        removeButton.addActionListener(e -> handleRemoveSelectedItem());
        plusButton.addActionListener(e -> handleChangeQuantity(true));
        minusButton.addActionListener(e -> handleChangeQuantity(false));
        confirmButton.addActionListener(e -> handleConfirm());
        cancelButton.addActionListener(e -> dispose());
    }

    private void updateDacSanPanel() {
        dacSanPanel.removeAll();
        dacSanPanel.setLayout(new BorderLayout());
        dacSanPanel.setBackground(Color.WHITE);

        // Panel tìm kiếm phường/xã
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        JLabel searchLabel = new JLabel("Phường/Xã:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        searchPanel.add(searchLabel);
        phuongXaComboBox = new JComboBox<>();
        phuongXaComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        phuongXaComboBox.addItem("Tất cả");
        for (PHUONG_XA px : phuongXas) {
            phuongXaComboBox.addItem(px.getTenPhuongXa() + " (" + px.getMaPhuongXa_ID() + ")");
        }
        searchPanel.add(phuongXaComboBox);
        dacSanPanel.add(searchPanel, BorderLayout.NORTH);

        // Panel hiển thị món
        JPanel itemsPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        itemsPanel.setBackground(Color.WHITE);
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        dacSanPanel.add(scrollPane, BorderLayout.CENTER);

        phuongXaComboBox.addActionListener(e -> loadDacSanItems(itemsPanel));
        loadDacSanItems(itemsPanel);

        dacSanPanel.revalidate();
        dacSanPanel.repaint();
    }

    private void loadDacSanItems(JPanel itemsPanel) {
        itemsPanel.removeAll();
        menuItems.clear();
        String selectedPhuongXa = (String) phuongXaComboBox.getSelectedItem();
        String maPhuongXa = selectedPhuongXa.equals("Tất cả") ? null : selectedPhuongXa.split("\\(")[1].replace(")", "");

        try {
            List<MENU> menus = menuBus.getMenusByLoaiAndPhuongXa("LSP%", maPhuongXa);
            for (MENU menu : menus) {
                itemsPanel.add(createMenuItemPanel(menu));
                menuItems.put(menu.getMaSanPham_ID(), menu);
                itemQuantities.putIfAbsent(menu.getMaSanPham_ID(), 0);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải món: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    private void updateNuocUongPanel() {
        nuocUongPanel.removeAll();
        nuocUongPanel.setLayout(new BorderLayout());
        nuocUongPanel.setBackground(Color.WHITE);

        JPanel itemsPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        itemsPanel.setBackground(Color.WHITE);
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        nuocUongPanel.add(scrollPane, BorderLayout.CENTER);

        try {
            List<MENU> menus = menuBus.getMenusByLoaiAndPhuongXa("LSP01", null);
            for (MENU menu : menus) {
                itemsPanel.add(createMenuItemPanel(menu));
                menuItems.put(menu.getMaSanPham_ID(), menu);
                itemQuantities.putIfAbsent(menu.getMaSanPham_ID(), 0);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải nước uống: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        nuocUongPanel.revalidate();
        nuocUongPanel.repaint();
    }

    private ImageIcon loadImage(MENU menu) {
        String fileName = imageMapping.getOrDefault(menu.getMaSanPham_ID(), menu.getMaSanPham_ID() + ".png");
        String basePath = menu.getMaLoaiSanPham_ID().equals("LSP01") ? imagePathDrink : imagePathFood;
        String resourcePath = basePath + fileName;
        URL imageUrl = getClass().getClassLoader().getResource(resourcePath);
        ImageIcon icon;
        if (imageUrl != null) {
            icon = new ImageIcon(imageUrl);
        } else {
            String fullImagePath = basePath + fileName;
            File imageFile = new File(fullImagePath);
            if (imageFile.exists()) {
                icon = new ImageIcon(fullImagePath);
            } else {
                String defaultResourcePath = menu.getMaLoaiSanPham_ID().equals("LSP01") ? imagePathDrink + "default_drink.png" : imagePathFood + "default_food.png";
                URL defaultImageUrl = getClass().getClassLoader().getResource(defaultResourcePath);
                if (defaultImageUrl != null) {
                    icon = new ImageIcon(defaultImageUrl);
                } else {
                    String defaultImagePath = menu.getMaLoaiSanPham_ID().equals("LSP01") ? imagePathDrink + "default_drink.png" : imagePathFood + "default_food.png";
                    File defaultImageFile = new File(defaultImagePath);
                    icon = defaultImageFile.exists() ? new ImageIcon(defaultImagePath) : null;
                }
            }
        }
        if (icon != null) {
            Image image = icon.getImage();
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            int newWidth = 250;
            int newHeight = (int) ((double) height / width * newWidth);
            if (newHeight > 200) {
                newHeight = 200;
                newWidth = (int) ((double) width / height * newHeight);
            }
            Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }

    private JPanel createMenuItemPanel(MENU menu) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(300, 350));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        ImageIcon icon = loadImage(menu);
        if (icon != null) {
            imageLabel.setIcon(icon);
        } else {
            imageLabel.setText("Không có ảnh");
            imageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            imageLabel.setForeground(Color.GRAY);
        }

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel(menu.getTenSanPham());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel priceLabel = new JLabel("Giá: " + menu.getDonGia() + " VNĐ");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        priceLabel.setHorizontalAlignment(JLabel.CENTER);
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);

        panel.add(imageLabel, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);

        // Hiệu ứng hover
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(245, 245, 245));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                int currentQuantity = itemQuantities.getOrDefault(menu.getMaSanPham_ID(), 0);
                currentQuantity++;
                itemQuantities.put(menu.getMaSanPham_ID(), currentQuantity);
                updateSelectedItemsList(menu);
            }
        });

        return panel;
    }

    private void updateSelectedItemsList(MENU menu) {
        selectedItemsModel.clear();
        selectedMenus.clear();
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            String maSanPham = entry.getKey();
            int quantity = entry.getValue();
            if (quantity > 0) {
                MENU selectedMenu = menuItems.get(maSanPham);
                if (selectedMenu != null) {
                    selectedMenus.add(selectedMenu);
                    selectedItemsModel.addElement(selectedMenu.getTenSanPham() +
                            " - SL: " + quantity + " - Giá: " + (selectedMenu.getDonGia() * quantity) + " VNĐ");
                    total += selectedMenu.getDonGia() * quantity;
                }
            }
        }
        selectedItemsModel.addElement("Tổng tiền: " + total + " VNĐ");
    }

    private void handleRemoveSelectedItem() {
        int selectedIndex = selectedItemsList.getSelectedIndex();
        if (selectedIndex == -1 || selectedIndex >= selectedMenus.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        MENU menu = selectedMenus.get(selectedIndex);
        itemQuantities.put(menu.getMaSanPham_ID(), 0);
        updateSelectedItemsList(menu);
    }

    private void handleChangeQuantity(boolean increase) {
        int selectedIndex = selectedItemsList.getSelectedIndex();
        if (selectedIndex == -1 || selectedIndex >= selectedMenus.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món để thay đổi số lượng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        MENU menu = selectedMenus.get(selectedIndex);
        String maSanPham = menu.getMaSanPham_ID();
        int currentQuantity = itemQuantities.getOrDefault(maSanPham, 0);
        if (increase) {
            currentQuantity++;
        } else if (currentQuantity > 0) {
            currentQuantity--;
        }
        itemQuantities.put(maSanPham, currentQuantity);
        updateSelectedItemsList(menu);
    }

    private void handleConfirm() {
        if (selectedMenus.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một món!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            BAN ban = banBus.getBanById(maBan);
            if (ban == null) {
                JOptionPane.showMessageDialog(this, "Bàn không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String maHoaDon = hoaDonBus.getLatestMaHoaDonFromMaBan(maBan);
            boolean isNewHoaDon = (maHoaDon == null);
            if (!isNewHoaDon) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Bàn đã có hóa đơn " + maHoaDon + ". Thêm món vào hóa đơn này?",
                        "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
            } else {
                maHoaDon = hoaDonBus.generateMaHoaDon();
            }

            List<Map.Entry<String, Integer>> selectedItems = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
                if (entry.getValue() > 0) selectedItems.add(entry);
            }
            if (selectedItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có món nào hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double tongTien = 0.0;
            List<HOA_DON_CHI_TIET> chiTietList = new ArrayList<>();
            List<String> maChiTietList = chiTietBus.generateMultipleMaChiTiet(selectedItems.size());
            for (int i = 0; i < selectedItems.size(); i++) {
                Map.Entry<String, Integer> entry = selectedItems.get(i);
                String maSanPham = entry.getKey();
                int soLuong = entry.getValue();
                MENU menuItem = menuItems.get(maSanPham);
                if (menuItem == null || soLuong <= 0) continue;
                String maChiTiet = maChiTietList.get(i);
                double donGia = menuItem.getDonGia();
                chiTietList.add(new HOA_DON_CHI_TIET(maChiTiet, maHoaDon, maSanPham, donGia, soLuong));
                tongTien += donGia * soLuong;
            }

            if (isNewHoaDon) {
                HOA_DON hoaDon = new HOA_DON(maHoaDon, maBan, tongTien, new Date(System.currentTimeMillis()), "Chưa thanh toán");
                hoaDonBus.addHoaDon(hoaDon, chiTietList);
            } else {
                for (HOA_DON_CHI_TIET chiTiet : chiTietList) chiTietBus.addChiTiet(chiTiet);
                double newTongTien = chiTietBus.tinhTongTien(maHoaDon);
                HOA_DON hoaDon = hoaDonBus.getHoaDonById(maHoaDon);
                hoaDon.setTongTien(newTongTien);
                hoaDonBus.updateHoaDon(hoaDon);
            }

            if (!ban.isTrangThai()) banBus.datBan(maBan);
            JOptionPane.showMessageDialog(this, "Gọi món thành công! Mã hóa đơn: " + maHoaDon, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            if (parentFrame != null) {
                parentFrame.initializeData();
                parentFrame.updateTableGrid();
            }
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi SQL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        CONNECTIONSQLSERVER connector = new CONNECTIONSQLSERVER();
        SwingUtilities.invokeLater(() -> new GUI_Goi_Mon(connector, "B00001", "Bàn 1", null).setVisible(true));
    }
}