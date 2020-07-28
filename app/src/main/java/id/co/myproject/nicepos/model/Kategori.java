package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Kategori {

    @SerializedName("id_kategori")
    @Expose
    private String idKategori;

    @SerializedName("nama_kategori")
    @Expose
    private String namaKategori;

    @SerializedName("gambar")
    @Expose
    private String gambar;

    public Kategori(String idKategori, String namaKategori, String gambar) {
        this.idKategori = idKategori;
        this.namaKategori = namaKategori;
        this.gambar = gambar;
    }

    public String getIdKategori() {
        return idKategori;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public String getGambar() {
        return gambar;
    }
}
