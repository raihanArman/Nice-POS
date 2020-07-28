package id.co.myproject.nicepos.view.kasir.home;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.BuildConfig;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.adapter.KasirMenuAdapter;
import id.co.myproject.nicepos.adapter.MenuAdapter;
import id.co.myproject.nicepos.database.OrderHelper;
import id.co.myproject.nicepos.model.Cafe;
import id.co.myproject.nicepos.model.Kasir;
import id.co.myproject.nicepos.model.Menu;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.view.kasir.KasirHomeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class KasirHomeFragment extends Fragment {
    RecyclerView rv_kasir_menu;
    EditText et_cari;
    public static CounterFab fb_transaksi;
    LinearLayout lv_empty;
    ImageView iv_cafe;
    TextView tv_kasir;
    KasirMenuAdapter kasirMenuAdapter;
    ApiRequest apiRequest;
    int idCafe;
    int id_kasir;
    SharedPreferences sharedPreferences;
    OrderHelper orderHelper;

    public KasirHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kasir_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        orderHelper = OrderHelper.getINSTANCE(getActivity());
        orderHelper.open();
        idCafe = sharedPreferences.getInt("id_cafe", 0);
        id_kasir = sharedPreferences.getInt("id_kasir", 0);

        rv_kasir_menu = view.findViewById(R.id.rv_kasir_menu);
        et_cari = view.findViewById(R.id.et_cari);
        fb_transaksi = view.findViewById(R.id.fb_transaksi);
        iv_cafe = view.findViewById(R.id.iv_cafe);
        tv_kasir = view.findViewById(R.id.tv_kasir);
        lv_empty = view.findViewById(R.id.lv_empty);
        lv_empty.setVisibility(View.GONE);

        rv_kasir_menu.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        kasirMenuAdapter = new KasirMenuAdapter(getActivity(),id_kasir);
        rv_kasir_menu.setAdapter(kasirMenuAdapter);

        et_cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                loadCariMenu(editable.toString());
            }
        });

        fb_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((KasirHomeActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_kasir_home, new OrderFragment()).addToBackStack(null)
                        .commit();
            }
        });

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        String date = DateFormat.format("dd MMM yyyy", cal).toString();
        Toast.makeText(getActivity(), "Hari ini : "+date, Toast.LENGTH_SHORT).show();

        loadDataCafeKasir();


    }


    private void loadCariMenu(String cari) {
        Call<List<Menu>> getCariMenu = apiRequest.cariMenuCallback(idCafe, cari);
        getCariMenu.enqueue(new Callback<List<Menu>>() {
            @Override
            public void onResponse(Call<List<Menu>> call, Response<List<Menu>> response) {
                List<Menu> menuList = response.body();
                kasirMenuAdapter.setMenuList(menuList);
            }

            @Override
            public void onFailure(Call<List<Menu>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMenu(){
        Call<List<Menu>> getKategori = apiRequest.getMenuCallback(idCafe);
        getKategori.enqueue(new Callback<List<Menu>>() {
            @Override
            public void onResponse(Call<List<Menu>> call, Response<List<Menu>> response) {
                List<Menu> menuList = response.body();
                kasirMenuAdapter.setMenuList(menuList);
                if (menuList.size() <= 0){
                    lv_empty.setVisibility(View.VISIBLE);
                    et_cari.setVisibility(View.INVISIBLE);
                    rv_kasir_menu.setVisibility(View.INVISIBLE);
                }else{
                    lv_empty.setVisibility(View.GONE);
                    et_cari.setVisibility(View.VISIBLE);
                    rv_kasir_menu.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Menu>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataCafeKasir(){
        Call<Cafe> getDataCafe = apiRequest.getCafeItem(idCafe);
        getDataCafe.enqueue(new Callback<Cafe>() {
            @Override
            public void onResponse(Call<Cafe> call, Response<Cafe> response) {
                if (response.isSuccessful()){
                    Cafe cafe = response.body();
                    if (!cafe.getGambar().equals("")){
                        iv_cafe.setVisibility(View.VISIBLE);
                        Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR + "cafe/" + cafe.getGambar()).into(iv_cafe);
                    }
                    Call<Kasir> kasirData = apiRequest.getKasirItemCallback(String.valueOf(id_kasir));
                    kasirData.enqueue(new Callback<Kasir>() {
                        @Override
                        public void onResponse(Call<Kasir> call, Response<Kasir> response) {
                            if (response.isSuccessful()){
                                tv_kasir.setText(response.body().getNamaKasir());
                            }
                        }

                        @Override
                        public void onFailure(Call<Kasir> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Cafe> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fb_transaksi.setCount(orderHelper.getCountCart(id_kasir));
        loadMenu();
    }

    @Override
    public void onStop() {
        super.onStop();
        orderHelper.close();
    }

}
