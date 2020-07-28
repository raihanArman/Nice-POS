package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Supplier {
    @SerializedName("id_supplier")
    @Expose
    private String idSupplier;

    @SerializedName("nama_supplier")
    @Expose
    private String namaSupplier;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("alamat")
    @Expose
    private String alamat;


    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("no_telp")
    @Expose
    private String no_telp;

    public Supplier(String idSupplier, String namaSupplier, String avatar, String alamat, String email, String no_telp) {
        this.idSupplier = idSupplier;
        this.namaSupplier = namaSupplier;
        this.avatar = avatar;
        this.alamat = alamat;
        this.email = email;
        this.no_telp = no_telp;
    }

    public String getIdSupplier() {
        return idSupplier;
    }

    public String getNamaSupplier() {
        return namaSupplier;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getEmail() {
        return email;
    }

    public String getNo_telp() {
        return no_telp;
    }
}
