/*
 * Implementation of HOA_DON_DAO for CRUD operations on HOA_DON table
 */
package SCHEMAOBJECT;

import CONNECTIONDATA.IConnector;
import SCHEMACLASS.HOA_DON;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HOA_DON_DAO implements ICRUD<HOA_DON, String> {
    private final IConnector connector;

    public HOA_DON_DAO(IConnector connector) {
        this.connector = connector;
    }

   @Override
public List<HOA_DON> getAll() throws SQLException {
    List<HOA_DON> list = new ArrayList<>();
    String sql = "SELECT * FROM HOA_DON";
    try (Connection conn = connector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            list.add(new HOA_DON(
                rs.getString("MaHoaDon_ID"),
                rs.getString("MaBan_ID"),
                rs.getDouble("TongTien"),
                rs.getDate("NgayBan"),
                rs.getString("TrangThai")
            ));
        }
    } catch (Exception ex) {
        System.err.println("Lỗi khi lấy danh sách hóa đơn: " + ex.getMessage());
        throw new SQLException("Lỗi khi lấy danh sách hóa đơn", ex);
    }
    return list;
}

    @Override
public HOA_DON getById(String id) throws SQLException {
    String sql = "SELECT * FROM HOA_DON WHERE MaHoaDon_ID = ?";
    try (Connection conn = connector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, id);
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? new HOA_DON(
                rs.getString("MaHoaDon_ID"),
                rs.getString("MaBan_ID"),
                rs.getDouble("TongTien"),
                rs.getDate("NgayBan"),
                rs.getString("TrangThai")
            ) : null;
        }
    }
}

    @Override
public boolean insert(HOA_DON entity) throws SQLException {
    String sql = "INSERT INTO HOA_DON (MaHoaDon_ID, MaBan_ID, TongTien, NgayBan, TrangThai) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = connector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, entity.getMaHoaDon_ID());
        stmt.setString(2, entity.getMaBan_ID());
        stmt.setDouble(3, entity.getTongTien());
        stmt.setDate(4, entity.getNgayBan());
        stmt.setString(5, entity.getTrangThai());
        return stmt.executeUpdate() > 0;
    }
}

    @Override
public boolean update(HOA_DON entity) throws SQLException {
    String sql = "UPDATE HOA_DON SET MaBan_ID = ?, TongTien = ?, NgayBan = ?, TrangThai = ? WHERE MaHoaDon_ID = ?";
    try (Connection conn = connector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, entity.getMaBan_ID());
        stmt.setDouble(2, entity.getTongTien());
        stmt.setDate(3, entity.getNgayBan());
        stmt.setString(4, entity.getTrangThai());
        stmt.setString(5, entity.getMaHoaDon_ID());
        return stmt.executeUpdate() > 0;
    }
}

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM HOA_DON WHERE MaHoaDon_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<HOA_DON> getByDate(Date ngay) throws SQLException {
        List<HOA_DON> list = new ArrayList<>();
        String sql = "SELECT * FROM HOA_DON WHERE NgayBan = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, ngay);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new HOA_DON(
                        rs.getString("MaHoaDon_ID"),
                    rs.getString("MaBan_ID"),
                    rs.getDouble("TongTien"),
                    rs.getDate("NgayBan"),
                    rs.getString("TrangThai")
                    ));
                }
            }
        }
        return list;
    }

    public List<HOA_DON> getByBan(String maBan) throws SQLException {
    List<HOA_DON> list = new ArrayList<>();
    String sql = "SELECT * FROM HOA_DON WHERE MaBan_ID = ? AND TrangThai = N'Chưa thanh toán'";
    try (Connection conn = connector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, maBan);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new HOA_DON(
                    rs.getString("MaHoaDon_ID"),
                    rs.getString("MaBan_ID"),
                    rs.getDouble("TongTien"),
                    rs.getDate("NgayBan"),
                    rs.getString("TrangThai")
                ));
            }
        }
    }
    return list;
}
    
    public int countAll() throws SQLException {
    String sql = "SELECT COUNT(*) FROM HOA_DON";
    try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            return rs.getInt(1);
        }
    }
    return 0;
}
    
    public IConnector getConnector() {
        return this.connector;
    }
}