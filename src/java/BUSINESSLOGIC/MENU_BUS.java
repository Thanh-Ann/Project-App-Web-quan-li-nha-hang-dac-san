/*
 * Implementation of MENU_BUS for business logic related to MENU
 */
/*
 * Implementation of MENU_BUS for business logic related to MENU
 */
package BUSINESSLOGIC;

import SCHEMAOBJECT.HOA_DON_CHI_TIET_DAO;
import SCHEMAOBJECT.MENU_DAO;
import SCHEMACLASS.MENU;
import java.sql.SQLException;
import java.util.List;

public class MENU_BUS {
    private final MENU_DAO menuDAO;
    private final HOA_DON_CHI_TIET_DAO hoaDonChiTietDAO;

    public MENU_BUS(MENU_DAO menuDAO) {
        this.menuDAO = menuDAO;
        this.hoaDonChiTietDAO = null;
    }

    public MENU_BUS(MENU_DAO menuDAO, HOA_DON_CHI_TIET_DAO hoaDonChiTietDAO) {
        this.menuDAO = menuDAO;
        this.hoaDonChiTietDAO = hoaDonChiTietDAO;
    }

    public String generateMaSanPham() throws SQLException {
        List<MENU> menus = menuDAO.getAll();
        int maxId = menus.stream()
            .map(menu -> {
                try {
                    return Integer.parseInt(menu.getMaSanPham_ID().substring(2));
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    return 0;
                }
            })
            .max(Integer::compare)
            .orElse(0);
        return String.format("SP%03d", maxId + 1);
    }

    public List<MENU> getAllMenu() throws SQLException {
        return menuDAO.getAll();
    }

    public MENU getMenuById(String id) throws SQLException {
        return menuDAO.getById(id);
    }

    public boolean addMenu(MENU menu) throws SQLException {
        if (menu.getMaSanPham_ID() == null || menu.getMaSanPham_ID().isEmpty()) {
            menu.setMaSanPham_ID(generateMaSanPham());
        }
        if (menuDAO.getById(menu.getMaSanPham_ID()) != null) {
            throw new SQLException("Mã sản phẩm " + menu.getMaSanPham_ID() + " đã tồn tại");
        }
        return menuDAO.insert(menu);
    }

    public boolean updateMenu(MENU menu) throws SQLException {
        if (menuDAO.getById(menu.getMaSanPham_ID()) == null) {
            throw new SQLException("Sản phẩm không tồn tại");
        }
        return menuDAO.update(menu);
    }

    public boolean deleteMenu(String id) throws SQLException {
        HOA_DON_CHI_TIET_DAO chiTietDAO = new HOA_DON_CHI_TIET_DAO(menuDAO.getConnector());
        if (!chiTietDAO.getBySanPham(id).isEmpty()) {
            throw new SQLException("Không thể xóa sản phẩm khi còn hóa đơn chi tiết");
        }
        return menuDAO.delete(id);
    }

    public List<MENU> getMenuByLoai(String maLoai) throws SQLException {
        return menuDAO.getByLoaiSanPham(maLoai);
    }

    public List<MENU> searchMenu(String keyword) throws SQLException {
        return menuDAO.searchByName(keyword);
    }

    public List<MENU> getMenuByPhuongXa(String maPhuongXa) throws SQLException {
        return menuDAO.getByPhuongXa(maPhuongXa);
    }
    public List<MENU> getMenusByLoaiAndPhuongXa(String maLoai, String maPhuongXa) throws SQLException {
    return menuDAO.getMenusByLoaiAndPhuongXa(maLoai, maPhuongXa);
}
}