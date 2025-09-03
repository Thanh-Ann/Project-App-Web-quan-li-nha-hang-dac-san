/*
 * Implementation of LOAI_BAN_DAO for CRUD operations on LOAI_BAN table
 */
package SCHEMAOBJECT;

import CONNECTIONDATA.IConnector;
import SCHEMACLASS.LOAI_BAN;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LOAI_BAN_DAO implements ICRUD<LOAI_BAN, String> {
    private final IConnector connector;

    public LOAI_BAN_DAO(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<LOAI_BAN> getAll() throws SQLException {
        List<LOAI_BAN> list = new ArrayList<>();
        String sql = "SELECT * FROM LOAI_BAN";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new LOAI_BAN(
                    rs.getString("MaLoaiBan_ID"),
                    rs.getString("TenLoaiBan"),
                    rs.getString("MoTa")
                ));
            }
        }catch (Exception ex) {
            
        }
        return list;
    }

    @Override
    public LOAI_BAN getById(String id) throws SQLException {
        String sql = "SELECT * FROM LOAI_BAN WHERE MaLoaiBan_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new LOAI_BAN(
                    rs.getString("MaLoaiBan_ID"),
                    rs.getString("TenLoaiBan"),
                    rs.getString("MoTa")
                ) : null;
            }
        }
    }

    @Override
    public boolean insert(LOAI_BAN entity) throws SQLException {
        String sql = "INSERT INTO LOAI_BAN (MaLoaiBan_ID, TenLoaiBan, MoTa) VALUES (?, ?, ?)";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getMaLoaiBan_ID());
            stmt.setString(2, entity.getTenLoaiBan());
            stmt.setString(3, entity.getMoTa());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(LOAI_BAN entity) throws SQLException {
        String sql = "UPDATE LOAI_BAN SET TenLoaiBan = ?, MoTa = ? WHERE MaLoaiBan_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getTenLoaiBan());
            stmt.setString(2, entity.getMoTa());
            stmt.setString(3, entity.getMaLoaiBan_ID());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM LOAI_BAN WHERE MaLoaiBan_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<LOAI_BAN> searchByName(String keyword) throws SQLException {
        List<LOAI_BAN> list = new ArrayList<>();
        String sql = "SELECT * FROM LOAI_BAN WHERE TenLoaiBan LIKE ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new LOAI_BAN(
                        rs.getString("MaLoaiBan_ID"),
                        rs.getString("TenLoaiBan"),
                        rs.getString("MoTa")
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