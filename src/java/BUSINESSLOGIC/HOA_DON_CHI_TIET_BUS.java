/*
 * Implementation of HOA_DON_CHI_TIET_BUS for business logic related to HOA_DON_CHI_TIET
 */
package BUSINESSLOGIC;

import SCHEMAOBJECT.HOA_DON_CHI_TIET_DAO;
import SCHEMACLASS.HOA_DON_CHI_TIET;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

public class HOA_DON_CHI_TIET_BUS {
    private final HOA_DON_CHI_TIET_DAO chiTietDAO;

    public HOA_DON_CHI_TIET_BUS(HOA_DON_CHI_TIET_DAO chiTietDAO) {
        this.chiTietDAO = chiTietDAO;
    }

    public String generateMaChiTiet() throws SQLException {
        List<String> maChiTietList = generateMultipleMaChiTiet(1);
        return maChiTietList.isEmpty() ? null : maChiTietList.get(0);
    }

    public List<String> generateMultipleMaChiTiet(int count) throws SQLException {
        if (count <= 0) {
            throw new IllegalArgumentException("Số lượng mã chi tiết phải lớn hơn 0");
        }

        Connection conn = null;
        int maxRetries = 5;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                conn = chiTietDAO.getConnector().getConnection();
                if (conn.isClosed()) {
                    throw new SQLException("Kết nối đã bị đóng trước khi tạo mã chi tiết hóa đơn!");
                }
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

                // Lấy mã số lớn nhất
                String sql = "SELECT MAX(CAST(SUBSTRING(MaHDCT_ID, 3, 3) AS INT)) FROM HOA_DON_CHI_TIET";
                int maxNumber;
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {
                    maxNumber = rs.next() && rs.getObject(1) != null ? rs.getInt(1) : 0;
                    if (maxNumber >= 999) {
                        throw new SQLException("Số lượng chi tiết hóa đơn đã đạt tối đa (999)!");
                    }
                }

                // Tạo danh sách mã chi tiết
                List<String> maChiTietList = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    boolean isUnique;
                    String maChiTiet;
                    do {
                        maxNumber++;
                        if (maxNumber > 999) {
                            throw new SQLException("Số lượng chi tiết hóa đơn vượt quá giới hạn (999)!");
                        }
                        maChiTiet = String.format("CT%03d", maxNumber);
                        String checkSql = "SELECT MaHDCT_ID FROM HOA_DON_CHI_TIET WHERE MaHDCT_ID = ?";
                        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                            checkStmt.setString(1, maChiTiet);
                            try (ResultSet checkRs = checkStmt.executeQuery()) {
                                isUnique = !checkRs.next();
                            }
                        }
                    } while (!isUnique || maChiTietList.contains(maChiTiet));
                    maChiTietList.add(maChiTiet);
                    System.out.println("Tạo mã chi tiết hóa đơn: " + maChiTiet);
                }

                conn.commit();
                return maChiTietList;
            } catch (SQLException e) {
                attempt++;
                if (conn != null) {
                    try {
                        conn.rollback();
                        System.err.println("Rollback giao dịch trong generateMultipleMaChiTiet (lần thử " + attempt + ")");
                    } catch (SQLException rollbackEx) {
                        System.err.println("Lỗi khi rollback: " + rollbackEx.getMessage());
                    }
                }
                if (attempt >= maxRetries) {
                    System.err.println("Lỗi SQL khi tạo mã chi tiết hóa đơn sau " + maxRetries + " lần thử: " + e.getMessage());
                    throw new SQLException("Không thể tạo mã chi tiết hóa đơn sau nhiều lần thử: " + e.getMessage(), e);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    System.err.println("Lỗi khi đợi retry: " + ie.getMessage());
                }
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true);
                        conn.close();
                        System.out.println("Đóng kết nối trong generateMultipleMaChiTiet");
                    } catch (SQLException closeEx) {
                        System.err.println("Lỗi khi đóng kết nối: " + closeEx.getMessage());
                    }
                }
            }
        }
        throw new SQLException("Không thể tạo mã chi tiết hóa đơn sau nhiều lần thử");
    }

    public List<HOA_DON_CHI_TIET> getAllChiTiet() throws SQLException {
        return chiTietDAO.getAll();
    }

    public HOA_DON_CHI_TIET getChiTietById(String id) throws SQLException {
        return chiTietDAO.getById(id);
    }

    public boolean addChiTiet(HOA_DON_CHI_TIET chiTiet) throws SQLException {
        if (chiTiet.getMaHDCT_ID() == null || chiTiet.getMaHDCT_ID().isEmpty()) {
            chiTiet.setMaHDCT_ID(generateMaChiTiet());
        }
        if (chiTietDAO.getById(chiTiet.getMaHDCT_ID()) != null) {
            throw new SQLException("Mã chi tiết hóa đơn " + chiTiet.getMaHDCT_ID() + " đã tồn tại");
        }
        return chiTietDAO.insert(chiTiet);
    }

    public boolean updateChiTiet(HOA_DON_CHI_TIET chiTiet) throws SQLException {
        return chiTietDAO.update(chiTiet);
    }

    public boolean deleteChiTiet(String id) throws SQLException {
        return chiTietDAO.delete(id);
    }

    public List<HOA_DON_CHI_TIET> getChiTietByHoaDon(String maHoaDon) throws SQLException {
        return chiTietDAO.getByHoaDon(maHoaDon);
    }

    public List<HOA_DON_CHI_TIET> getChiTietBySanPham(String maSanPham) throws SQLException {
        return chiTietDAO.getBySanPham(maSanPham);
    }

    public double tinhTongTien(String maHoaDon) throws SQLException {
        return chiTietDAO.tinhTongTien(maHoaDon);
    }
}