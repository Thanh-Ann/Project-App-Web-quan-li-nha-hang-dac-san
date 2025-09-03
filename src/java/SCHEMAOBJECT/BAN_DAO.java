/*
 * Implementation of BAN_DAO for CRUD operations on BAN table
 */
package SCHEMAOBJECT;

import CONNECTIONDATA.IConnector;
import SCHEMACLASS.BAN;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BAN_DAO implements ICRUD<BAN, String> {
    private final IConnector connector ;

    public BAN_DAO(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<BAN> getAll() throws SQLException {
        List<BAN> list = new ArrayList<>();
        String sql = "SELECT * FROM BAN";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new BAN(
                    rs.getString("MaBan_ID"),
                    rs.getString("MaLoaiBan_ID"),
                    rs.getString("MaKhuVuc_ID"),
                    rs.getString("TenBan"),
                    rs.getBoolean("TrangThai")
                ));
            }
        } catch (Exception ex) {
            
        }
        return list;
    }

    @Override
    public BAN getById(String id) throws SQLException {
        String sql = "SELECT * FROM BAN WHERE MaBan_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new BAN(
                    rs.getString("MaBan_ID"),
                    rs.getString("MaLoaiBan_ID"),
                    rs.getString("MaKhuVuc_ID"),
                    rs.getString("TenBan"),
                    rs.getBoolean("TrangThai")
                ) : null;
            }
        }
    }

    @Override
    public boolean insert(BAN entity) throws SQLException {
        String sql = "INSERT INTO BAN (MaBan_ID, MaLoaiBan_ID, MaKhuVuc_ID, TenBan, TrangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getMaBan_ID());
            stmt.setString(2, entity.getMaLoaiBan_ID());
            stmt.setString(3, entity.getMaKhuVuc_ID());
            stmt.setString(4, entity.getTenBan());
            stmt.setBoolean(5, entity.isTrangThai());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(BAN entity) throws SQLException {
        String sql = "UPDATE BAN SET MaLoaiBan_ID = ?, MaKhuVuc_ID = ?, TenBan = ?, TrangThai = ? WHERE MaBan_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getMaLoaiBan_ID());
            stmt.setString(2, entity.getMaKhuVuc_ID());
            stmt.setString(3, entity.getTenBan());
            stmt.setBoolean(4, entity.isTrangThai());
            stmt.setString(5, entity.getMaBan_ID());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM BAN WHERE MaBan_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<BAN> getByKhuVuc(String maKhuVuc) throws SQLException {
        List<BAN> list = new ArrayList<>();
        String sql = "SELECT * FROM BAN WHERE MaKhuVuc_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maKhuVuc);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new BAN(
                        rs.getString("MaBan_ID"),
                        rs.getString("MaLoaiBan_ID"),
                        rs.getString("MaKhuVuc_ID"),
                        rs.getString("TenBan"),
                        rs.getBoolean("TrangThai")
                    ));
                }
            }
        }
        return list;
    }

    public List<BAN> getByLoaiBan(String maLoaiBan) throws SQLException {
        List<BAN> list = new ArrayList<>();
        String sql = "SELECT * FROM BAN WHERE MaLoaiBan_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maLoaiBan);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new BAN(
                        rs.getString("MaBan_ID"),
                        rs.getString("MaLoaiBan_ID"),
                        rs.getString("MaKhuVuc_ID"),
                        rs.getString("TenBan"),
                        rs.getBoolean("TrangThai")
                    ));
                }
            }
        }
        return list;
    }

    public boolean updateTrangThai(String maBan, boolean trangThai) throws SQLException {
        String sql = "UPDATE BAN SET TrangThai = ? WHERE MaBan_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, trangThai);
            stmt.setString(2, maBan);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public IConnector getConnector() {
        return this.connector;
    }
}