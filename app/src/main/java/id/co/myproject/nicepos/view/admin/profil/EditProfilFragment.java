package id.co.myproject.nicepos.view.admin.profil;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import id.co.myproject.nicepos.BuildConfig;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.model.User;
import id.co.myproject.nicepos.model.Value;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.util.ConvertBitmap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfilFragment extends Fragment implements ConvertBitmap {

    public static final int REQUEST_GALERY = 96;
    public static final int REQUEST_CAMERA = 98;
    public static final int CAMERA_PERMISSION = 90;
    ImageView ivBack, ivUser, ivCamera;
    TextView tvCamera, tvGalery;
    Button btnUpdate;
    EditText etNama;
    Bitmap bitmap = null;
    String photoUser = null;
    int idUser;
    ApiRequest apiRequest;
//    public static String TAMPIL_USER_ITEM = "http://" + ipAddress + "/NONGKI_SERVER/API/tampil_user_item.php";
//    public static String EDIT_USER = "http://" + ipAddress + "/NONGKI_SERVER/API/edit_user.php";

    public EditProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);

        ivBack = view.findViewById(R.id.iv_back);
        ivUser = view.findViewById(R.id.iv_user);
        ivCamera = view.findViewById(R.id.iv_camera);
        etNama = view.findViewById(R.id.et_nama);
        btnUpdate = view.findViewById(R.id.btn_update);

        idUser = getArguments().getInt("id_user");

        loadDataUser();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_GALERY);
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_GALERY);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });

    }

    public boolean checkPermissions(String check) {
        int permissionRequest = ActivityCompat.checkSelfPermission(getActivity(), check);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED){
            return false;
        }else {
            return true;
        }
    }

//    private void getImage(){
//        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View menu = inflater.inflate(R.layout.dialog_image, null);
//        tvCamera = menu.findViewById(R.id.tv_camera);
//        tvGalery = menu.findViewById(R.id.tv_galery);
//        tvCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (checkPermissions(Manifest.permission.CAMERA)){
//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
//                }
//            }
//        });
//
//        tvGalery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, REQUEST_GALERY);
//            }
//        });
//        alert.setView(menu);
//        alert.show();
//    }

    private void editProfile() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        progressDialog.show();

        Call<Value> editUser = apiRequest.editUserCallback(idUser, etNama.getText().toString(), photoUser);
        editUser.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()){
                    if (response.body().getValue() == 1) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        ProfilFragment.statusUpdate = true;
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

    private void loadDataUser(){
        Call<User> userCall = apiRequest.getUserItem(idUser);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User user = response.body();
                    String avatar = user.getAvatar();
                    etNama.setText(user.getNamaUser());
                    if(avatar.equals("")){
                        avatar = "avatar.jpg";
                    }
                    Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR + "user/" + avatar).into(ivUser);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }else {
                Toast.makeText(getActivity(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALERY){
            if(resultCode == RESULT_OK && data != null){
                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    ivUser.setImageBitmap(bitmap);
                    new LoadBitmapConverterCallback(getActivity(), EditProfilFragment.this::bitmapToString).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == REQUEST_CAMERA){
            if (resultCode == RESULT_OK && data != null){
                bitmap = (Bitmap) data.getExtras().get("data");
                ivUser.setImageBitmap(bitmap);
                new LoadBitmapConverterCallback(getActivity(), EditProfilFragment.this::bitmapToString).execute();
            }
        }
    }

    @Override
    public void bitmapToString(String imgConvert) {
        photoUser = imgConvert;
    }

    private class LoadBitmapConverterCallback extends AsyncTask<Void, Void, String> {

        private WeakReference<Context> weakContext;
        private WeakReference<ConvertBitmap> weakCallback;

        public LoadBitmapConverterCallback(Context context, ConvertBitmap convertBitmap){
            this.weakContext = new WeakReference<>(context);
            this.weakCallback = new WeakReference<>(convertBitmap);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakContext.get();
        }

        @Override
        protected String doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();
            String imgBitmap = Base64.encodeToString(imgByte, Base64.DEFAULT);
            return imgBitmap;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            weakCallback.get().bitmapToString(s);
        }
    }

}