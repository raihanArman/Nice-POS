package id.co.myproject.nicepos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import id.co.myproject.nicepos.model.BarangSupplier;
import id.co.myproject.nicepos.model.Menu;

import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.GAMBAR_BARANG;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.GAMBAR_MENU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.HARGA_BARANG;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.HARGA_MENU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_BARANG;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_CAFE;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_KASIR;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_MENU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.ID_SUPPLIER;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.NAMA_BARANG;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.NAMA_MENU;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.QUANTITY;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.QUANTITY_BARANG;
import static id.co.myproject.nicepos.database.DatabaseContract.PesananColumns.SATUAN;
import static id.co.myproject.nicepos.database.DatabaseContract.TABLE_MARKET;
import static id.co.myproject.nicepos.database.DatabaseContract.TABLE_PESAN;

public class MarketHelper {
    public static DatabaseHelper databaseHelper;
    private static MarketHelper INSTANCE;

    private static SQLiteDatabase database;
    Context context;

    public MarketHelper(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public static MarketHelper getINSTANCE(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new MarketHelper(context);
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

    public long addToCart(int idCafe, BarangSupplier barangSupplier, String qty){
        ContentValues args = new ContentValues();
        args.put(ID_CAFE, idCafe);
        args.put(ID_SUPPLIER, barangSupplier.getIdSupplier());
        args.put(ID_BARANG, barangSupplier.getIdBarang());
        args.put(NAMA_BARANG, barangSupplier.getNamaBarang());
        args.put(HARGA_BARANG, barangSupplier.getHarga());
        args.put(GAMBAR_BARANG, barangSupplier.getGambar());
        args.put(SATUAN, barangSupplier.getSatuan());
        args.put(QUANTITY_BARANG, qty);
        return database.insert(TABLE_MARKET, null, args);
    }

    public int getCountCart(int idCafe){
        int count = 0;
        String query = String.format("SELECT COUNT(*) FROM %s WHERE %s = %d", TABLE_MARKET, ID_CAFE, idCafe);
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

    public List<BarangSupplier> getIdSupplier(){
        List<BarangSupplier> barangSupplierList = new ArrayList<>();
        String query = String.format("SELECT %s, COUNT(%s) AS jumlah FROM %s GROUP BY %s",ID_SUPPLIER, ID_BARANG, TABLE_MARKET, ID_SUPPLIER );
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        BarangSupplier barangSupplier;
        if (cursor.moveToFirst()){
            do{
                barangSupplier = new BarangSupplier();
                barangSupplier.setIdSupplier(cursor.getString(cursor.getColumnIndex(ID_SUPPLIER)));
                barangSupplier.setJumlah(cursor.getInt(cursor.getColumnIndex("jumlah")));
                barangSupplierList.add(barangSupplier);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return barangSupplierList;
    }

    public List<BarangSupplier> getAllCart(int idCafe){
        List<BarangSupplier> barangSupplierList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_MARKET, null,
                ID_CAFE+"="+idCafe,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        BarangSupplier barangSupplier;
        if (cursor.moveToFirst()){
            do{
                barangSupplier = new BarangSupplier();
                barangSupplier.setIdSupplier(cursor.getString(cursor.getColumnIndex(ID_SUPPLIER)));
                barangSupplier.setIdBarang(cursor.getString(cursor.getColumnIndex(ID_BARANG)));
                barangSupplier.setNamaBarang(cursor.getString(cursor.getColumnIndex(NAMA_BARANG)));
                barangSupplier.setHarga(cursor.getString(cursor.getColumnIndex(HARGA_BARANG)));
                barangSupplier.setGambar(cursor.getString(cursor.getColumnIndex(GAMBAR_BARANG)));
                barangSupplier.setSatuan(cursor.getString(cursor.getColumnIndex(SATUAN)));
                barangSupplier.setQty(cursor.getString(cursor.getColumnIndex(QUANTITY)));
                barangSupplierList.add(barangSupplier);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return barangSupplierList;
    }

    public List<BarangSupplier> getAllCartBySupplier(String idSupplier){
        List<BarangSupplier> barangSupplierList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_MARKET, null,
                ID_SUPPLIER+"="+idSupplier,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        BarangSupplier barangSupplier;
        if (cursor.moveToFirst()){
            do{
                barangSupplier = new BarangSupplier();
                barangSupplier.setIdSupplier(cursor.getString(cursor.getColumnIndex(ID_SUPPLIER)));
                barangSupplier.setIdBarang(cursor.getString(cursor.getColumnIndex(ID_BARANG)));
                barangSupplier.setNamaBarang(cursor.getString(cursor.getColumnIndex(NAMA_BARANG)));
                barangSupplier.setHarga(cursor.getString(cursor.getColumnIndex(HARGA_BARANG)));
                barangSupplier.setGambar(cursor.getString(cursor.getColumnIndex(GAMBAR_BARANG)));
                barangSupplier.setSatuan(cursor.getString(cursor.getColumnIndex(SATUAN)));
                barangSupplier.setQty(cursor.getString(cursor.getColumnIndex(QUANTITY)));
                barangSupplierList.add(barangSupplier);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return barangSupplierList;
    }

    public void updateCart(BarangSupplier barangSupplier, int id_cafe) {
        String query = String.format("UPDATE %s SET %s = '%s' WHERE %s = %d AND %s = '%s'", TABLE_MARKET, QUANTITY, barangSupplier.getQty(), ID_CAFE, id_cafe, ID_BARANG, barangSupplier.getIdBarang());
        database.execSQL(query);
    }

    public void removeToCart(String id_barang, int id_cafe) {
        String query = String.format("DELETE FROM %s WHERE %s = '%s' and %s = %d", TABLE_MARKET, ID_BARANG, id_barang, ID_CAFE, id_cafe);
        database.execSQL(query);
    }

    public long cleanCart(int id_cafe){
        return database.delete(TABLE_MARKET, ID_CAFE+" = "+id_cafe, null);
    }

}
