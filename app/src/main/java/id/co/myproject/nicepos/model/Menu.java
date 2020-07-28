package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Menu {

    @SerializedName("id_menu")
    @Expose
    private String idMenu;

    @SerializedName("id_kategori")
    @Expose
    private String idKategori;


    @SerializedName("nama_menu")
    @Expose
    private String namaMenu;

    @SerializedName("harga")
    @Expose
    private String harga;

    @SerializedName("gambar")
    @Expose
    private String gambar;

    private String quantity;

    public Menu() {
    }

    public Menu(String idMenu, String idKategori, String namaMenu, String harga, String gambar, String quantity) {
        this.idMenu = idMenu;
        this.idKategori = idKategori;
        this.namaMenu = namaMenu;
        this.harga = harga;
        this.gambar = gambar;
        this.quantity = quantity;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getIdMenu() {
        return idMenu;
    }

    public String getIdKategori() {
        return idKategori;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public String getHarga() {
        return harga;
    }

    public String getGambar() {
        return gambar;
    }

    public String getQuantity() {
        return quantity;
    }
}
