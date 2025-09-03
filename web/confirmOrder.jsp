<%-- 
    Document   : confirmOrder
    Created on : May 25, 2025, 10:43:06 PM
    Author     : ADMIN
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Xác Nhận Gọi Món</title>
    <style>
        .form-container { max-width: 600px; margin: 20px auto; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid black; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .error { color: red; }
        .message { color: green; }
        .action-btn { padding: 10px 20px; margin: 10px 5px; border: none; border-radius: 4px; cursor: pointer; color: white; }
        .confirm-btn { background-color: #4CAF50; }
        .cancel-btn { background-color: #9E9E9E; }
        .total-row { font-weight: bold; }
    </style>
</head>
<body>
    <div class="form-container">
        <h2>Xác Nhận Gọi Món - Bàn: ${tenBan}</h2>
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty message}">
            <p class="message">${message}</p>
        </c:if>
        <form action="ConfirmOrderServlet" method="post" id="confirmForm">
            <input type="hidden" name="maBan" value="${maBan}">
            <input type="hidden" name="maHoaDon" value="${maHoaDon}">
            <input type="hidden" name="tenBan" value="${tenBan}">
            <table>
                <tr>
                    <th>Tên Sản Phẩm</th>
                    <th>Số Lượng</th>
                    <th>Đơn Giá</th>
                    <th>Thành Tiền</th>
                </tr>
                <c:set var="total" value="0"/>
                <c:forEach var="entry" items="${items}">
                    <c:set var="menu" value="${menuBus.getMenuById(entry.key)}"/>
                    <tr>
                        <td>${menu.tenSanPham}</td>
                        <td>${entry.value}</td>
                        <td><fmt:formatNumber value="${menu.donGia}" type="currency" currencySymbol="₫"/></td>
                        <td><fmt:formatNumber value="${menu.donGia * entry.value}" type="currency" currencySymbol="₫"/></td>
                        <input type="hidden" name="items" value="${entry.key}:${entry.value}">
                    </tr>
                    <c:set var="total" value="${total + (menu.donGia * entry.value)}"/>
                </c:forEach>
                <tr class="total-row">
                    <td colspan="3">Tổng cộng</td>
                    <td><fmt:formatNumber value="${total}" type="currency" currencySymbol="₫"/></td>
                </tr>
            </table>
            <c:if test="${not empty maHoaDon}">
                <p>Bàn đã có hóa đơn: ${maHoaDon}. Thêm món vào hóa đơn này?</p>
                <input type="hidden" name="confirmExisting" value="yes">
            </c:if>
            <input type="submit" name="action" value="Confirm" class="action-btn confirm-btn">
            <input type="submit" name="action" value="cancel" class="action-btn cancel-btn">
        </form>
        <a href="TableServlet">Quay lại danh sách bàn</a>
    </div>
</body>
</html>