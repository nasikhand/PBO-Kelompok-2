/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Kota {
    private int id;
    private String namaKota;
    private String provinsi;

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNamaKota() { return namaKota; }
    public void setNamaKota(String namaKota) { this.namaKota = namaKota; }
    public String getProvinsi() { return provinsi; }
    public void setProvinsi(String provinsi) { this.provinsi = provinsi; }

    // Override toString() agar tampil nama kota di JComboBox
    @Override
    public String toString() {
        return namaKota;
    }
}
