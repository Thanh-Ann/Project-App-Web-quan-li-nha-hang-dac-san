/*
 * Implementation of LOAI_SAN_PHAM_DAO for CRUD operations on LOAI_SAN_PHAM table
 */
package SCHEMAOBJECT;

import CONNECTIONDATA.IConnector;
import SCHEMACLASS.LOAI_SAN_PHAM;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LOAI_SAN_PHAM_DAO implements ICRUD<LOAI_SAN_PHAM, String> {
    private final IConnector connector;

    public LOAI_SAN_PHAM_DAO(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<LOAI_SAN_PHAM> getAll() throws SQLException {
        List<LOAI_SAN_PHAM> list = new ArrayList<>();
        String sql = "SELECT * FROM LOAI_SAN_PHAM";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new LOAI_SAN_PHAM(
                    rs.getString("MaLoaiSanPham_ID"),
                    rs.getString("TenLoaiSanPham")
                ));
            }
        }catch (Exception ex) {
            
        }
        return list;
    }

    @Override
    public LOAI_SAN_PHAM getById(String id) throws SQLException {
        String sql = "SELECT * FROM LOAI_SAN_PHAM WHERE MaLoaiSanPham_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new LOAI_SAN_PHAM(
                    rs.getString("MaLoaiSanPham_ID"),
                    rs.getString("TenLoaiSanPham")
                ) : null;
            }
        }
    }

    @Override
    public boolean insert(LOAI_SAN_PHAM entity) throws SQLException {
        String sql = "INSERT INTO LOAI_SAN_PHAM (MaLoaiSanPham_ID, TenLoaiSanPham) VALUES (?, ?)";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getMaLoaiSanPham_ID());
            stmt.setString(2, entity.getTenLoaiSanPham());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(LOAI_SAN_PHAM entity) throws SQLException {
        String sql = "UPDATE LOAI_SAN_PHAM SET TenLoaiSanPham = ? WHERE MaLoaiSanPham_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getTenLoaiSanPham());
            stmt.setString(2, entity.getMaLoaiSanPham_ID());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM LOAI_SAN_PHAM WHERE MaLoaiSanPham_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public IConnector getConnector() {
        return this.connector;
    }
}