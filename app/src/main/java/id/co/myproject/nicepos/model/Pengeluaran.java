package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Pengeluaran {

    @SerializedName("id_cafe")
    @Expose
    private int idCafe;

    @SerializedName("id_pengeluaran")
    @Expose
    private String idPengeluaran;

    @SerializedName("tanggal")
    @Expose
    private Date tanggal;

    @SerializedName("total")
    @Expose
    private String total;

    public Pengeluaran() {
    }

    public Pengeluaran(int idCafe, String idPengeluaran, Date tanggal, String total) {
        this.idCafe = idCafe;
        this.idPengeluaran = idPengeluaran;
        this.tanggal = tanggal;
        this.total = total;
    }

    public int getIdCafe() {
        return idCafe;
    }

    public void setIdCafe(int idCafe) {
        this.idCafe = idCafe;
    }

    public String getIdPengeluaran() {
        return idPengeluaran;
    }

    public void setIdPengeluaran(String idPengeluaran) {
        this.idPengeluaran = idPengeluaran;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
