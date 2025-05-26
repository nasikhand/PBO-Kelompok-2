/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Admin {
    private int id;
    private String namaLengkap;
    private String email;

    public Admin(int id, String namaLengkap, String email) {
        this.id = id;
        this.namaLengkap = namaLengkap;
        this.email = email;
    }

    public int getId() { return id; }
    public String getNamaLengkap() { return namaLengkap; }
    public String getEmail() { return email; }
}


