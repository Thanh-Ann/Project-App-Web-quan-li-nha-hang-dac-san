<%-- 
    Document   : viewTable
    Created on : May 25, 2025, 8:38:18 PM
    Author     : ADMIN
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi Tiết Bàn</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <style>
        body {
            background-image: url('https://images.unsplash.com/photo-1557683316-973673baf926?ixlib=rb-4.0.3&auto=format&fit=crop&w=1920&q=80');
            background-size: cover;
            background-position: center;
            background-attachment: fixed;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .container {
            background-color: rgba(255, 255, 255, 0.95);
            border-radius: 1rem;
            box-shadow: 0 10px 15px rgba(0, 0, 0, 0.2);
            padding: 2rem;
            max-width: 36rem;
            width: 100%;
        }
        .error {
            background-color: #fee2e2;
            border: 1px solid #ef4444;
            border-radius: 0.5rem;
            padding: 0.75rem;
            margin-bottom: 1rem;
        }
        table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            border-radius: 0.5rem;
            overflow: hidden;
        }
        th, td {
            padding: 0.75rem;
            text-align: left;
        }
        th {
            background-color: #f3f4f6;
            font-weight: 600;
            color: #374151;
        }
        tr:nth-child(even) {
            background-color: #f9fafb;
        }
        .total-row {
            font-weight: 600;
            background-color: #e5e7eb;
        }
        .action-btn {
            background-color: #10b981;
            color: white;
            padding: 0.5rem 1.5rem;
            border-radius: 0.375rem;
            transition: background-color 0.2s;
        }
        .action-btn:hover {
            background-color: #059669;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Chi Tiết Bàn: ${tenBan != null ? tenBan : 'Không xác định'}</h2>
        <c:if test="${not empty error}">
            <p class="error text-red-600">${error}</p>
        </c:if>
        <c:if test="${empty chiTietList}">
            <p class="text-gray-600 text-center">Không có món nào được gọi!</p>
        </c:if>
        <c:if test="${not empty chiTietList}">
            <p class="text-gray-700 mb-4">Mã Hóa Đơn: ${maHoaDon}</p>
            <table>
                <thead>
                    <tr>
                        <th>Tên Món</th>
                        <th>Số Lượng</th>
                        <th>Đơn Giá</th>
                        <th>Thành Tiền</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="total" value="0"/>
                    <c:forEach var="chiTiet" items="${chiTietList}">
                        <c:set var="menu" value="${menuMap[chiTiet.maSanPham_ID]}"/>
                        <tr>
                            <td>${menu.tenSanPham}</td>
                            <td>${chiTiet.soLuong}</td>
                            <td><fmt:formatNumber value="${chiTiet.donGia}" type="currency" currencySymbol="₫"/></td>
                            <td><fmt:formatNumber value="${chiTiet.donGia * chiTiet.soLuong}" type="currency" currencySymbol="₫"/></td>
                        </tr>
                        <c:set var="total" value="${total + (chiTiet.donGia * chiTiet.soLuong)}"/>
                    </c:forEach>
                    <tr class="total-row">
                        <td colspan="3">Tổng Tiền</td>
                        <td><fmt:formatNumber value="${total}" type="currency" currencySymbol="₫"/></td>
                    </tr>
                </tbody>
            </table>
            <form action="PaymentServlet" method="get" class="mt-6 text-center">
                <input type="hidden" name="maBan" value="${maBan}">
                <input type="hidden" name="maHoaDon" value="${maHoaDon}">
                <input type="hidden" name="tenBan" value="${tenBan}">
                <input type="hidden" name="tongTien" value="${total}">
            </form>
        </c:if>
        <div class="text-center mt-4">
            <a href="TableServlet" class="text-blue-600 hover:underline">Quay lại</a>
        </div>
    </div>
</body>
</html>