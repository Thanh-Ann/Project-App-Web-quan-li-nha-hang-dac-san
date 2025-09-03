/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SCHEMACLASS;

public class HOA_DON_CHI_TIET {
    private String MaHDCT_ID;
    private String MaHoaDon_ID;
    private String MaSanPham_ID;
    private double DonGia;
    private int SoLuong;

    // Constructor
    public HOA_DON_CHI_TIET() {}
    
    public HOA_DON_CHI_TIET(String MaHDCT_ID, String MaHoaDon_ID, String MaSanPham_ID, double DonGia, int SoLuong) {
        this.MaHDCT_ID = MaHDCT_ID;
        this.MaHoaDon_ID = MaHoaDon_ID;
        this.MaSanPham_ID = MaSanPham_ID;
        this.DonGia = DonGia;
        this.SoLuong = SoLuong;
    }

    // Getter & Setter
    public String getMaHDCT_ID() {
        return MaHDCT_ID;
    }

    public void setMaHDCT_ID(String MaHDCT_ID) {
        this.MaHDCT_ID = MaHDCT_ID;
    }

    public String getMaHoaDon_ID() {
        return MaHoaDon_ID;
    }

    public void setMaHoaDon_ID(String MaHoaDon_ID) {
        this.MaHoaDon_ID = MaHoaDon_ID;
    }

    public String getMaSanPham_ID() {
        return MaSanPham_ID;
    }

    public void setMaSanPham_ID(String MaSanPham_ID) {
        this.MaSanPham_ID = MaSanPham_ID;
    }

    public double getDonGia() {
        return DonGia;
    }

    public void setDonGia(double DonGia) {
        this.DonGia = DonGia;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    // toString()
    @Override
    public String toString() {
        return "HOA_DON_CHI_TIET{" + "MaHDCT_ID=" + MaHDCT_ID + ", MaHoaDon_ID=" + MaHoaDon_ID + ", MaSanPham_ID=" + MaSanPham_ID + ", DonGia=" + DonGia + ", SoLuong=" + SoLuong + '}';
    }
}