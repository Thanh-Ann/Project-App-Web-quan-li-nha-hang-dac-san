/*
 * Implementation of MENU_DAO for CRUD operations on MENU table
 */
package SCHEMAOBJECT;

import CONNECTIONDATA.IConnector;
import SCHEMACLASS.MENU;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MENU_DAO implements ICRUD<MENU, String> {
    private final IConnector connector;

    public MENU_DAO(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<MENU> getAll() throws SQLException {
        List<MENU> list = new ArrayList<>();
        String sql = "SELECT * FROM MENU";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new MENU(
                    rs.getString("MaSanPham_ID"),
                    rs.getString("TenSanPham"),
                    rs.getString("MaLoaiSanPham_ID"),
                    rs.getString("MaPhuongXa_ID"),
                    rs.getDouble("DonGia"),
                    rs.getBoolean("TrangThai")
                ));
            }
        }catch (Exception ex) {
            
        }
        return list;
    }

    @Override
    public MENU getById(String id) throws SQLException {
        String sql = "SELECT * FROM MENU WHERE MaSanPham_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new MENU(
                    rs.getString("MaSanPham_ID"),
                    rs.getString("TenSanPham"),
                    rs.getString("MaLoaiSanPham_ID"),
                    rs.getString("MaPhuongXa_ID"),
                    rs.getDouble("DonGia"),
                    rs.getBoolean("TrangThai")
                ) : null;
            }
        }
    }

    @Override
    public boolean insert(MENU entity) throws SQLException {
        String sql = "INSERT INTO MENU (MaSanPham_ID, TenSanPham, MaLoaiSanPham_ID, MaPhuongXa_ID, DonGia, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getMaSanPham_ID());
            stmt.setString(2, entity.getTenSanPham());
            stmt.setString(3, entity.getMaLoaiSanPham_ID());
            stmt.setString(4, entity.getMaPhuongXa_ID());
            stmt.setDouble(5, entity.getDonGia());
            stmt.setBoolean(6, entity.isTrangThai());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(MENU entity) throws SQLException {
        String sql = "UPDATE MENU SET TenSanPham = ?, MaLoaiSanPham_ID = ?, MaPhuongXa_ID = ?, DonGia = ?, TrangThai = ? WHERE MaSanPham_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getTenSanPham());
            stmt.setString(2, entity.getMaLoaiSanPham_ID());
            stmt.setString(3, entity.getMaPhuongXa_ID());
            stmt.setDouble(4, entity.getDonGia());
            stmt.setBoolean(5, entity.isTrangThai());
            stmt.setString(6, entity.getMaSanPham_ID());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM MENU WHERE MaSanPham_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<MENU> getByLoaiSanPham(String maLoai) throws SQLException {
        List<MENU> list = new ArrayList<>();
        String sql = "SELECT * FROM MENU WHERE MaLoaiSanPham_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maLoai);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new MENU(
                        rs.getString("MaSanPham_ID"),
                        rs.getString("TenSanPham"),
                        rs.getString("MaLoaiSanPham_ID"),
                        rs.getString("MaPhuongXa_ID"),
                        rs.getDouble("DonGia"),
                        rs.getBoolean("TrangThai")
                    ));
                }
            }
        }
        return list;
    }
    
    public List<MENU> getMenusByLoaiAndPhuongXa(String maLoai, String maPhuongXa) throws SQLException {
    String sql = "SELECT * FROM MENU WHERE MaLoaiSanPham_ID LIKE ? AND (MaPhuongXa_ID = ? OR ? IS NULL)";
    try (Connection conn = connector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, maLoai);
        stmt.setString(2, maPhuongXa);
        stmt.setString(3, maPhuongXa);
        try (ResultSet rs = stmt.executeQuery()) {
            List<MENU> menus = new ArrayList<>();
            while (rs.next()) {
                menus.add(new MENU(
                   rs.getString("MaSanPham_ID"),
                        rs.getString("TenSanPham"),
                        rs.getString("MaLoaiSanPham_ID"),
                        rs.getString("MaPhuongXa_ID"),
                        rs.getDouble("DonGia"),
                        rs.getBoolean("TrangThai")
                ));
            }
            return menus;
        }
    }
}

    public List<MENU> searchByName(String keyword) throws SQLException {
        List<MENU> list = new ArrayList<>();
        String sql = "SELECT * FROM MENU WHERE TenSanPham LIKE ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new MENU(
                        rs.getString("MaSanPham_ID"),
                        rs.getString("TenSanPham"),
                        rs.getString("MaLoaiSanPham_ID"),
                        rs.getString("MaPhuongXa_ID"),
                        rs.getDouble("DonGia"),
                        rs.getBoolean("TrangThai")
                    ));
                }
            }
        }
        return list;
    }
    
    public List<MENU> getByPhuongXa(String maPhuongXa) throws SQLException {
    List<MENU> list = new ArrayList<>();
    String sql = "SELECT * FROM MENU WHERE MaPhuongXa_ID = ?";
    
    try (Connection conn = connector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, maPhuongXa);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new MENU(
                    rs.getString("MaSanPham_ID"),
                    rs.getString("TenSanPham"),
                    rs.getString("MaLoaiSanPham_ID"),
                    rs.getString("MaPhuongXa_ID"),
                    rs.getDouble("DonGia"),
                    rs.getBoolean("TrangThai")
                ));
            }
        }
    }
    return list;
}
    
    public IConnector getConnector() {
        return this.connector;
    }
}