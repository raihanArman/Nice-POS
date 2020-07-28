package id.co.myproject.nicepos.view.data;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.adapter.TransaksiKasirAdapter;
import id.co.myproject.nicepos.model.Transaksi;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataTransaksiKasirFragment extends Fragment {

    RecyclerView rv_data;
    ApiRequest apiRequest;
    TransaksiKasirAdapter transaksiAdapter;

    public DataTransaksiKasirFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_transaksi_kasir, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        rv_data = view.findViewById(R.id.rv_data_transaksi);
        rv_data.setLayoutManager(new LinearLayoutManager(getActivity()));
        transaksiAdapter = new TransaksiKasirAdapter(getActivity());
        rv_data.setAdapter(transaksiAdapter);

        loadDataTransaksi();

    }

    private void loadDataTransaksi() {
        Call<List<Transaksi>> loadTransaksi = apiRequest.getTransaksi(4);
        loadTransaksi.enqueue(new Callback<List<Transaksi>>() {
            @Override
            public void onResponse(Call<List<Transaksi>> call, Response<List<Transaksi>> response) {
                if (response.isSuccessful()){
                    List<Transaksi> transaksiList = response.body();
                    transaksiAdapter.setTransaksiList(transaksiList);
                    Toast.makeText(getActivity(), "List : "+transaksiList.size(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Transaksi>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
