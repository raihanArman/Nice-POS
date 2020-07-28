package id.co.myproject.nicepos.adapter;

import android.content.Context;
import android.content.Intent;
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
import id.co.myproject.nicepos.model.Kasir;
import id.co.myproject.nicepos.view.admin.home.menu.kasir.TambahKasirActivity;

import static id.co.myproject.nicepos.util.Helper.TYPE_EDIT;

public class KasirAdapter extends RecyclerView.Adapter<KasirAdapter.ViewHolder> {

    List<Kasir> kasirList = new ArrayList<>();
    Context context;

    public KasirAdapter(Context context) {
        this.context = context;
    }

    public void setKasirList(List<Kasir> kasirList){
        this.kasirList.clear();
        this.kasirList.addAll(kasirList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KasirAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kasir, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KasirAdapter.ViewHolder holder, int position) {
//        String avatar = "";
        if (kasirList.size() > 0){
//            if (kasirList.get(position).getAvatar().equals("")){
//                avatar = kasirList.get(position).getAvatar();
//            }
            holder.tvKasir.setText(kasirList.get(position).getNamaKasir());
            holder.tvStatus.setText("Status : "+kasirList.get(position).getStatus());
//            Glide.with(context).load(avatar).into(holder.ivKasir);

            if (kasirList.get(position).getStatus().equals("online")){
                holder.layoutStatus.setBackgroundColor(context.getResources().getColor(R.color.colorRose));
            }else if (kasirList.get(position).getStatus().equals("offline")){
                holder.layoutStatus.setBackgroundColor(context.getResources().getColor(R.color.red_main));
            }

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TambahKasirActivity.class);
                intent.putExtra("id_kasir", kasirList.get(position).getIdKasir());
                intent.putExtra("type", TYPE_EDIT);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kasirList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivKasir;
        TextView tvStatus, tvKasir;
        View layoutStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivKasir = itemView.findViewById(R.id.iv_kasir);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvKasir = itemView.findViewById(R.id.tv_kasir);
            layoutStatus = itemView.findViewById(R.id.layout_status);
        }
    }
}
