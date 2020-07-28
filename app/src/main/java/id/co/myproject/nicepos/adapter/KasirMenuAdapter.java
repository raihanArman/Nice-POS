package id.co.myproject.nicepos.adapter;

import android.content.Context;
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
import id.co.myproject.nicepos.model.Menu;
import id.co.myproject.nicepos.util.CustomBottomSheetDialogFragment;
import id.co.myproject.nicepos.view.kasir.KasirHomeActivity;

import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

public class KasirMenuAdapter extends RecyclerView.Adapter<KasirMenuAdapter.ViewHolder> {

    List<Menu> menuList = new ArrayList<>();
    Context context;
    int id_kasir;

    public KasirMenuAdapter(Context context, int id_kasir) {
        this.context = context;
        this.id_kasir = id_kasir;
    }

    public void setMenuList(List<Menu> menuList){
        this.menuList.clear();
        this.menuList.addAll(menuList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KasirMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kasir_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KasirMenuAdapter.ViewHolder holder, int position) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.bg_image);
        if (menuList.size() > 0) {
            holder.tvMenu.setText(menuList.get(position).getNamaMenu());
            holder.tvHarga.setText(rupiahFormat(Integer.valueOf(menuList.get(position).getHarga())));
            Glide.with(context).applyDefaultRequestOptions(options).load(BuildConfig.BASE_URL_GAMBAR + "menu/" + menuList.get(position).getGambar()).into(holder.ivMenu);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Todo: NavBottomSheet
                    BottomSheetDialogFragment bottomSheetDialogFragment = new CustomBottomSheetDialogFragment(menuList.get(position), id_kasir);
                    bottomSheetDialogFragment.show(((KasirHomeActivity)view.getContext()).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMenu, tvHarga;
        ImageView ivMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenu = itemView.findViewById(R.id.tv_menu);
            tvHarga = itemView.findViewById(R.id.tv_harga);
            ivMenu = itemView.findViewById(R.id.iv_menu);
        }

    }
}
