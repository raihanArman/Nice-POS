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
import id.co.myproject.nicepos.database.OrderHelper;
import id.co.myproject.nicepos.model.Menu;
import id.co.myproject.nicepos.util.Helper;

import static id.co.myproject.nicepos.util.Helper.rupiahFormat;
import static id.co.myproject.nicepos.view.kasir.home.OrderFragment.tv_total;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<Menu> menuList = new ArrayList<>();
    Context context;
    OrderHelper orderHelper;
    int id_kasir;

    public CartAdapter(Context context, OrderHelper orderHelper, int id_kasir) {
        this.context = context;
        this.orderHelper = orderHelper;
        this.id_kasir = id_kasir;
    }

    public void setMenuList(List<Menu> menuList){
        if (menuList.size() > 0){
            this.menuList.clear();
        }
        this.menuList.addAll(menuList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        if (menuList.size() > 0){
            holder.tv_menu.setText(menuList.get(position).getNamaMenu());
            holder.tv_harga.setText(rupiahFormat(Integer.valueOf(menuList.get(position).getHarga())));
            Glide.with(context).load(BuildConfig.BASE_URL_GAMBAR + "menu/" + menuList.get(position).getGambar()).into(holder.iv_menu);
            holder.btn_quantity.setNumber(menuList.get(position).getQuantity());
            holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    Menu menu = menuList.get(position);
                    menu.setQuantity(String.valueOf(newValue));
                    orderHelper.updateCart(menu, id_kasir);
                    int total = 0;
                    List<Menu> menuList = orderHelper.getAllCart(id_kasir);
                    for (Menu item : menuList){
                        total+=(Integer.parseInt(item.getHarga()))*(Integer.parseInt(item.getQuantity()));
                    }

                    tv_total.setText(rupiahFormat(total));

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public Menu getItem(int position){
        return menuList.get(position);
    }

    public void removeItem(int position){
        menuList.remove(position);
    }

    public void restoreItem(Menu item, int position){
        menuList.add(position, item);
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
