package id.co.myproject.nicepos.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import id.co.myproject.nicepos.BuildConfig;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.database.MarketHelper;
import id.co.myproject.nicepos.model.BahanMenu;
import id.co.myproject.nicepos.model.BarangSupplier;
import id.co.myproject.nicepos.model.Takaran;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.view.admin.home.menu.market.MarketFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

public class TakaranBottomSheet extends BottomSheetDialogFragment {

    private ListView lv_takaran;
//    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
//
//        @Override
//        public void onStateChanged(@NonNull View bottomSheet, int newState) {
//            setStateText(newState);
//            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                dismiss();
//            }
//
//        }
//
//        @Override
//        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//            setOffsetText(slideOffset);
//        }
//    };
    private LinearLayoutManager mLinearLayoutManager;
    private ApiRequest apiRequest;
    private String id_menu;
    Context context;
    public TakaranBottomSheet(Context context, ApiRequest apiRequest, String id_menu) {
        this.apiRequest = apiRequest;
        this.id_menu = id_menu;
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(View contentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(contentView, savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.takaran_bottom_sheet, null);
        dialog.setContentView(contentView);
        lv_takaran = contentView.findViewById(R.id.lv_takaran);


        Call<List<BahanMenu>> getBahanBaku = apiRequest.getBahanBaku(id_menu);
        getBahanBaku.enqueue(new Callback<List<BahanMenu>>() {
            @Override
            public void onResponse(Call<List<BahanMenu>> call, Response<List<BahanMenu>> response) {
                if (response.isSuccessful()){
                    List<BahanMenu> bahanMenuList = response.body();
                    List<String> namaBahan = new ArrayList<>();
                    for (BahanMenu bahanMenu : bahanMenuList){
                        namaBahan.add("- "+bahanMenu.getNama_bahan_baku()+" sebanyak "+bahanMenu.getTakaran()+" "+bahanMenu.getSatuan());
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, namaBahan);
                    lv_takaran.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<BahanMenu>> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}