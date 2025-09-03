/*
 * Implementation of KHU_VUC_DAO for CRUD operations on KHU_VUC table
 */
package SCHEMAOBJECT;

import CONNECTIONDATA.IConnector;
import SCHEMACLASS.KHU_VUC;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KHU_VUC_DAO implements ICRUD<KHU_VUC, String> {
    private final IConnector connector;

    public KHU_VUC_DAO(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<KHU_VUC> getAll() throws SQLException {
        List<KHU_VUC> list = new ArrayList<>();
        String sql = "SELECT * FROM KHU_VUC";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new KHU_VUC(
                    rs.getString("MaKhuVuc_ID"),
                    rs.getString("MaTang_ID"),
                    rs.getString("TenKhuVuc"),
                    rs.getString("MoTa")
                ));
            }
        }catch (Exception ex) {
            
        }
        return list;
    }

    @Override
    public KHU_VUC getById(String id) throws SQLException {
        String sql = "SELECT * FROM KHU_VUC WHERE MaKhuVuc_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new KHU_VUC(
                    rs.getString("MaKhuVuc_ID"),
                    rs.getString("MaTang_ID"),
                    rs.getString("TenKhuVuc"),
                    rs.getString("MoTa")
                ) : null;
            }
        }
    }

    @Override
    public boolean insert(KHU_VUC entity) throws SQLException {
        String sql = "INSERT INTO KHU_VUC (MaKhuVuc_ID, MaTang_ID, TenKhuVuc, MoTa) VALUES (?, ?, ?, ?)";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getMaKhuVuc_ID());
            stmt.setString(2, entity.getMaTang_ID());
            stmt.setString(3, entity.getTenKhuVuc());
            stmt.setString(4, entity.getMoTa());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(KHU_VUC entity) throws SQLException {
        String sql = "UPDATE KHU_VUC SET MaTang_ID = ?, TenKhuVuc = ?, MoTa = ? WHERE MaKhuVuc_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getMaTang_ID());
            stmt.setString(2, entity.getTenKhuVuc());
            stmt.setString(3, entity.getMoTa());
            stmt.setString(4, entity.getMaKhuVuc_ID());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM KHU_VUC WHERE MaKhuVuc_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<KHU_VUC> getByTang(String maTang) throws SQLException {
        List<KHU_VUC> list = new ArrayList<>();
        String sql = "SELECT * FROM KHU_VUC WHERE MaTang_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maTang);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new KHU_VUC(
                        rs.getString("MaKhuVuc_ID"),
                        rs.getString("MaTang_ID"),
                        rs.getString("TenKhuVuc"),
                        rs.getString("MoTa")
                    ));
                }
            }
        }
        return list;
    }
    
    // Thêm phương thức này vào class TANG_DAO
public IConnector getConnector() {
    return this.connector;
}
}