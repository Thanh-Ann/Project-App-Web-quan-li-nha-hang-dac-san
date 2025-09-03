/*
 * Implementation of LOAI_SAN_PHAM_BUS for business logic related to LOAI_SAN_PHAM
 */
/*
 * Implementation of LOAI_SAN_PHAM_BUS for business logic related to LOAI_SAN_PHAM
 */
package BUSINESSLOGIC;

import SCHEMAOBJECT.LOAI_SAN_PHAM_DAO;
import SCHEMACLASS.LOAI_SAN_PHAM;
import SCHEMAOBJECT.MENU_DAO;
import java.sql.SQLException;
import java.util.List;

public class LOAI_SAN_PHAM_BUS {
    private final LOAI_SAN_PHAM_DAO loaiSanPhamDAO;

    public LOAI_SAN_PHAM_BUS(LOAI_SAN_PHAM_DAO loaiSanPhamDAO) {
        this.loaiSanPhamDAO = loaiSanPhamDAO;
    }

    public String generateMaLoaiSanPham() throws SQLException {
        List<LOAI_SAN_PHAM> loaiSanPhams = loaiSanPhamDAO.getAll();
        int maxId = loaiSanPhams.stream()
            .map(lsp -> {
                try {
                    return Integer.parseInt(lsp.getMaLoaiSanPham_ID().substring(3));
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    return 0;
                }
            })
            .max(Integer::compare)
            .orElse(0);
        return String.format("LSP%03d", maxId + 1);
    }

    public List<LOAI_SAN_PHAM> getAllLoaiSanPham() throws SQLException {
        return loaiSanPhamDAO.getAll();
    }

    public LOAI_SAN_PHAM getLoaiSanPhamById(String id) throws SQLException {
        return loaiSanPhamDAO.getById(id);
    }

    public boolean addLoaiSanPham(LOAI_SAN_PHAM loaiSanPham) throws SQLException {
        if (loaiSanPham.getMaLoaiSanPham_ID() == null || loaiSanPham.getMaLoaiSanPham_ID().isEmpty()) {
            loaiSanPham.setMaLoaiSanPham_ID(generateMaLoaiSanPham());
        }
        if (loaiSanPhamDAO.getById(loaiSanPham.getMaLoaiSanPham_ID()) != null) {
            throw new SQLException("Mã loại sản phẩm " + loaiSanPham.getMaLoaiSanPham_ID() + " đã tồn tại");
        }
        return loaiSanPhamDAO.insert(loaiSanPham);
    }

    public boolean updateLoaiSanPham(LOAI_SAN_PHAM loaiSanPham) throws SQLException {
        if (loaiSanPhamDAO.getById(loaiSanPham.getMaLoaiSanPham_ID()) == null) {
            throw new SQLException("Loại sản phẩm không tồn tại");
        }
        return loaiSanPhamDAO.update(loaiSanPham);
    }

    public boolean deleteLoaiSanPham(String id) throws SQLException {
        MENU_DAO menuDAO = new MENU_DAO(loaiSanPhamDAO.getConnector());
        if (!menuDAO.getByLoaiSanPham(id).isEmpty()) {
            throw new SQLException("Không thể xóa loại sản phẩm khi còn sản phẩm thuộc loại này");
        }
        return loaiSanPhamDAO.delete(id);
    }
}