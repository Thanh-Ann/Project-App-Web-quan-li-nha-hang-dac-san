/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SCHEMACLASS;

public class TANG {
    private String MaTang_ID;
    private String TenTang;
    private String MoTa;

    // Constructor
    public TANG() {}
    
    public TANG(String MaTang_ID, String TenTang, String MoTa) {
        this.MaTang_ID = MaTang_ID;
        this.TenTang = TenTang;
        this.MoTa = MoTa;
    }

    // Getter & Setter
    public String getMaTang_ID() {
        return MaTang_ID;
    }

    public void setMaTang_ID(String MaTang_ID) {
        this.MaTang_ID = MaTang_ID;
    }

    public String getTenTang() {
        return TenTang;
    }

    public void setTenTang(String TenTang) {
        this.TenTang = TenTang;
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
        return "TANG{" + "MaTang_ID=" + MaTang_ID + ", TenTang=" + TenTang + ", MoTa=" + MoTa + '}';
    }
}