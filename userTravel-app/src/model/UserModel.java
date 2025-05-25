package model;

public class UserModel {
    private int id;
    private String namaLengkap;
    private String email;
    private String password;
    private String nomorTelepon;
    private String alamat;
    private String gambar;

    // Constructor untuk registrasi (tanpa ID, telepon, alamat)
    public UserModel(String namaLengkap, String email, String password) {
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.password = password;
    }

    // Constructor lengkap (4 parameter)
    public UserModel(int id, String namaLengkap, String email, String password) {
        this.id = id;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.password = password;
    }

    // Constructor lengkap (5 parameter dan id)
    public UserModel(int id, String namaLengkap, String email, String nomorTelepon, String alamat) {
        this.id = id;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.nomorTelepon = nomorTelepon;
        this.alamat = alamat;
    }

    // Constructor lengkap (5 parameter tanpa id)
    public UserModel(String namaLengkap, String email, String password, String nomorTelepon, String alamat) {
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.password = password;
        this.nomorTelepon = nomorTelepon;
        this.alamat = alamat;
    }

    // Constructor lengkap (7 parameter)
    public UserModel(int id, String namaLengkap, String email, String password, String nomorTelepon, String alamat, String gambar) {
        this.id = id; 
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.password = password;
        this.nomorTelepon = nomorTelepon;
        this.alamat = alamat;
        this.gambar = gambar;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGambar() {
    return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
