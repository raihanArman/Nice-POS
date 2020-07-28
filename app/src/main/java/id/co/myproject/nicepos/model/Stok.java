package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Stok {
    @SerializedName("id_bahan_baku")
    @Expose
    private String idBahanBaku;

    @SerializedName("id_cafe")
    @Expose
    private String idcafe;

    @SerializedName("nama_bahan_baku")
    @Expose
    private String namaBahanBaku;

    @SerializedName("stok")
    @Expose
    private int stok;

    @SerializedName("satuan")
    @Expose
    private String satuan;

    @SerializedName("tanggal")
    @Expose
    private Date tanggal;

    public Stok(String idBahanBaku, String idcafe, String namaBahanBaku, int stok, String satuan, Date tanggal) {
        this.idBahanBaku = idBahanBaku;
        this.idcafe = idcafe;
        this.namaBahanBaku = namaBahanBaku;
        this.stok = stok;
        this.satuan = satuan;
        this.tanggal = tanggal;
    }

    public String getIdBahanBaku() {
        return idBahanBaku;
    }

    public String getIdcafe() {
        return idcafe;
    }

    public String getNamaBahanBaku() {
        return namaBahanBaku;
    }

    public int getStok() {
        return stok;
    }

    public String getSatuan() {
        return satuan;
    }

    public Date getTanggal() {
        return tanggal;
    }
}
