package id.co.myproject.nicepos.adapter;

import android.util.Log;
import android.widget.TextView;

import com.app.feng.fixtablelayout.inter.IDataAdapter;

import java.util.List;

import id.co.myproject.nicepos.model.Pesanan;

import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

public class PesananAdapter implements IDataAdapter {

    public String[] title;
    public List<Pesanan> pesananList;
    public static final String TAG = PesananAdapter.class.getSimpleName();

    public PesananAdapter(String[] title, List<Pesanan> pesananList) {
        this.title = title;
        this.pesananList = pesananList;
    }

    public void setPesananList(List<Pesanan> pesananList) {
        this.pesananList = pesananList;
    }

    @Override
    public String getTitleAt(int i) {
        return title[i];
    }

    @Override
    public int getTitleCount() {
        return title.length;
    }

    @Override
    public int getItemCount() {
        return pesananList.size();
    }

    @Override
    public void convertData(int i, List<TextView> list) {
        Pesanan pesanan = pesananList.get(i);
        list.get(0).setText(pesanan.getNamaMenu());
        list.get(1).setText(rupiahFormat(Integer.parseInt(pesanan.getHarga())));
        list.get(2).setText(pesanan.getQty());
        Log.d(TAG, "convertData: Nama Menu : "+pesanan.getNamaMenu());
    }

    @Override
    public void convertLeftData(int i, TextView textView) {
        textView.setText(pesananList.get(i).getNamaMenu());
    }
}
