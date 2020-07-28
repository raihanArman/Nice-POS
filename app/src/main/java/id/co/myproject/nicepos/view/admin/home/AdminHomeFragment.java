package id.co.myproject.nicepos.view.admin.home;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import id.co.myproject.nicepos.BuildConfig;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.model.Cafe;
import id.co.myproject.nicepos.model.User;
import id.co.myproject.nicepos.model.Value;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.view.admin.MainActivity;
import id.co.myproject.nicepos.view.admin.home.menu.kas.KasFragment;
import id.co.myproject.nicepos.view.admin.login.InputCafeFragment;
import id.co.myproject.nicepos.view.admin.home.menu.market.MarketFragment;
import id.co.myproject.nicepos.view.admin.home.menu.kasir.KasirFragment;
import id.co.myproject.nicepos.view.admin.home.menu.kategori.KategoriFragment;
import id.co.myproject.nicepos.view.admin.home.menu.menu.MenuFragment;
import id.co.myproject.nicepos.view.admin.home.menu.stok.StokFragment;
import id.co.myproject.nicepos.view.admin.notif.NotifFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static id.co.myproject.nicepos.util.Helper.TYPE_EDIT;
import static id.co.myproject.nicepos.util.Helper.rupiahFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminHomeFragment extends Fragment {

    FrameLayout frameLayout, fm_cafe;
    TextView tv_transaksi;
    ApiRequest apiRequest;
    LinearLayout btn_menu, btn_ketagori, btn_diskon, btn_pembeli, btn_kasir, btn_pengaturan;
    SharedPreferences sharedPreferences;
    TextView tv_cafe_title, tv_alamat_cafe, tv_nama_user;
    ImageView iv_cafe_setting, iv_notif;
    CircleImageView iv_cafe;

    public AdminHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frameLayout = getActivity().findViewById(R.id.frame_home);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        tv_cafe_title = view.findViewById(R.id.tv_cafe_title);
        tv_alamat_cafe = view.findViewById(R.id.tv_alamat_cafe);
        tv_nama_user = view.findViewById(R.id.tv_nama_user);
        iv_cafe_setting = view.findViewById(R.id.iv_cafe_setting);
        iv_cafe = view.findViewById(R.id.iv_cafe);
        fm_cafe = view.findViewById(R.id.fm_cafe);
        tv_transaksi = view.findViewById(R.id.tv_transaksi_admin);
        iv_notif = view.findViewById(R.id.iv_notif);

        int idUser = sharedPreferences.getInt("id_user", 0);
        int idCafe = sharedPreferences.getInt("id_cafe", 0);

        btn_menu = view.findViewById(R.id.btn_menu);
        btn_ketagori = view.findViewById(R.id.btn_kategori);
        btn_diskon = view.findViewById(R.id.btn_diskon);
        btn_pembeli = view.findViewById(R.id.btn_pembeli);
        btn_pengaturan = view.findViewById(R.id.btn_pengaturan);
        btn_kasir = view.findViewById(R.id.btn_kasir);

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(view, new MenuFragment());
            }
        });

        btn_ketagori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(view, new KategoriFragment());
            }
        });

        btn_diskon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(view, new MarketFragment());
            }
        });

        btn_pembeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(view, new KasFragment());
            }
        });

        btn_kasir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(view, new KasirFragment());
            }
        });

        iv_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(view, new NotifFragment());
            }
        });

        iv_cafe_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputCafeFragment inputCafeFragment = new InputCafeFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("type", TYPE_EDIT);
                inputCafeFragment.setArguments(bundle);
                setFragment(view, inputCafeFragment);
            }
        });


        loadDataUser(idUser);
        loadDataCafe(idCafe);
        loadTotalTransaksi(idCafe);

    }

    private void loadTotalTransaksi(int idCafe) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time1 = " 00:00:00";
        String time2 = " 23:59:00";

        String tanggal1 = date + time1;
        String tanggal2 = date + time2;

        Call<Value> getTotalTransaksi = apiRequest.getTotalTransaksi(idCafe, tanggal1, tanggal2);
        getTotalTransaksi.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()){
                    if (response.body().getValue() == 1){
                        tv_transaksi.setText(rupiahFormat(response.body().getTotalTransaksi()));
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataCafe(int idCafe) {
        Call<Cafe> cafeCall = apiRequest.getCafeItem(idCafe);
        cafeCall.enqueue(new Callback<Cafe>() {
            @Override
            public void onResponse(Call<Cafe> call, Response<Cafe> response) {
                if (response.isSuccessful()){
                    Cafe cafe = response.body();
                    tv_cafe_title.setText(cafe.getNamaCafe());
                    tv_alamat_cafe.setText(cafe.getAlamatCafe());
                    if (!cafe.getGambar().equals("")){
                        fm_cafe.setVisibility(View.VISIBLE);
                        Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR + "cafe/" + cafe.getGambar()).into(iv_cafe);
                    }
                }
            }

            @Override
            public void onFailure(Call<Cafe> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataUser(int idUser) {
        Call<User> userCall = apiRequest.getUserItem(idUser);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User user = response.body();
                    tv_nama_user.setText(user.getNamaUser());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    private void loadKategori(){
//        Call<List<Kategori>> getKategori = apiRequest.getKategoriCallback();
//        getKategori.enqueue(new Callback<List<Kategori>>() {
//            @Override
//            public void onResponse(Call<List<Kategori>> call, Response<List<Kategori>> response) {
//                List<Kategori> kategoriList = response.body();
//                rvKategori.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//                kategoriAdapter = new KategoriAdapter(kategoriList, getActivity());
//                rvKategori.setAdapter(kategoriAdapter);
//                kategoriAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<List<Kategori>> call, Throwable t) {
//                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public void setFragment(View view, Fragment fragment){
        ((MainActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_home, fragment)
                        .addToBackStack(null)
                        .commit();
    }

}
