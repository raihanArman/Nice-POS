package id.co.myproject.nicepos.adapter;

import android.text.format.DateFormat;
import android.widget.TextView;

import com.app.feng.fixtablelayout.inter.IDataAdapter;

import java.util.List;

import id.co.myproject.nicepos.model.Pemasukan;
import id.co.myproject.nicepos.model.StrukTransaksi;

import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

public class LaporanPemasukanAdapter implements IDataAdapter {

    public String[] title = {"Tanggal","Total Pemasukan"};
    public List<Pemasukan> pemasukanList;
    public static final String TAG = LaporanTransaksiAdapter.class.getSimpleName();

    public LaporanPemasukanAdapter(List<Pemasukan> pemasukanList) {
        this.pemasukanList = pemasukanList;
    }

    public void setPesananList(List<Pemasukan> pemasukanList) {
        this.pemasukanList = pemasukanList;
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
        return pemasukanList.size();
    }

    @Override
    public void convertData(int i, List<TextView> list) {
        Pemasukan pemasukan = pemasukanList.get(i);
        list.get(1).setText(pemasukan.getTotal());
    }

    @Override
    public void convertLeftData(int i, TextView textView) {
        String date = DateFormat.format("dd MMM yyyy", pemasukanList.get(i).getTanggal()).toString();
        textView.setText(date);
    }
}
