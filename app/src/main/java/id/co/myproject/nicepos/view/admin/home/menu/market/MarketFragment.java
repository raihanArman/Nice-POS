package id.co.myproject.nicepos.view.admin.home.menu.market;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.adapter.MarketAdapter;
import id.co.myproject.nicepos.database.MarketHelper;
import id.co.myproject.nicepos.model.BarangSupplier;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.view.admin.MainActivity;
import id.co.myproject.nicepos.view.kasir.KasirHomeActivity;
import id.co.myproject.nicepos.view.kasir.home.OrderFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends Fragment {

    ApiRequest apiRequest;
    RecyclerView rv_market;
    MarketAdapter marketAdapter;
    SharedPreferences sharedPreferences;
    public static CounterFab fb_cart_market;
    MarketHelper marketHelper;
    int idCafe;

    public MarketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_market, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        marketHelper = MarketHelper.getINSTANCE(getActivity());
        marketHelper.open();

        rv_market = getActivity().findViewById(R.id.rv_market);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        fb_cart_market = getActivity().findViewById(R.id.fb_cart_market);
        idCafe = sharedPreferences.getInt("id_cafe", 0);

        marketAdapter = new MarketAdapter(getActivity(), idCafe);
        rv_market.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv_market.setAdapter(marketAdapter);

        loadDataMarket();

        fb_cart_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_home, new MarketOrderFragment()).addToBackStack(null)
                        .commit();
            }
        });

    }

    private void loadDataMarket() {

        Call<List<BarangSupplier>>  getMarket = apiRequest.getBarangRequest();
        getMarket.enqueue(new Callback<List<BarangSupplier>>() {
            @Override
            public void onResponse(Call<List<BarangSupplier>> call, Response<List<BarangSupplier>> response) {
                if (response.isSuccessful()){
                    List<BarangSupplier> barangSupplierList = response.body();
                    marketAdapter.setBarangSupplierList(barangSupplierList);
                }
            }

            @Override
            public void onFailure(Call<List<BarangSupplier>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        fb_cart_market.setCount(marketHelper.getCountCart(idCafe));
    }

    @Override
    public void onStop() {
        super.onStop();
        marketHelper.close();
    }
}
