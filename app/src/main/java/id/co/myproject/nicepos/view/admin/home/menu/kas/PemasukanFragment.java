package id.co.myproject.nicepos.view.admin.home.menu.kas;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.adapter.PemasukanAdapter;
import id.co.myproject.nicepos.adapter.TransaksiAdminAdapter;
import id.co.myproject.nicepos.database.OrderHelper;
import id.co.myproject.nicepos.model.Pemasukan;
import id.co.myproject.nicepos.model.StrukTransaksi;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.view.admin.MainActivity;
import id.co.myproject.nicepos.view.admin.PreviewFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.nicepos.util.Helper.FILTER_HARI_INI;
import static id.co.myproject.nicepos.util.Helper.FILTER_SEMUA;
import static id.co.myproject.nicepos.util.Helper.LAPORAN_PEMASUKAN;
import static id.co.myproject.nicepos.util.Helper.LAPORAN_TRANSAKSI;

/**
 * A simple {@link Fragment} subclass.
 */
public class PemasukanFragment extends Fragment {

    RecyclerView rv_pemasukan;
    LinearLayout lv_preview;
    LinearLayout lv_cetak, lv_empty, lv_filter;
    ApiRequest apiRequest;
    PemasukanAdapter pemasukanAdapter;
    SharedPreferences sharedPreferences;
    Spinner sp_filter;
    int type_filter;
    ProgressDialog progressDialog;

    public PemasukanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pemasukan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        int idCafe = sharedPreferences.getInt("id_cafe", 0);
        rv_pemasukan = view.findViewById(R.id.rv_pemasukan);
        lv_preview = view.findViewById(R.id.lv_preview);
        lv_empty = view.findViewById(R.id.lv_empty);
        lv_filter = view.findViewById(R.id.lv_filter);
        rv_pemasukan.setLayoutManager(new LinearLayoutManager(getActivity()));
        pemasukanAdapter = new PemasukanAdapter(getActivity(), apiRequest);
        rv_pemasukan.setAdapter(pemasukanAdapter);
        sp_filter = view.findViewById(R.id.sp_filter);

        List<String> filterList = new ArrayList<>();
        filterList.add("Hari ini");
        filterList.add("Semua");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner,R.id.weekofday, filterList);
        sp_filter.setAdapter(arrayAdapter);

        sp_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type_filter = i;
                loadDataPemasukan(idCafe, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lv_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hari_ini = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
                PreviewFragment cobahFragment = new PreviewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("tanggal", hari_ini);
                bundle.putInt("type_filter", type_filter);
                bundle.putString("type_laporan", LAPORAN_PEMASUKAN);
                cobahFragment.setArguments(bundle);
                ((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_home, cobahFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    private void loadDataPemasukan(int idCafe, int filter_position) {
        progressDialog.show();
        if (filter_position == FILTER_HARI_INI) {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String time1 = " 00:00:00";
            String time2 = " 23:59:00";
            String tanggal1 = date + time1;
            String tanggal2 = date + time2;

            Call<List<Pemasukan>> getPemasukanRequeset = apiRequest.getPemasukanTodayRequest(idCafe, tanggal1, tanggal2);
            getPemasukanRequeset.enqueue(new Callback<List<Pemasukan>>() {
                @Override
                public void onResponse(Call<List<Pemasukan>> call, Response<List<Pemasukan>> response) {
                    if (response.isSuccessful()){
                        List<Pemasukan> pemasukanList = response.body();
                        pemasukanAdapter.setPemasukanList(pemasukanList);
                        if (pemasukanList.size() <= 0){
                            lv_empty.setVisibility(View.VISIBLE);
                            rv_pemasukan.setVisibility(View.INVISIBLE);
                        }else{
                            lv_empty.setVisibility(View.GONE);
                            rv_pemasukan.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Pemasukan>> call, Throwable t) {
                    Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else if (type_filter == FILTER_SEMUA){
            Call<List<Pemasukan>> getPemasukanAllRequest = apiRequest.getPemasukanAllRequest(idCafe);
            getPemasukanAllRequest.enqueue(new Callback<List<Pemasukan>>() {
                @Override
                public void onResponse(Call<List<Pemasukan>> call, Response<List<Pemasukan>> response) {
                    if (response.isSuccessful()){
                        List<Pemasukan> pemasukanList = response.body();
                        pemasukanAdapter.setPemasukanList(pemasukanList);
                        if (pemasukanList.size() <= 0){
                            lv_empty.setVisibility(View.VISIBLE);
                            rv_pemasukan.setVisibility(View.INVISIBLE);
                        }else{
                            lv_empty.setVisibility(View.GONE);
                            rv_pemasukan.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Pemasukan>> call, Throwable t) {
                    Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        progressDialog.dismiss();
    }
}
