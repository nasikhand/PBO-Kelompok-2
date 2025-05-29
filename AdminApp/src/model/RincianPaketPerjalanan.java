/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class RincianPaketPerjalanan {
    private int id;
    private int paketPerjalananId;
    private int destinasiId;
    private Integer urutanKunjungan; // Menggunakan Integer agar bisa null jika opsional
    private Integer durasiJam;      // Menggunakan Integer agar bisa null jika opsional

    // Atribut tambahan untuk tampilan
    private String namaDestinasi;

    // Constructor
    public RincianPaketPerjalanan() {}

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPaketPerjalananId() { return paketPerjalananId; }
    public void setPaketPerjalananId(int paketPerjalananId) { this.paketPerjalananId = paketPerjalananId; }

    public int getDestinasiId() { return destinasiId; }
    public void setDestinasiId(int destinasiId) { this.destinasiId = destinasiId; }

    public Integer getUrutanKunjungan() { return urutanKunjungan; }
    public void setUrutanKunjungan(Integer urutanKunjungan) { this.urutanKunjungan = urutanKunjungan; }

    public Integer getDurasiJam() { return durasiJam; }
    public void setDurasiJam(Integer durasiJam) { this.durasiJam = durasiJam; }

    public String getNamaDestinasi() { return namaDestinasi; }
    public void setNamaDestinasi(String namaDestinasi) { this.namaDestinasi = namaDestinasi; }
}
