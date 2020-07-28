package id.co.myproject.nicepos.view.admin.home.menu.menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.nicepos.BuildConfig;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.database.TakaranHelper;
import id.co.myproject.nicepos.model.Kategori;
import id.co.myproject.nicepos.model.Menu;
import id.co.myproject.nicepos.model.Takaran;
import id.co.myproject.nicepos.model.Value;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.util.ConvertBitmap;
import id.co.myproject.nicepos.util.MarketBottomSheet;
import id.co.myproject.nicepos.util.TakaranBottomSheet;
import id.co.myproject.nicepos.view.admin.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static id.co.myproject.nicepos.util.Helper.GALLERY_REQUEST;
import static id.co.myproject.nicepos.util.Helper.TYPE_ADD;
import static id.co.myproject.nicepos.util.Helper.TYPE_EDIT;

public class TambahMenuActivity extends AppCompatActivity implements ConvertBitmap {

    Toolbar toolbar;
    Spinner spKategori;
    ApiRequest apiRequest;
    EditText et_nama_menu, et_harga;
    TextView tv_ganti_gambar, tv_simpan, tv_update, tv_hapus,tv_bahan_baku, tv_bahan;
    ImageView iv_menu;
    int type_intent;
    int spinnerPosition;
    ProgressDialog progressDialog;
    LinearLayout btnLayoutAddImage;
    Bitmap bitmap;
    String image;
    String id_menu;
    TakaranHelper takaranHelper;
    int idCafe;
    SharedPreferences sharedPreferences;

    List<String> listIdKategori = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_menu);
        toolbar = findViewById(R.id.toolbar_menu_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_nama_menu = findViewById(R.id.et_nama_menu);
        et_harga = findViewById(R.id.et_harga);
        spKategori = findViewById(R.id.sp_kategori);
        btnLayoutAddImage = findViewById(R.id.layout_btn_add_image);
        iv_menu = findViewById(R.id.iv_menu);
        tv_ganti_gambar = findViewById(R.id.tv_ganti_gambar);
        tv_simpan = findViewById(R.id.tv_simpan);
        tv_update = findViewById(R.id.tv_update);
        tv_hapus = findViewById(R.id.tv_hapus);
        tv_bahan_baku = findViewById(R.id.tv_bahan_baku);
        tv_bahan = findViewById(R.id.tv_bahan);

        takaranHelper = TakaranHelper.getINSTANCE(this);
        takaranHelper.open();

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        idCafe = sharedPreferences.getInt("id_cafe", 0);

        type_intent = getIntent().getIntExtra("type", 0);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ...");

        List<String> listKategori = new ArrayList<>();
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        Call<List<Kategori>> call = apiRequest.getKategoriCallback(idCafe);
        call.enqueue(new Callback<List<Kategori>>() {
            @Override
            public void onResponse(Call<List<Kategori>> call, Response<List<Kategori>> response) {
                List<Kategori> kategoriList = response.body();

                for (int i=0; i<kategoriList.size(); i++){
                    listKategori.add(kategoriList.get(i).getNamaKategori());
                    listIdKategori.add(kategoriList.get(i).getIdKategori());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.item_spinner, R.id.weekofday, listKategori);
                spKategori.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Kategori>> call, Throwable t) {

            }
        });

        spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnLayoutAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });


        tv_ganti_gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });

        et_nama_menu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cekInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_harga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cekInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (type_intent == TYPE_ADD) {
            tv_simpan.setVisibility(View.VISIBLE);
            tv_hapus.setVisibility(View.GONE);
            tv_update.setVisibility(View.GONE);
            tv_simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    inputMenu();
                }
            });
            tv_bahan_baku.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type_intent == TYPE_ADD) {
                        Intent intent = new Intent(TambahMenuActivity.this, TambahBahanBakuActivity.class);
                        intent.putExtra("id_cafe", idCafe);
                        startActivity(intent);
                    }
                }
            });
        }else if (type_intent == TYPE_EDIT){
            tv_simpan.setVisibility(View.GONE);
            tv_hapus.setVisibility(View.VISIBLE);
            tv_update.setVisibility(View.VISIBLE);
            tv_bahan_baku.setText("Lihat bahan baku");
            String id_menu = getIntent().getStringExtra("id_menu");
            loadMenuItem(id_menu);
            tv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    updateMenu(id_menu);
                }
            });

            tv_bahan_baku.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BottomSheetDialogFragment bottomSheetDialogFragment = new TakaranBottomSheet(view.getContext(), apiRequest, id_menu);
                    bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                }
            });

            tv_hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    deleteMenu(id_menu);
                }
            });
        }

        loadBahan();

    }

    private void loadBahan() {
        int count = takaranHelper.getCountCart(idCafe);
        if (count > 0){
            tv_bahan.setVisibility(View.VISIBLE);
            tv_bahan.setText(takaranHelper.getCountCart(idCafe)+" telah dimasukkan");
        }
    }

    private void deleteMenu(String id_menu) {
        Call<Value> deleteMenu = apiRequest.hapusMenuCallback(id_menu);
        deleteMenu.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getValue() == 1) {
                        Toast.makeText(TambahMenuActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TambahMenuActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(TambahMenuActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMenu(String id_menu) {
        Call<Value> editMenu = apiRequest.editMenuCallback(id_menu, listIdKategori.get(spinnerPosition), et_nama_menu.getText().toString(), Integer.valueOf(et_harga.getText().toString()), image);
        editMenu.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getValue() == 1) {
                        Toast.makeText(TambahMenuActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TambahMenuActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(TambahMenuActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMenuItem(String id_menu) {
        Call<Menu> menuCall = apiRequest.getMenuItemCallback(id_menu);
        menuCall.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                if (response.isSuccessful()){
                    Menu menu = response.body();
                    setDataMenu(menu);
                }
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
                Toast.makeText(TambahMenuActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataMenu(Menu menu) {
        iv_menu.setVisibility(View.VISIBLE);
        btnLayoutAddImage.setVisibility(View.INVISIBLE);
        tv_ganti_gambar.setVisibility(View.VISIBLE);
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.bg_image);
        Glide.with(this).applyDefaultRequestOptions(options).load(BuildConfig.BASE_URL_GAMBAR + "menu/" + menu.getGambar()).into(iv_menu);
        et_nama_menu.setText(menu.getNamaMenu());
        et_harga.setText(menu.getHarga());

        int index = listIdKategori.indexOf(menu.getIdKategori());
        spKategori.setSelection(index);

    }

    private void inputMenu() {
        int harga = Integer.valueOf(et_harga.getText().toString());
        Call<Value> inputKategori = apiRequest.inputMenuCallback(idCafe,listIdKategori.get(spinnerPosition) ,et_nama_menu.getText().toString(),harga, image);
        inputKategori.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getValue() == 1) {
                        int id_menu = response.body().getId_menu();
                        List<Takaran> takaranList = takaranHelper.getAllCart(idCafe);
                        for (Takaran takaran : takaranList){
                            Call<Value> inputTakaran = apiRequest.inputBahanMenuRequest(
                                    id_menu, takaran.getIdBahanBaku(), takaran.getTakaran()
                            );
                            inputTakaran.enqueue(new Callback<Value>() {
                                @Override
                                public void onResponse(Call<Value> call, Response<Value> response) {
                                    if (response.isSuccessful()){
                                        if(response.body().getValue() == 1){
                                            long deleteTakaranDatabase = takaranHelper.cleanTakaran();
                                            if (deleteTakaranDatabase > 0){
                                                Toast.makeText(TambahMenuActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Value> call, Throwable t) {
                                    Toast.makeText(TambahMenuActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        Toast.makeText(TambahMenuActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TambahMenuActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(TambahMenuActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cekInput(){
        if (bitmap != null){
            if (!TextUtils.isEmpty(et_nama_menu.getText().toString())){
                if (!TextUtils.isEmpty(et_harga.getText().toString())){
                    tv_simpan.setEnabled(true);
                }else {
                    tv_simpan.setEnabled(false);
                }
            }else {
                tv_simpan.setEnabled(false);
            }
        }else {
            tv_simpan.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST){
            if (resultCode == RESULT_OK && data != null){
                Uri imageResepUri = data.getData();
                iv_menu.setVisibility(View.VISIBLE);
                btnLayoutAddImage.setVisibility(View.INVISIBLE);
                tv_ganti_gambar.setVisibility(View.VISIBLE);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageResepUri);
                    iv_menu.setImageBitmap(bitmap);
                    cekInput();
                    new LoadBitmapConvertCallback(TambahMenuActivity.this, this).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void bitmapToString(String imgConvert) {
        image = imgConvert;
    }

    private class LoadBitmapConvertCallback extends AsyncTask<Void, Void, String> {
        private WeakReference<Context> weakContext;
        private WeakReference<ConvertBitmap> weakInsert;

        public LoadBitmapConvertCallback(Context context, ConvertBitmap insertKategori) {
            this.weakContext = new WeakReference<>(context);
            this.weakInsert = new WeakReference<>(insertKategori);
        }

        @Override
        protected String doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();
            String imageBitmap = Base64.encodeToString(imgByte,Base64.DEFAULT);
            return imageBitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakContext.get();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            weakInsert.get().bitmapToString(aVoid);
        }
    }


}
