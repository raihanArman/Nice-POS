package id.co.myproject.nicepos.view.kasir.home;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.adapter.CartAdapter;
import id.co.myproject.nicepos.database.OrderHelper;
import id.co.myproject.nicepos.model.BahanMenu;
import id.co.myproject.nicepos.model.Menu;
import id.co.myproject.nicepos.model.Value;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.util.RecyclerItemTouchHelper;
import id.co.myproject.nicepos.util.RecyclerItemTouchHelperListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.nicepos.util.Helper.TYPE_BUNDLE_FROM_ORDER;
import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements RecyclerItemTouchHelperListener {

    RecyclerView rv_cart;
    RelativeLayout rl_root;
    CartAdapter cartAdapter;
    OrderHelper orderHelper;
    LinearLayout lv_empty;
    CardView cv_cart;
    ImageView iv_back;
    public static TextView tv_total, tv_kembalian;
    EditText et_uang;
    Button btn_pesan;
    int id_kasir, id_cafe, totalBayar, uangKembalian;
    ApiRequest apiRequest;
    SharedPreferences sharedPreferences;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderHelper = OrderHelper.getINSTANCE(getActivity());
        orderHelper.open();


        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        id_cafe = sharedPreferences.getInt("id_cafe", 0);
        id_kasir = sharedPreferences.getInt("id_kasir", 0);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);

        rv_cart = view.findViewById(R.id.rv_cart);
        tv_total = view.findViewById(R.id.tv_total);
        tv_kembalian = view.findViewById(R.id.tv_kembalian);
        et_uang = view.findViewById(R.id.et_uang);
        rl_root = view.findViewById(R.id.rootLayout);
        btn_pesan = view.findViewById(R.id.btn_pesan);
        lv_empty = view.findViewById(R.id.lv_empty);
        cv_cart = view.findViewById(R.id.cd_cart);
        iv_back = view.findViewById(R.id.iv_back);
        cartAdapter = new CartAdapter(getActivity(), orderHelper, id_kasir);

        rv_cart.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_cart.setAdapter(cartAdapter);
        tv_kembalian.setText(rupiahFormat(0));

        ItemTouchHelper.SimpleCallback itemSimpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemSimpleCallback).attachToRecyclerView(rv_cart);

        loadDataCart(id_kasir);

        btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTransaksi(id_kasir, id_cafe);
            }
        });

        et_uang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                prosesKembalian(editable.toString());
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });


    }

    private void prosesKembalian(String uangText) {
//        Todo: Bug kembalian
        if (!TextUtils.isEmpty(uangText)){
            uangKembalian =  Integer.parseInt(uangText) - totalBayar;
            if (uangKembalian != 0) {
                tv_kembalian.setText(rupiahFormat(uangKembalian));
            }else {
                tv_kembalian.setText(0);
            }
        }
    }

    private void inputTransaksi(int id_kasir, int id_cafe) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        Call<Value> inputTransaksi = apiRequest.inpuTransaksiCallback(
                id_cafe,
                id_kasir,
                totalBayar,
                Integer.parseInt(et_uang.getText().toString()),
                uangKembalian
        );
        inputTransaksi.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()){
                    if (response.body().getValue() == 1){
                        int id_transaksi = response.body().getIdTransaksi();
                        List<Menu> menuList = orderHelper.getAllCart(id_kasir);
                        for (Menu menu : menuList){
                            int subTotal = Integer.parseInt(menu.getHarga())*Integer.parseInt(menu.getQuantity());
                            Call<Value> inputPesanan = apiRequest.inputPesananCallback(
                                    id_transaksi,
                                    menu.getIdMenu(),
                                    menu.getQuantity(),
                                    subTotal
                            );
                            inputPesanan.enqueue(new Callback<Value>() {
                                @Override
                                public void onResponse(Call<Value> call, Response<Value> response) {
                                    if (response.isSuccessful()){
                                        if (response.body().getValue() == 1){
                                            Call<List<BahanMenu>> getBahanMenu = apiRequest.getBahanBaku(menu.getIdMenu());
                                            getBahanMenu.enqueue(new Callback<List<BahanMenu>>() {
                                                @Override
                                                public void onResponse(Call<List<BahanMenu>> call, Response<List<BahanMenu>> response) {
                                                    List<BahanMenu> bahanMenuList = response.body();
                                                    for (BahanMenu bahanMenu : bahanMenuList){
                                                        Call<Value> updateStok = apiRequest.updateStokRequest(
                                                                bahanMenu.getId_bahan_baku(),
                                                                bahanMenu.getTakaran(),
                                                                menu.getQuantity()
                                                        );
                                                        updateStok.enqueue(new Callback<Value>() {
                                                            @Override
                                                            public void onResponse(Call<Value> call, Response<Value> response) {
                                                                if (response.isSuccessful()){
                                                                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                                    if (response.body().getValue() == 1){

                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Value> call, Throwable t) {
                                                                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<List<BahanMenu>> call, Throwable t) {
                                                    Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Value> call, Throwable t) {
                                    Toast.makeText(getActivity(), "Pesanan : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        Call<Value> inputPemasukan = apiRequest.inputPemasukanRequest(id_cafe, totalBayar);
                        inputPemasukan.enqueue(new Callback<Value>() {
                            @Override
                            public void onResponse(Call<Value> call, Response<Value> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    if (response.body().getValue() == 1){
                                        long delete = orderHelper.cleanCart(id_kasir);
                                        if (delete > 0){
                                            progressDialog.dismiss();
                                            StrukFragment strukFragment = new StrukFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("id_transaksi", id_transaksi);
                                            bundle.putInt("type_bundle", TYPE_BUNDLE_FROM_ORDER);
                                            strukFragment.setArguments(bundle);
                                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                            transaction.replace(R.id.frame_kasir_home, strukFragment);
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                            KasirHomeFragment.fb_transaksi.setCount(orderHelper.getCountCart(id_kasir));
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Value> call, Throwable t) {
                                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(getActivity(), "Transaksi : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void loadDataCart(int id_kasir) {
        List<Menu> menuList = orderHelper.getAllCart(id_kasir);
        cartAdapter.setMenuList(menuList);
        if (menuList.size() <= 0){
            lv_empty.setVisibility(View.VISIBLE);
            iv_back.setVisibility(View.VISIBLE);
            cv_cart.setVisibility(View.INVISIBLE);
            rv_cart.setVisibility(View.INVISIBLE);
        }else{
            iv_back.setVisibility(View.GONE);
            lv_empty.setVisibility(View.GONE);
            cv_cart.setVisibility(View.VISIBLE);
            rv_cart.setVisibility(View.VISIBLE);
        }

        int total = 0;
        for (Menu menu : menuList){
            total+=(Integer.parseInt(menu.getHarga()))*(Integer.parseInt(menu.getQuantity()));
        }

        totalBayar = total;

        tv_total.setText(rupiahFormat(total));

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.ViewHolder){
            String name = ((CartAdapter)rv_cart.getAdapter()).getItem(viewHolder.getAdapterPosition()).getIdMenu();
            final Menu deleteItem = ((CartAdapter)rv_cart.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();
            cartAdapter.removeItem(deleteIndex);
            orderHelper.removeToCart(deleteItem.getIdMenu(), id_kasir);

            int total = 0;
            List<Menu> menuList = orderHelper.getAllCart(id_kasir);
            for (Menu item : menuList){
                total+=(Integer.parseInt(item.getHarga()))*(Integer.parseInt(item.getQuantity()));
            }

            tv_total.setText(rupiahFormat(total));

            Snackbar snackbar = Snackbar.make(rl_root, name+" menghapus dari keranjang", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartAdapter.restoreItem(deleteItem, deleteIndex);
                    orderHelper.addToCart(id_kasir, deleteItem, deleteItem.getQuantity());

                    int total = 0;
                    List<Menu> menuList = orderHelper.getAllCart(id_kasir);
                    for (Menu item : menuList){
                        total+=(Integer.parseInt(item.getHarga()))*(Integer.parseInt(item.getQuantity()));
                    }

                    tv_total.setText(rupiahFormat(total));

                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }
}
