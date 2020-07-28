package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class StrukTransaksi {
    @SerializedName("id_transaksi")
    @Expose
    private String idTransaksi;

    @SerializedName("nama_cafe")
    @Expose
    private String namaCafe;

    @SerializedName("nama_kasir")
    @Expose
    private String namaKasir;

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

    public StrukTransaksi() {
    }

    public StrukTransaksi(String idTransaksi, String namaCafe, String namaKasir, String total, String uangBayar, String uangKembali, Date tanggal) {
        this.idTransaksi = idTransaksi;
        this.namaCafe = namaCafe;
        this.namaKasir = namaKasir;
        this.total = total;
        this.uangBayar = uangBayar;
        this.uangKembali = uangKembali;
        this.tanggal = tanggal;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public void setNamaCafe(String namaCafe) {
        this.namaCafe = namaCafe;
    }

    public void setNamaKasir(String namaKasir) {
        this.namaKasir = namaKasir;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setUangBayar(String uangBayar) {
        this.uangBayar = uangBayar;
    }

    public void setUangKembali(String uangKembali) {
        this.uangKembali = uangKembali;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public String getNamaCafe() {
        return namaCafe;
    }

    public String getNamaKasir() {
        return namaKasir;
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
