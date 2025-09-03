/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Implementation of TANG_BUS for business logic related to TANG
 */
package BUSINESSLOGIC;

import SCHEMAOBJECT.TANG_DAO;
import SCHEMAOBJECT.KHU_VUC_DAO;
import SCHEMACLASS.TANG;
import java.sql.SQLException;
import java.util.List;

public class TANG_BUS {
    private final TANG_DAO tangDAO;
    private final KHU_VUC_DAO khuVucDAO;

    public TANG_BUS(TANG_DAO tangDAO) {
        this.tangDAO = tangDAO;
        this.khuVucDAO = null;
    }

    public TANG_BUS(TANG_DAO tangDAO, KHU_VUC_DAO khuVucDAO) {
        this.tangDAO = tangDAO;
        this.khuVucDAO = khuVucDAO;
    }

    public String generateMaTang() throws SQLException {
        List<TANG> tangs = tangDAO.getAll();
        int maxId = tangs.stream()
            .map(tang -> {
                try {
                    return Integer.parseInt(tang.getMaTang_ID().substring(1));
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    return 0;
                }
            })
            .max(Integer::compare)
            .orElse(0);
        return String.format("T%03d", maxId + 1);
    }

    public List<TANG> getAllTANG() throws SQLException {
        return tangDAO.getAll();
    }

    public TANG getTANGById(String id) throws SQLException {
        return tangDAO.getById(id);
    }

    public boolean addTANG(TANG tang) throws SQLException {
        if (tang.getMaTang_ID() == null || tang.getMaTang_ID().isEmpty()) {
            tang.setMaTang_ID(generateMaTang());
        }
        if (tangDAO.getById(tang.getMaTang_ID()) != null) {
            throw new SQLException("Mã tầng " + tang.getMaTang_ID() + " đã tồn tại");
        }
        return tangDAO.insert(tang);
    }

    public boolean updateTANG(TANG tang) throws SQLException {
        if (tangDAO.getById(tang.getMaTang_ID()) == null) {
            throw new SQLException("Tầng không tồn tại");
        }
        return tangDAO.update(tang);
    }

    public boolean deleteTANG(String id) throws SQLException {
        KHU_VUC_DAO khuVucDAO = new KHU_VUC_DAO(tangDAO.getConnector());
        if (!khuVucDAO.getByTang(id).isEmpty()) {
            throw new SQLException("Không thể xóa tầng khi còn khu vực");
        }
        return tangDAO.delete(id);
    }

    public int countTANG() throws SQLException {
        return tangDAO.countAll();
    }
}