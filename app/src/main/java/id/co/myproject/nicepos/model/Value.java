package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Value {
    @SerializedName("value")
    @Expose
    private int value;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("id_user")
    @Expose
    private int idUser;

    @SerializedName("id_kasir")
    @Expose
    private int idKasir;

    @SerializedName("id_cafe")
    @Expose
    private int idCafe;

    @SerializedName("id_transaksi")
    @Expose
    private int idTransaksi;


    @SerializedName("id_request")
    @Expose
    private int idRequest;

    @SerializedName("total_transaksi")
    @Expose
    private int totalTransaksi;


    @SerializedName("id_menu")
    @Expose
    private int id_menu;

    public Value() {
    }

    public Value(int value, String message, int idUser) {
        this.value = value;
        this.message = message;
        this.idUser = idUser;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdCafe() {
        return idCafe;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public int getTotalTransaksi() {
        return totalTransaksi;
    }

    public int getIdKasir() {
        return idKasir;
    }

    public int getIdRequest() {
        return idRequest;
    }

    public int getId_menu() {
        return id_menu;
    }
}
