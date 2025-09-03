<%-- 
    Document   : payment
    Created on : May 25, 2025, 8:42:52 PM
    Author     : ADMIN
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh Toán</title>
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
            max-width: 28rem;
            width: 100%;
        }
        .error {
            background-color: #fee2e2;
            border: 1px solid #ef4444;
            border-radius: 0.5rem;
            padding: 0.75rem;
            margin-bottom: 1rem;
        }
        .form-group label {
            font-weight: 600;
            color: #374151;
        }
        .form-group input, .form-group select {
            border: 1px solid #d1d5db;
            border-radius: 0.375rem;
            padding: 0.5rem;
            width: 100%;
            transition: border-color 0.2s;
        }
        .form-group input:focus, .form-group select:focus {
            border-color: #3b82f6;
            outline: none;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
        }
        .change {
            color: #16a34a;
            font-weight: 600;
        }
        .action-panel button:disabled {
            background-color: #9ca3af;
            cursor: not-allowed;
        }
    </style>
    <script>
        function updateChange() {
            const total = parseFloat(document.getElementById("total").value.replace(/,/g, '')) || 0;
            const received = parseFloat(document.getElementById("received").value) || 0;
            const change = received - total;
            document.getElementById("change").innerText = change.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
            const valid = change >= 0 && document.getElementById("name").value.trim() !== "" && document.getElementById("phone").value.trim() !== "";
            document.getElementById("confirmBtn").disabled = !valid;
        }
    </script>
</head>
<body>
    <div class="container">
        <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Thanh Toán - ${tenBan}</h2>
        <c:if test="${not empty error}">
            <p class="error text-red-600">${error}</p>
        </c:if>
        <form action="PaymentServlet" method="post" class="space-y-4">
            <input type="hidden" name="maBan" value="${maBan}">
            <input type="hidden" name="maHoaDon" value="${maHoaDon}">
            <input type="hidden" name="tenBan" value="${tenBan}">
            <div class="form-group flex flex-col">
                <label class="mb-1">Bàn:</label>
                <input type="text" value="${tenBan}" disabled class="bg-gray-100 cursor-not-allowed">
            </div>
            <div class="form-group flex flex-col">
                <label class="mb-1">Tổng tiền:</label>
                <input type="text" id="total" value="${String.format('%,.2f', tongTien)}" disabled class="bg-gray-100 cursor-not-allowed">
            </div>
            <div class="form-group flex flex-col">
                <label class="mb-1">Phương thức TT:</label>
                <select name="payMethod" class="w-full">
                    <option value="Tiền mặt">Tiền mặt</option>
                    <option value="Thẻ tín dụng">Thẻ tín dụng</option>
                    <option value="Chuyển khoản">Chuyển khoản</option>
                </select>
            </div>
            <div class="form-group flex flex-col">
                <label class="mb-1">Họ tên:</label>
                <input type="text" id="name" name="name" onkeyup="updateChange()" required>
            </div>
            <div class="form-group flex flex-col">
                <label class="mb-1">Số ĐT:</label>
                <input type="text" id="phone" name="phone" onkeyup="updateChange()" required>
            </div>
            <div class="form-group flex flex-col">
                <label class="mb-1">Tiền nhận:</label>
                <input type="number" id="received" name="received" step="0.01" onkeyup="updateChange()" required>
            </div>
            <div class="form-group flex flex-col">
                <label class="mb-1">Tiền thừa:</label>
                <span id="change" class="change">0 VNĐ</span>
            </div>
            <div class="action-panel flex justify-center space-x-4">
                <button type="submit" id="confirmBtn" disabled class="bg-blue-600 text-white px-6 py-2 rounded-md hover:bg-blue-700 transition">Xác nhận</button>
                <a href="TableServlet" class="text-blue-600 hover:underline">Quay lại</a>
            </div>
        </form>
    </div>
</body>
</html>