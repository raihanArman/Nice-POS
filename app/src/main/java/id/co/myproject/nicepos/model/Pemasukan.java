package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Pemasukan {
    @SerializedName("id_cafe")
    @Expose
    private int idCafe;

    @SerializedName("id_pemasukan")
    @Expose
    private String idPemasukan;

    @SerializedName("tanggal")
    @Expose
    private Date tanggal;

    @SerializedName("total")
    @Expose
    private String total;

    public Pemasukan() {
    }

    public Pemasukan(int idCafe, String idPemasukan, Date tanggal, String total) {
        this.idCafe = idCafe;
        this.idPemasukan = idPemasukan;
        this.tanggal = tanggal;
        this.total = total;
    }

    public int getIdCafe() {
        return idCafe;
    }

    public void setIdCafe(int idCafe) {
        this.idCafe = idCafe;
    }

    public String getIdPemasukan() {
        return idPemasukan;
    }

    public void setIdPemasukan(String idPemasukan) {
        this.idPemasukan = idPemasukan;
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
