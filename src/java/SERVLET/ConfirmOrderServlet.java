package SERVLET;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import SCHEMACLASS.HOA_DON;
import SCHEMACLASS.HOA_DON_CHI_TIET;
import SCHEMACLASS.BAN;
import BUSINESSLOGIC.HOA_DON_BUS;
import BUSINESSLOGIC.HOA_DON_CHI_TIET_BUS;
import BUSINESSLOGIC.BAN_BUS;
import CONNECTIONDATA.CONNECTIONSQLSERVER;
import SCHEMAOBJECT.HOA_DON_DAO;
import SCHEMAOBJECT.HOA_DON_CHI_TIET_DAO;
import SCHEMAOBJECT.BAN_DAO;
import java.sql.Date;

@WebServlet("/ConfirmOrderServlet")
public class ConfirmOrderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        Gson gson = new Gson();
        
        try {
            // Đọc dữ liệu JSON từ request
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            Map<String, Object> data = gson.fromJson(sb.toString(), new TypeToken<Map<String, Object>>(){}.getType());
            String maBan = (String) data.get("maBan");
            Map<String, Map<String, Object>> items = (Map<String, Map<String, Object>>) data.get("items");

            CONNECTIONSQLSERVER connector = new CONNECTIONSQLSERVER();
            HOA_DON_BUS hoaDonBus = new HOA_DON_BUS(new HOA_DON_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector));
            HOA_DON_CHI_TIET_BUS chiTietBus = new HOA_DON_CHI_TIET_BUS(new HOA_DON_CHI_TIET_DAO(connector));
            BAN_BUS banBus = new BAN_BUS(new BAN_DAO(connector));

            BAN ban = banBus.getBanById(maBan);
            if (ban == null) {
                response.getWriter().write(gson.toJson(new Response(false, "Bàn không tồn tại!")));
                return;
            }

            String maHoaDon = hoaDonBus.getLatestMaHoaDonFromMaBan(maBan);
            boolean isNewHoaDon = (maHoaDon == null);
            if (isNewHoaDon) {
                maHoaDon = hoaDonBus.generateMaHoaDon();
            }

            List<HOA_DON_CHI_TIET> chiTietList = new ArrayList<>();
            List<String> maChiTietList = chiTietBus.generateMultipleMaChiTiet(items.size());
            double tongTien = 0.0;
            int index = 0;
            for (Map.Entry<String, Map<String, Object>> entry : items.entrySet()) {
                String maSanPham = entry.getKey();
                Map<String, Object> item = entry.getValue();
                int soLuong = ((Double) item.get("quantity")).intValue();
                double donGia = (Double) item.get("price");
                if (soLuong > 0) {
                    String maChiTiet = maChiTietList.get(index++);
                    chiTietList.add(new HOA_DON_CHI_TIET(maChiTiet, maHoaDon, maSanPham, donGia, soLuong));
                    tongTien += donGia * soLuong;
                }
            }

            if (chiTietList.isEmpty()) {
                response.getWriter().write(gson.toJson(new Response(false, "Không có món nào hợp lệ!")));
                return;
            }

            if (isNewHoaDon) {
                HOA_DON hoaDon = new HOA_DON(maHoaDon, maBan, tongTien, new Date(System.currentTimeMillis()), "Chưa thanh toán");
                hoaDonBus.addHoaDon(hoaDon, chiTietList);
            } else {
                for (HOA_DON_CHI_TIET chiTiet : chiTietList) {
                    chiTietBus.addChiTiet(chiTiet);
                }
                double newTongTien = chiTietBus.tinhTongTien(maHoaDon);
                HOA_DON hoaDon = hoaDonBus.getHoaDonById(maHoaDon);
                hoaDon.setTongTien(newTongTien);
                hoaDonBus.updateHoaDon(hoaDon);
            }

            if (!ban.isTrangThai()) {
                banBus.datBan(maBan);
            }

            response.getWriter().write(gson.toJson(new Response(true, "Gọi món thành công!", maHoaDon)));
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write(gson.toJson(new Response(false, "Lỗi SQL: " + e.getMessage())));
        }
    }

    private static class Response {
        boolean success;
        String message;
        String maHoaDon;

        Response(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        Response(boolean success, String message, String maHoaDon) {
            this.success = success;
            this.message = message;
            this.maHoaDon = maHoaDon;
        }
    }
}