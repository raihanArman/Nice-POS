package id.co.myproject.nicepos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.BuildConfig;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.model.BarangSupplier;
import id.co.myproject.nicepos.util.CustomBottomSheetDialogFragment;
import id.co.myproject.nicepos.util.MarketBottomSheet;
import id.co.myproject.nicepos.view.admin.MainActivity;
import id.co.myproject.nicepos.view.admin.home.menu.market.DetailBarangActivity;
import id.co.myproject.nicepos.view.kasir.KasirHomeActivity;

import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> {

    List<BarangSupplier> barangSupplierList = new ArrayList<>();
    Context context;
    int idCafe;

    public MarketAdapter(Context context, int idCafe) {
        this.idCafe = idCafe;
        this.context = context;
    }

    public void setBarangSupplierList(List<BarangSupplier> barangSupplierList){
        this.barangSupplierList.clear();
        this.barangSupplierList.addAll(barangSupplierList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MarketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketAdapter.ViewHolder holder, int position) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.bg_image);
        if (barangSupplierList.size() > 0) {
            holder.tvSupplier.setText(barangSupplierList.get(position).getNamaSupplier());
            holder.tvHarga.setText(rupiahFormat(Integer.valueOf(barangSupplierList.get(position).getHarga())));
            holder.tvNamaBarang.setText(barangSupplierList.get(position).getNamaBarang());
            Glide.with(context).applyDefaultRequestOptions(options).load(BuildConfig.BASE_URL_GAMBAR + "barang/" + barangSupplierList.get(position).getGambar()).into(holder.ivBarang);
            holder.tv_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailBarangActivity.class);
                    intent.putExtra("id_barang", barangSupplierList.get(position).getIdBarang());
                    context.startActivity(intent);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BottomSheetDialogFragment bottomSheetDialogFragment = new MarketBottomSheet(barangSupplierList.get(position), idCafe);
                    bottomSheetDialogFragment.show(((MainActivity)view.getContext()).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return barangSupplierList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSupplier, tvNamaBarang, tvHarga, tv_detail;
        ImageView ivBarang;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSupplier = itemView.findViewById(R.id.tv_supplier);
            tvNamaBarang = itemView.findViewById(R.id.tv_nama_barang);
            tvHarga = itemView.findViewById(R.id.tv_harga);
            ivBarang = itemView.findViewById(R.id.iv_barang);
            tv_detail = itemView.findViewById(R.id.tv_detail);
        }
    }
}
