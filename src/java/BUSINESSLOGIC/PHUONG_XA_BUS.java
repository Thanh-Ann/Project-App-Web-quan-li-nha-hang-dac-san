/*
 * Implementation of PHUONG_XA_BUS for business logic related to PHUONG_XA
 */
/*
 * Implementation of PHUONG_XA_BUS for business logic related to PHUONG_XA
 */
package BUSINESSLOGIC;

import SCHEMAOBJECT.MENU_DAO;
import SCHEMAOBJECT.PHUONG_XA_DAO;
import SCHEMACLASS.PHUONG_XA;
import java.sql.SQLException;
import java.util.List;

public class PHUONG_XA_BUS {
    private final PHUONG_XA_DAO phuongXaDAO;
    private final MENU_DAO menuDao;

    public PHUONG_XA_BUS(PHUONG_XA_DAO phuongXaDAO, MENU_DAO menuDao) {
        this.phuongXaDAO = phuongXaDAO;
        this.menuDao = menuDao;
    }

    public PHUONG_XA_BUS(PHUONG_XA_DAO phuongXaDAO) {
        this.phuongXaDAO = phuongXaDAO;
        this.menuDao = null;
    }

    public String generateMaPhuongXa() throws SQLException {
        List<PHUONG_XA> phuongXas = phuongXaDAO.getAll();
        int maxId = phuongXas.stream()
            .map(px -> {
                try {
                    return Integer.parseInt(px.getMaPhuongXa_ID().substring(2));
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    return 0;
                }
            })
            .max(Integer::compare)
            .orElse(0);
        return String.format("PX%03d", maxId + 1);
    }

    public List<PHUONG_XA> getAllPhuongXa() throws SQLException {
        return phuongXaDAO.getAll();
    }

    public PHUONG_XA getPhuongXaById(String id) throws SQLException {
        return phuongXaDAO.getById(id);
    }

    public boolean addPhuongXa(PHUONG_XA phuongXa) throws SQLException {
        if (phuongXa.getMaPhuongXa_ID() == null || phuongXa.getMaPhuongXa_ID().isEmpty()) {
            phuongXa.setMaPhuongXa_ID(generateMaPhuongXa());
        }
        if (phuongXaDAO.getById(phuongXa.getMaPhuongXa_ID()) != null) {
            throw new SQLException("Mã phường/xã " + phuongXa.getMaPhuongXa_ID() + " đã tồn tại");
        }
        return phuongXaDAO.insert(phuongXa);
    }

    public boolean updatePhuongXa(PHUONG_XA phuongXa) throws SQLException {
        if (phuongXaDAO.getById(phuongXa.getMaPhuongXa_ID()) == null) {
            throw new SQLException("Phường/xã không tồn tại");
        }
        return phuongXaDAO.update(phuongXa);
    }

    public boolean deletePhuongXa(String id) throws SQLException {
        MENU_DAO menuDAO = new MENU_DAO(phuongXaDAO.getConnector());
        if (!menuDAO.getByPhuongXa(id).isEmpty()) {
            throw new SQLException("Không thể xóa phường/xã khi còn sản phẩm");
        }
        return phuongXaDAO.delete(id);
    }
}