package id.co.myproject.nicepos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("nama")
    @Expose
    private String namaUser;

    @SerializedName("email")
    @Expose
    private String emailUser;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("type")
    @Expose
    private String type;


    public User(String idUser, String namaUser, String emailUser, String avatar, String type) {
        this.idUser = idUser;
        this.namaUser = namaUser;
        this.emailUser = emailUser;
        this.avatar = avatar;
        this.type = type;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getType() {
        return type;
    }
}
