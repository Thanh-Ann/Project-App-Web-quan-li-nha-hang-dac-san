package SCHEMACLASS;

import java.sql.Date;

public class HOA_DON {
    private String MaHoaDon_ID;
    private String MaBan_ID;
    private double TongTien;
    private Date NgayBan;
    private String TrangThai; // Thêm thuộc tính

    // Constructor
    public HOA_DON() {
        this.TrangThai = "Chưa thanh toán"; // Giá trị mặc định
    }

    public HOA_DON(String MaHoaDon_ID, String MaBan_ID, double TongTien, Date NgayBan, String TrangThai) {
        this.MaHoaDon_ID = MaHoaDon_ID;
        this.MaBan_ID = MaBan_ID;
        this.TongTien = TongTien;
        this.NgayBan = NgayBan;
        this.TrangThai = TrangThai;
    }

    // Getter & Setter
    public String getMaHoaDon_ID() {
        return MaHoaDon_ID;
    }

    public void setMaHoaDon_ID(String MaHoaDon_ID) {
        this.MaHoaDon_ID = MaHoaDon_ID;
    }

    public String getMaBan_ID() {
        return MaBan_ID;
    }

    public void setMaBan_ID(String MaBan_ID) {
        this.MaBan_ID = MaBan_ID;
    }

    public double getTongTien() {
        return TongTien;
    }

    public void setTongTien(double TongTien) {
        this.TongTien = TongTien;
    }

    public Date getNgayBan() {
        return NgayBan;
    }

    public void setNgayBan(Date NgayBan) {
        this.NgayBan = NgayBan;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String TrangThai) {
        this.TrangThai = TrangThai;
    }

    // toString()
    @Override
    public String toString() {
        return "HOA_DON{" + "MaHoaDon_ID=" + MaHoaDon_ID + ", MaBan_ID=" + MaBan_ID + 
               ", TongTien=" + TongTien + ", NgayBan=" + NgayBan + ", TrangThai=" + TrangThai + '}';
    }
}