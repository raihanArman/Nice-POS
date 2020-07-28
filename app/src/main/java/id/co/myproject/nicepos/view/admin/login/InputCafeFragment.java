package id.co.myproject.nicepos.view.admin.login;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.nicepos.BuildConfig;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.model.Cafe;
import id.co.myproject.nicepos.model.Value;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.util.ConvertBitmap;
import id.co.myproject.nicepos.view.admin.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static id.co.myproject.nicepos.util.Helper.GALLERY_REQUEST;
import static id.co.myproject.nicepos.util.Helper.TYPE_ADD;
import static id.co.myproject.nicepos.util.Helper.TYPE_EDIT;

/**
 * A simple {@link Fragment} subclass.
 */
public class InputCafeFragment extends Fragment implements ConvertBitmap {

    ApiRequest apiRequest;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView iv_back, iv_cafe;
    LinearLayout btnLayoutAddImage;
    TextView tv_ganti_gambar, tv_simpan, tv_update;
    EditText et_nama_cafe, et_alamat_cafe, et_no_telp;
    Bitmap bitmap;
    String image = null;
    ProgressDialog progressDialog;
    FrameLayout parentFrameLayout;
    GoogleApiClient googleApiClient;

    public InputCafeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_cafe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        iv_back = view.findViewById(R.id.iv_back);
        iv_cafe = view.findViewById(R.id.iv_cafe_add);
        btnLayoutAddImage = view.findViewById(R.id.layout_btn_add_image);
        tv_ganti_gambar = view.findViewById(R.id.tv_ganti_gambar);
        tv_update = view.findViewById(R.id.tv_update);
        tv_simpan = view.findViewById(R.id.tv_simpan);
        et_nama_cafe = view.findViewById(R.id.et_nama_cafe);
        et_alamat_cafe = view.findViewById(R.id.et_alamat_cafe);
        et_no_telp = view.findViewById(R.id.et_no_telp);
        parentFrameLayout = getActivity().findViewById(R.id.frame_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleApiClient.connect();


        int typeIntent = getArguments().getInt("type");
        if (typeIntent == TYPE_EDIT){
            int idCafe = sharedPreferences.getInt("id_cafe", 0);
            tv_simpan.setVisibility(View.GONE);
            tv_update.setVisibility(View.VISIBLE);
            loadDataCafe(idCafe);
            tv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    updateCafe(idCafe);
                }
            });

        }else if (typeIntent == TYPE_ADD){
            int idUser = sharedPreferences.getInt("id_user", 0);
            tv_simpan.setVisibility(View.VISIBLE);
            tv_update.setVisibility(View.GONE);
            tv_simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputCafe(idUser);
                }
            });
        }


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

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeIntent == TYPE_ADD){
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    setFragment(new SignInFragment());
                                }
                            });
                }else if (typeIntent == TYPE_EDIT) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });

    }

    private void inputCafe(int idUser) {
        Call<Value> inputCafe = apiRequest.inputCafeCallback(idUser, et_nama_cafe.getText().toString(), et_alamat_cafe.getText().toString(), et_no_telp.getText().toString(), image);
        inputCafe.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getValue() == 1) {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        editor.putInt("id_cafe", response.body().getIdCafe());
                        editor.apply();
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCafe(int idCafe) {
        String namaCafe = et_nama_cafe.getText().toString();
        String alamatCafe = et_alamat_cafe.getText().toString();
        String noTelp = et_no_telp.getText().toString();
        Call<Value> editCafe = apiRequest.editCafeCallback(idCafe, namaCafe, alamatCafe, noTelp, image);
        editCafe.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().getValue() == 1){
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
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
                    setDataCafe(cafe);
                }
            }

            @Override
            public void onFailure(Call<Cafe> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataCafe(Cafe cafe) {
        iv_cafe.setVisibility(View.VISIBLE);
        btnLayoutAddImage.setVisibility(View.INVISIBLE);
        tv_ganti_gambar.setVisibility(View.VISIBLE);
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.bg_image);
        Glide.with(this).applyDefaultRequestOptions(options).load(BuildConfig.BASE_URL_GAMBAR + "cafe/" + cafe.getGambar()).into(iv_cafe);
        et_nama_cafe.setText(cafe.getNamaCafe());
        et_alamat_cafe.setText(cafe.getAlamatCafe());
        et_no_telp.setText(cafe.getNoTelp());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST){
            if (resultCode == RESULT_OK && data != null){
                Uri imageResepUri = data.getData();
                iv_cafe.setVisibility(View.VISIBLE);
                btnLayoutAddImage.setVisibility(View.INVISIBLE);
                tv_ganti_gambar.setVisibility(View.VISIBLE);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageResepUri);
                    iv_cafe.setImageBitmap(bitmap);
                    cekInput();
                    new LoadBitmapConvertCallback(getActivity(), InputCafeFragment.this::bitmapToString).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getActivity(), "Data Null", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "Salah request", Toast.LENGTH_SHORT).show();
        }
    }

    private void cekInput(){
        if (bitmap != null){
            if (!TextUtils.isEmpty(et_nama_cafe.getText().toString())){
                if(!TextUtils.isEmpty(et_alamat_cafe.getText().toString())){
                    if(!TextUtils.isEmpty(et_no_telp.toString())){
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
        }else {
            tv_simpan.setEnabled(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}
