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

import id.co.myproject.nicepos.model.Pemasukan;
import id.co.myproject.nicepos.model.Pengeluaran;
import id.co.myproject.nicepos.model.Takaran;

import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_BAHAN_BAKU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_CAFE;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_KAS;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_KASIR;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.NAMA_BAHAN_BAKU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.TAKARAN;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.TANGGAL;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.TOTAL_KAS;
import static id.co.myproject.nicepos.database.DatabaseContract.TABLE_KAS;
import static id.co.myproject.nicepos.database.DatabaseContract.TABLE_LAPORAN;
import static id.co.myproject.nicepos.database.DatabaseContract.TABLE_PESAN;
import static id.co.myproject.nicepos.database.DatabaseContract.TABLE_TAKARAN;

public class KasHelper {
    public static DatabaseHelper databaseHelper;
    private static KasHelper INSTANCE;

    private static SQLiteDatabase database;
    Context context;

    public KasHelper(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public static KasHelper getINSTANCE(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new KasHelper(context);
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

    public long addToPemasukan(Pemasukan pemasukan){
        ContentValues args = new ContentValues();
        args.put(ID_KAS, pemasukan.getIdPemasukan());
        args.put(TANGGAL, DateFormat.format("dd MMM yyyy", pemasukan.getTanggal()).toString());
        args.put(TOTAL_KAS, pemasukan.getTotal());
        return database.insert(TABLE_KAS, null, args);
    }

    public long addToPengeluaran(Pengeluaran pengeluaran){
        ContentValues args = new ContentValues();
        args.put(ID_KAS, pengeluaran.getIdPengeluaran());
        args.put(TANGGAL, DateFormat.format("dd MMM yyyy", pengeluaran.getTanggal()).toString());
        args.put(TOTAL_KAS, pengeluaran.getTotal());
        return database.insert(TABLE_KAS, null, args);
    }

    public List<Pemasukan> getAllPemasukan(){
        List<Pemasukan> pemasukanList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_KAS, null,
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Pemasukan pemasukan;
        if (cursor.getCount() > 0){
            do{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
                Date date = new Date();
                try {
                    date = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(TANGGAL)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                pemasukan = new Pemasukan();
                pemasukan.setIdPemasukan(cursor.getString(cursor.getColumnIndex(ID_KAS)));
                pemasukan.setTanggal(date);
                pemasukan.setTotal(cursor.getString(cursor.getColumnIndex(TOTAL_KAS)));
                pemasukanList.add(pemasukan);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return pemasukanList;
    }

    public List<Pengeluaran> getAllPengeluaran(){
        List<Pengeluaran> pengeluaranList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_KAS, null,
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Pengeluaran pengeluaran;
        if (cursor.getCount() > 0){
            do{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
                Date date = new Date();
                try {
                    date = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(TANGGAL)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                pengeluaran = new Pengeluaran();
                pengeluaran.setIdPengeluaran(cursor.getString(cursor.getColumnIndex(ID_KAS)));
                pengeluaran.setTanggal(date);
                pengeluaran.setTotal(cursor.getString(cursor.getColumnIndex(TOTAL_KAS)));
                pengeluaranList.add(pengeluaran);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return pengeluaranList;
    }


    public long cleanKas(){
        return database.delete(TABLE_KAS, null, null);
    }

}
