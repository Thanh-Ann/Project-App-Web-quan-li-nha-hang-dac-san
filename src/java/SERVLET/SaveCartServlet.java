package SERVLET;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@WebServlet("/SaveCartServlet")
public class SaveCartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        HttpSession session = request.getSession();
        String maBan = request.getParameter("maBan");

        // Đọc dữ liệu JSON từ request
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }
        Gson gson = new Gson();
        Map<String, Map<String, Object>> cart = gson.fromJson(sb.toString(), new TypeToken<Map<String, Map<String, Object>>>(){}.getType());

        // Lưu giỏ hàng vào session theo mã bàn
        session.setAttribute("cart_" + maBan, cart);

        // Trả về phản hồi
        response.getWriter().write(gson.toJson(new Response(true, "Cart saved successfully")));
    }

    private static class Response {
        boolean success;
        String message;

        Response(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}