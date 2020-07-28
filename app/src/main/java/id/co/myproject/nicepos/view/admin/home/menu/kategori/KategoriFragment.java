package id.co.myproject.nicepos.view.admin.home.menu.kategori;


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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.adapter.KategoriAdapter;
import id.co.myproject.nicepos.model.Kategori;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.view.admin.home.menu.kategori.TambahKategoriActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.nicepos.util.Helper.TYPE_ADD;

/**
 * A simple {@link Fragment} subclass.
 */
public class KategoriFragment extends Fragment {

    ImageView iv_back;
    LinearLayout lv_empty;
    RecyclerView rv_kategori;
    KategoriAdapter kategoriAdapter;
    EditText et_cari;
    FloatingActionButton fb_tambah;
    ApiRequest apiRequest;
    int idCafe;
    SharedPreferences sharedPreferences;

    public KategoriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kategori, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        iv_back = view.findViewById(R.id.iv_back);
        rv_kategori = view.findViewById(R.id.rv_kategori);
        et_cari = view.findViewById(R.id.et_cari);
        fb_tambah = view.findViewById(R.id.fb_tambah);
        lv_empty = view.findViewById(R.id.lv_empty);
        lv_empty.setVisibility(View.GONE);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        idCafe = sharedPreferences.getInt("id_cafe", 0);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        fb_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TambahKategoriActivity.class);
                intent.putExtra("type", TYPE_ADD);
                startActivity(intent);
            }
        });

        et_cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                loadCariKategori(editable.toString());
            }
        });

        rv_kategori.setLayoutManager(new LinearLayoutManager(getActivity()));
        kategoriAdapter = new KategoriAdapter(getActivity());
        rv_kategori.setAdapter(kategoriAdapter);
        kategoriAdapter.notifyDataSetChanged();
    }

    private void loadCariKategori(String cari) {
        Call<List<Kategori>> getCariResep = apiRequest.cariKategoriCallback(idCafe,cari);
        getCariResep.enqueue(new Callback<List<Kategori>>() {
            @Override
            public void onResponse(Call<List<Kategori>> call, Response<List<Kategori>> response) {
                List<Kategori> kategoriList = response.body();
                kategoriAdapter.setKategoriList(kategoriList);
            }

            @Override
            public void onFailure(Call<List<Kategori>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error Cari : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadKategori();
    }

    private void loadKategori(){
        Call<List<Kategori>> getKategori = apiRequest.getKategoriCallback(idCafe);
        getKategori.enqueue(new Callback<List<Kategori>>() {
            @Override
            public void onResponse(Call<List<Kategori>> call, Response<List<Kategori>> response) {
                List<Kategori> kategoriList = response.body();
                kategoriAdapter.setKategoriList(kategoriList);
                if (kategoriList.size() <= 0){
                    lv_empty.setVisibility(View.VISIBLE);
                    et_cari.setVisibility(View.INVISIBLE);
                    rv_kategori.setVisibility(View.INVISIBLE);
                }else{
                    lv_empty.setVisibility(View.GONE);
                    et_cari.setVisibility(View.VISIBLE);
                    rv_kategori.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Kategori>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
