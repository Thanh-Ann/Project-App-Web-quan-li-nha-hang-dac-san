package BUSINESSLOGIC;

import SCHEMACLASS.HOA_DON;
import SCHEMACLASS.HOA_DON_CHI_TIET;
import SCHEMAOBJECT.HOA_DON_DAO;
import SCHEMAOBJECT.HOA_DON_CHI_TIET_DAO;

import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HOA_DON_BUS {
    private HOA_DON_DAO hoaDonDao;
    private HOA_DON_CHI_TIET_DAO chiTietDao;

    public HOA_DON_BUS(HOA_DON_DAO hoaDonDao, HOA_DON_CHI_TIET_DAO chiTietDao) {
        this.hoaDonDao = hoaDonDao;
        this.chiTietDao = chiTietDao;
    }

    public List<HOA_DON> getAllHoaDon() throws SQLException {
        return hoaDonDao.getAll();
    }

    public HOA_DON getHoaDonById(String maHoaDon) throws SQLException {
        return hoaDonDao.getById(maHoaDon);
    }

    public void addHoaDon(HOA_DON hoaDon, List<HOA_DON_CHI_TIET> chiTietList) throws SQLException {
        Connection conn = null;
        try {
            conn = hoaDonDao.getConnector().getConnection();
            if (conn.isClosed()) {
                throw new SQLException("Kết nối đã bị đóng trước khi thêm hóa đơn!");
            }
            conn.setAutoCommit(false);
            System.out.println("Bắt đầu thêm hóa đơn: " + hoaDon.getMaHoaDon_ID());
            hoaDonDao.insert(hoaDon);
            for (HOA_DON_CHI_TIET chiTiet : chiTietList) {
                System.out.println("Thêm chi tiết hóa đơn: " + chiTiet.getMaHDCT_ID() + ", Món: " + chiTiet.getMaSanPham_ID());
                chiTietDao.insert(chiTiet);
            }
            conn.commit();
            System.out.println("Thêm hóa đơn thành công: " + hoaDon.getMaHoaDon_ID());
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Rollback giao dịch cho hóa đơn: " + hoaDon.getMaHoaDon_ID());
                } catch (SQLException rollbackEx) {
                    System.err.println("Lỗi khi rollback: " + rollbackEx.getMessage());
                }
            }
            System.err.println("Lỗi SQL khi thêm hóa đơn: " + e.getMessage());
            throw new SQLException("Lỗi khi thêm hóa đơn và chi tiết: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("Đóng kết nối trong addHoaDon");
                } catch (SQLException closeEx) {
                    System.err.println("Lỗi khi đóng kết nối: " + closeEx.getMessage());
                }
            }
        }
    }

    public String generateMaHoaDon() throws SQLException {
        Connection conn = null;
        try {
            conn = hoaDonDao.getConnector().getConnection();
            if (conn.isClosed()) {
                throw new SQLException("Kết nối đã bị đóng trước khi tạo mã hóa đơn!");
            }
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // Lấy mã số lớn nhất
            String sql = "SELECT MAX(CAST(SUBSTRING(MaHoaDon_ID, 3, 3) AS INT)) FROM HOA_DON";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                int maxNumber = rs.next() && rs.getObject(1) != null ? rs.getInt(1) : 0;
                if (maxNumber >= 999) {
                    throw new SQLException("Số lượng hóa đơn đã đạt tối đa (999)!");
                }

                // Tạo mã mới
                String maHoaDon;
                boolean isUnique;
                do {
                    maxNumber++;
                    maHoaDon = String.format("HD%03d", maxNumber);
                    // Kiểm tra mã có tồn tại không
                    String checkSql = "SELECT MaHoaDon_ID FROM HOA_DON WHERE MaHoaDon_ID = ?";
                    try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                        checkStmt.setString(1, maHoaDon);
                        try (ResultSet checkRs = checkStmt.executeQuery()) {
                            isUnique = !checkRs.next();
                        }
                    }
                } while (!isUnique);

                System.out.println("Tạo mã hóa đơn: " + maHoaDon);
                conn.commit();
                return maHoaDon;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Rollback giao dịch trong generateMaHoaDon");
                } catch (SQLException rollbackEx) {
                    System.err.println("Lỗi khi rollback: " + rollbackEx.getMessage());
                }
            }
            System.err.println("Lỗi SQL khi tạo mã hóa đơn: " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("Đóng kết nối trong generateMaHoaDon");
                } catch (SQLException closeEx) {
                    System.err.println("Lỗi khi đóng kết nối: " + closeEx.getMessage());
                }
            }
        }
    }

    public void updateHoaDon(HOA_DON hoaDon) throws SQLException {
        hoaDonDao.update(hoaDon);
    }

    public void deleteHoaDon(String maHoaDon) throws SQLException {
        chiTietDao.deleteByHoaDon(maHoaDon);
        hoaDonDao.delete(maHoaDon);
    }

    public String getMaHoaDonFromMaBan(String maBan) throws SQLException {
        String sql = "SELECT TOP 1 MaHoaDon_ID FROM HOA_DON WHERE MaBan_ID = ?";
        try (Connection conn = hoaDonDao.getConnector().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maBan);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString("MaHoaDon_ID") : null;
            }
        }
    }

    public String getLatestMaHoaDonFromMaBan(String maBan) throws SQLException {
        String sql = "SELECT TOP 1 MaHoaDon_ID FROM HOA_DON WHERE MaBan_ID = ? AND TrangThai = N'Chưa thanh toán' ORDER BY NgayBan DESC";
        try (Connection conn = hoaDonDao.getConnector().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maBan);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString("MaHoaDon_ID") : null;
            }
        }
    }

    public void switchTable(String maBanFrom, String maBanTo) throws SQLException {
        String maHoaDon = getLatestMaHoaDonFromMaBan(maBanFrom);
        if (maHoaDon == null) {
            throw new SQLException("Không tìm thấy hóa đơn chưa thanh toán cho bàn nguồn!");
        }

        Connection conn = null;
        try {
            conn = hoaDonDao.getConnector().getConnection();
            if (conn.isClosed()) {
                throw new SQLException("Kết nối đã bị đóng trước khi đổi bàn!");
            }
            conn.setAutoCommit(false);

            String sql = "UPDATE HOA_DON SET MaBan_ID = ? WHERE MaHoaDon_ID = ? AND TrangThai = N'Chưa thanh toán'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, maBanTo);
                stmt.setString(2, maHoaDon);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Không thể cập nhật hóa đơn cho bàn đích!");
                }
            }

            conn.commit();
            System.out.println("Đổi bàn thành công từ " + maBanFrom + " sang " + maBanTo + " cho hóa đơn " + maHoaDon);
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Rollback giao dịch khi đổi bàn");
                } catch (SQLException rollbackEx) {
                    System.err.println("Lỗi khi rollback: " + rollbackEx.getMessage());
                }
            }
            System.err.println("Lỗi SQL khi đổi bàn: " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("Đóng kết nối trong switchTable");
                } catch (SQLException closeEx) {
                    System.err.println("Lỗi khi đóng kết nối: " + closeEx.getMessage());
                }
            }
        }
    }
}