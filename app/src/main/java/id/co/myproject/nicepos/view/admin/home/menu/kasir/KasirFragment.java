package id.co.myproject.nicepos.view.admin.home.menu.kasir;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.adapter.KasirAdapter;
import id.co.myproject.nicepos.model.Kasir;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.nicepos.util.Helper.TYPE_ADD;

/**
 * A simple {@link Fragment} subclass.
 */
public class KasirFragment extends Fragment {


    ImageView iv_back;
    LinearLayout lv_empty;
    RecyclerView rv_kasir;
    EditText et_cari;
    FloatingActionButton fb_tambah;
    ApiRequest apiRequest;
    KasirAdapter kasirAdapter;
    int idCafe;
    SharedPreferences sharedPreferences;

    public KasirFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kasir, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_back = view.findViewById(R.id.iv_back);
        rv_kasir = view.findViewById(R.id.rv_kasir);
        et_cari = view.findViewById(R.id.et_cari);
        fb_tambah = view.findViewById(R.id.fb_tambah);
        lv_empty = view.findViewById(R.id.lv_empty);
        lv_empty.setVisibility(View.GONE);


        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        idCafe = sharedPreferences.getInt("id_cafe", 0);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        fb_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TambahKasirActivity.class);
                intent.putExtra("type", TYPE_ADD);
                startActivity(intent);
            }
        });

        rv_kasir.setLayoutManager(new LinearLayoutManager(getActivity()));
        kasirAdapter = new KasirAdapter(getActivity());
        rv_kasir.setAdapter(kasirAdapter);

        et_cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                loadCariKasir(editable.toString());
            }
        });


    }

    private void loadCariKasir(String cari) {
        Call<List<Kasir>> getCariKasir = apiRequest.cariKasirCariCallback(idCafe,cari);
        getCariKasir.enqueue(new Callback<List<Kasir>>() {
            @Override
            public void onResponse(Call<List<Kasir>> call, Response<List<Kasir>> response) {
                List<Kasir> kasirList = response.body();
                kasirAdapter.setKasirList(kasirList);
            }

            @Override
            public void onFailure(Call<List<Kasir>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error Cari : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadKasir(){
        Call<List<Kasir>> getKasir = apiRequest.getKasirCallback(idCafe);
        getKasir.enqueue(new Callback<List<Kasir>>() {
            @Override
            public void onResponse(Call<List<Kasir>> call, Response<List<Kasir>> response) {
                List<Kasir> kasirList = response.body();
                kasirAdapter.setKasirList(kasirList);
                if (kasirList.size() <= 0){
                    lv_empty.setVisibility(View.VISIBLE);
                    et_cari.setVisibility(View.INVISIBLE);
                    rv_kasir.setVisibility(View.INVISIBLE);
                }else{
                    lv_empty.setVisibility(View.GONE);
                    et_cari.setVisibility(View.VISIBLE);
                    rv_kasir.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Kasir>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadKasir();
    }
}
