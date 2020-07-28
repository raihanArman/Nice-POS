package id.co.myproject.nicepos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.myproject.nicepos.model.Menu;
import id.co.myproject.nicepos.model.StrukTransaksi;
import id.co.myproject.nicepos.model.Takaran;

import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.GAMBAR_MENU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.HARGA_MENU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_BAHAN_BAKU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_CAFE;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_KASIR;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_MENU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_TRANSAKSI;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.KEMBALIAN;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.NAMA_BAHAN_BAKU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.NAMA_KASIR;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.NAMA_MENU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.QUANTITY;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.TAKARAN;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.TANGGAL;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.TOTAL_BAYAR;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.TOTAL_HARGA;
import static id.co.myproject.nicepos.database.DatabaseContract.TABLE_LAPORAN;
import static id.co.myproject.nicepos.database.DatabaseContract.TABLE_PESAN;
import static id.co.myproject.nicepos.database.DatabaseContract.TABLE_TAKARAN;

public class TakaranHelper {
    public static DatabaseHelper databaseHelper;
    private static TakaranHelper INSTANCE;

    private static SQLiteDatabase database;
    Context context;

    public TakaranHelper(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public static TakaranHelper getINSTANCE(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new TakaranHelper(context);
                }
            }
        }

        return INSTANCE;
    }

    public void open(){
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();
        if (database.isOpen()){
            database.close();
        }
    }

    public long addToCart(int idCafe, Takaran takaran){
        ContentValues args = new ContentValues();
        args.put(ID_CAFE, idCafe);
        args.put(ID_BAHAN_BAKU, takaran.getIdBahanBaku());
        args.put(NAMA_BAHAN_BAKU, takaran.getNama_bahan_baku());
        args.put(TAKARAN, takaran.getTakaran());
        return database.insert(TABLE_TAKARAN, null, args);
    }

    public int getCountCart(int idCafe){
        int count = 0;
        String query = String.format("SELECT COUNT(*) FROM %s WHERE %s = %d", TABLE_TAKARAN, ID_CAFE, idCafe);
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do{
                count = cursor.getInt(0);
            }while (cursor.moveToNext());
        }else {
            return 0;
        }

        return count;
    }

    public List<Takaran> getAllCart(int idCafe){
        List<Takaran> takaranList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_TAKARAN, null,
                ID_CAFE+"="+idCafe,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Takaran takaran;
        if (cursor.moveToFirst()){
            do{
                takaran = new Takaran();
                takaran.setIdBahanBaku(cursor.getString(cursor.getColumnIndex(ID_BAHAN_BAKU)));
                takaran.setNama_bahan_baku(cursor.getString(cursor.getColumnIndex(NAMA_BAHAN_BAKU)));
                takaran.setTakaran(cursor.getString(cursor.getColumnIndex(TAKARAN)));
                takaranList.add(takaran);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return takaranList;
    }

    public long cleanTakaran(){
        return database.delete(TABLE_TAKARAN, null, null);
    }
}
