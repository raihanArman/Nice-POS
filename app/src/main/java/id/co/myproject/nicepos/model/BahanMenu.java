package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BahanMenu {

    @SerializedName("id_bahan_menu")
    @Expose
    private String id_bahan_menu;

    @SerializedName("id_bahan_baku")
    @Expose
    private String id_bahan_baku;

    @SerializedName("id_menu")
    @Expose
    private String id_menu;

    @SerializedName("nama_bahan_baku")
    @Expose
    private String nama_bahan_baku;

    @SerializedName("takaran")
    @Expose
    private String takaran;

    @SerializedName("satuan")
    @Expose
    private String satuan;

    public BahanMenu(String id_bahan_menu, String id_bahan_baku, String id_menu, String nama_bahan_baku, String takaran, String satuan) {
        this.id_bahan_menu = id_bahan_menu;
        this.id_bahan_baku = id_bahan_baku;
        this.id_menu = id_menu;
        this.nama_bahan_baku = nama_bahan_baku;
        this.takaran = takaran;
        this.satuan = satuan;
    }

    public String getId_bahan_menu() {
        return id_bahan_menu;
    }

    public String getId_bahan_baku() {
        return id_bahan_baku;
    }

    public String getId_menu() {
        return id_menu;
    }

    public String getNama_bahan_baku() {
        return nama_bahan_baku;
    }

    public String getTakaran() {
        return takaran;
    }

    public String getSatuan() {
        return satuan;
    }
}
