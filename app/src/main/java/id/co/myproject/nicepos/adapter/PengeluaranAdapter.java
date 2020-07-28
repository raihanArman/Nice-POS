package id.co.myproject.nicepos.adapter;

import android.content.Context;
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
import id.co.myproject.nicepos.model.Pemasukan;
import id.co.myproject.nicepos.model.Pengeluaran;
import id.co.myproject.nicepos.model.Value;
import id.co.myproject.nicepos.request.ApiRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

public class PengeluaranAdapter extends RecyclerView.Adapter<PengeluaranAdapter.ViewHolder> {

    List<Pengeluaran> pengeluaranList = new ArrayList<>();
    Context context;
    ApiRequest apiRequest;

    public PengeluaranAdapter(Context context, ApiRequest apiRequest) {
        this.context = context;
        this.apiRequest = apiRequest;

    }

    public void setPemasukanList(List<Pengeluaran> pengeluaranList){
        this.pengeluaranList.clear();
        this.pengeluaranList.addAll(pengeluaranList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PengeluaranAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PengeluaranAdapter.ViewHolder holder, int position) {
        String tanggal = DateFormat.format("dd MMM yyyy", pengeluaranList.get(position).getTanggal()).toString();
        String jam = DateFormat.format("HH:mm", pengeluaranList.get(position).getTanggal()).toString();
        holder.tvTanggal.setText(tanggal);
        holder.tvJam.setText(jam);
        holder.tvTotal.setText(rupiahFormat(Integer.parseInt(pengeluaranList.get(position).getTotal())));

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Value> deletePengeluaranRequest = apiRequest.hapusPengeluaranRequest(pengeluaranList.get(position).getIdPengeluaran());
                deletePengeluaranRequest.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.body().getValue() == 1){
                                removeItem(position);
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

    }

    @Override
    public int getItemCount() {
        return pengeluaranList.size();
    }

    public void removeItem(int position){
        pengeluaranList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTanggal, tvJam, tvTotal;
        ImageView ivDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvJam = itemView.findViewById(R.id.tv_jam);
            tvTotal = itemView.findViewById(R.id.tv_total);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }
    }
}
