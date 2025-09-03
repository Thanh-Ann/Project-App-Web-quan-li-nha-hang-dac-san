/*
 * Implementation of TANG_DAO for CRUD operations on TANG table
 */
package SCHEMAOBJECT;

import CONNECTIONDATA.IConnector;
import SCHEMACLASS.TANG;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TANG_DAO implements ICRUD<TANG, String> {
    private final IConnector connector;

    public TANG_DAO(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<TANG> getAll() throws SQLException {
        List<TANG> list = new ArrayList<>();
        String sql = "SELECT * FROM TANG";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new TANG(
                    rs.getString("MaTang_ID"),
                    rs.getString("TenTang"),
                    rs.getString("MoTa")
                ));
            }
        }catch (Exception ex) {
            
        }
        return list;
    }

    @Override
    public TANG getById(String id) throws SQLException {
        String sql = "SELECT * FROM TANG WHERE MaTang_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new TANG(
                    rs.getString("MaTang_ID"),
                    rs.getString("TenTang"),
                    rs.getString("MoTa")
                ) : null;
            }
        }
    }

    @Override
    public boolean insert(TANG entity) throws SQLException {
        String sql = "INSERT INTO TANG (MaTang_ID, TenTang, MoTa) VALUES (?, ?, ?)";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getMaTang_ID());
            stmt.setString(2, entity.getTenTang());
            stmt.setString(3, entity.getMoTa());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(TANG entity) throws SQLException {
        String sql = "UPDATE TANG SET TenTang = ?, MoTa = ? WHERE MaTang_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getTenTang());
            stmt.setString(2, entity.getMoTa());
            stmt.setString(3, entity.getMaTang_ID());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM TANG WHERE MaTang_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM TANG";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    

    public IConnector getConnector() {
        return this.connector;
    }
}