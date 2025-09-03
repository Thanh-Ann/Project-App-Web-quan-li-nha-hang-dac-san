<%-- 
    Document   : index
    Created on : June 04, 2025, 1:34:00 AM
    Author     : ADMIN
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nhà Hàng Đặc Sản Việt</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        *{
            scroll-behavior: smooth;
        }
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            overflow-x: hidden;
        }
        .hero {
            position: relative;
            height: 100vh;
            background: #000;
            overflow: hidden;
        }
        .hero-slide {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-size: cover;
            background-position: center;
            opacity: 0;
            transition: opacity 1s ease-in-out;
        }
        .hero-slide.active {
            opacity: 1;
        }
        .hero-overlay {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            text-align: center;
            color: white;
        }
        .nav-menu {
            position: fixed;
            top: 0;
            width: 100%;
            background: rgba(0, 0, 0, 0.8);
            z-index: 50;
            transition: background 0.3s;
        }
        .nav-menu.scrolled {
            background: rgba(0, 0, 0, 0.95);
        }
        .nav-menu a {
            color: white;
            transition: color 0.3s, transform 0.3s;
        }
        .nav-menu a:hover {
            color: #4CAF50;
            transform: scale(1.1);
        }
        .section {
            padding: 4rem 2rem;
            max-width: 1200px;
            margin: 0 auto;
        }
        .dish-card {
            transition: transform 0.3s, box-shadow 0.3s;
        }
        .dish-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
        }
        .review-card {
            background: #f9f9f9;
            padding: 1.5rem;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s;
        }
        .review-card:hover {
            transform: scale(1.05);
        }
        .fade-in {
            opacity: 0;
            transform: translateY(20px);
            transition: opacity 0.5s, transform 0.5s;
        }
        .fade-in.visible {
            opacity: 1;
            transform: translateY(0);
        }
        .social-icons a {
            font-size: 2.5rem;
            margin: 0 1.5rem;
            color: #4CAF50;
            transition: color 0.3s, transform 0.3s;
        }
        .social-icons a:hover {
            color: #45a049;
            transform: scale(1.2);
        }
        /* Login Modal Styles */
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            z-index: 100;
            justify-content: center;
            align-items: center;
        }
        .modal-content {
            background: white;
            padding: 2rem;
            border-radius: 10px;
            width: 100%;
            max-width: 400px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            position: relative;
        }
        .modal-content input {
            width: 100%;
            padding: 0.75rem;
            margin-bottom: 1rem;
            border: 1px solid #ccc;
            border-radius: 5px;
            transition: border-color 0.3s;
        }
        .modal-content input:focus {
            border-color: #4CAF50;
            outline: none;
        }
        .modal-content button {
            width: 100%;
            padding: 0.75rem;
            background: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background 0.3s;
        }
        .modal-content button:hover {
            background: #45a049;
        }
        .close-btn {
            position: absolute;
            top: 0.5rem;
            right: 0.5rem;
            font-size: 1.5rem;
            cursor: pointer;
            color: #333;
        }
        .error {
            color: red;
            text-align: center;
            margin-bottom: 1rem;
            display: none;
        }
        
    </style>
</head>
<body>
    <!-- Navigation Menu -->
    <nav class="nav-menu py-4">
        <div class="max-w-7xl mx-auto flex justify-between items-center px-4">
            <h1 class="text-2xl font-bold text-white">Đặc Sản Việt</h1>
            <div class="space-x-6">
                <a href="#home" class="text-lg">Trang chủ</a>
                <a href="#" class="text-lg" onclick="showLoginModal()">Đặt bàn</a>
                <a href="#dishes" class="text-lg">Thực đơn nổi bật</a>
                <a href="#reviews" class="text-lg">Đánh giá</a>
                <a href="#contact" class="text-lg">Liên hệ</a>
            </div>
        </div>
    </nav>

    <!-- Hero Section with Slideshow -->
    <section id="home" class="hero">
        <div class="hero-slide active" style="background-image: url('https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1920&q=80')"></div>
        <div class="hero-slide" style="background-image: url('https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=1920&q=80')"></div>
        <div class="hero-slide" style="background-image: url('https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=1920&q=80')"></div>
        <div class="hero-overlay">
            <h1 class="text-5xl font-bold mb-4 animate-bounce">Nhà Hàng Đặc Sản Việt</h1>
            <p class="text-xl mb-6 max-w-2xl">
                Thưởng thức hương vị truyền thống Việt Nam với những món ăn đặc sản được chế biến từ nguyên liệu tươi ngon, trong không gian ấm cúng và sang trọng.
            </p>
            <a href="#dishes" class="bg-green-500 text-white px-6 py-3 rounded-full hover:bg-green-600 transition">Khám phá thực đơn</a>
        </div>
    </section>

    <!-- Featured Dishes Section -->
    <section id="dishes" class="section bg-gray-100">
        <h2 class="text-3xl font-bold text-center mb-8 fade-in">Thực Đơn Nổi Bật</h2>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div class="dish-card bg-white rounded-lg overflow-hidden fade-in">
                <img src="https://vietfood.org.vn/wp-content/uploads/2020/12/Pho1.jpg" alt="Phở Bò" class="w-full h-48 object-cover">
                <div class="p-4">
                    <h3 class="text-xl font-semibold">Phở Bò</h3>
                    <p class="text-gray-600">Món phở truyền thống với nước dùng thơm lừng, thịt bò mềm và bánh phở dai ngon.</p>
                </div>
            </div>
            <div class="dish-card bg-white rounded-lg overflow-hidden fade-in">
                <img src="https://kyluc.vn/Userfiles/Upload/images/Download/2019/8/16/7cea0a7d58424ce89828737ca70a8458.jpg" alt="Bún Chả" class="w-full h-48 object-cover">
                <div class="p-4">
                    <h3 class="text-xl font-semibold">Bún Chả</h3>
                    <p class="text-gray-600">Chả nướng thơm lừng ăn kèm bún tươi và nước mắm chua ngọt đậm đà.</p>
                </div>
            </div>
            <div class="dish-card bg-white rounded-lg overflow-hidden fade-in">
                <img src="https://cdn3.ivivu.com/2022/09/den-pho-nui-pleiku-an-pho-kho-gia-lai-02.jpg" alt="Phở Khô Gia Lai" class="w-full h-48 object-cover">
                <div class="p-4">
                    <h3 class="text-xl font-semibold">Phở Khô Gia Lai</h3>
                    <p class="text-gray-600">Thơm ngon trong từng hương vị.</p>
                </div>
            </div>
        </div>
    </section>

    <!-- Customer Reviews Section -->
    <section id="reviews" class="section bg-white">
        <h2 class="text-3xl font-bold text-center mb-8 fade-in">Đánh Giá Từ Khách Hàng</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div class="review-card fade-in">
                <p class="text-gray-700 italic">"Món ăn tuyệt vời, không gian ấm cúng, nhân viên thân thiện. Chắc chắn sẽ quay lại!"</p>
                <p class="text-right font-semibold mt-2">– Thái Thanh Tú</p>
            </div>
            <div class="review-card fade-in">
                <p class="text-gray-700 italic">"Phở bò ở đây là ngon nhất tôi từng thử, nước dùng rất đậm đà và thơm!"</p>
                <p class="text-right font-semibold mt-2">– Nguyễn Anh Tuấn</p>
            </div>
        </div>
    </section>

    <!-- Contact Section -->
    <section id="contact" class="section bg-gray-100">
        <h2 class="text-3xl font-bold text-center mb-8 fade-in">Liên Hệ Với Chúng Tôi</h2>
        <div class="max-w-lg mx-auto text-center">
            <div class="mb-6">
                <p><i class="fas fa-map-marker-alt mr-2"></i> 123 Đường Ẩm Thực, TP. Hồ Chí Minh</p>
                <p><i class="fas fa-phone mr-2"></i> (028) 1234-5678</p>
                <p><i class="fas fa-envelope mr-2"></i> contact@dacsanviet.com</p>
            </div>
            <div class="social-icons">
                <a href="https://www.facebook.com" target="_blank" aria-label="Facebook"><i class="fab fa-facebook-f"></i></a>
                <a href="https://zalo.me" target="_blank" aria-label="Zalo"><i class="fab fa-whatsapp"></i></a>
                <a href="https://x.com" target="_blank" aria-label="X"><i class="fab fa-x-twitter"></i></a>
                <a href="https://www.instagram.com" target="_blank" aria-label="Instagram"><i class="fab fa-instagram"></i></a>
                <a href="https://www.youtube.com" target="_blank" aria-label="YouTube"><i class="fab fa-youtube"></i></a>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="bg-gray-800 text-white py-6 text-center">
        <p>© 2025 Nhà Hàng Đặc Sản Việt. All Rights Reserved.</p>
        <div class="mt-2">
            <a href="https://www.facebook.com/dacsanviet" class="text-green-400 mx-2" aria-label="Facebook"><i class="fab fa-facebook-f"></i></a>
            <a href="https://zalo.me/dacsanviet" class="text-green-400 mx-2" aria-label="Zalo"><i class="fab fa-whatsapp"></i></a>
            <a href="https://x.com/dacsanviet" class="text-green-400 mx-2" aria-label="X"><i class="fab fa-x-twitter"></i></a>
            <a href="https://www.instagram.com/dacsanviet" class="text-green-400 mx-2" aria-label="Instagram"><i class="fab fa-instagram"></i></a>
            <a href="https://www.youtube.com/@dacsanviet" class="text-green-400 mx-2" aria-label="YouTube"><i class="fab fa-youtube"></i></a>
        </div>
    </footer>

    <!-- Login Modal -->
    <div id="loginModal" class="modal">
        <div class="modal-content">
            <span class="close-btn" onclick="closeLoginModal()">&times;</span>
            <h2 class="text-2xl font-bold text-center mb-4">Đăng Nhập</h2>
            <p id="loginError" class="error">Tài khoản hoặc mật khẩu không đúng!</p>
            <form action="LoginServlet" method="post">
                <input type="text" name="username" placeholder="Tài khoản" required>
                <input type="password" name="password" placeholder="Mật khẩu" required>
                <button type="submit">Đăng nhập</button>
            </form>
        </div>
    </div>

    <script>
        // Slideshow for Hero Section
        const slides = document.querySelectorAll('.hero-slide');
        let currentSlide = 0;

        function showSlide(index) {
            slides.forEach((slide, i) => {
                slide.classList.toggle('active', i === index);
            });
        }

        function nextSlide() {
            currentSlide = (currentSlide + 1) % slides.length;
            showSlide(currentSlide);
        }

        setInterval(nextSlide, 5000);
        showSlide(currentSlide);

        // Fade-in animation on scroll
        const fadeIns = document.querySelectorAll('.fade-in');
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('visible');
                }
            });
        }, { threshold: 0.2 });

        fadeIns.forEach(element => observer.observe(element));

        // Sticky nav effect
        window.addEventListener('scroll', () => {
            const nav = document.querySelector('.nav-menu');
            nav.classList.toggle('scrolled', window.scrollY > 50);
        });

        // Login Modal Functions
        function showLoginModal() {
            document.getElementById('loginModal').style.display = 'flex';
            document.getElementById('loginError').style.display = 'none';
        }

        function closeLoginModal() {
            document.getElementById('loginModal').style.display = 'none';
        }

        // Check for login error from servlet
        <% if (request.getAttribute("loginError") != null) { %>
            showLoginModal();
            document.getElementById('loginError').style.display = 'block';
        <% } %>
    </script>
</body>
</html>