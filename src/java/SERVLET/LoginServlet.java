package SERVLET;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final String ADMIN_USERNAME = "user";
    private static final String ADMIN_PASSWORD = "123456";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            // Login successful, create session
            HttpSession session = request.getSession();
            session.setAttribute("user", username);
            // Redirect to TableServlet
            response.sendRedirect("TableServlet");
        } else {
            // Login failed, show error in modal
            request.setAttribute("loginError", "Tài khoản hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}