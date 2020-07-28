package id.co.myproject.nicepos.request;

import java.util.List;

import id.co.myproject.nicepos.model.BahanMenu;
import id.co.myproject.nicepos.model.BarangSupplier;
import id.co.myproject.nicepos.model.Cafe;
import id.co.myproject.nicepos.model.Kas;
import id.co.myproject.nicepos.model.Kasir;
import id.co.myproject.nicepos.model.Kategori;
import id.co.myproject.nicepos.model.Menu;
import id.co.myproject.nicepos.model.Pemasukan;
import id.co.myproject.nicepos.model.Pengeluaran;
import id.co.myproject.nicepos.model.Pesan;
import id.co.myproject.nicepos.model.Pesanan;
import id.co.myproject.nicepos.model.Stok;
import id.co.myproject.nicepos.model.StrukTransaksi;
import id.co.myproject.nicepos.model.Supplier;
import id.co.myproject.nicepos.model.Transaksi;
import id.co.myproject.nicepos.model.User;
import id.co.myproject.nicepos.model.Value;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiRequest {


//    Kategori Proses
    @GET("tampil_kategori.php")
    Call<List<Kategori>> getKategoriCallback(
            @Query("id_cafe") int idCafe
    );

    @GET("tampil_kategori.php")
    Call<Kategori> getKategoriItemCallback(
            @Query("id_kategori") String idKatgori
    );

    @FormUrlEncoded
    @POST("input_kategori.php")
    Call<Value> inputKategoriCallback(
            @Field("id_cafe") int idCafe,
            @Field("nama_kategori") String namaKategori,
            @Field("gambar") String gambar
    );

    @FormUrlEncoded
    @POST("edit_kategori.php")
    Call<Value> editKategoriCallback(
            @Field("id_kategori") String idKategori,
            @Field("nama_kategori") String namaKategori,
            @Field("gambar") String gambar
    );

    @FormUrlEncoded
    @POST("hapus_kategori.php")
    Call<Value> hapusKategoriCallback(
            @Field("id_kategori") String idKategori
    );

    @GET("tampil_kategori.php")
    Call<List<Kategori>> cariKategoriCallback(
            @Query("id_cafe") int idCafe,
            @Query("cari") String cari
    );
//    Kategori Proses

//    Menu Proses
    @FormUrlEncoded
    @POST("input_menu.php")
    Call<Value> inputMenuCallback(
            @Field("id_cafe") int idCafe,
            @Field("id_kategori") String idKategori,
            @Field("nama_menu") String namaMenu,
            @Field("harga") int harga,
            @Field("gambar") String gambar
    );

    @GET("tampil_menu.php")
    Call<List<Menu>> getMenuCallback(
            @Query("id_cafe") int idCafe
    );

    @GET("tampil_menu.php")
    Call<Menu> getMenuItemCallback(
            @Query("id_menu") String idMenu
    );

    @FormUrlEncoded
    @POST("edit_menu.php")
    Call<Value> editMenuCallback(
            @Field("id_menu") String idMenu,
            @Field("id_kategori") String idKategori,
            @Field("nama_menu") String namaMenu,
            @Field("harga") int harga,
            @Field("gambar") String gambar
    );

    @FormUrlEncoded
    @POST("hapus_menu.php")
    Call<Value> hapusMenuCallback(
            @Field("id_menu") String idMenu
    );

    @GET("tampil_menu.php")
    Call<List<Menu>> cariMenuCallback(
            @Query("id_cafe") int idCafe,
            @Query("cari") String cari
    );
//    Menu Proses

//    User Proses
    @FormUrlEncoded
    @POST("input_user.php")
    Call<Value> inputUserCallback(
            @Field("email") String email,
            @Field("nama") String nama,
            @Field("avatar") String avatar,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("cek_user.php")
    Call<Value> cekUserCallback(
            @Field("email") String email
    );

    @GET("tampil_user.php")
    Call<User> getUserItem(
            @Query("id_user") int idUser
    );

    @FormUrlEncoded
    @POST("edit_user.php")
    Call<Value> editUserCallback(
            @Field("id_user") int idUser,
            @Field("nama_user") String nama,
            @Field("avatar") String avatar
    );
//  User proses

//    Cafe Proses
    @FormUrlEncoded
    @POST("cek_cafe.php")
    Call<Value> cekCafeCallback(
            @Field("id_user") int idUser
    );

    @GET("tampil_cafe.php")
    Call<Cafe> getCafeItem(
            @Query("id_cafe") int idCafe
    );

    @FormUrlEncoded
    @POST("edit_cafe.php")
    Call<Value> editCafeCallback(
            @Field("id_cafe") int idCafe,
            @Field("nama_cafe") String namaCafe,
            @Field("alamat_cafe") String alamatCafe,
            @Field("no_telp") String noTelp,
            @Field("gambar") String gambar
    );

    @FormUrlEncoded
    @POST("input_cafe.php")
    Call<Value> inputCafeCallback(
            @Field("id_user") int idUser,
            @Field("nama_cafe") String namaCafe,
            @Field("alamat_cafe") String alamatCafe,
            @Field("no_telp") String noTelp,
            @Field("gambar") String gambar
    );

//    Cafe Proses

//    Kasir Proses
    @FormUrlEncoded
    @POST("login_kasir.php")
    Call<Value> loginKasirCallback(
            @Field("username_kasir") String usernameKasir,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("input_kasir.php")
    Call<Value> inputKasirCallback(
            @Field("id_cafe") int idCafe,
            @Field("username_kasir") String usernameKasir,
            @Field("nama_kasir") String namaKasir,
            @Field("password") String password
    );
    @GET("tampil_kasir.php")
    Call<List<Kasir>> getKasirCallback(
            @Query("id_cafe") int idCafe
    );

    @GET("tampil_kasir.php")
    Call<Kasir> getKasirItemCallback(
            @Query("id_kasir") String idKasir
    );

    @GET("tampil_kasir.php")
    Call<List<Kasir>> cariKasirCariCallback(
            @Query("id_cafe") int idCafe,
            @Query("cari") String cari
    );

    @FormUrlEncoded
    @POST("edit_kasir.php")
    Call<Value> editKasirCallback(
            @Field("id_kasir") String idKasir,
            @Field("username_kasir") String usernameKasir,
            @Field("nama_kasir") String namaKasir,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("hapus_kasir.php")
    Call<Value> hapusKasirCallback(
            @Field("id_kasir") String idKasir
    );
//    Kasir Proses


//    Transaksi Proses
    @FormUrlEncoded
    @POST("input_transaksi.php")
    Call<Value> inpuTransaksiCallback(
            @Field("id_cafe") int idCafe,
            @Field("id_kasir") int idKasir,
            @Field("total") int total,
            @Field("uang_bayar") int uangBayar,
            @Field("uang_kembali") int uangKembali
    );

    @GET("tampil_transaksi.php")
    Call<List<Transaksi>> getTransaksi(
            @Query("id_kasir") int idKasir
    );


    @GET("tampil_struk.php")
    Call<StrukTransaksi> getStruk(
            @Query("id_transaksi") int idTransaksi
    );


    @GET("tampil_struk.php")
    Call<List<StrukTransaksi>> getLaporanTransaksi(
            @Query("id_cafe") int id_cafe,
            @Query("tanggal_1") String tanggal1,
            @Query("tanggal_2") String tanggal2
    );

    @GET("tampil_struk.php")
    Call<List<StrukTransaksi>> getAllLaporanTransaksi(
            @Query("id_cafe") int id_cafe
    );

    @GET("tampil_pesanan.php")
    Call<List<Pesanan>> getPesanan(
            @Query("id_transaksi") int idTransaksi
    );

    @FormUrlEncoded
    @POST("input_pesanan.php")
    Call<Value> inputPesananCallback(
            @Field("id_transaksi") int idTransaksi,
            @Field("id_menu") String idMenu,
            @Field("qty") String qty,
            @Field("sub_total") int subTotal
    );

    @GET("total_transaksi.php")
    Call<Value> getTotalTransaksi(
            @Query("id_cafe") int id_cafe,
            @Query("tanggal_1") String tanggal1,
            @Query("tanggal_2") String tanggal2
    );

    @FormUrlEncoded
    @POST("hapus_transaksi.php")
    Call<Value> hapusTransaksi(
            @Field("id_transaksi") String id_transaksi
    );

//    Transaksi Proses



//    Market Proses
    @GET("supplier/tampil_barang.php")
    Call<List<BarangSupplier>> getBarangRequest();

    @GET("supplier/tampil_barang.php")
    Call<BarangSupplier> getBarangItemRequest(
            @Query("id_barang") String idBarang
    );

    @GET("supplier/tampil_supplier.php")
    Call<Supplier> getSupplierItemRequest(
            @Query("id_supplier") String idSupplier
    );

    @FormUrlEncoded
    @POST("supplier/input_request.php")
    Call<Value> inputRequestRequest(
            @Field("id_cafe") int idCafe,
            @Field("id_supplier") String idSupplier,
            @Field("total") int total,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("supplier/input_request_item.php")
    Call<Value> inputRequestItemRequest(
            @Field("id_request") int idRequest,
            @Field("id_barang") String idBarang,
            @Field("qty") String qty,
            @Field("sub_total") int subTotal
    );

//    Notif
    @GET("tampil_pesan_user.php")
    Call<List<Pesan>> getPesanUserRequest(
            @Query("id_cafe") int idCafe
    );
//    Notif


//    Stok
    @GET("tampil_stok.php")
    Call<List<Stok>> getStokRequest(
            @Query("id_cafe") int idCafe
    );

    @FormUrlEncoded
    @POST("input_bahan_menu.php")
    Call<Value> inputBahanMenuRequest(
            @Field("id_menu") int id_menu,
            @Field("id_bahan_baku") String id_bahan_baku,
            @Field("takaran") String takaran
    );

    @GET("tampil_bahan_menu.php")
    Call<List<BahanMenu>> getBahanBaku(
            @Query("id_menu") String idMenu
    );

    @FormUrlEncoded
    @POST("update_stok.php")
    Call<Value> updateStokRequest(
            @Field("id_bahan_baku") String id_bahan_baku,
            @Field("takaran") String takaran,
            @Field("qty") String qty
    );
//    Stok

//    input pemasukan
    @FormUrlEncoded
    @POST("input_pemasukan.php")
    Call<Value> inputPemasukanRequest(
            @Field("id_cafe") int idCafe,
            @Field("total") int total
    );
//    input pemasukan

//    Kas
    @GET("total_kas.php")
    Call<Kas> totalKasRequest(
            @Query("id_cafe") int id_cafe,
            @Query("tanggal_1") String tanggal1,
            @Query("tanggal_2") String tanggal2
    );

    @GET("tampil_pemasukan.php")
    Call<List<Pemasukan>> getPemasukanTodayRequest(
            @Query("id_cafe") int idCafe,
            @Query("tanggal_1") String tanggal1,
            @Query("tanggal_2") String tanggal2
    );

    @GET("tampil_pemasukan.php")
    Call<List<Pemasukan>> getPemasukanAllRequest(
            @Query("id_cafe") int idCafe
    );

    @GET("tampil_pengeluaran.php")
    Call<List<Pengeluaran>> getPengeluaranAllRequest(
            @Query("id_cafe") int idCafe
    );

    @GET("tampil_pengeluaran.php")
    Call<List<Pengeluaran>> getPengeluaranTodayRequest(
            @Query("id_cafe") int idCafe,
            @Query("tanggal_1") String tanggal1,
            @Query("tanggal_2") String tanggal2
    );

    @FormUrlEncoded
    @POST("hapus_pemasukan.php")
    Call<Value> hapusPemasukanRequest(
            @Field("id_pemasukan") String idPemasukan
    );

    @FormUrlEncoded
    @POST("hapus_pengeluaran.php")
    Call<Value> hapusPengeluaranRequest(
            @Field("id_pengeluaran") String idPengeluaran
    );


//    Kas

}
