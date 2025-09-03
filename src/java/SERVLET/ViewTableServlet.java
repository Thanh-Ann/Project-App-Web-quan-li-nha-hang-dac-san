package SERVLET;

import BUSINESSLOGIC.*;
import CONNECTIONDATA.CONNECTIONSQLSERVER;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import SCHEMACLASS.*;
import SCHEMAOBJECT.*;

@WebServlet("/ViewTableServlet")
public class ViewTableServlet extends HttpServlet {
    private HOA_DON_BUS hoaDonBus;
    private HOA_DON_CHI_TIET_BUS chiTietBus;
    private MENU_BUS menuBus;

    @Override
    public void init() throws ServletException {
        CONNECTIONSQLSERVER connector = new CONNECTIONSQLSERVER();
        hoaDonBus = new HOA_DON_BUS(new HOA_DON_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector));
        chiTietBus = new HOA_DON_CHI_TIET_BUS(new HOA_DON_CHI_TIET_DAO(connector));
        menuBus = new MENU_BUS(new MENU_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String maBan = request.getParameter("maBan");
        String tenBan = request.getParameter("tenBan");
        try {
            String maHoaDon = hoaDonBus.getLatestMaHoaDonFromMaBan(maBan);
            if (maHoaDon == null) {
                request.setAttribute("error", "Bàn hiện tại chưa có hóa đơn!");
                request.setAttribute("maBan", maBan);
                request.setAttribute("tenBan", tenBan);
                request.getRequestDispatcher("viewTable.jsp").forward(request, response);
                return;
            }

            List<HOA_DON_CHI_TIET> chiTietList = chiTietBus.getChiTietByHoaDon(maHoaDon);
            Map<String, MENU> menuMap = new HashMap<>();
            for (HOA_DON_CHI_TIET ct : chiTietList) {
                MENU menu = menuBus.getMenuById(ct.getMaSanPham_ID());
                if (menu != null) {
                    menuMap.put(ct.getMaSanPham_ID(), menu);
                }
            }

            request.setAttribute("maBan", maBan);
            request.setAttribute("tenBan", tenBan);
            request.setAttribute("maHoaDon", maHoaDon);
            request.setAttribute("chiTietList", chiTietList);
            request.setAttribute("menuMap", menuMap);
            request.getRequestDispatcher("viewTable.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Lỗi tải chi tiết bàn: " + e.getMessage());
        }
    }
}