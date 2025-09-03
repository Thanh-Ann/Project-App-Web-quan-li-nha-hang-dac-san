package SERVLET;

import BUSINESSLOGIC.*;
import SCHEMACLASS.*;
import SCHEMAOBJECT.*;
import CONNECTIONDATA.CONNECTIONSQLSERVER;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {
    private HOA_DON_BUS hoaDonBus;
    private BAN_BUS banBus;

    @Override
    public void init() throws ServletException {
        CONNECTIONSQLSERVER connector = new CONNECTIONSQLSERVER();
        hoaDonBus = new HOA_DON_BUS(new HOA_DON_DAO(connector), new HOA_DON_CHI_TIET_DAO(connector));
        banBus = new BAN_BUS(new BAN_DAO(connector));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String maBan = request.getParameter("maBan");
        String tenBan = request.getParameter("tenBan");
        String maHoaDon = request.getParameter("maHoaDon");
        try {
            HOA_DON hd = hoaDonBus.getHoaDonById(maHoaDon);
            double tongTien = (hd != null) ? hd.getTongTien() : 0.0;
            request.setAttribute("maBan", maBan);
            request.setAttribute("tenBan", tenBan);
            request.setAttribute("maHoaDon", maHoaDon);
            request.setAttribute("tongTien", tongTien);
            request.getRequestDispatcher("/payment.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Lỗi tải hóa đơn: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String maBan = request.getParameter("maBan");
        String maHoaDon = request.getParameter("maHoaDon");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String received = request.getParameter("received");
        String payMethod = request.getParameter("payMethod");

        try {
            if (name.isEmpty() || phone.isEmpty() || received.isEmpty()) {
                request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
                doGet(request, response);
                return;
            }

            double receivedAmount;
            try {
                receivedAmount = Double.parseDouble(received);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Tiền nhận phải là số hợp lệ!");
                doGet(request, response);
                return;
            }

            HOA_DON hd = hoaDonBus.getHoaDonById(maHoaDon);
            double tongTien = hd.getTongTien();
            if (receivedAmount < tongTien) {
                request.setAttribute("error", "Tiền nhận không đủ!");
                doGet(request, response);
                return;
            }

            hd.setNgayBan(new Date(System.currentTimeMillis()));
            hd.setTrangThai("Đã thanh toán");
            hoaDonBus.updateHoaDon(hd);

            BAN ban = banBus.getBanById(maBan);
            if (ban != null && ban.isTrangThai()) {
                banBus.huyDatBan(maBan);
            }

            response.sendRedirect("TableServlet");
        } catch (SQLException e) {
            throw new ServletException("Lỗi xử lý thanh toán: " + e.getMessage());
        }
    }
}