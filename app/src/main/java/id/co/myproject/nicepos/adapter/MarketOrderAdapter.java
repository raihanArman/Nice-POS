package id.co.myproject.nicepos.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.BuildConfig;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.database.MarketHelper;
import id.co.myproject.nicepos.database.OrderHelper;
import id.co.myproject.nicepos.model.BarangSupplier;
import id.co.myproject.nicepos.model.Menu;
import id.co.myproject.nicepos.util.Helper;

import static id.co.myproject.nicepos.util.Helper.rupiahFormat;
import static id.co.myproject.nicepos.view.admin.home.menu.market.MarketOrderFragment.tv_total;

public class MarketOrderAdapter extends RecyclerView.Adapter<MarketOrderAdapter.ViewHolder> {

    List<BarangSupplier> barangSupplierList = new ArrayList<>();
    Context context;
    MarketHelper marketHelper;
    int idCafe;

    public MarketOrderAdapter(Context context, MarketHelper marketHelper, int idCafe) {
        this.context = context;
        this.marketHelper = marketHelper;
        this.idCafe = idCafe;
    }

    public void setBarangSupplierList(List<BarangSupplier> barangSupplierList){
        if (barangSupplierList.size() > 0){
            this.barangSupplierList.clear();
        }
        this.barangSupplierList.addAll(barangSupplierList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MarketOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketOrderAdapter.ViewHolder holder, int position) {
        if (barangSupplierList.size() > 0){
            holder.tv_menu.setText(barangSupplierList.get(position).getNamaBarang());
            holder.tv_harga.setText(rupiahFormat(Integer.valueOf(barangSupplierList.get(position).getHarga())));
            Glide.with(context).load(BuildConfig.BASE_URL_GAMBAR + "barang/" + barangSupplierList.get(position).getGambar()).into(holder.iv_menu);
            holder.btn_quantity.setNumber(barangSupplierList.get(position).getQty());
            holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    BarangSupplier barangSupplier = barangSupplierList.get(position);
                    barangSupplier.setQty(String.valueOf(newValue));
                    marketHelper.updateCart(barangSupplier, idCafe);
                    int total = 0;
                    List<BarangSupplier> barangSupplierList = marketHelper.getAllCart(idCafe);
                    for (BarangSupplier item : barangSupplierList){
                        total+=(Integer.parseInt(item.getHarga()))*(Integer.parseInt(item.getQty()));
                    }

                    tv_total.setText(rupiahFormat(total));

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return barangSupplierList.size();
    }

    public BarangSupplier getItem(int position){
        return barangSupplierList.get(position);
    }

    public void removeItem(int position){
        barangSupplierList.remove(position);
    }

    public void restoreItem(BarangSupplier item, int position){
        barangSupplierList.add(position, item);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        public TextView tv_menu, tv_harga;
        public ElegantNumberButton btn_quantity;
        public ImageView iv_menu;

        public RelativeLayout view_background;
        public LinearLayout view_foreground;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_menu = (ImageView) itemView.findViewById(R.id.iv_menu);
            tv_menu = (TextView) itemView.findViewById(R.id.tv_menu);
            tv_harga = (TextView) itemView.findViewById(R.id.tv_harga);
            btn_quantity = (ElegantNumberButton) itemView.findViewById(R.id.btn_quantity);
            view_background = (RelativeLayout) itemView.findViewById(R.id.view_background);
            view_foreground = (LinearLayout) itemView.findViewById(R.id.view_foreground);
        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select your option");
            contextMenu.add(0,0,getAdapterPosition(), Helper.DELETE);
        }
    }
}
