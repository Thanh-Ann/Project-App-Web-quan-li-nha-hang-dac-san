/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SCHEMACLASS;

public class LOAI_SAN_PHAM {
    private String MaLoaiSanPham_ID;
    private String TenLoaiSanPham;

    // Constructor
    public LOAI_SAN_PHAM() {}
    
    public LOAI_SAN_PHAM(String MaLoaiSanPham_ID, String TenLoaiSanPham) {
        this.MaLoaiSanPham_ID = MaLoaiSanPham_ID;
        this.TenLoaiSanPham = TenLoaiSanPham;
    }

    // Getter & Setter
    public String getMaLoaiSanPham_ID() {
        return MaLoaiSanPham_ID;
    }

    public void setMaLoaiSanPham_ID(String MaLoaiSanPham_ID) {
        this.MaLoaiSanPham_ID = MaLoaiSanPham_ID;
    }

    public String getTenLoaiSanPham() {
        return TenLoaiSanPham;
    }

    public void setTenLoaiSanPham(String TenLoaiSanPham) {
        this.TenLoaiSanPham = TenLoaiSanPham;
    }

    // toString()
    @Override
    public String toString() {
        return "LOAI_SAN_PHAM{" + "MaLoaiSanPham_ID=" + MaLoaiSanPham_ID + ", TenLoaiSanPham=" + TenLoaiSanPham + '}';
    }
}