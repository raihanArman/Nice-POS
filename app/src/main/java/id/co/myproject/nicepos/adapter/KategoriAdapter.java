package id.co.myproject.nicepos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.BuildConfig;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.model.Kategori;
import id.co.myproject.nicepos.view.admin.home.menu.kategori.TambahKategoriActivity;

import static id.co.myproject.nicepos.util.Helper.TYPE_EDIT;


public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.ViewHolder> {

    List<Kategori> kategoriList = new ArrayList<>();
    Context context;
    FrameLayout parentFrameLayout;

    public KategoriAdapter(Context context) {
        this.context = context;
    }

    public void setKategoriList(List<Kategori> kategoriList){
        this.kategoriList.clear();
        this.kategoriList.addAll(kategoriList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kategori, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.bg_image);
        if (kategoriList.size() > 0) {
            holder.tvKategori.setText(kategoriList.get(position).getNamaKategori());
            Glide.with(context).applyDefaultRequestOptions(options).load(BuildConfig.BASE_URL_GAMBAR + "kategori/" + kategoriList.get(position).getGambar()).into(holder.ivKategori);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TambahKategoriActivity.class);
                    intent.putExtra("id_kategori", kategoriList.get(position).getIdKategori());
                    intent.putExtra("type", TYPE_EDIT);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return kategoriList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivKategori;
        TextView tvKategori;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivKategori = itemView.findViewById(R.id.iv_kategori);
            tvKategori = itemView.findViewById(R.id.tv_kategori);
        }
    }
}
