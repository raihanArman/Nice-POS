package id.co.myproject.nicepos.adapter;

import android.text.format.DateFormat;
import android.widget.TextView;

import com.app.feng.fixtablelayout.inter.IDataAdapter;

import java.util.List;

import id.co.myproject.nicepos.model.Pemasukan;
import id.co.myproject.nicepos.model.Pengeluaran;

public class LaporanPengeluarkanAdapter implements IDataAdapter {

    public String[] title = {"Tanggal","Total Pengeluaran"};
    public List<Pengeluaran> pengeluaranList;
    public static final String TAG = LaporanTransaksiAdapter.class.getSimpleName();

    public LaporanPengeluarkanAdapter(List<Pengeluaran> pengeluaranList) {
        this.pengeluaranList = pengeluaranList;
    }

    public void setPesananList(List<Pengeluaran> pengeluaranList) {
        this.pengeluaranList = pengeluaranList;
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
        return pengeluaranList.size();
    }

    @Override
    public void convertData(int i, List<TextView> list) {
        Pengeluaran pengeluaran = pengeluaranList.get(i);
        list.get(1).setText(pengeluaran.getTotal());
    }

    @Override
    public void convertLeftData(int i, TextView textView) {
        String date = DateFormat.format("dd MMM yyyy", pengeluaranList.get(i).getTanggal()).toString();
        textView.setText(date);
    }
}
