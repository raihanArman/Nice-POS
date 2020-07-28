package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class BarangSupplier {
    @SerializedName("id_barang")
    @Expose
    private String idBarang;

    @SerializedName("id_supplier")
    @Expose
    private String idSupplier;

    @SerializedName("nama_supplier")
    @Expose
    private String namaSupplier;

    @SerializedName("nama_barang")
    @Expose
    private String namaBarang;

    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;


    @SerializedName("harga")
    @Expose
    private String harga;

    @SerializedName("stok")
    @Expose
    private double stok;

    @SerializedName("satuan")
    @Expose
    private String satuan;

    @SerializedName("gambar")
    @Expose
    private String gambar;

    @SerializedName("tanggal")
    @Expose
    private Date tanggal;


    private String qty;

    private int jumlah;

    public BarangSupplier() {
    }

    public BarangSupplier(String idBarang, String namaBarang, String harga, double stok, String satuan, Date tanggal) {
        this.idBarang = idBarang;
        this.namaBarang = namaBarang;
        this.harga = harga;
        this.stok = stok;
        this.satuan = satuan;
        this.tanggal = tanggal;
    }

    public String getIdBarang() {
        return idBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public String getHarga() {
        return harga;
    }

    public double getStok() {
        return stok;
    }

    public String getSatuan() {
        return satuan;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public String getGambar() {
        return gambar;
    }

    public String getNamaSupplier() {
        return namaSupplier;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getIdSupplier() {
        return idSupplier;
    }

    public void setIdBarang(String idBarang) {
        this.idBarang = idBarang;
    }

    public void setIdSupplier(String idSupplier) {
        this.idSupplier = idSupplier;
    }

    public void setNamaSupplier(String namaSupplier) {
        this.namaSupplier = namaSupplier;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public void setStok(double stok) {
        this.stok = stok;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
