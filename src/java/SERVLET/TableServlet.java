package SERVLET;

import BUSINESSLOGIC.BAN_BUS;
import CONNECTIONDATA.CONNECTIONSQLSERVER;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import SCHEMACLASS.*;
import SCHEMAOBJECT.BAN_DAO;

@WebServlet("/TableServlet")
public class TableServlet extends HttpServlet {
    private BAN_BUS banBus;

    @Override
    public void init() throws ServletException {
        CONNECTIONSQLSERVER connector = new CONNECTIONSQLSERVER();
        banBus = new BAN_BUS(new BAN_DAO(connector));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            request.setAttribute("loginError", "Vui lòng đăng nhập để truy cập!");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                request.getRequestDispatcher("addTable.jsp").forward(request, response);
            } else if ("edit".equals(action)) {
                request.getRequestDispatcher("editTable.jsp").forward(request, response);
            } else if ("delete".equals(action)) {
                request.getRequestDispatcher("deleteTable.jsp").forward(request, response);
            } else {
                List<BAN> banList = banBus.getAllBan();
                List<BAN> banTrong = banList.stream()
                        .filter(b -> !b.isTrangThai())
                        .collect(Collectors.toList());
                request.setAttribute("banList", banList);
                request.setAttribute("banTrong", banTrong);
                request.getRequestDispatcher("tables.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Lỗi tải danh sách bàn: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            request.setAttribute("loginError", "Vui lòng đăng nhập để truy cập!");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("addTable".equals(action)) {
                String maBan = request.getParameter("maBan");
                String tenBan = request.getParameter("tenBan");
                String maLoaiBan = request.getParameter("maLoaiBan");
                String maKhuVuc = request.getParameter("maKhuVuc");
                String trangThai = request.getParameter("trangThai");

                if (maBan.isEmpty() || tenBan.isEmpty() || maLoaiBan.isEmpty() || maKhuVuc.isEmpty() || trangThai.isEmpty()) {
                    request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
                    request.getRequestDispatcher("addTable.jsp").forward(request, response);
                    return;
                }

                boolean trangThaiBool;
                try {
                    trangThaiBool = Boolean.parseBoolean(trangThai);
                } catch (Exception e) {
                    request.setAttribute("error", "Trạng thái phải là true hoặc false!");
                    request.getRequestDispatcher("addTable.jsp").forward(request, response);
                    return;
                }

                BAN ban = new BAN(maBan, maLoaiBan, maKhuVuc, tenBan, trangThaiBool);
                banBus.addBan(ban);
                response.sendRedirect("TableServlet");
            } else if ("editTable".equals(action)) {
                String maBan = request.getParameter("maBan");
                String tenBan = request.getParameter("tenBan");
                String maLoaiBan = request.getParameter("maLoaiBan");
                String maKhuVuc = request.getParameter("maKhuVuc");
                String trangThai = request.getParameter("trangThai");

                if (maBan.isEmpty() || tenBan.isEmpty() || maLoaiBan.isEmpty() || maKhuVuc.isEmpty() || trangThai.isEmpty()) {
                    request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
                    request.getRequestDispatcher("editTable.jsp").forward(request, response);
                    return;
                }

                boolean trangThaiBool;
                try {
                    trangThaiBool = Boolean.parseBoolean(trangThai);
                } catch (Exception e) {
                    request.setAttribute("error", "Trạng thái phải là true hoặc false!");
                    request.getRequestDispatcher("editTable.jsp").forward(request, response);
                    return;
                }

                BAN ban = new BAN(maBan, maLoaiBan, maKhuVuc, tenBan, trangThaiBool);
                banBus.updateBan(ban);
                response.sendRedirect("TableServlet");
            } else if ("deleteTable".equals(action)) {
                String maBan = request.getParameter("maBan");
                if (maBan.isEmpty()) {
                    request.setAttribute("error", "Vui lòng nhập mã bàn!");
                    request.getRequestDispatcher("deleteTable.jsp").forward(request, response);
                    return;
                }
                banBus.deleteBan(maBan);
                response.sendRedirect("TableServlet");
            } else if ("export".equals(action)) {
                response.setContentType("text/csv; charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=\"tables.csv\"");
                List<BAN> banList = banBus.getAllBan();
                response.getWriter().write("Mã Bàn,Tên Bàn,Mã Loại Bàn,Mã Khu Vực,Trạng Thái\n");
                for (BAN ban : banList) {
                    response.getWriter().write(String.format("%s,%s,%s,%s,%b\n",
                            ban.getMaBan_ID(), ban.getTenBan(), ban.getMaLoaiBan_ID(),
                            ban.getMaKhuVuc_ID(), ban.isTrangThai()));
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Lỗi xử lý: " + e.getMessage());
        }
    }
}