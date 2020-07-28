package id.co.myproject.nicepos.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.model.Transaksi;
import id.co.myproject.nicepos.view.kasir.KasirHomeActivity;
import id.co.myproject.nicepos.view.kasir.home.StrukFragment;

import static id.co.myproject.nicepos.util.Helper.TYPE_BUNDLE_FROM_TRANSAKSI;
import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

public class TransaksiKasirAdapter extends RecyclerView.Adapter<TransaksiKasirAdapter.ViewHolder> {

    List<Transaksi> transaksiList = new ArrayList<>();
    Context context;

    public TransaksiKasirAdapter(Context context) {
        this.context = context;
    }

    public void setTransaksiList(List<Transaksi> transaksiList){
        if (transaksiList.size() > 0){
            this.transaksiList.clear();
        }
        this.transaksiList.addAll(transaksiList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransaksiKasirAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaksi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiKasirAdapter.ViewHolder holder, int position) {
        String date = DateFormat.format("dd MMM yyyy", transaksiList.get(position).getTanggal()).toString();
        holder.tv_tanggal.setText(date);
        holder.tv_total_harga.setText("Total Harga : "+rupiahFormat(Integer.parseInt(transaksiList.get(position).getTotal())));
        holder.tv_total_bayar.setText("Total Bayar : "+rupiahFormat(Integer.parseInt(transaksiList.get(position).getUangBayar())));
        holder.tv_kembalian.setText("Kembalian : "+rupiahFormat(Integer.parseInt(transaksiList.get(position).getUangKembali())));
        holder.iv_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrukFragment strukFragment = new StrukFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id_transaksi", Integer.parseInt(transaksiList.get(position).getIdTransaksi()));
                bundle.putInt("type_bundle", TYPE_BUNDLE_FROM_TRANSAKSI);
                strukFragment.setArguments(bundle);
                ((KasirHomeActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_kasir_home, strukFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_tanggal;
        TextView tv_total_harga;
        TextView tv_total_bayar;
        TextView tv_kembalian;
        ImageView iv_arrow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);
            tv_total_harga = itemView.findViewById(R.id.tv_total_harga);
            tv_total_bayar=  itemView.findViewById(R.id.tv_total_bayar);
            tv_kembalian = itemView.findViewById(R.id.tv_kembalian);
            iv_arrow = itemView.findViewById(R.id.iv_arrow);
        }
    }
}
