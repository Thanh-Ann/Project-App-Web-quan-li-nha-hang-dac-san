/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SCHEMACLASS;

public class KHU_VUC {
    private String MaKhuVuc_ID;
    private String MaTang_ID;
    private String TenKhuVuc;
    private String MoTa;

    // Constructor
    public KHU_VUC() {}
    
    public KHU_VUC(String MaKhuVuc_ID, String MaTang_ID, String TenKhuVuc, String MoTa) {
        this.MaKhuVuc_ID = MaKhuVuc_ID;
        this.MaTang_ID = MaTang_ID;
        this.TenKhuVuc = TenKhuVuc;
        this.MoTa = MoTa;
    }

    // Getter & Setter
    public String getMaKhuVuc_ID() {
        return MaKhuVuc_ID;
    }

    public void setMaKhuVuc_ID(String MaKhuVuc_ID) {
        this.MaKhuVuc_ID = MaKhuVuc_ID;
    }

    public String getMaTang_ID() {
        return MaTang_ID;
    }

    public void setMaTang_ID(String MaTang_ID) {
        this.MaTang_ID = MaTang_ID;
    }

    public String getTenKhuVuc() {
        return TenKhuVuc;
    }

    public void setTenKhuVuc(String TenKhuVuc) {
        this.TenKhuVuc = TenKhuVuc;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String MoTa) {
        this.MoTa = MoTa;
    }

    // toString()
    @Override
    public String toString() {
        return "KHU_VUC{" + "MaKhuVuc_ID=" + MaKhuVuc_ID + ", MaTang_ID=" + MaTang_ID + ", TenKhuVuc=" + TenKhuVuc + ", MoTa=" + MoTa + '}';
    }
}