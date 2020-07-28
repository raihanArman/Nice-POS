package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pesanan {
    @SerializedName("id_pesanan")
    @Expose
    private String idPesanan;

    @SerializedName("id_transaksi")
    @Expose
    private String idTransaksi;

    @SerializedName("nama_menu")
    @Expose
    private String namaMenu;

    @SerializedName("harga")
    @Expose
    private String harga;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("sub_total")
    @Expose
    private String subTotal;

    public Pesanan(String idPesanan, String idTransaksi, String namaMenu, String harga, String qty, String subTotal) {
        this.idPesanan = idPesanan;
        this.idTransaksi = idTransaksi;
        this.namaMenu = namaMenu;
        this.harga = harga;
        this.qty = qty;
        this.subTotal = subTotal;
    }

    public String getIdPesanan() {
        return idPesanan;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public String getHarga() {
        return harga;
    }

    public String getQty() {
        return qty;
    }

    public String getSubTotal() {
        return subTotal;
    }
}
