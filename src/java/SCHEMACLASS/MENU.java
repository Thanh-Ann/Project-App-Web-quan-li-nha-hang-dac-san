/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SCHEMACLASS;

public class MENU {
    private String MaSanPham_ID;
    private String TenSanPham;
    private String MaLoaiSanPham_ID;
    private String MaPhuongXa_ID;
    private double DonGia;
    private boolean TrangThai; // Sửa từ String → boolean nếu SQL là bit

    // Constructor
    public MENU(String MaSanPham_ID, String TenSanPham, String MaLoaiSanPham_ID, 
                String MaPhuongXa_ID, double DonGia, boolean TrangThai) {
        this.MaSanPham_ID = MaSanPham_ID;
        this.TenSanPham = TenSanPham;
        this.MaLoaiSanPham_ID = MaLoaiSanPham_ID;
        this.MaPhuongXa_ID = MaPhuongXa_ID;
        this.DonGia = DonGia;
        this.TrangThai = TrangThai;
    }

    public MENU() {
    }

    public String getMaSanPham_ID() {
        return MaSanPham_ID;
    }

    public String getTenSanPham() {
        return TenSanPham;
    }

    public String getMaLoaiSanPham_ID() {
        return MaLoaiSanPham_ID;
    }

    public String getMaPhuongXa_ID() {
        return MaPhuongXa_ID;
    }

    public double getDonGia() {
        return DonGia;
    }

    // Getter cho boolean
    public boolean isTrangThai() {
        return TrangThai;
    }

    // Setter
    public void setTrangThai(boolean TrangThai) {
        this.TrangThai = TrangThai;
    }

    public void setMaSanPham_ID(String MaSanPham_ID) {
        this.MaSanPham_ID = MaSanPham_ID;
    }

    public void setTenSanPham(String TenSanPham) {
        this.TenSanPham = TenSanPham;
    }

    public void setMaLoaiSanPham_ID(String MaLoaiSanPham_ID) {
        this.MaLoaiSanPham_ID = MaLoaiSanPham_ID;
    }

    public void setMaPhuongXa_ID(String MaPhuongXa_ID) {
        this.MaPhuongXa_ID = MaPhuongXa_ID;
    }

    public void setDonGia(double DonGia) {
        this.DonGia = DonGia;
    }

    
}