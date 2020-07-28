package id.co.myproject.nicepos.adapter;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import id.co.myproject.nicepos.BuildConfig;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.model.Pesan;
import id.co.myproject.nicepos.util.TimeStampFormatter;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ViewHolder> {

    List<Pesan> pesanList = new ArrayList<>();
    Context context;

    public NotifAdapter(Context context) {
        this.context = context;
    }

    public void setNotificationList(List<Pesan> pesanList) {
        this.pesanList.clear();
        this.pesanList.addAll(pesanList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotifAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notif, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifAdapter.ViewHolder holder, int position) {
        if (pesanList.size() > 0){
            String date = DateFormat.format("dd MMM yyyy HH:mm", pesanList.get(position).getTanggal()).toString();
            Glide.with(context).load(BuildConfig.BASE_URL_GAMBAR+"cafe/"+pesanList.get(position).getAvatar()).into(holder.ivUser);
            holder.tvUser.setText(pesanList.get(position).getNamaSupplier());
            holder.tvTanggal.setText(date);
            holder.tvJumlah.setText(pesanList.get(position).getIsi());
            holder.tvStatus.setText(pesanList.get(position).getStatus());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    DetailRequestFragment detailRequestFragment = new DetailRequestFragment();
//                    Bundle bundle = new Bundle();
//                    detailRequestFragment.setArguments(bundle);
//                    bundle.putString("id_request", notificationList.get(position).getIdRequest());
//                    ((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.frame_home, detailRequestFragment)
//                            .addToBackStack("null")
//                            .commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return pesanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView ivUser;
        TextView tvUser, tvTanggal, tvJumlah, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.iv_cafe);
            tvUser = itemView.findViewById(R.id.tv_user);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvJumlah =itemView.findViewById(R.id.tv_jumlah_pesanan);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
