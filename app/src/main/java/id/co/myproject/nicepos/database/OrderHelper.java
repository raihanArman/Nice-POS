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

import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.GAMBAR_MENU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.HARGA_MENU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_KASIR;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_MENU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_TRANSAKSI;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.KEMBALIAN;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.NAMA_KASIR;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.NAMA_MENU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.QUANTITY;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.TANGGAL;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.TOTAL_BAYAR;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.TOTAL_HARGA;
import static id.co.myproject.nicepos.database.DatabaseContract.TABLE_LAPORAN;
import static id.co.myproject.nicepos.database.DatabaseContract.TABLE_PESAN;
import static id.co.myproject.nicepos.database.DatabaseHelper.DATABASE_NAME;

public class OrderHelper {
    public static DatabaseHelper databaseHelper;
    private static OrderHelper INSTANCE;

    private static SQLiteDatabase database;
    Context context;

    public OrderHelper(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public static OrderHelper getINSTANCE(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new OrderHelper(context);
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

    public long addToCart(int idKasir, Menu menu, String qty){
        ContentValues args = new ContentValues();
        args.put(ID_KASIR, idKasir);
        args.put(ID_MENU, menu.getIdMenu());
        args.put(NAMA_MENU, menu.getNamaMenu());
        args.put(HARGA_MENU, menu.getHarga());
        args.put(GAMBAR_MENU, menu.getGambar());
        args.put(QUANTITY, qty);
        return database.insert(TABLE_PESAN, null, args);
    }

    public int getCountCart(int id_kasir){
        int count = 0;
        String query = String.format("SELECT COUNT(*) FROM %s WHERE %s = %d", TABLE_PESAN, ID_KASIR, id_kasir);
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

    public List<Menu> getAllCart(int idKasir){
        List<Menu> menuList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_PESAN, null,
                ID_KASIR+"="+idKasir,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Menu menu;
        if (cursor.moveToFirst()){
            do{
                menu = new Menu();
                menu.setIdMenu(cursor.getString(cursor.getColumnIndex(ID_MENU)));
                menu.setNamaMenu(cursor.getString(cursor.getColumnIndex(NAMA_MENU)));
                menu.setHarga(cursor.getString(cursor.getColumnIndex(HARGA_MENU)));
                menu.setGambar(cursor.getString(cursor.getColumnIndex(GAMBAR_MENU)));
                menu.setQuantity(cursor.getString(cursor.getColumnIndex(QUANTITY)));
                menuList.add(menu);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return menuList;
    }

    public void updateCart(Menu menu, int id_kasir) {
        String query = String.format("UPDATE %s SET %s = '%s' WHERE %s = %d AND %s = '%s'", TABLE_PESAN, QUANTITY, menu.getQuantity(), ID_KASIR, id_kasir, ID_MENU, menu.getIdMenu());
        database.execSQL(query);
    }

    public void removeToCart(String id_menu, int id_kasir) {
        String query = String.format("DELETE FROM %s WHERE %s = '%s' and %s = %d", TABLE_PESAN, ID_MENU, id_menu, ID_KASIR, id_kasir);
        database.execSQL(query);
    }

    public long cleanCart(int id_kasir){
        return database.delete(TABLE_PESAN, ID_KASIR+" = "+id_kasir, null);
    }

    public long addToLaporan(StrukTransaksi strukTransaksi){
        ContentValues args = new ContentValues();
        args.put(ID_TRANSAKSI, strukTransaksi.getIdTransaksi());
        args.put(NAMA_KASIR, strukTransaksi.getNamaKasir());
        args.put(TOTAL_HARGA, strukTransaksi.getTotal());
        args.put(TOTAL_BAYAR, strukTransaksi.getUangBayar());
        args.put(KEMBALIAN, strukTransaksi.getUangKembali());
        args.put(TANGGAL, DateFormat.format("dd MMM yyyy", strukTransaksi.getTanggal()).toString());
        return database.insert(TABLE_LAPORAN, null, args);
    }

    public List<StrukTransaksi> getAllLaporan(){
        List<StrukTransaksi> strukTransaksiList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_LAPORAN, null,
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        StrukTransaksi strukTransaksi;
        if (cursor.getCount() > 0){
            do{
                strukTransaksi = new StrukTransaksi();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
                Date date = new Date();
                try {
                    date = dateFormat.parse(cursor.getString(cursor.getColumnIndex(TANGGAL)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                strukTransaksi.setIdTransaksi(cursor.getString(cursor.getColumnIndex(ID_TRANSAKSI)));
                strukTransaksi.setNamaKasir(cursor.getString(cursor.getColumnIndex(NAMA_KASIR)));
                strukTransaksi.setTotal(cursor.getString(cursor.getColumnIndex(TOTAL_HARGA)));
                strukTransaksi.setUangBayar(cursor.getString(cursor.getColumnIndex(TOTAL_BAYAR)));
                strukTransaksi.setUangKembali(cursor.getString(cursor.getColumnIndex(KEMBALIAN)));
                strukTransaksi.setTanggal(date);
                strukTransaksiList.add(strukTransaksi);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return strukTransaksiList;
    }

    public long cleanLaporan(){
        return database.delete(TABLE_LAPORAN, null, null);
    }

}
