package id.co.myproject.nicepos.view.admin.login;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.model.Value;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private TextView tvLogin;
    private EditText etNama, etEmail, etPassword, etConfirmPassword;
    private Button btnDaftar;
    private FrameLayout parentFrameLayout;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private ProgressDialog progressDialog;
    public static final String TAG = SignUpFragment.class.getSimpleName();
    FirebaseAuth auth;
    ApiRequest apiRequest;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);

        tvLogin = view.findViewById(R.id.tv_login);
        etNama = view.findViewById(R.id.et_nama);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        btnDaftar = view.findViewById(R.id.btn_sign_up);
        parentFrameLayout = getActivity().findViewById(R.id.frame_login);

        auth = FirebaseAuth.getInstance();
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");

        etNama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daftar();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });

    }

    private void checkInput() {
        if (!TextUtils.isEmpty(etNama.getText())) {
            if (!TextUtils.isEmpty(etEmail.getText())) {
                if (!TextUtils.isEmpty(etPassword.getText()) && etPassword.length() > 8) {
                    if (!TextUtils.isEmpty(etConfirmPassword.getText())) {
                        btnDaftar.setEnabled(true);
                    } else {
                        btnDaftar.setEnabled(false);
                    }
                } else {
                    btnDaftar.setEnabled(false);
                }
            } else {
                btnDaftar.setEnabled(false);
            }
        } else {
            btnDaftar.setEnabled(false);
        }

    }

    private void daftar(){
        String nama = etNama.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        Drawable customIconError = getResources().getDrawable(R.drawable.custom_error_icon);
        customIconError.setBounds(0,0,customIconError.getIntrinsicWidth(), customIconError.getIntrinsicHeight());
        if (etEmail.getText().toString().matches(emailPattern)){
            if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
                progressDialog.show();
                Call<Value> cekEmail = apiRequest.cekUserCallback(email);
                cekEmail.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        if (response.isSuccessful()){
                            if (response.body().getValue() == 1){
                                progressDialog.dismiss();
                                btnDaftar.setEnabled(true);
                                Toast.makeText(getActivity(), "Email sudah ada, gunakan email yang lain", Toast.LENGTH_SHORT).show();
                            }else if (response.body().getValue() == 0){
                                btnDaftar.setEnabled(false);
                                auth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    sendVerification();
                                                    progressDialog.dismiss();
                                                    inputUser(nama, email);
                                                    auth.signOut();
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                    builder.setMessage("Cek di email anda, lakukan verifikasi untuk login");
                                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            setFragment(new SignInFragment());
                                                            dialogInterface.dismiss();
                                                        }
                                                    });
                                                    AlertDialog alertDialog = builder.create();
                                                    alertDialog.show();
//                                        }
                                                } else {
                                                    progressDialog.dismiss();
                                                    btnDaftar.setEnabled(true);
                                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                etPassword.setError("Password tidak cocok", customIconError);
            }
        }else {
            etEmail.setError("Email tidak cocok", customIconError);
        }
    }

    private void sendVerification(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
//                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Berhasil mengirim", Toast.LENGTH_SHORT).show();
//                                sendVerifSucces = true;
                            }else {
                                Toast.makeText(getActivity(), "Gagal mengirim", Toast.LENGTH_SHORT).show();
//                                sendVerifSucces = false;
                            }
                        }
                    });
        }
//        return sendVerifSucces;
    }

    private void inputUser(String nama, String email){
        Call<Value> inputUser = apiRequest.inputUserCallback(email, nama, "", "akun");
        inputUser.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.body().getValue() == 1){

                }
            }
            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }


}