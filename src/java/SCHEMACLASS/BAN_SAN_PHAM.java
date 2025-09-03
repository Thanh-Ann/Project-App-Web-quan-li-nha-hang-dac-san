/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SCHEMACLASS;

public class BAN_SAN_PHAM {
    private String MaBan_ID;
    private String MaSanPham_ID;

    // Constructor
    public BAN_SAN_PHAM() {}
    
    public BAN_SAN_PHAM(String MaBan_ID, String MaSanPham_ID) {
        this.MaBan_ID = MaBan_ID;
        this.MaSanPham_ID = MaSanPham_ID;
    }

    // Getter & Setter
    public String getMaBan_ID() {
        return MaBan_ID;
    }

    public void setMaBan_ID(String MaBan_ID) {
        this.MaBan_ID = MaBan_ID;
    }

    public String getMaSanPham_ID() {
        return MaSanPham_ID;
    }

    public void setMaSanPham_ID(String MaSanPham_ID) {
        this.MaSanPham_ID = MaSanPham_ID;
    }

    // toString()
    @Override
    public String toString() {
        return "BAN_SAN_PHAM{" + "MaBan_ID=" + MaBan_ID + ", MaSanPham_ID=" + MaSanPham_ID + '}';
    }
}