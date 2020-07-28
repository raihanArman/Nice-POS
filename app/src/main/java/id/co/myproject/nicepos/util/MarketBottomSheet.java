package id.co.myproject.nicepos.util;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import id.co.myproject.nicepos.BuildConfig;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.database.MarketHelper;
import id.co.myproject.nicepos.database.OrderHelper;
import id.co.myproject.nicepos.model.BarangSupplier;
import id.co.myproject.nicepos.model.Menu;
import id.co.myproject.nicepos.view.admin.home.menu.market.MarketFragment;
import id.co.myproject.nicepos.view.kasir.home.KasirHomeFragment;

import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

public class MarketBottomSheet extends BottomSheetDialogFragment {

    private ImageView iv_barang;
    private TextView tv_barang, tv_harga, tv_satuan;
    private ElegantNumberButton btn_qty;
    private Button btn_pesan;
    MarketHelper marketHelper;
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
    private BarangSupplier barangSupplier;
    private int id_cafe;
    public MarketBottomSheet(BarangSupplier barangSupplier, int id_cafe) {
        this.barangSupplier = barangSupplier;
        this.id_cafe = id_cafe;
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
        View contentView = View.inflate(getContext(), R.layout.market_bottom_sheet, null);
        dialog.setContentView(contentView);

        marketHelper = MarketHelper.getINSTANCE(contentView.getContext());
        marketHelper.open();

        iv_barang = contentView.findViewById(R.id.iv_barang);
        tv_barang = contentView.findViewById(R.id.tv_barang);
        tv_harga = contentView.findViewById(R.id.tv_harga);
        btn_pesan = contentView.findViewById(R.id.btn_pesan);
        btn_qty = contentView.findViewById(R.id.btn_quantity);
        tv_satuan = contentView.findViewById(R.id.tv_satuan);
        tv_barang.setText(barangSupplier.getNamaBarang());
        tv_harga.setText(rupiahFormat(Integer.valueOf(barangSupplier.getHarga())));
        tv_satuan.setText("/"+barangSupplier.getSatuan());
        Glide.with(contentView.getContext()).load(BuildConfig.BASE_URL_GAMBAR + "barang/" + barangSupplier.getGambar()).into(iv_barang);
        btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long result = marketHelper.addToCart(id_cafe, barangSupplier, btn_qty.getNumber());
                if (result > 0){
                    Toast.makeText(getActivity(), "Berhasil menyimpan", Toast.LENGTH_SHORT).show();
                    MarketFragment.fb_cart_market.setCount(marketHelper.getCountCart(id_cafe));
                    dismiss();
                }else {
                    Toast.makeText(getActivity(), "Gagal menyimpan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        marketHelper.close();
    }
}