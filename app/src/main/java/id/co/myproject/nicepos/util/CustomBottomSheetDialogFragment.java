package id.co.myproject.nicepos.util;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import id.co.myproject.nicepos.database.OrderHelper;
import id.co.myproject.nicepos.model.Menu;
import id.co.myproject.nicepos.view.kasir.home.KasirHomeFragment;

import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private ImageView iv_cafe;
    private TextView tv_cafe, tv_harga;
    private Button btn_pesan;
    OrderHelper orderHelper;
    ElegantNumberButton btn_quantity;
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
    private Menu menu;
    private int id_kasir;
    public CustomBottomSheetDialogFragment(Menu menu, int id_kasir) {
        this.menu = menu;
        this.id_kasir = id_kasir;
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
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet, null);
        dialog.setContentView(contentView);

        orderHelper = OrderHelper.getINSTANCE(contentView.getContext());
        orderHelper.open();

        iv_cafe = contentView.findViewById(R.id.iv_cafe);
        tv_cafe = contentView.findViewById(R.id.tv_cafe);
        tv_harga = contentView.findViewById(R.id.tv_harga);
        btn_pesan = contentView.findViewById(R.id.btn_pesan);
        btn_quantity = contentView.findViewById(R.id.btn_quantity);
        tv_cafe.setText(menu.getNamaMenu());
        tv_harga.setText(rupiahFormat(Integer.valueOf(menu.getHarga())));
        Glide.with(contentView.getContext()).load(BuildConfig.BASE_URL_GAMBAR + "menu/" + menu.getGambar()).into(iv_cafe);

        btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long result = orderHelper.addToCart(id_kasir, menu, btn_quantity.getNumber());
                if (result > 0){
                    Toast.makeText(getActivity(), "Berhasil menyimpan", Toast.LENGTH_SHORT).show();
                    KasirHomeFragment.fb_transaksi.setCount(orderHelper.getCountCart(id_kasir));
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
        orderHelper.close();
    }
}