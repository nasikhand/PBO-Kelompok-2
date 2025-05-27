/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Kota {
    private int id;
    private String namaKota;

    public Kota(int id, String namaKota) {
        this.id = id;
        this.namaKota = namaKota;
    }

    public int getId() { return id; }
    public String getNamaKota() { return namaKota; }

    @Override
    public String toString() {
        return namaKota;
    }
}

