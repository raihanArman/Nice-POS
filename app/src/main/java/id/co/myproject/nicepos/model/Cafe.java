package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cafe {

    @SerializedName("id_cafe")
    @Expose
    private String idCafe;

    @SerializedName("nama_cafe")
    @Expose
    private String namaCafe;

    @SerializedName("alamat_cafe")
    @Expose
    private String alamatCafe;

    @SerializedName("no_telp")
    @Expose
    private String noTelp;

    @SerializedName("gambar")
    @Expose
    private String gambar;

    public Cafe(String idCafe, String namaCafe, String alamatCafe, String noTelp, String gambar) {
        this.idCafe = idCafe;
        this.namaCafe = namaCafe;
        this.alamatCafe = alamatCafe;
        this.noTelp = noTelp;
        this.gambar = gambar;
    }

    public String getIdCafe() {
        return idCafe;
    }

    public String getNamaCafe() {
        return namaCafe;
    }

    public String getAlamatCafe() {
        return alamatCafe;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public String getGambar() {
        return gambar;
    }
}
