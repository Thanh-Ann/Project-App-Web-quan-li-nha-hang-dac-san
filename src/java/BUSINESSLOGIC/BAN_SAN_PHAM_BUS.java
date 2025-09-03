/*
 * Implementation of BAN_SAN_PHAM_BUS for business logic related to BAN_SAN_PHAM
 */
package BUSINESSLOGIC;

import SCHEMAOBJECT.BAN_SAN_PHAM_DAO;
import SCHEMACLASS.BAN_SAN_PHAM;
import java.sql.SQLException;
import java.util.List;

public class BAN_SAN_PHAM_BUS {
    private final BAN_SAN_PHAM_DAO banSanPhamDAO;

    public BAN_SAN_PHAM_BUS(BAN_SAN_PHAM_DAO banSanPhamDAO) {
        this.banSanPhamDAO = banSanPhamDAO;
    }

    public List<BAN_SAN_PHAM> getAllBanSanPham() throws SQLException {
        return banSanPhamDAO.getAll();
    }

    public BAN_SAN_PHAM getBanSanPham(String maBan, String maSanPham) throws SQLException {
        return banSanPhamDAO.getByCompositeKey(maBan, maSanPham);
    }

    public boolean addBanSanPham(BAN_SAN_PHAM banSanPham) throws SQLException {
        return banSanPhamDAO.insert(banSanPham);
    }

    public void deleteBanSanPham(String maBan, String maSanPham) throws SQLException {
        banSanPhamDAO.delete(maBan, maSanPham);
    }

    public List<BAN_SAN_PHAM> getSanPhamByBan(String maBan) throws SQLException {
        return banSanPhamDAO.getByBan(maBan);
    }

    public List<BAN_SAN_PHAM> getBanBySanPham(String maSanPham) throws SQLException {
        return banSanPhamDAO.getBySanPham(maSanPham);
    }
}