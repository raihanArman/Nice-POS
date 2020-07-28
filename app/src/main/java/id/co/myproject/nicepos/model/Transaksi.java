package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Transaksi {
    @SerializedName("id_transaksi")
    @Expose
    private String idTransaksi;

    @SerializedName("id_cafe")
    @Expose
    private String idCafe;

    @SerializedName("id_kasir")
    @Expose
    private String idKasir;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("uang_bayar")
    @Expose
    private String uangBayar;

    @SerializedName("uang_kembali")
    @Expose
    private String uangKembali;

    @SerializedName("tanggal")
    @Expose
    private Date tanggal;

    public Transaksi(String idTransaksi, String idCafe, String idKasir, String total, String uangBayar, String uangKembali, Date tanggal) {
        this.idTransaksi = idTransaksi;
        this.idCafe = idCafe;
        this.idKasir = idKasir;
        this.total = total;
        this.uangBayar = uangBayar;
        this.uangKembali = uangKembali;
        this.tanggal = tanggal;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public String getIdCafe() {
        return idCafe;
    }

    public String getIdKasir() {
        return idKasir;
    }

    public String getTotal() {
        return total;
    }

    public String getUangBayar() {
        return uangBayar;
    }

    public String getUangKembali() {
        return uangKembali;
    }

    public Date getTanggal() {
        return tanggal;
    }
}
