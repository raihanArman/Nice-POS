package id.co.myproject.nicepos.view.data;


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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.adapter.TransaksiAdminAdapter;
import id.co.myproject.nicepos.database.OrderHelper;
import id.co.myproject.nicepos.model.StrukTransaksi;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.view.admin.PreviewFragment;
import id.co.myproject.nicepos.view.admin.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.nicepos.util.Helper.FILTER_HARI_INI;
import static id.co.myproject.nicepos.util.Helper.FILTER_SEMUA;
import static id.co.myproject.nicepos.util.Helper.LAPORAN_TRANSAKSI;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataTransaksiAdminFragment extends Fragment {

    RecyclerView rv_transaksi;
    TextView tv_tanggal;
    LinearLayout lv_cetak, lv_empty, lv_filter;
    ApiRequest apiRequest;
    TransaksiAdminAdapter transaksiAdminAdapter;
    SharedPreferences sharedPreferences;
    OrderHelper orderHelper;
    Spinner sp_filter;
    int type_filter;
    ProgressDialog progressDialog;

    public DataTransaksiAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_transaksi_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        orderHelper = OrderHelper.getINSTANCE(getActivity());
        orderHelper.open();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        int idCafe = sharedPreferences.getInt("id_cafe", 0);
        rv_transaksi = view.findViewById(R.id.rv_transaksi);
        tv_tanggal = view.findViewById(R.id.tv_tanggal_hari_ini);
        lv_cetak = view.findViewById(R.id.lv_cetak);
        lv_empty = view.findViewById(R.id.lv_empty);
        lv_filter = view.findViewById(R.id.lv_filter);
        rv_transaksi.setLayoutManager(new LinearLayoutManager(getActivity()));
        transaksiAdminAdapter = new TransaksiAdminAdapter(getActivity(), apiRequest);
        rv_transaksi.setAdapter(transaksiAdminAdapter);
        sp_filter = view.findViewById(R.id.sp_filter);

        List<String> filterList = new ArrayList<>();
        filterList.add("Hari ini");
        filterList.add("Lihat semua");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.item_spinner, R.id.weekofday, filterList);
        sp_filter.setAdapter(adapter);

        sp_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type_filter = i;
                loadDataTransaksi(idCafe, i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        lv_cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviewFragment cobahFragment = new PreviewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("tanggal", tv_tanggal.getText().toString());
                bundle.putInt("type_filter", type_filter);
                bundle.putString("type_laporan", LAPORAN_TRANSAKSI);
                cobahFragment.setArguments(bundle);
                ((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_home, cobahFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void loadDataTransaksi(int idCafe, int filter_position) {
        progressDialog.show();
        if (filter_position == FILTER_HARI_INI) {
            String hari_ini = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
            tv_tanggal.setText(hari_ini);
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String time1 = " 00:00:00";
            String time2 = " 23:59:00";

            String tanggal1 = date + time1;
            String tanggal2 = date + time2;

            Call<List<StrukTransaksi>> getLaporan = apiRequest.getLaporanTransaksi(idCafe, tanggal1, tanggal2);
            getLaporan.enqueue(new Callback<List<StrukTransaksi>>() {
                @Override
                public void onResponse(Call<List<StrukTransaksi>> call, Response<List<StrukTransaksi>> response) {
                    List<StrukTransaksi> strukTransaksiList = response.body();
                    transaksiAdminAdapter.setTransaksiList(strukTransaksiList);
                    if (strukTransaksiList.size() <= 0){
                        lv_empty.setVisibility(View.VISIBLE);
                        rv_transaksi.setVisibility(View.INVISIBLE);
                    }else{
                        lv_empty.setVisibility(View.GONE);
                        rv_transaksi.setVisibility(View.VISIBLE);
                    }
                    for (StrukTransaksi strukTransaksi : strukTransaksiList) {
                        long result = orderHelper.addToLaporan(strukTransaksi);
                        if (result > 0) {
//                        Toast.makeText(getActivity(), "Mantap", Toast.LENGTH_SHORT).show();
                        } else {
//                        Toast.makeText(getActivity(), "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<StrukTransaksi>> call, Throwable t) {
                    Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else if(filter_position == FILTER_SEMUA){
            Call<List<StrukTransaksi>> getLaporan = apiRequest.getAllLaporanTransaksi(idCafe);
            getLaporan.enqueue(new Callback<List<StrukTransaksi>>() {
                @Override
                public void onResponse(Call<List<StrukTransaksi>> call, Response<List<StrukTransaksi>> response) {
                    List<StrukTransaksi> strukTransaksiList = response.body();
                    transaksiAdminAdapter.setTransaksiList(strukTransaksiList);
                    if (strukTransaksiList.size() <= 0){
                        lv_empty.setVisibility(View.VISIBLE);
                        rv_transaksi.setVisibility(View.INVISIBLE);
                    }else{
                        lv_empty.setVisibility(View.GONE);
                        rv_transaksi.setVisibility(View.VISIBLE);
                    }
                    for (StrukTransaksi strukTransaksi : strukTransaksiList) {
                        long result = orderHelper.addToLaporan(strukTransaksi);
                        if (result > 0) {
//                        Toast.makeText(getActivity(), "Mantap", Toast.LENGTH_SHORT).show();
                        } else {
//                        Toast.makeText(getActivity(), "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<StrukTransaksi>> call, Throwable t) {
                    Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        progressDialog.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
        orderHelper.close();
    }


}
