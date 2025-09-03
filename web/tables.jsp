<%-- 
    Document   : tables
    Created on : May 25, 2025, 8:35:27 PM
    Author     : ADMIN
--%>

<%@page import="SCHEMACLASS.BAN"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, SCHEMACLASS.BAN" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Bàn</title>
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
            padding: 1rem;
        }
        .container {
            background-color: rgba(255, 255, 255, 0.95);
            border-radius: 1rem;
            box-shadow: 0 10px 15px rgba(0, 0, 0, 0.2);
            padding: 1.5rem;
            margin-top: 3rem;
            max-width: 80rem;
            width: 100%;
            position: relative;
        }
        .table-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(16rem, 1fr));
            gap: 1.5rem;
        }
        .table-card {
            border: 1px solid #d1d5db;
            border-radius: 0.5rem;
            padding: 1rem;
            text-align: center;
            transition: all 0.2s;
        }
        .table-card.occupied {
            background-color: #fee2e2;
        }
        .table-card.empty {
            background-color: #d1fae5;
        }
        .table-card.empty.selectable {
            cursor: pointer;
        }
        .table-card.empty.selectable:hover {
            background-color: #a7f3d0;
        }
        .table-card.selected {
            border: 2px solid #3b82f6;
        }
        .table-card h3 {
            margin: 0;
            font-size: 1.25rem;
            color: #374151;
        }
        .button-panel {
            margin-top: 1rem;
            display: flex;
            justify-content: center;
            gap: 0.5rem;
            flex-wrap: wrap;
        }
        .button-panel button {
            padding: 0.5rem 1rem;
            border-radius: 0.375rem;
            color: white;
            font-size: 0.875rem;
            transition: background-color 0.2s;
        }
        .view-btn {
            background-color: #06b6d4;
        }
        .view-btn:hover {
            background-color: #0891b2;
        }
        .switch-btn {
            background-color: #7c3aed;
        }
        .switch-btn:hover {
            background-color: #6d28d9;
        }
        .order-btn {
            background-color: #10b981;
        }
        .order-btn:hover {
            background-color: #059669;
        }
        .action-panel {
            margin-top: 1.5rem;
            text-align: center;
        }
        .action-panel button {
            padding: 0.75rem 1.5rem;
            margin: 0.25rem;
            background-color: #3b82f6;
            color: white;
            border-radius: 0.375rem;
            transition: background-color 0.2s;
        }
        .action-panel button:hover {
            background-color: #2563eb;
        }
        .error {
            background-color: #fee2e2;
            border: 1px solid #ef4444;
            border-radius: 0.5rem;
            padding: 0.75rem;
            margin-bottom: 1rem;
            color: #dc2626;
        }
        .text-gray-600 {
            margin-top: 1rem;
            margin-bottom: 2rem;
        }
        .nav-bar {
            background-color: #1f2937;
            padding: 1rem;
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
            color: white;
        }
        .nav-menu {
            display: flex;
            gap: 1.5rem;
        }
        .nav-menu a {
            color: white;
            font-size: 0.875rem;
            transition: color 0.2s;
        }
        .nav-menu a:hover {
            color: #3b82f6;
        }
        .hamburger {
            display: none;
            font-size: 1.5rem;
            cursor: pointer;
        }
        .mobile-menu {
            display: none;
            background-color: #1f2937;
            position: absolute;
            top: 4rem;
            left: 0;
            right: 0;
            flex-direction: column;
            padding: 1rem;
            z-index: 10;
        }
        .mobile-menu.active {
            display: flex;
        }
        .mobile-menu a {
            color: white;
            padding: 0.75rem;
            font-size: 0.875rem;
            text-align: center;
            transition: background-color 0.2s;
        }
        .mobile-menu a:hover {
            background-color: #374151;
        }
        @media (max-width: 640px) {
            .container {
                padding: 1rem;
                margin-top: 5rem;
            }
            .table-grid {
                grid-template-columns: 1fr;
                gap: 1rem;
            }
            .table-card h3 {
                font-size: 1rem;
            }
            .button-panel {
                flex-direction: column;
                gap: 0.5rem;
            }
            .button-panel button {
                width: 100%;
                padding: 0.75rem;
                font-size: 0.75rem;
            }
            .action-panel button {
                width: 100%;
                padding: 0.75rem;
                font-size: 0.875rem;
            }
            h2 {
                font-size: 1.5rem;
            }
            .nav-menu {
                display: none;
            }
            .hamburger {
                display: block;
            }
            .nav-bar {
                padding: 0.75rem;
            }
        }
    </style>
    <script>
        let selectedSourceTable = null;

        function startSwitchTable(maBan) {
            if (selectedSourceTable) {
                document.querySelector(`#table-${selectedSourceTable}`).classList.remove('selected');
            }
            selectedSourceTable = maBan;
            document.querySelector(`#table-${maBan}`).classList.add('selected');
            document.querySelectorAll('.table-card.empty').forEach(card => {
                card.classList.add('selectable');
            });
        }

        function confirmSwitchTable(maBanTo) {
            if (selectedSourceTable === maBanTo) {
                alert('Không thể đổi sang cùng bàn!');
                return;
            }

            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'SwitchTableServlet';
            form.style.display = 'none';

            const inputFrom = document.createElement('input');
            inputFrom.type = 'hidden';
            inputFrom.name = 'maBanFrom';
            inputFrom.value = selectedSourceTable;
            form.appendChild(inputFrom);

            const inputTo = document.createElement('input');
            inputTo.type = 'hidden';
            inputTo.name = 'maBanTo';
            inputTo.value = maBanTo;
            form.appendChild(inputTo);

            document.body.appendChild(form);
            form.submit();
        }

        function resetSelection() {
            if (selectedSourceTable) {
                document.querySelector(`#table-${selectedSourceTable}`).classList.remove('selected');
                selectedSourceTable = null;
            }
            document.querySelectorAll('.table-card.empty').forEach(card => {
                card.classList.remove('selectable');
            });
        }

        function toggleMenu() {
            const mobileMenu = document.querySelector('.mobile-menu');
            mobileMenu.classList.toggle('active');
        }
    </script>
</head>
<body>
    <nav class="nav-bar">
        <div class="hamburger" onclick="toggleMenu()">&#9776;</div>
        <div class="nav-menu">
            <a href="index.jsp">Trang chủ</a>
        </div>
        <div class="mobile-menu">
            <a href="index.jsp">Trang chủ</a>
        </div>
    </nav>
    <div class="container">
        <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Danh Sách Bàn</h2>
        <% 
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <p class="error"><%= error %></p>
        <% } %>
        <div class="table-grid">
            <% 
                List<BAN> banList = (List<BAN>) request.getAttribute("banList");
                if (banList != null) {
                    for (BAN ban : banList) {
            %>
                <div class="table-card <%= ban.isTrangThai() ? "occupied" : "empty" %>" 
                     id="table-<%= ban.getMaBan_ID() %>"
                     <%= !ban.isTrangThai() ? "onclick=\"confirmSwitchTable('" + ban.getMaBan_ID() + "')\"" : "" %>>
                    <h3><%= ban.getTenBan() %></h3>
                    <p class="text-gray-600">Mã Bàn: <%= ban.getMaBan_ID() %></p>

                    <div class="button-panel">
                        <% if (ban.isTrangThai()) { %>
                            <form action="ViewTableServlet" method="get" class="inline w-full">
                                <input type="hidden" name="maBan" value="<%= ban.getMaBan_ID() %>">
                                <input type="hidden" name="tenBan" value="<%= ban.getTenBan() %>">
                                <button type="submit" class="view-btn w-full">Xem</button>
                            </form>
                        <% } %>
                        <form action="OrderServlet" method="get" class="inline w-full">
                            <input type="hidden" name="maBan" value="<%= ban.getMaBan_ID() %>">
                            <input type="hidden" name="tenBan" value="<%= ban.getTenBan() %>">
                            <button type="submit" class="order-btn w-full">Gọi món</button>
                        </form>
                    </div>
                </div>
            <% 
                    }
                } else {
            %>
                <p class="text-gray-600 text-center">Không có dữ liệu bàn!</p>
            <% } %>
        </div>
    </div>
</body>
</html>