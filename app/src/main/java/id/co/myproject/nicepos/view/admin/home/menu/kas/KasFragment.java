package id.co.myproject.nicepos.view.admin.home.menu.kas;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.viewpager.widget.ViewPager;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.adapter.ViewPagerAdapter;
import id.co.myproject.nicepos.model.Kas;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class KasFragment extends Fragment {

    TextView tv_tanggal_hari_ini, tv_pemasukan, tv_pengeluaran;
    ViewPager vp_tab;
    TabLayout tb_layout;

    SharedPreferences sharedPreferences;
    ApiRequest apiRequest;
    ViewPagerAdapter viewPagerAdapter;

    public KasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        int idCafe = sharedPreferences.getInt("id_cafe", 0);

        tv_tanggal_hari_ini = view.findViewById(R.id.tv_tanggal_hari_ini);
        tv_pemasukan = view.findViewById(R.id.tv_pemasukan);
        tv_pengeluaran = view.findViewById(R.id.tv_pengeluaran);
        vp_tab = view.findViewById(R.id.vp_tab);
        tb_layout = view.findViewById(R.id.tab_layout);

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        vp_tab.setAdapter(viewPagerAdapter);
        tb_layout.setupWithViewPager(vp_tab);

        loadDataKas(idCafe);

    }

    private void loadDataKas(int idCafe) {
        String hari_ini = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        tv_tanggal_hari_ini.setText(hari_ini);

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time1 = " 00:00:00";
        String time2 = " 23:59:00";

        String tanggal1 = date+time1;
        String tanggal2 = date+time2;

        Call<Kas> kasCall = apiRequest.totalKasRequest(idCafe, tanggal1, tanggal2);
        kasCall.enqueue(new Callback<Kas>() {
            @Override
            public void onResponse(Call<Kas> call, Response<Kas> response) {
                if (response.isSuccessful()){
                    Kas kas = response.body();
                    tv_pemasukan.setText(rupiahFormat(Integer.parseInt(kas.getPemasukan())));
                    tv_pengeluaran.setText(rupiahFormat(Integer.parseInt(kas.getPengeluaran())));
                }
            }

            @Override
            public void onFailure(Call<Kas> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
