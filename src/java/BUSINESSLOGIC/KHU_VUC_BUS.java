/*
 * Implementation of KHU_VUC_BUS for business logic related to KHU_VUC
 */
/*
 * Implementation of KHU_VUC_BUS for business logic related to KHU_VUC
 */
package BUSINESSLOGIC;

import SCHEMAOBJECT.KHU_VUC_DAO;
import SCHEMAOBJECT.TANG_DAO;
import SCHEMACLASS.KHU_VUC;
import SCHEMAOBJECT.BAN_DAO;
import java.sql.SQLException;
import java.util.List;

public class KHU_VUC_BUS {
    private final KHU_VUC_DAO khuVucDAO;
    private final TANG_DAO tangDAO;

    public KHU_VUC_BUS(KHU_VUC_DAO khuVucDAO, TANG_DAO tangDAO) {
        this.khuVucDAO = khuVucDAO;
        this.tangDAO = tangDAO;
    }

    public String generateMaKhuVuc() throws SQLException {
        List<KHU_VUC> khuVucs = khuVucDAO.getAll();
        int maxId = khuVucs.stream()
            .map(kv -> {
                try {
                    return Integer.parseInt(kv.getMaKhuVuc_ID().substring(2));
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    return 0;
                }
            })
            .max(Integer::compare)
            .orElse(0);
        return String.format("KV%03d", maxId + 1);
    }

    public List<KHU_VUC> getAllKhuVuc() throws SQLException {
        return khuVucDAO.getAll();
    }

    public KHU_VUC getKhuVucById(String id) throws SQLException {
        return khuVucDAO.getById(id);
    }

    public boolean addKhuVuc(KHU_VUC khuVuc) throws SQLException {
        if (khuVuc.getMaKhuVuc_ID() == null || khuVuc.getMaKhuVuc_ID().isEmpty()) {
            khuVuc.setMaKhuVuc_ID(generateMaKhuVuc());
        }
        if (tangDAO.getById(khuVuc.getMaTang_ID()) == null) {
            throw new SQLException("Tầng không tồn tại");
        }
        if (khuVucDAO.getById(khuVuc.getMaKhuVuc_ID()) != null) {
            throw new SQLException("Mã khu vực " + khuVuc.getMaKhuVuc_ID() + " đã tồn tại");
        }
        return khuVucDAO.insert(khuVuc);
    }

    public boolean updateKhuVuc(KHU_VUC khuVuc) throws SQLException {
        if (khuVucDAO.getById(khuVuc.getMaKhuVuc_ID()) == null) {
            throw new SQLException("Khu vực không tồn tại");
        }
        if (tangDAO.getById(khuVuc.getMaTang_ID()) == null) {
            throw new SQLException("Tầng không tồn tại");
        }
        return khuVucDAO.update(khuVuc);
    }

    public boolean deleteKhuVuc(String id) throws SQLException {
        BAN_DAO banDAO = new BAN_DAO(khuVucDAO.getConnector());
        if (!banDAO.getByKhuVuc(id).isEmpty()) {
            throw new SQLException("Không thể xóa khu vực khi còn bàn");
        }
        return khuVucDAO.delete(id);
    }

    public List<KHU_VUC> getKhuVucByTang(String maTang) throws SQLException {
        return khuVucDAO.getByTang(maTang);
    }
}