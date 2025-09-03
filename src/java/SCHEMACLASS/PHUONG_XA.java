/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SCHEMACLASS;

public class PHUONG_XA {
    private String MaPhuongXa_ID;
    private String TenPhuongXa;
    private String MoTa;

    // Constructor
    public PHUONG_XA() {}
    
    public PHUONG_XA(String MaPhuongXa_ID, String TenPhuongXa, String MoTa) {
        this.MaPhuongXa_ID = MaPhuongXa_ID;
        this.TenPhuongXa = TenPhuongXa;
        this.MoTa = MoTa;
    }

    // Getter & Setter
    public String getMaPhuongXa_ID() {
        return MaPhuongXa_ID;
    }

    public void setMaPhuongXa_ID(String MaPhuongXa_ID) {
        this.MaPhuongXa_ID = MaPhuongXa_ID;
    }

    public String getTenPhuongXa() {
        return TenPhuongXa;
    }

    public void setTenPhuongXa(String TenPhuongXa) {
        this.TenPhuongXa = TenPhuongXa;
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
        return "PHUONG_XA{" + "MaPhuongXa_ID=" + MaPhuongXa_ID + ", TenPhuongXa=" + TenPhuongXa + ", MoTa=" + MoTa + '}';
    }
}