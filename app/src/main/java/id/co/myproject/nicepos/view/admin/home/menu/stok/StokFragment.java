package id.co.myproject.nicepos.view.admin.home.menu.stok;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.adapter.StokAdapter;
import id.co.myproject.nicepos.model.Stok;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StokFragment extends Fragment {

    ApiRequest apiRequest;
    SharedPreferences sharedPreferences;
    RecyclerView rv_stok;
    ImageView iv_back;
    int idCafe;
    StokAdapter stokAdapter;

    public StokFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stok, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        idCafe = sharedPreferences.getInt("id_cafe", 0);

        rv_stok = view.findViewById(R.id.rv_stok);
        iv_back = view.findViewById(R.id.iv_back);

        stokAdapter = new StokAdapter(getActivity());
        rv_stok.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_stok.setAdapter(stokAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataStok();
    }

    private void loadDataStok() {
        Call<List<Stok>> getStok = apiRequest.getStokRequest(idCafe);
        getStok.enqueue(new Callback<List<Stok>>() {
            @Override
            public void onResponse(Call<List<Stok>> call, Response<List<Stok>> response) {
                if (response.isSuccessful()){
                    List<Stok> stokList = response.body();
                    stokAdapter.setStokList(stokList);
                }
            }

            @Override
            public void onFailure(Call<List<Stok>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
