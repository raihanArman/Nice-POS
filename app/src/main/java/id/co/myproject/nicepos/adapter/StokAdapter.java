package id.co.myproject.nicepos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.model.Stok;

public class StokAdapter extends RecyclerView.Adapter<StokAdapter.ViewHolder> {

    List<Stok> stokList = new ArrayList<>();
    Context context;

    public StokAdapter(Context context) {
        this.context = context;
    }

    public void setStokList(List<Stok> stokList){
        this.stokList.clear();
        this.stokList.addAll(stokList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StokAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stok, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StokAdapter.ViewHolder holder, int position) {
        if (stokList.size() > 0){
            holder.tv_nama_barang.setText(stokList.get(position).getNamaBahanBaku());
            holder.tv_stok.setText("Jumlah Stok "+stokList.get(position).getStok()+" "+stokList.get(position).getSatuan());
        }
    }

    @Override
    public int getItemCount() {
        return stokList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_nama_barang, tv_stok;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nama_barang = itemView.findViewById(R.id.tv_nama_barang);
            tv_stok = itemView.findViewById(R.id.tv_stok);
        }
    }
}
