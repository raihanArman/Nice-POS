package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Pesan {

    @SerializedName("id_pesan")
    @Expose
    private String idPesan;

    @SerializedName("id_cafe")
    @Expose
    private String idCafe;

    @SerializedName("id_supplier")
    @Expose
    private String idSupplier;

    @SerializedName("isi")
    @Expose
    private String isi;

    @SerializedName("tanggal")
    @Expose
    private Date tanggal;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("nama_supplier")
    @Expose
    private String namaSupplier;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    public Pesan(String idPesan, String idCafe, String idSupplier, String isi, Date tanggal, String namaSupplier, String avatar) {
        this.idPesan = idPesan;
        this.idCafe = idCafe;
        this.idSupplier = idSupplier;
        this.isi = isi;
        this.tanggal = tanggal;
        this.namaSupplier = namaSupplier;
        this.avatar = avatar;
    }

    public String getIdPesan() {
        return idPesan;
    }

    public String getIdCafe() {
        return idCafe;
    }

    public String getIdSupplier() {
        return idSupplier;
    }

    public String getIsi() {
        return isi;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public String getNamaSupplier() {
        return namaSupplier;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getStatus() {
        return status;
    }
}
