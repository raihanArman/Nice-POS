package id.co.myproject.nicepos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "db_pesan";
    public static final int DATABASE_VERSION = 1;
    public static final String SQL_CREATE_TABLE_PESAN = String.format("CREATE TABLE %s"+
            "(%s INTEGER, "+
            "%s TEXT NOT NULL, "+
            "%s TEXT NOT NULL, "+
            "%s TEXT NOT NULL, "+
            "%s TEXT NOT NULL, "+
            "%s TEXT NOT NULL)",
            DatabaseContract.TABLE_PESAN,
            DatabaseContract.PesananColumns.ID_KASIR,
            DatabaseContract.PesananColumns.ID_MENU,
            DatabaseContract.PesananColumns.NAMA_MENU,
            DatabaseContract.PesananColumns.HARGA_MENU,
            DatabaseContract.PesananColumns.GAMBAR_MENU,
            DatabaseContract.PesananColumns.QUANTITY
    );
    public static final String SQL_CREATE_TABLE_LAPORAN = String.format("CREATE TABLE %s"+
                    "(%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL)",
            DatabaseContract.TABLE_LAPORAN,
            DatabaseContract.PesananColumns.ID_TRANSAKSI,
            DatabaseContract.PesananColumns.NAMA_KASIR,
            DatabaseContract.PesananColumns.TOTAL_HARGA,
            DatabaseContract.PesananColumns.TOTAL_BAYAR,
            DatabaseContract.PesananColumns.KEMBALIAN,
            DatabaseContract.PesananColumns.TANGGAL
    );
    public static final String SQL_CREATE_TABLE_MARKET = String.format("CREATE TABLE %s"+
                    "(%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL)",
            DatabaseContract.TABLE_MARKET,
            DatabaseContract.PesananColumns.ID_CAFE,
            DatabaseContract.PesananColumns.ID_SUPPLIER,
            DatabaseContract.PesananColumns.ID_BARANG,
            DatabaseContract.PesananColumns.NAMA_BARANG,
            DatabaseContract.PesananColumns.HARGA_BARANG,
            DatabaseContract.PesananColumns.GAMBAR_BARANG,
            DatabaseContract.PesananColumns.SATUAN,
            DatabaseContract.PesananColumns.QUANTITY_BARANG
    );

    public static final String SQL_CREATE_TABLE_TAKARAN = String.format("CREATE TABLE %s"+
                    "(%s INTEGER, "+
                    "%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL)",
            DatabaseContract.TABLE_TAKARAN,
            DatabaseContract.PesananColumns.ID_CAFE,
            DatabaseContract.PesananColumns.ID_BAHAN_BAKU,
            DatabaseContract.PesananColumns.NAMA_BAHAN_BAKU,
            DatabaseContract.PesananColumns.TAKARAN
    );
    public static final String SQL_CREATE_TABLE_KAS = String.format("CREATE TABLE %s"+
                    "(%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL, "+
                    "%s TEXT NOT NULL)",
            DatabaseContract.TABLE_KAS,
            DatabaseContract.PesananColumns.ID_KAS,
            DatabaseContract.PesananColumns.TANGGAL,
            DatabaseContract.PesananColumns.TOTAL_KAS
    );
    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_PESAN);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_LAPORAN);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MARKET);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_KAS);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_TAKARAN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DatabaseContract.TABLE_PESAN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DatabaseContract.TABLE_LAPORAN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DatabaseContract.TABLE_MARKET);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DatabaseContract.TABLE_TAKARAN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DatabaseContract.TABLE_KAS);
        onCreate(sqLiteDatabase);
    }
}
