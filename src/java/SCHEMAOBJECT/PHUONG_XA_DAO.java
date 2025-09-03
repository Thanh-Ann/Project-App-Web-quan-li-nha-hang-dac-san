/*
 * Implementation of PHUONG_XA_DAO for CRUD operations on PHUONG_XA table
 */
package SCHEMAOBJECT;

import CONNECTIONDATA.IConnector;
import SCHEMACLASS.PHUONG_XA;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PHUONG_XA_DAO implements ICRUD<PHUONG_XA, String> {
    private final IConnector connector;

    public PHUONG_XA_DAO(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<PHUONG_XA> getAll() throws SQLException {
        List<PHUONG_XA> list = new ArrayList<>();
        String sql = "SELECT * FROM PHUONG_XA";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new PHUONG_XA(
                    rs.getString("MaPhuongXa_ID"),
                    rs.getString("TenPhuongXa"),
                    rs.getString("MoTa")
                ));
            }
        }catch(Exception e){
            
        }
        return list;
    }

    @Override
    public PHUONG_XA getById(String id) throws SQLException {
        String sql = "SELECT * FROM PHUONG_XA WHERE MaPhuongXa_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new PHUONG_XA(
                    rs.getString("MaPhuongXa_ID"),
                    rs.getString("TenPhuongXa"),
                    rs.getString("MoTa")
                ) : null;
            }
        }
    }

    @Override
    public boolean insert(PHUONG_XA entity) throws SQLException {
        String sql = "INSERT INTO PHUONG_XA (MaPhuongXa_ID, TenPhuongXa, MoTa) VALUES (?, ?, ?)";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getMaPhuongXa_ID());
            stmt.setString(2, entity.getTenPhuongXa());
            stmt.setString(3, entity.getMoTa());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(PHUONG_XA entity) throws SQLException {
        String sql = "UPDATE PHUONG_XA SET TenPhuongXa = ?, MoTa = ? WHERE MaPhuongXa_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getTenPhuongXa());
            stmt.setString(2, entity.getMoTa());
            stmt.setString(3, entity.getMaPhuongXa_ID());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM PHUONG_XA WHERE MaPhuongXa_ID = ?";
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