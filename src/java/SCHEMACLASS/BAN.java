/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SCHEMACLASS;

public class BAN {
    private String MaBan_ID;
    private String MaLoaiBan_ID;
    private String MaKhuVuc_ID;
    private String TenBan;
    private boolean TrangThai;

    // Constructor
    public BAN() {}
    
    public BAN(String MaBan_ID, String MaLoaiBan_ID, String MaKhuVuc_ID, String TenBan, boolean TrangThai) {
        this.MaBan_ID = MaBan_ID;
        this.MaLoaiBan_ID = MaLoaiBan_ID;
        this.MaKhuVuc_ID = MaKhuVuc_ID;
        this.TenBan = TenBan;
        this.TrangThai = TrangThai;
    }

    // Getter & Setter
    public String getMaBan_ID() {
        return MaBan_ID;
    }

    public void setMaBan_ID(String MaBan_ID) {
        this.MaBan_ID = MaBan_ID;
    }

    public String getMaLoaiBan_ID() {
        return MaLoaiBan_ID;
    }

    public void setMaLoaiBan_ID(String MaLoaiBan_ID) {
        this.MaLoaiBan_ID = MaLoaiBan_ID;
    }

    public String getMaKhuVuc_ID() {
        return MaKhuVuc_ID;
    }

    public void setMaKhuVuc_ID(String MaKhuVuc_ID) {
        this.MaKhuVuc_ID = MaKhuVuc_ID;
    }

    public String getTenBan() {
        return TenBan;
    }

    public void setTenBan(String TenBan) {
        this.TenBan = TenBan;
    }

    public boolean isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean TrangThai) {
        this.TrangThai = TrangThai;
    }

    // toString()
    @Override
    public String toString() {
        return "BAN{" + "MaBan_ID=" + MaBan_ID + ", MaLoaiBan_ID=" + MaLoaiBan_ID + ", MaKhuVuc_ID=" + MaKhuVuc_ID + ", TenBan=" + TenBan + ", TrangThai=" + TrangThai + '}';
    }
}