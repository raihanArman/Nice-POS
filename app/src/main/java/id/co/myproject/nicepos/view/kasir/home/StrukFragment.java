package id.co.myproject.nicepos.view.kasir.home;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feng.fixtablelayout.FixTableLayout;

import java.util.List;

import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.adapter.PesananAdapter;
import id.co.myproject.nicepos.model.Pesanan;
import id.co.myproject.nicepos.model.StrukTransaksi;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.nicepos.util.Helper.TYPE_BUNDLE_FROM_ORDER;
import static id.co.myproject.nicepos.util.Helper.TYPE_BUNDLE_FROM_TRANSAKSI;
import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class StrukFragment extends Fragment {

    TextView tv_tanggal, tv_cafe, tv_kasir, tv_total, tv_bayar, tv_kembalian;
    int id_transaksi, type_bundle;
    FixTableLayout tb_menu;
    ApiRequest apiRequest;
    public String[] title = {"Menu","Harga","Qty"};
    Button btn_back;
    ImageView iv_back;

    public StrukFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_struk, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        id_transaksi = getArguments().getInt("id_transaksi");
        type_bundle = getArguments().getInt("type_bundle");
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);

        tv_tanggal = view.findViewById(R.id.tv_tanggal);
        tv_cafe = view.findViewById(R.id.tv_cafe);
        tv_kasir = view.findViewById(R.id.tv_kasir);
        tv_total = view.findViewById(R.id.tv_total);
        tv_bayar = view.findViewById(R.id.tv_bayar);
        tv_kembalian = view.findViewById(R.id.tv_kembalian);
        tb_menu = view.findViewById(R.id.tb_menu);
        btn_back = view.findViewById(R.id.btn_back);
        iv_back = view.findViewById(R.id.iv_back);

        loadData();
        loadTableMenu();

        if (type_bundle == TYPE_BUNDLE_FROM_ORDER){
            iv_back.setVisibility(View.GONE);
        }else if (type_bundle == TYPE_BUNDLE_FROM_TRANSAKSI){
            iv_back.setVisibility(View.VISIBLE);
            btn_back.setVisibility(View.INVISIBLE);
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

    }

    private void loadData(){
        Call<StrukTransaksi> getStruk = apiRequest.getStruk(id_transaksi);
        getStruk.enqueue(new Callback<StrukTransaksi>() {
            @Override
            public void onResponse(Call<StrukTransaksi> call, Response<StrukTransaksi> response) {
                if (response.isSuccessful()){
                    StrukTransaksi strukTransaksi = response.body();
                    setData(strukTransaksi);
                }
            }

            @Override
            public void onFailure(Call<StrukTransaksi> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(StrukTransaksi strukTransaksi) {
        tv_cafe.setText(strukTransaksi.getNamaCafe());
        String date = DateFormat.format("dd MMM yyyy", strukTransaksi.getTanggal()).toString();
        tv_tanggal.setText(date);
        tv_total.setText(strukTransaksi.getTotal());
        tv_bayar.setText(rupiahFormat(Integer.parseInt(strukTransaksi.getUangBayar())));
        tv_kembalian.setText(rupiahFormat(Integer.parseInt(strukTransaksi.getUangKembali())));
        tv_kasir.setText(strukTransaksi.getNamaKasir());
    }

    private void loadTableMenu(){
        Call<List<Pesanan>> getPesanan = apiRequest.getPesanan(id_transaksi);
        getPesanan.enqueue(new Callback<List<Pesanan>>() {
            @Override
            public void onResponse(Call<List<Pesanan>> call, Response<List<Pesanan>> response) {
                if (response.isSuccessful()){
                    List<Pesanan> pesananList = response.body();
                    PesananAdapter pesananAdapter = new PesananAdapter(title, pesananList);
                    tb_menu.setAdapter(pesananAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Pesanan>> call, Throwable t) {

            }
        });
    }
}
