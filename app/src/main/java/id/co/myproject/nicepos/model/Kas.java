package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Kas {
    @SerializedName("pemasukan")
    @Expose
    private String pemasukan;

    @SerializedName("pengeluaran")
    @Expose
    private String pengeluaran;

    public Kas(String pemasukan, String pengeluaran) {
        this.pemasukan = pemasukan;
        this.pengeluaran = pengeluaran;
    }

    public String getPemasukan() {
        return pemasukan;
    }

    public void setPemasukan(String pemasukan) {
        this.pemasukan = pemasukan;
    }

    public String getPengeluaran() {
        return pengeluaran;
    }

    public void setPengeluaran(String pengeluaran) {
        this.pengeluaran = pengeluaran;
    }
}
