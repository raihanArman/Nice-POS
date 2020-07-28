package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Kasir {

    @SerializedName("id_kasir")
    @Expose
    private String idKasir;

    @SerializedName("username_kasir")
    @Expose
    private String usernameKasir;

    @SerializedName("nama_kasir")
    @Expose
    private String namaKasir;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("status")
    @Expose
    private String status;

    public Kasir(String idKasir, String namaKasir, String password, String status, String usernameKasir) {
        this.idKasir = idKasir;
        this.namaKasir = namaKasir;
        this.password = password;
        this.status = status;
        this.usernameKasir = usernameKasir;
    }

    public String getIdKasir() {
        return idKasir;
    }

    public String getNamaKasir() {
        return namaKasir;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public String getUsernameKasir() {
        return usernameKasir;
    }
}
