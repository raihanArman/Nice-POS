package id.co.myproject.nicepos.view.admin.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.model.Value;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.view.admin.MainActivity;
import id.co.myproject.nicepos.view.kasir.KasirHomeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import static id.co.myproject.nicepos.util.Helper.TYPE_ADD;
import static id.co.myproject.nicepos.util.Helper.TYPE_LOGIN_LEVEL_ADMIN;
import static id.co.myproject.nicepos.util.Helper.TYPE_LOGIN_LEVEL_KASIR;


public class LoginActivity extends AppCompatActivity {

    FrameLayout frameLayout;
//    public static boolean onResetPasswordFragment = false;
    public static boolean setSignUpFragment = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApiRequest apiRequest;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        frameLayout = findViewById(R.id.frame_login);
    }


    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(), fragment);
        transaction.commit();
    }

    private void updateUI(final boolean isSignedIn) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        int typeLogin = sharedPreferences.getInt("type_login", 0);
        if (typeLogin == TYPE_LOGIN_LEVEL_ADMIN) {
            int idUser = sharedPreferences.getInt("id_user", 0);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isSignedIn) {
                        if (idUser != 0) {
                            Call<Value> cekCafe = apiRequest.cekCafeCallback(idUser);
                            cekCafe.enqueue(new Callback<Value>() {
                                @Override
                                public void onResponse(Call<Value> call, Response<Value> response) {
                                    progressDialog.dismiss();
                                    if (response.body().getValue() == 1) {
                                        editor.putInt("id_cafe", response.body().getIdCafe());
                                        editor.apply();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        InputCafeFragment inputCafeFragment = new InputCafeFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("type", TYPE_ADD);
                                        inputCafeFragment.setArguments(bundle);
                                        setFragment(inputCafeFragment);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Value> call, Throwable t) {
                                    Toast.makeText(LoginActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            });
        }else if(typeLogin == TYPE_LOGIN_LEVEL_KASIR){
            Intent intent = new Intent(LoginActivity.this, KasirHomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        int idUser = sharedPreferences.getInt("id_user", 0);
        int idKasir = sharedPreferences.getInt("id_kasir", 0);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){
            updateUI(true);
        }else if (idUser != 0 || idKasir != 0){
            updateUI(true);
        }else {
            setFragment(new LoginFragment());
//            if (setSignUpFragment) {
//                setSignUpFragment = false;
//                setFragment(new SignUpFragment());
//            } else {
//                setFragment(new SignInFragment());
//            }
        }
    }
}
