package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Takaran {
    @SerializedName("id_bahan_menu")
    @Expose
    private String idBahanMenu;

    @SerializedName("id_menu")
    @Expose
    private String idMenu;

    @SerializedName("id_bahan_baku")
    @Expose
    private String idBahanBaku;

    @SerializedName("takaran")
    @Expose
    private String takaran;

    @SerializedName("nama_bahan_baku")
    @Expose
    private String nama_bahan_baku;

    public Takaran(String idBahanMenu, String idMenu, String idBahanBaku, String takaran, String nama_bahan_baku) {
        this.idBahanMenu = idBahanMenu;
        this.idMenu = idMenu;
        this.idBahanBaku = idBahanBaku;
        this.takaran = takaran;
        this.nama_bahan_baku = nama_bahan_baku;
    }

    public Takaran() {
    }

    public String getIdBahanMenu() {
        return idBahanMenu;
    }

    public void setIdBahanMenu(String idBahanMenu) {
        this.idBahanMenu = idBahanMenu;
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public String getIdBahanBaku() {
        return idBahanBaku;
    }

    public void setIdBahanBaku(String idBahanBaku) {
        this.idBahanBaku = idBahanBaku;
    }

    public String getTakaran() {
        return takaran;
    }

    public void setTakaran(String takaran) {
        this.takaran = takaran;
    }

    public String getNama_bahan_baku() {
        return nama_bahan_baku;
    }

    public void setNama_bahan_baku(String nama_bahan_baku) {
        this.nama_bahan_baku = nama_bahan_baku;
    }
}
