/*
 * Implementation of BAN_SAN_PHAM_DAO for CRUD operations on BAN_SAN_PHAM table
 */
/*
 * Implementation of BAN_SAN_PHAM_DAO for CRUD operations on BAN_SAN_PHAM table
 */
package SCHEMAOBJECT;

import CONNECTIONDATA.IConnector;
import SCHEMACLASS.BAN_SAN_PHAM;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BAN_SAN_PHAM_DAO implements ICRUD<BAN_SAN_PHAM, String> {
    private final IConnector connector;

    public BAN_SAN_PHAM_DAO(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<BAN_SAN_PHAM> getAll() throws SQLException {
        List<BAN_SAN_PHAM> list = new ArrayList<>();
        String sql = "SELECT * FROM BAN_SAN_PHAM";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new BAN_SAN_PHAM(
                    rs.getString("MaBan_ID"),
                    rs.getString("MaSanPham_ID")
                ));
            }
        }
        return list;
    }

    @Override
    public BAN_SAN_PHAM getById(String id) throws SQLException {
        // Không áp dụng vì khóa chính gồm 2 cột
        return null;
    }

    public BAN_SAN_PHAM getByCompositeKey(String maBan, String maSanPham) throws SQLException {
        String sql = "SELECT * FROM BAN_SAN_PHAM WHERE MaBan_ID = ? AND MaSanPham_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maBan);
            stmt.setString(2, maSanPham);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new BAN_SAN_PHAM(
                    rs.getString("MaBan_ID"),
                    rs.getString("MaSanPham_ID")
                ) : null;
            }
        }
    }

    @Override
    public boolean insert(BAN_SAN_PHAM entity) throws SQLException {
        String sql = "INSERT INTO BAN_SAN_PHAM (MaBan_ID, MaSanPham_ID) VALUES (?, ?)";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getMaBan_ID());
            stmt.setString(2, entity.getMaSanPham_ID());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(BAN_SAN_PHAM entity) throws SQLException {
        // Không hỗ trợ update vì đây là bảng liên kết (chỉ có khóa chính, không có cột dữ liệu để cập nhật)
        // Nếu cần "cập nhật", bạn có thể xóa và thêm lại bản ghi
        delete(entity.getMaBan_ID(), entity.getMaSanPham_ID());
        return insert(entity);
    }

    @Override
    public boolean delete(String id) throws SQLException {
        // Không áp dụng vì khóa chính gồm 2 cột
        return false;
    }

    public void delete(String maBan, String maSanPham) throws SQLException {
        String sql = "DELETE FROM BAN_SAN_PHAM WHERE MaBan_ID = ? AND MaSanPham_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maBan);
            stmt.setString(2, maSanPham);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy bản ghi với MaBan_ID: " + maBan + " và MaSanPham_ID: " + maSanPham);
            }
        }
    }

    public List<BAN_SAN_PHAM> getByBan(String maBan) throws SQLException {
        List<BAN_SAN_PHAM> list = new ArrayList<>();
        String sql = "SELECT * FROM BAN_SAN_PHAM WHERE MaBan_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maBan);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new BAN_SAN_PHAM(
                        rs.getString("MaBan_ID"),
                        rs.getString("MaSanPham_ID")
                    ));
                }
            }
        }
        return list;
    }

    public List<BAN_SAN_PHAM> getBySanPham(String maSanPham) throws SQLException {
        List<BAN_SAN_PHAM> list = new ArrayList<>();
        String sql = "SELECT * FROM BAN_SAN_PHAM WHERE MaSanPham_ID = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSanPham);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new BAN_SAN_PHAM(
                        rs.getString("MaBan_ID"),
                        rs.getString("MaSanPham_ID")
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