package id.co.myproject.nicepos.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    static String TABLE_PESAN = "tb_pesan";
    static String TABLE_LAPORAN = "tb_laporan";
    static String TABLE_MARKET = "tb_market";
    static String TABLE_TAKARAN = "tb_takaran";
    static String TABLE_KAS = "tb_kas";
    static final class PesananColumns implements BaseColumns{
        static String ID_KASIR = "id_kasir";
        static String ID_MENU = "id_menu";
        static String NAMA_MENU = "nama_menu";
        static String HARGA_MENU = "harga_menu";
        static String GAMBAR_MENU = "gambar_menu";
        static String QUANTITY = "qty";
        static String ID_TRANSAKSI = "id_transaksi";
        static String TANGGAL = "tanggal";
        static String NAMA_KASIR = "nama_kasir";
        static String TOTAL_BAYAR = "total_bayar";
        static String TOTAL_HARGA = "total_harga";
        static String KEMBALIAN = "kembalian";
        static String ID_CAFE = "id_cafe";
        static String ID_SUPPLIER = "id_supplier";
        static String ID_BARANG = "id_barang";
        static String NAMA_BARANG = "nama_barang";
        static String GAMBAR_BARANG = "gambar";
        static String HARGA_BARANG = "harga";
        static String SATUAN = "satuan";
        static String QUANTITY_BARANG = "qty";
        static String ID_BAHAN_BAKU = "id_bahan_baku";
        static String NAMA_BAHAN_BAKU = "nama_bahan_baku";
        static String TAKARAN = "takaran";
        static String ID_KAS = "id_kas";
        static String TOTAL_KAS = "total_kas";
    }
}
