package id.co.myproject.nicepos.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.model.StrukTransaksi;
import id.co.myproject.nicepos.model.Value;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.view.admin.MainActivity;
import id.co.myproject.nicepos.view.kasir.home.StrukFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.nicepos.util.Helper.TYPE_BUNDLE_FROM_TRANSAKSI;
import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

public class TransaksiAdminAdapter extends RecyclerView.Adapter<TransaksiAdminAdapter.ViewHolder> {

    List<StrukTransaksi> strukTransaksiList = new ArrayList<>();
    Context context;
    ApiRequest apiRequest;

    public TransaksiAdminAdapter(Context context, ApiRequest apiRequest) {
        this.context = context;
        this.apiRequest = apiRequest;
    }

    public void setTransaksiList(List<StrukTransaksi> strukTransaksiList){
        this.strukTransaksiList.clear();
        this.strukTransaksiList.addAll(strukTransaksiList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransaksiAdminAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaksi_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiAdminAdapter.ViewHolder holder, int position) {
        String date = DateFormat.format("dd MMM yyyy", strukTransaksiList.get(position).getTanggal()).toString();
        holder.tv_tanggal.setText(date);
        holder.tv_kasir.setText("Transaksi oleh : "+strukTransaksiList.get(position).getNamaKasir());
        holder.tv_total_harga.setText("Total Transaksi : "+rupiahFormat(Integer.parseInt(strukTransaksiList.get(position).getTotal())));
        holder.tv_total_bayar.setText("Total Bayar : "+rupiahFormat(Integer.parseInt(strukTransaksiList.get(position).getUangBayar())));
        holder.tv_kembalian.setText("Kembalian : "+rupiahFormat(Integer.parseInt(strukTransaksiList.get(position).getUangKembali())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrukFragment strukFragment = new StrukFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id_transaksi", Integer.parseInt(strukTransaksiList.get(position).getIdTransaksi()));
                bundle.putInt("type_bundle", TYPE_BUNDLE_FROM_TRANSAKSI);
                strukFragment.setArguments(bundle);
                ((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_home, strukFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Apakah anda yakin ingin menghapusnya ? ");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Value> hapusTransaksi = apiRequest.hapusTransaksi(strukTransaksiList.get(position).getIdTransaksi());
                        hapusTransaksi.enqueue(new Callback<Value>() {
                            @Override
                            public void onResponse(Call<Value> call, Response<Value> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    if (response.body().getValue() == 1){
                                        dialogInterface.dismiss();
                                        notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Value> call, Throwable t) {
                                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return strukTransaksiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_tanggal;
        TextView tv_kasir;
        TextView tv_total_harga;
        TextView tv_total_bayar;
        TextView tv_kembalian;
        ImageView iv_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);
            tv_kasir = itemView.findViewById(R.id.tv_kasir);
            tv_total_harga = itemView.findViewById(R.id.tv_total_harga);
            tv_total_bayar=  itemView.findViewById(R.id.tv_total_bayar);
            tv_kembalian = itemView.findViewById(R.id.tv_kembalian);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }
}
