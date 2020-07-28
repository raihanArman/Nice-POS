package id.co.myproject.nicepos.view.admin.home.menu.kategori;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import id.co.myproject.nicepos.BuildConfig;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.model.Kategori;
import id.co.myproject.nicepos.model.Value;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.util.ConvertBitmap;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import static id.co.myproject.nicepos.util.Helper.TYPE_ADD;
import static id.co.myproject.nicepos.util.Helper.TYPE_EDIT;


public class TambahKategoriActivity extends AppCompatActivity implements ConvertBitmap{

    Toolbar toolbar;
    ApiRequest apiRequest;
    EditText et_nama_kategori;
    TextView tv_ganti_gambar, tv_simpan, tv_update, tv_hapus;
    ImageView iv_kategori;
    int type_intent;
    ProgressDialog progressDialog;
    LinearLayout btnLayoutAddImage;
    Bitmap bitmap;
    String image = null;
    int idCafe;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kategori);

        toolbar = findViewById(R.id.toolbar_menu_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        type_intent = getIntent().getIntExtra("type", 0);
        idCafe = sharedPreferences.getInt("id_cafe", 0);
        et_nama_kategori = findViewById(R.id.et_nama_kategori);
        btnLayoutAddImage = findViewById(R.id.layout_btn_add_image);
        iv_kategori = findViewById(R.id.iv_kategori);
        tv_ganti_gambar = findViewById(R.id.tv_ganti_gambar);
        tv_simpan = findViewById(R.id.tv_simpan);
        tv_update = findViewById(R.id.tv_update);
        tv_hapus = findViewById(R.id.tv_hapus);

        progressDialog = new ProgressDialog(TambahKategoriActivity.this);
        progressDialog.setMessage("Proses ...");

        btnLayoutAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 199);
            }
        });

        tv_ganti_gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 199);
            }
        });

        et_nama_kategori.addTextChangedListener(new TextWatcher() {
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
                    inputKategori();
                }
            });
        }else if (type_intent == TYPE_EDIT){
            tv_simpan.setVisibility(View.GONE);
            tv_hapus.setVisibility(View.VISIBLE);
            tv_update.setVisibility(View.VISIBLE);
            String id_kategori = getIntent().getStringExtra("id_kategori");
            loadKategoriItem(id_kategori);
            tv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    updateKategori(id_kategori);
                }
            });

            tv_hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    deleteKategori(id_kategori);
                }
            });
        }

    }

    private void inputKategori() {
        Call<Value> inputKategori = apiRequest.inputKategoriCallback(idCafe, et_nama_kategori.getText().toString(), image);
        inputKategori.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getValue() == 1) {
                        Toast.makeText(TambahKategoriActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TambahKategoriActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(TambahKategoriActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteKategori(String id_kategori) {
        Call<Value> deleteKategori = apiRequest.hapusKategoriCallback(id_kategori);
        deleteKategori.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getValue() == 1) {
                        Toast.makeText(TambahKategoriActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TambahKategoriActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(TambahKategoriActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateKategori(String id_kategori) {
        Call<Value> updateKategori = apiRequest.editKategoriCallback(id_kategori, et_nama_kategori.getText().toString(), image);
        updateKategori.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getValue() == 1) {
                        Toast.makeText(TambahKategoriActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TambahKategoriActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(TambahKategoriActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 199){
            if (resultCode == RESULT_OK && data != null){
                Uri imageResepUri = data.getData();
                iv_kategori.setVisibility(View.VISIBLE);
                btnLayoutAddImage.setVisibility(View.INVISIBLE);
                tv_ganti_gambar.setVisibility(View.VISIBLE);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageResepUri);
                    iv_kategori.setImageBitmap(bitmap);
                    cekInput();
                    new LoadBitmapConvertCallback(TambahKategoriActivity.this, this).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

    private void loadKategoriItem(String id_kategori){
        Call<Kategori> kategoriCall = apiRequest.getKategoriItemCallback(id_kategori);
        kategoriCall.enqueue(new Callback<Kategori>() {
            @Override
            public void onResponse(Call<Kategori> call, Response<Kategori> response) {
                if (response.isSuccessful()){
                    Kategori kategori = response.body();
                    setDataKategori(kategori);
                }
            }

            @Override
            public void onFailure(Call<Kategori> call, Throwable t) {
                Toast.makeText(TambahKategoriActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataKategori(Kategori kategori) {
        iv_kategori.setVisibility(View.VISIBLE);
        btnLayoutAddImage.setVisibility(View.INVISIBLE);
        tv_ganti_gambar.setVisibility(View.VISIBLE);
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.bg_image);
        Glide.with(this).applyDefaultRequestOptions(options).load(BuildConfig.BASE_URL_GAMBAR + "kategori/" + kategori.getGambar()).into(iv_kategori);
        et_nama_kategori.setText(kategori.getNamaKategori());
    }

    private void cekInput(){
        if (bitmap != null){
            if (!TextUtils.isEmpty(et_nama_kategori.getText().toString())){
                tv_simpan.setEnabled(true);
            }else {
                tv_simpan.setEnabled(false);
            }
        }else {
            tv_simpan.setEnabled(false);
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
