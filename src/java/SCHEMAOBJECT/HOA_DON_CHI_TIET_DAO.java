/*
 * Implementation of HOA_DON_CHI_TIET_DAO for CRUD operations on HOA_DON_CHI_TIET table
 */
package SCHEMAOBJECT;

import CONNECTIONDATA.IConnector;
import SCHEMACLASS.HOA_DON_CHI_TIET;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HOA_DON_CHI_TIET_DAO implements ICRUD<HOA_DON_CHI_TIET, String> {
    private final IConnector connector;

    public HOA_DON_CHI_TIET_DAO(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<HOA_DON_CHI_TIET> getAll() throws SQLException {
        List<HOA_DON_CHI_TIET> list = new ArrayList<>();
        String sql = "SELECT * FROM HOA_DON_CHI_TIET";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new HOA_DON_CHI_TIET(
                    rs.getString("MaHDCT_ID"),
                    rs.getString("MaHoaDon_ID"),
                    rs.getString("MaSanPham_ID"),
                    rs.getDouble("DonGia"),
                    rs.getInt("SoLuong")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách chi tiết hóa đơn: " + e.getMessage());
            throw e;
        }
        return list;
    }

    @Override
    public HOA_DON_CHI_TIET getById(String id) throws SQLException {
        String sql = "SELECT * FROM HOA_DON_CHI_TIET WHERE MaHDCT_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new HOA_DON_CHI_TIET(
                    rs.getString("MaHDCT_ID"),
                    rs.getString("MaHoaDon_ID"),
                    rs.getString("MaSanPham_ID"),
                    rs.getDouble("DonGia"),
                    rs.getInt("SoLuong")
                ) : null;
            }
        }
    }

    @Override
    public boolean insert(HOA_DON_CHI_TIET entity) throws SQLException {
        // Kiểm tra MaSanPham_ID
        String checkSql = "SELECT MaSanPham_ID FROM MENU WHERE MaSanPham_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, entity.getMaSanPham_ID());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Mã sản phẩm " + entity.getMaSanPham_ID() + " không tồn tại!");
                }
            }
        }

        String sql = "INSERT INTO HOA_DON_CHI_TIET (MaHDCT_ID, MaHoaDon_ID, MaSanPham_ID, DonGia, SoLuong) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getMaHDCT_ID());
            stmt.setString(2, entity.getMaHoaDon_ID());
            stmt.setString(3, entity.getMaSanPham_ID());
            stmt.setDouble(4, entity.getDonGia());
            stmt.setInt(5, entity.getSoLuong());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(HOA_DON_CHI_TIET entity) throws SQLException {
        String sql = "UPDATE HOA_DON_CHI_TIET SET MaHoaDon_ID = ?, MaSanPham_ID = ?, DonGia = ?, SoLuong = ? WHERE MaHDCT_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getMaHoaDon_ID());
            stmt.setString(2, entity.getMaSanPham_ID());
            stmt.setDouble(3, entity.getDonGia());
            stmt.setInt(4, entity.getSoLuong());
            stmt.setString(5, entity.getMaHDCT_ID());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM HOA_DON_CHI_TIET WHERE MaHDCT_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<HOA_DON_CHI_TIET> getByHoaDon(String maHoaDon) throws SQLException {
        List<HOA_DON_CHI_TIET> list = new ArrayList<>();
        String sql = "SELECT * FROM HOA_DON_CHI_TIET WHERE MaHoaDon_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new HOA_DON_CHI_TIET(
                        rs.getString("MaHDCT_ID"),
                        rs.getString("MaHoaDon_ID"),
                        rs.getString("MaSanPham_ID"),
                        rs.getDouble("DonGia"),
                        rs.getInt("SoLuong")
                    ));
                }
            }
        }
        return list;
    }

    public double tinhTongTien(String maHoaDon) throws SQLException {
        double tongTien = 0;
        String sql = "SELECT SUM(DonGia * SoLuong) FROM HOA_DON_CHI_TIET WHERE MaHoaDon_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tongTien = rs.getDouble(1);
                }
            }
        }
        return tongTien;
    }

    public List<HOA_DON_CHI_TIET> getBySanPham(String maSanPham) throws SQLException {
        List<HOA_DON_CHI_TIET> list = new ArrayList<>();
        String sql = "SELECT * FROM HOA_DON_CHI_TIET WHERE MaSanPham_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSanPham);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new HOA_DON_CHI_TIET(
                        rs.getString("MaHDCT_ID"),
                        rs.getString("MaHoaDon_ID"),
                        rs.getString("MaSanPham_ID"),
                        rs.getDouble("DonGia"),
                        rs.getInt("SoLuong")
                    ));
                }
            }
        }
        return list;
    }

    public boolean deleteByHoaDon(String maHoaDon) throws SQLException {
        String sql = "DELETE FROM HOA_DON_CHI_TIET WHERE MaHoaDon_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            return stmt.executeUpdate() > 0;
        }
    }

    public IConnector getConnector() {
        return this.connector;
    }
}