package model;

public class UserModel {
    private int id;
    private String namaLengkap;
    private String email;
    private String password;

    // Constructor tanpa id (misal untuk registrasi)
    public UserModel(String namaLengkap, String email, String password) {
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.password = password;
    }

    // Constructor lengkap (misal untuk data dari database)
    public UserModel(int id, String namaLengkap, String email, String password) {
        this.id = id;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.password = password;
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
}
