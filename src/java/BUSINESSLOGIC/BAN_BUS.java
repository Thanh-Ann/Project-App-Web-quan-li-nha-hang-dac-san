package BUSINESSLOGIC;

import SCHEMAOBJECT.BAN_DAO;
import SCHEMACLASS.BAN;
import SCHEMAOBJECT.HOA_DON_DAO;
import java.sql.SQLException;
import java.util.List;

public class BAN_BUS {
    private final BAN_DAO banDAO;

    public BAN_BUS(BAN_DAO banDAO) {
        this.banDAO = banDAO;
    }

    public String generateMaBan() throws SQLException {
        List<BAN> bans = banDAO.getAll();
        int maxId = bans.stream()
            .map(ban -> {
                try {
                    return Integer.parseInt(ban.getMaBan_ID().substring(1));
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    return 0;
                }
            })
            .max(Integer::compare)
            .orElse(0);
        return String.format("B%04d", maxId + 1);
    }

    public List<BAN> getAllBan() throws SQLException {
        return banDAO.getAll();
    }

    public BAN getBanById(String id) throws SQLException {
        return banDAO.getById(id);
    }

    public boolean addBan(BAN ban) throws SQLException {
        if (ban.getMaBan_ID() == null || ban.getMaBan_ID().isEmpty()) {
            ban.setMaBan_ID(generateMaBan());
        }
        if (banDAO.getById(ban.getMaBan_ID()) != null) {
            throw new SQLException("Mã bàn " + ban.getMaBan_ID() + " đã tồn tại");
        }
        return banDAO.insert(ban);
    }

    public boolean updateBan(BAN ban) throws SQLException {
        if (banDAO.getById(ban.getMaBan_ID()) == null) {
            throw new SQLException("Bàn không tồn tại");
        }
        return banDAO.update(ban);
    }

    public boolean deleteBan(String id) throws SQLException {
        HOA_DON_DAO hoaDonDAO = new HOA_DON_DAO(banDAO.getConnector());
        if (!hoaDonDAO.getByBan(id).isEmpty()) {
            throw new SQLException("Không thể xóa bàn khi còn hóa đơn");
        }
        return banDAO.delete(id);
    }

    public List<BAN> getBanByKhuVuc(String maKhuVuc) throws SQLException {
        return banDAO.getByKhuVuc(maKhuVuc);
    }

    public List<BAN> getBanByLoaiBan(String maLoaiBan) throws SQLException {
        return banDAO.getByLoaiBan(maLoaiBan);
    }

    public boolean datBan(String maBan) throws SQLException {
        BAN ban = banDAO.getById(maBan);
        if (ban == null) {
            throw new SQLException("Bàn không tồn tại");
        }
        if (ban.isTrangThai()) {
            throw new SQLException("Bàn đã được đặt");
        }
        return banDAO.updateTrangThai(maBan, true);
    }

    public boolean huyDatBan(String maBan) throws SQLException {
        BAN ban = banDAO.getById(maBan);
        if (ban == null) {
            throw new SQLException("Bàn không tồn tại");
        }
        if (!ban.isTrangThai()) {
            throw new SQLException("Bàn chưa được đặt");
        }
        return banDAO.updateTrangThai(maBan, false);
    }

    public List<BAN> getBanTrong() throws SQLException {
        List<BAN> allBan = banDAO.getAll();
        return allBan.stream()
                .filter(ban -> !ban.isTrangThai())
                .toList();
    }

    public List<BAN> getBanDaDat() throws SQLException {
        List<BAN> allBan = banDAO.getAll();
        return allBan.stream()
                .filter(BAN::isTrangThai)
                .toList();
    }

    public boolean updateTableStatus(String maBan, boolean trangThai) throws SQLException {
        BAN ban = banDAO.getById(maBan);
        if (ban == null) {
            throw new SQLException("Bàn không tồn tại");
        }
        return banDAO.updateTrangThai(maBan, trangThai);
    }
}