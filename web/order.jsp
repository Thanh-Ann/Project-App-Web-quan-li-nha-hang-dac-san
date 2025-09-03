<%-- order.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map, java.util.HashMap, SCHEMACLASS.MENU, SCHEMACLASS.PHUONG_XA, BUSINESSLOGIC.MENU_BUS, BUSINESSLOGIC.PHUONG_XA_BUS, CONNECTIONDATA.CONNECTIONSQLSERVER, SCHEMAOBJECT.MENU_DAO, SCHEMAOBJECT.PHUONG_XA_DAO" %>
<%
    String maBan = request.getParameter("maBan");
    String tenBan = request.getParameter("tenBan");
    CONNECTIONSQLSERVER connector = new CONNECTIONSQLSERVER();
    MENU_BUS menuBus = new MENU_BUS(new MENU_DAO(connector), null);
    PHUONG_XA_BUS phuongXaBus = new PHUONG_XA_BUS(new PHUONG_XA_DAO(connector), new MENU_DAO(connector));
    
    List<MENU> dacSanMenus = menuBus.getMenusByLoaiAndPhuongXa("LSP%", null);
    List<MENU> nuocUongMenus = menuBus.getMenusByLoaiAndPhuongXa("LSP01", null);
    List<PHUONG_XA> phuongXas = phuongXaBus.getAllPhuongXa();
    
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

    // Reset giỏ hàng trong session khi mở bàn mới
    session.setAttribute("cart_" + maBan, new HashMap<String, Map<String, Object>>());
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gọi Món - Bàn: <%= tenBan %></title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" 
          integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <style>

    body { background-color: #f0f0f0; }
    .container { max-width: 1200px; margin: auto; padding: 20px; }
    .menu-card { height: 350px; text-align: center; margin-bottom: 20px; }
    .menu-card img { height: 150px; object-fit: cover; }
    .selected-items {
        min-height: 200px;
        max-height: 400px;
        overflow-y: auto;
        padding: 10px;
    }
    .table {
        font-size: 14px;
        margin-bottom: 0;
    }
    .table th, .table td {
        vertical-align: middle;
        padding: 8px;
    }
    .table tbody tr:hover {
        cursor: pointer;
        background-color: #f0f0f0;
    }
    .table .active {
        background-color: #d3d3d3;
    }
    .empty-row td {
        text-align: center;
        color: #666;
    }
    .tab-content { min-height: 300px; }
    .card-footer { display: flex; justify-content: space-between; padding: 10px; }
    .btn { margin: 0 5px; }
</style>
    </style>
</head>
<body>
    <div class="container">
        <h2 class="text-center text-primary">GỌI MÓN - <%= tenBan %></h2>
        <div class="row">
            <!-- Danh sách món đã chọn -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header text-center"><h5>MÓN ĐÃ CHỌN</h5></div>
                    <div class="card-body selected-items">
                        <table class="table table-bordered table-hover" id="orderItemsTable">
                            <thead>
                                <tr>
                                    <th scope="col">Tên món</th>
                                    <th scope="col">Số lượng</th>
                                    <th scope="col">Giá (VNĐ)</th>
                                </tr>
                            </thead>
                            <tbody id="tableBody">
                                <tr class="empty-row">
                                    <td colspan="3" class="text-center">-</td>
                                </tr>
                            </tbody>
                        </table>
                        <p class="mt-3 text-center"><strong>Tổng tiền: <span id="totalAmount">0</span> VNĐ</strong></p>
                    </div>
                    <div class="card-footer">
                        <button class="btn btn-danger" onclick="removeSelectedItem()">Xóa</button>
                        <button class="btn btn-primary" onclick="changeQuantity(true)">+</button>
                        <button class="btn btn-primary" onclick="changeQuantity(false)">-</button>
                        <button class="btn btn-success" onclick="confirmOrder()">Xác nhận</button>
                        <button class="btn btn-secondary" onclick="window.location.href='TableServlet?action=reset'">Hủy</button>
                    </div>
                </div>
            </div>
            <!-- Danh sách món -->
            <div class="col-md-8">
                <ul class="nav nav-tabs">
                    <li class="nav-item">
                        <a class="nav-link active" data-bs-toggle="tab" href="#dacSan" onclick="loadDacSanItems()">ĐẶC SẢN</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" data-bs-toggle="tab" href="#nuocUong" onclick="loadNuocUongItems()">NƯỚC UỐNG</a>
                    </li>
                </ul>
                <div class="tab-content mt-3">
                    <!-- Tab Đặc sản -->
                    <div class="tab-pane fade show active" id="dacSan">
                        <div class="mb-3">
                            <label for="phuongXaSelect" class="form-label">Phường/Xã:</label>
                            <select id="phuongXaSelect" class="form-select" onchange="loadDacSanItems()">
                                <option value="">Tất cả</option>
                                <% for (PHUONG_XA px : phuongXas) { %>
                                    <option value="<%= px.getMaPhuongXa_ID() %>"><%= px.getTenPhuongXa() %> (<%= px.getMaPhuongXa_ID() %>)</option>
                                <% } %>
                            </select>
                        </div>
                        <div class="row" id="dacSanItems">
                            <!-- Danh sách món đặc sản sẽ được load bằng AJAX -->
                        </div>
                    </div>
                    <!-- Tab Nước uống -->
                    <div class="tab-pane fade" id="nuocUong">
                        <div class="row" id="nuocUongItems">
                            <!-- Danh sách nước uống sẽ được load bằng AJAX -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" 
            integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
    <script>
        if (!window.fetch) {
            document.write('<script src="https://cdnjs.cloudflare.com/ajax/libs/fetch/3.6.2/fetch.min.js"><\/script>');
        }
    </script>
    <script>
        let selectedItems = {};

        function addItem(id, name, price) {
            if (!selectedItems[id]) {
                selectedItems[id] = { name: name, price: parseFloat(price), quantity: 0 };
            }
            selectedItems[id].quantity++;
            updateSelectedItemsList();
            saveCartToSession();
            console.log("Added item:", id, name, price);
        }

        function removeSelectedItem() {
            const selected = document.querySelector('#orderItemsTable tbody tr.active');
            if (!selected) {
                alert('Vui lòng chọn món để xóa!');
                return;
            }
            const id = selected.dataset.id;
            delete selectedItems[id];
            updateSelectedItemsList();
            saveCartToSession();
        }

        function changeQuantity(increase) {
            const selected = document.querySelector('#orderItemsTable tbody tr.active');
            if (!selected) {
                alert('Vui lòng chọn món để thay đổi số lượng!');
                return;
            }
            const id = selected.dataset.id;
            if (increase) {
                selectedItems[id].quantity++;
            } else if (selectedItems[id].quantity > 1) {
                selectedItems[id].quantity--;
            } else {
                delete selectedItems[id];
            }
            updateSelectedItemsList();
            saveCartToSession();
        }

        function updateSelectedItemsList() {
            const tbody = document.getElementById('tableBody');
            if (!tbody) {
                console.error("Element #tableBody not found!");
                return;
            }
            tbody.innerHTML = ''; // Xóa nội dung cũ
            let total = 0;
            let itemCount = 0;

            for (const id in selectedItems) {
                const item = selectedItems[id];
                if (item && item.quantity > 0) {
                    itemCount++;
                    const tr = document.createElement('tr');
                    tr.dataset.id = id;

                    const nameTd = document.createElement('td');
                    nameTd.textContent = item.name || 'Không có tên';
                    tr.appendChild(nameTd);

                    const quantityTd = document.createElement('td');
                    quantityTd.textContent = item.quantity.toString();
                    tr.appendChild(quantityTd);

                    const priceTd = document.createElement('td');
                    priceTd.textContent = (item.price * item.quantity).toFixed(0);
                    tr.appendChild(priceTd);

                    tr.addEventListener('click', function(e) {
                        document.querySelectorAll('#orderItemsTable tbody tr').forEach(row => row.classList.remove('active'));
                        tr.classList.add('active');
                    });
                    tbody.appendChild(tr);
                    total += item.price * item.quantity;
                }
            }

            while (tbody.rows.length < 3) {
                const tr = document.createElement('tr');
                tr.className = 'empty-row';
                tr.innerHTML = '<td colspan="3" class="text-center">-</td>';
                tbody.appendChild(tr);
            }

            document.getElementById('totalAmount').textContent = total.toFixed(0) || 0;
            console.log("Updated table:", selectedItems, "Total:", total, "Item count:", itemCount, "tbody rows:", tbody.rows.length);
        }

        function saveCartToSession() {
            fetch('SaveCartServlet?maBan=<%= maBan %>', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(selectedItems)
            })
            .then(response => response.json())
            .then(result => console.log("Cart saved to session:", result))
            .catch(error => console.error("Error saving cart:", error));
        }

        function confirmOrder() {
            if (Object.keys(selectedItems).length === 0) {
                alert('Vui lòng chọn ít nhất một món!');
                return;
            }
            const data = { maBan: '<%= maBan %>', items: selectedItems };
            fetch('ConfirmOrderServlet', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    alert('Gọi món thành công! Mã hóa đơn: ' + result.maHoaDon);
                    selectedItems = {};
                    updateSelectedItemsList();
                    saveCartToSession();
                    window.location.href = 'TableServlet?action=refresh';
                } else {
                    alert('Lỗi: ' + result.message);
                }
            })
            .catch(error => {
                alert('Lỗi khi xác nhận đơn hàng!');
                console.error('Error:', error);
            });
        }

        function loadDacSanItems() {
    const phuongXaSelect = document.getElementById('phuongXaSelect');
    if (!phuongXaSelect) {
        console.error("Element #phuongXaSelect not found!");
        document.getElementById('dacSanItems').innerHTML = '<div class="col-md-12"><p>Lỗi: Không tìm thấy combobox phường/xã!</p></div>';
        return;
    }
    const phuongXa = encodeURIComponent(phuongXaSelect.value || '');
    console.log("phuongXaSelect.value:", phuongXaSelect.value);
    console.log("Encoded phuongXa:", phuongXa);
    fetch(`OrderServlet?maBan=<%= maBan %>&tenBan=<%= tenBan %>&phuongXa=${phuongXa}&type=dacSan`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.text();
        })
        .then(data => {
            const dacSanItems = document.getElementById('dacSanItems');
            dacSanItems.innerHTML = data || '<div class="col-md-12"><p>Không có món ăn nào!</p></div>';
            document.querySelectorAll('.add-item').forEach(btn => {
                btn.addEventListener('click', function() {
                    addItem(this.dataset.id, this.dataset.name, parseFloat(this.dataset.price));
                });
            });
        })
        .catch(error => {
            console.error("Error loading đặc sản items:", error);
            document.getElementById('dacSanItems').innerHTML = '<div class="col-md-12"><p>Lỗi khi tải danh sách món: ' + error.message + '</p></div>';
        });
}

document.addEventListener('DOMContentLoaded', () => {
    const phuongXaSelect = document.getElementById('phuongXaSelect');
    if (phuongXaSelect) {
        console.log("Initial phuongXaSelect.value:", phuongXaSelect.value);
        loadDacSanItems();
    } else {
        console.error("phuongXaSelect not found on page load");
    }
});

function loadNuocUongItems() {
    console.log("Loading nước uống"); // Debug
    fetch(`OrderServlet?maBan=<%= maBan %>&tenBan=<%= tenBan %>&type=nuocUong`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.text();
        })
        .then(data => {
            const nuocUongItems = document.getElementById('nuocUongItems');
            nuocUongItems.innerHTML = data || '<div class="col-md-12"><p>Không có nước uống nào!</p></div>';
            document.querySelectorAll('.add-item').forEach(btn => {
                btn.addEventListener('click', function() {
                    addItem(this.dataset.id, this.dataset.name, parseFloat(this.dataset.price));
                });
            });
        })
        .catch(error => {
            console.error("Error loading nước uống items:", error);
            document.getElementById('nuocUongItems').innerHTML = '<div class="col-md-12"><p>Lỗi khi tải danh sách nước uống!</p></div>';
        });
}



        document.getElementById('orderItemsTable').addEventListener('click', function(e) {
            const tr = e.target.closest('tr');
            if (tr && !tr.classList.contains('empty-row')) {
                document.querySelectorAll('#orderItemsTable tbody tr').forEach(row => row.classList.remove('active'));
                tr.classList.add('active');
            }
        });

      // Load danh sách đặc sản khi trang được tải
        document.addEventListener('DOMContentLoaded', loadDacSanItems);
        updateSelectedItemsList();
    </script>
</body>
</html>