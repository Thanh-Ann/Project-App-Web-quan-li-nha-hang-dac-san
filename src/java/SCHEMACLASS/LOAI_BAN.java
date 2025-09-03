/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SCHEMACLASS;

public class LOAI_BAN {
    private String MaLoaiBan_ID;
    private String TenLoaiBan;
    private String MoTa;

    // Constructor
    public LOAI_BAN() {}
    
    public LOAI_BAN(String MaLoaiBan_ID, String TenLoaiBan, String MoTa) {
        this.MaLoaiBan_ID = MaLoaiBan_ID;
        this.TenLoaiBan = TenLoaiBan;
        this.MoTa = MoTa;
    }

    // Getter & Setter
    public String getMaLoaiBan_ID() {
        return MaLoaiBan_ID;
    }

    public void setMaLoaiBan_ID(String MaLoaiBan_ID) {
        this.MaLoaiBan_ID = MaLoaiBan_ID;
    }

    public String getTenLoaiBan() {
        return TenLoaiBan;
    }

    public void setTenLoaiBan(String TenLoaiBan) {
        this.TenLoaiBan = TenLoaiBan;
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
        return "LOAI_BAN{" + "MaLoaiBan_ID=" + MaLoaiBan_ID + ", TenLoaiBan=" + TenLoaiBan + ", MoTa=" + MoTa + '}';
    }
}