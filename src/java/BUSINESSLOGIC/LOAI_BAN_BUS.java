/*
 * Implementation of LOAI_BAN_BUS for business logic related to LOAI_BAN
 */
/*
 * Implementation of LOAI_BAN_BUS for business logic related to LOAI_BAN
 */
package BUSINESSLOGIC;

import SCHEMAOBJECT.BAN_DAO;
import SCHEMAOBJECT.LOAI_BAN_DAO;
import SCHEMACLASS.LOAI_BAN;
import java.sql.SQLException;
import java.util.List;

public class LOAI_BAN_BUS {
    private final LOAI_BAN_DAO loaiBanDAO;

    public LOAI_BAN_BUS(LOAI_BAN_DAO loaiBanDAO) {
        this.loaiBanDAO = loaiBanDAO;
    }

    public String generateMaLoaiBan() throws SQLException {
        List<LOAI_BAN> loaiBans = loaiBanDAO.getAll();
        int maxId = loaiBans.stream()
            .map(lb -> {
                try {
                    return Integer.parseInt(lb.getMaLoaiBan_ID().substring(2));
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    return 0;
                }
            })
            .max(Integer::compare)
            .orElse(0);
        return String.format("LB%03d", maxId + 1);
    }

    public List<LOAI_BAN> getAllLoaiBan() throws SQLException {
        return loaiBanDAO.getAll();
    }

    public LOAI_BAN getLoaiBanById(String id) throws SQLException {
        return loaiBanDAO.getById(id);
    }

    public boolean addLoaiBan(LOAI_BAN loaiBan) throws SQLException {
        if (loaiBan.getMaLoaiBan_ID() == null || loaiBan.getMaLoaiBan_ID().isEmpty()) {
            loaiBan.setMaLoaiBan_ID(generateMaLoaiBan());
        }
        if (loaiBanDAO.getById(loaiBan.getMaLoaiBan_ID()) != null) {
            throw new SQLException("Mã loại bàn " + loaiBan.getMaLoaiBan_ID() + " đã tồn tại");
        }
        return loaiBanDAO.insert(loaiBan);
    }

    public boolean updateLoaiBan(LOAI_BAN loaiBan) throws SQLException {
        if (loaiBanDAO.getById(loaiBan.getMaLoaiBan_ID()) == null) {
            throw new SQLException("Loại bàn không tồn tại");
        }
        return loaiBanDAO.update(loaiBan);
    }

    public boolean deleteLoaiBan(String id) throws SQLException {
        BAN_DAO banDAO = new BAN_DAO(loaiBanDAO.getConnector());
        if (!banDAO.getByLoaiBan(id).isEmpty()) {
            throw new SQLException("Không thể xóa loại bàn khi còn bàn thuộc loại này");
        }
        return loaiBanDAO.delete(id);
    }

    public List<LOAI_BAN> searchLoaiBan(String keyword) throws SQLException {
        return loaiBanDAO.searchByName(keyword);
    }
}