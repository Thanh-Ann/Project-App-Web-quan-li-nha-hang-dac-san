package SERVLET;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import SCHEMACLASS.MENU;
import BUSINESSLOGIC.MENU_BUS;
import CONNECTIONDATA.CONNECTIONSQLSERVER;
import SCHEMAOBJECT.MENU_DAO;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String maBan = request.getParameter("maBan");
        String tenBan = request.getParameter("tenBan");
        String phuongXa = request.getParameter("phuongXa");
        String type = request.getParameter("type");
        System.out.println("Raw query string: " + request.getQueryString());
System.out.println("Received parameters: maBan=" + maBan + ", tenBan=" + tenBan + ", phuongXa=" + phuongXa + ", type=" + type);
        if (maBan == null || tenBan == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu thông tin mã bàn hoặc tên bàn!");
            return;
        }
if (phuongXa != null && phuongXa.isEmpty()) {
            phuongXa = null;
        }
        String contextPath = request.getContextPath();
        String imagePathDrink = "images/drinks/";
        String imagePathFood = "images/food/";
        String defaultDrinkImage = imagePathDrink + "default_drink.png";
        String defaultFoodImage = imagePathFood + "default_food.png";
        Map<String, String> imageMapping = new HashMap<>();
        imageMapping.put("SP001", "Cocacola.png");
        imageMapping.put("SP002", "Pepsi.png");
        imageMapping.put("SP003", "Fanta.png");
        imageMapping.put("SP004", "Sprite.png");
        imageMapping.put("SP005", "Mirinda.png");
        imageMapping.put("SP006", "RedBull.png");
        imageMapping.put("SP007", "Heineken.png");
        imageMapping.put("SP008", "Tiger.png");
        imageMapping.put("SP009", "Saigon.png");
        imageMapping.put("SP010", "Budweiser.png");
        imageMapping.put("SP011", "Corona.png");
        imageMapping.put("SP012", "LauMamChauDoc.png");
        imageMapping.put("SP013", "HaoLongSon.png");
        imageMapping.put("SP014", "VitLangGiang.png");
        imageMapping.put("SP015", "RauDonBacKan.png");
        imageMapping.put("SP016", "BunNuocLeo.png");
        imageMapping.put("SP017", "NemBui.png");
        imageMapping.put("SP018", "BanhXeoOcGao.png");
        imageMapping.put("SP019", "BunChaCaQuyNhon.png");
        imageMapping.put("SP020", "GoiGaMangCut.png");
        imageMapping.put("SP021", "CaLangNuongSongBe.png");
        imageMapping.put("SP022", "BanhCanPhanThiet.png");
        imageMapping.put("SP023", "TomTichNuong.png");
        imageMapping.put("SP024", "BanhCongCanTho.png");
        imageMapping.put("SP025", "KhauNhucCaoBang.png");
        imageMapping.put("SP026", "MiQuang.png");
        imageMapping.put("SP027", "GaNuongBanDon.png");
        imageMapping.put("SP028", "CaSuoiKhoRieng.png");
        imageMapping.put("SP029", "MangDang.png");
        imageMapping.put("SP030", "GoiCaBienHoa.png");
        imageMapping.put("SP031", "LauCaLinhBongDienDien.png");
        imageMapping.put("SP032", "PhoKhoGiaLai.png");
        imageMapping.put("SP033", "ChaoAuTau.png");
        imageMapping.put("SP034", "BanhCuonChaNuong.png");
        imageMapping.put("SP035", "BunThang.png");
        imageMapping.put("SP036", "BanhBeoHaTinh.png");
        imageMapping.put("SP037", "BanhDucHaiDuong.png");
        imageMapping.put("SP038", "BanhDaCua.png");
        imageMapping.put("SP039", "LauMamHauGiang.png");
        imageMapping.put("SP040", "ComLam.png");
        imageMapping.put("SP041", "NhongOngRung.png");
        imageMapping.put("SP042", "BanhCanNhaTrang.png");
        imageMapping.put("SP043", "CaNhamNuong.png");
        imageMapping.put("SP044", "GoiLaKonTum.png");
        imageMapping.put("SP045", "ReuDaNuong.png");
        imageMapping.put("SP046", "DauDaLatTronKem.png");
        imageMapping.put("SP047", "VitQuayLangSon.png");
        imageMapping.put("SP048", "ThaoKe.png");
        imageMapping.put("SP049", "LauMamLongAn.png");
        imageMapping.put("SP050", "CheKho.png");
        imageMapping.put("SP051", "ChaoLuonVinh.png");
        imageMapping.put("SP052", "ComChayNinhBinh.png");
        imageMapping.put("SP053", "NhoNinhThuan.png");
        imageMapping.put("SP054", "BanhTai.png");
        imageMapping.put("SP055", "ChaDong.png");
        imageMapping.put("SP056", "BanhLoc.png");
        imageMapping.put("SP057", "CaoLauHoiAn.png");
        imageMapping.put("SP058", "DonQuangNgai.png");
        imageMapping.put("SP059", "ChaMucHaLong.png");
        imageMapping.put("SP060", "BunHen.png");
        imageMapping.put("SP061", "BanhPiaSocTrang.png");
        imageMapping.put("SP062", "CaHoiMocChau.png");
        imageMapping.put("SP063", "BanhTrangPhoiSuong.png");
        imageMapping.put("SP064", "BunCaRo.png");
        imageMapping.put("SP065", "CheKhoaiDeo.png");
        imageMapping.put("SP066", "NemChuaThanhHoa.png");
        imageMapping.put("SP067", "ComHen.png");
        imageMapping.put("SP068", "HuTieuMyTho.png");
        imageMapping.put("SP069", "ComTamSaiGon.png");
        imageMapping.put("SP070", "BunNuocLeoTraVinh.png");
        imageMapping.put("SP071", "CaChienSongLo.png");
        imageMapping.put("SP072", "BanhXeoVinhLong.png");
        imageMapping.put("SP073", "SuSuTamDaoXaoToi.png");
        imageMapping.put("SP074", "MangVauYenBai.png");

        if (type != null) {
        try {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            CONNECTIONSQLSERVER connector = new CONNECTIONSQLSERVER();
            MENU_BUS menuBus = new MENU_BUS(new MENU_DAO(connector), null);
            List<MENU> menus;

            if (type.equals("nuocUong")) {
                // Load danh sách nước uống (LSP01)
                menus = menuBus.getMenusByLoaiAndPhuongXa("LSP01", null);
            } else if (type.equals("dacSan")) {
                // Load danh sách đặc sản
                if (phuongXa != null && !phuongXa.isEmpty()) {
    
                    // Lấy các món thuộc phường/xã cụ thể
                    menus = menuBus.getMenusByLoaiAndPhuongXa("LSP%", phuongXa);
                } else {
                    // Lấy tất cả món (bao gồm cả LSP01)
                    menus = menuBus.getMenusByLoaiAndPhuongXa("LSP%", null);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Loại yêu cầu không hợp lệ!");
                return;
            }

            if (menus.isEmpty()) {
                out.println("<div class='col-md-12'><p>Không có món ăn nào!</p></div>");
                return;
            }

            for (MENU menu : menus) {
                String fileName = imageMapping.getOrDefault(menu.getMaSanPham_ID(), menu.getMaSanPham_ID() + ".png");
                String basePath = menu.getMaLoaiSanPham_ID().equals("LSP01") ? imagePathDrink : imagePathFood;
                String imagePath = contextPath + "/" + basePath + fileName;

                String realPath = getServletContext().getRealPath(imagePath.substring(contextPath.length()));
                File imageFile = new File(realPath);
                if (!imageFile.exists()) {
                    imagePath = contextPath + "/" + (menu.getMaLoaiSanPham_ID().equals("LSP01") ? defaultDrinkImage : defaultFoodImage);
                    realPath = getServletContext().getRealPath(imagePath.substring(contextPath.length()));
                    imageFile = new File(realPath);
                    if (!imageFile.exists()) {
                        imagePath = contextPath + "/" + basePath + "placeholder.png";
                    }
                }

                out.println("<div class='col-md-4 mb-3'>");
                out.println("<div class='card menu-card'>");
                out.println("<img src='" + imagePath + "' class='card-img-top' alt='" + menu.getTenSanPham() + "'>");
                out.println("<div class='card-body'>");
                out.println("<h5 class='card-title'>" + menu.getTenSanPham() + "</h5>");
                out.println("<p class='card-text'>Giá: " + menu.getDonGia() + " VNĐ</p>");
                out.println("<button class='btn btn-primary add-item' data-id='" + menu.getMaSanPham_ID() + 
                        "' data-name='" + menu.getTenSanPham() + "' data-price='" + menu.getDonGia() + "'>Thêm</button>");
                out.println("</div></div></div>");
            }
            return;
        } catch (SQLException ex) {
            Logger.getLogger(OrderServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi cơ sở dữ liệu!");
            return;
        }
    }

        request.getRequestDispatcher("order.jsp").forward(request, response);
        System.out.println("phuongXa parameter: " + phuongXa);
    }
}