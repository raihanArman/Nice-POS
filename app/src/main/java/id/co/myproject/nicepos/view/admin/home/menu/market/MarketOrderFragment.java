package id.co.myproject.nicepos.view.admin.home.menu.market;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import id.co.myproject.nicepos.adapter.MarketOrderAdapter;
import id.co.myproject.nicepos.database.MarketHelper;
import id.co.myproject.nicepos.database.OrderHelper;
import id.co.myproject.nicepos.model.BarangSupplier;
import id.co.myproject.nicepos.model.Menu;
import id.co.myproject.nicepos.model.Value;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.util.RecyclerItemTouchHelper;
import id.co.myproject.nicepos.util.RecyclerItemTouchHelperListener;
import id.co.myproject.nicepos.view.admin.home.AdminHomeFragment;
import id.co.myproject.nicepos.view.kasir.home.KasirHomeFragment;
import id.co.myproject.nicepos.view.kasir.home.StrukFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.nicepos.util.Helper.TYPE_BUNDLE_FROM_ORDER;
import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketOrderFragment extends Fragment implements RecyclerItemTouchHelperListener {

    int jumlah_request = 0, jumlah_pesanan = 0;
    RecyclerView rv_cart;
    RelativeLayout rl_root;
    MarketOrderAdapter marketOrderAdapter;
    MarketHelper marketHelper;
    LinearLayout lv_empty;
    CardView cv_cart;
    ImageView iv_back;
    public static TextView tv_total;
    Button btn_pesan;
    int id_cafe, totalBayar, uangKembalian;
    ApiRequest apiRequest;
    SharedPreferences sharedPreferences;

    public MarketOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_market_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        marketHelper = MarketHelper.getINSTANCE(getActivity());
        marketHelper.open();


        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        id_cafe = sharedPreferences.getInt("id_cafe", 0);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);

        rv_cart = view.findViewById(R.id.rv_cart);
        tv_total = view.findViewById(R.id.tv_total);
        rl_root = view.findViewById(R.id.rootLayout);
        btn_pesan = view.findViewById(R.id.btn_pesan);
        lv_empty = view.findViewById(R.id.lv_empty);
        cv_cart = view.findViewById(R.id.cd_cart);
        iv_back = view.findViewById(R.id.iv_back);
        marketOrderAdapter = new MarketOrderAdapter(getActivity(), marketHelper, id_cafe);

        rv_cart.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_cart.setAdapter(marketOrderAdapter);

        ItemTouchHelper.SimpleCallback itemSimpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemSimpleCallback).attachToRecyclerView(rv_cart);

        loadDataCart(id_cafe);

        btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputRequest();
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });


    }

    private void inputRequest(){
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        List<BarangSupplier> idSupplierList = marketHelper.getIdSupplier();
        for (BarangSupplier bySupplier : idSupplierList){
            Toast.makeText(getActivity(), "Id Supplier : "+bySupplier.getIdSupplier(), Toast.LENGTH_SHORT).show();
            Call<Value> inputRequest = apiRequest.inputRequestRequest(
                    id_cafe,
                    bySupplier.getIdSupplier(),
                    totalBayar,
                    "pending"
            );
            inputRequest.enqueue(new Callback<Value>() {
                @Override
                public void onResponse(Call<Value> call, Response<Value> response) {
                    if (response.isSuccessful()){
                        if (response.body().getValue() == 1) {
                            int id_request = response.body().getIdRequest();
                            List<BarangSupplier> barangSupplierList = marketHelper.getAllCartBySupplier(bySupplier.getIdSupplier());
                            for (BarangSupplier barangSupplier : barangSupplierList) {
                                int subTotal = Integer.parseInt(barangSupplier.getHarga())*Integer.parseInt(barangSupplier.getQty());
                                Call<Value> insertRequestItem = apiRequest.inputRequestItemRequest(
                                        id_request,
                                        barangSupplier.getIdBarang(),
                                        barangSupplier.getQty(),
                                        subTotal
                                );
                                insertRequestItem.enqueue(new Callback<Value>() {
                                    @Override
                                    public void onResponse(Call<Value> call, Response<Value> response) {
                                        if (response.isSuccessful()){
                                            if (response.body().getValue() == 1){
                                                if (jumlah_request == idSupplierList.size() && jumlah_pesanan == barangSupplierList.size()){
                                                    long delete = marketHelper.cleanCart(id_cafe);
                                                    if (delete > 0){
                                                        progressDialog.dismiss();
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                        builder.setMessage("Pesanan anda berhasil diterima\nSilahkan menunggu");
                                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                                transaction.replace(R.id.frame_home, new AdminHomeFragment());
                                                                transaction.addToBackStack(null);
                                                                transaction.commit();
                                                                jumlah_request = 0;
                                                                jumlah_pesanan = 0;
                                                                MarketFragment.fb_cart_market.setCount(marketHelper.getCountCart(id_cafe));
                                                                dialogInterface.dismiss();
                                                            }
                                                        });
                                                        builder.show();
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Value> call, Throwable t) {
                                        Toast.makeText(getActivity(), "Pesanan : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                jumlah_pesanan++;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Value> call, Throwable t) {
                    Toast.makeText(getActivity(), "Request : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            jumlah_request++;
        }
    }

    private void loadDataCart(int id_cafe) {
        List<BarangSupplier> barangSupplierList = marketHelper.getAllCart(id_cafe);
        marketOrderAdapter.setBarangSupplierList(barangSupplierList);
        if (barangSupplierList.size() <= 0){
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
        for (BarangSupplier barangSupplier : barangSupplierList){
            total+=(Integer.parseInt(barangSupplier.getHarga()))*(Integer.parseInt(barangSupplier.getQty()));
        }

        totalBayar = total;

        tv_total.setText(rupiahFormat(total));

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof MarketOrderAdapter.ViewHolder){
            String name = ((MarketOrderAdapter)rv_cart.getAdapter()).getItem(viewHolder.getAdapterPosition()).getNamaBarang();
            final BarangSupplier deleteItem = ((MarketOrderAdapter)rv_cart.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();
            marketOrderAdapter.removeItem(deleteIndex);
            marketHelper.removeToCart(deleteItem.getIdBarang(), id_cafe);

            int total = 0;
            List<BarangSupplier> barangSupplierList = marketHelper.getAllCart(id_cafe);
            for (BarangSupplier item : barangSupplierList){
                total+=(Integer.parseInt(item.getHarga()))*(Integer.parseInt(item.getQty()));
            }

            tv_total.setText(rupiahFormat(total));

            Snackbar snackbar = Snackbar.make(rl_root, name+" menghapus dari keranjang", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    marketOrderAdapter.restoreItem(deleteItem, deleteIndex);
                    marketHelper.addToCart(id_cafe, deleteItem, deleteItem.getQty());

                    int total = 0;
                    List<BarangSupplier> menuList = marketHelper.getAllCart(id_cafe);
                    for (BarangSupplier item : menuList){
                        total+=(Integer.parseInt(item.getHarga()))*(Integer.parseInt(item.getQty()));
                    }

                    tv_total.setText(rupiahFormat(total));

                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

}
