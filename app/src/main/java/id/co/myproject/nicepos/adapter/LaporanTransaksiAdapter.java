package id.co.myproject.nicepos.adapter;

import android.text.format.DateFormat;
import android.widget.TextView;

import com.app.feng.fixtablelayout.inter.IDataAdapter;

import java.util.List;

import id.co.myproject.nicepos.model.StrukTransaksi;

import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

public class LaporanTransaksiAdapter implements IDataAdapter {

    public String[] title = {"Tanggal","Kasir","Total Transaksi", "Total Bayar", "Total Kembalian"};
    public List<StrukTransaksi> pesananList;
    public static final String TAG = LaporanTransaksiAdapter.class.getSimpleName();

    public LaporanTransaksiAdapter(List<StrukTransaksi> pesananList) {
        this.pesananList = pesananList;
    }

    public void setPesananList(List<StrukTransaksi> pesananList) {
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
        StrukTransaksi pesanan = pesananList.get(i);
        list.get(1).setText(pesanan.getNamaKasir());
        list.get(2).setText(rupiahFormat(Integer.parseInt(pesanan.getTotal())));
        list.get(3).setText(rupiahFormat(Integer.parseInt(pesanan.getUangBayar())));
        list.get(4).setText(rupiahFormat(Integer.parseInt(pesanan.getUangKembali())));
    }

    @Override
    public void convertLeftData(int i, TextView textView) {
        String date = DateFormat.format("dd MMM yyyy", pesananList.get(i).getTanggal()).toString();
        textView.setText(date);
    }
}
