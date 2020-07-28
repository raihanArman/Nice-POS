package id.co.myproject.nicepos.view.admin.login;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

import static id.co.myproject.nicepos.util.Helper.LEVEL_ADMIN;
import static id.co.myproject.nicepos.util.Helper.LEVEL_KASIR;
import static id.co.myproject.nicepos.util.Helper.TYPE_ADD;
import static id.co.myproject.nicepos.util.Helper.TYPE_LOGIN_LEVEL_ADMIN;
import static id.co.myproject.nicepos.util.Helper.TYPE_LOGIN_LEVEL_KASIR;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    private static final int RC_SIGN_IN = 12345; //semacam kunci
    ImageView signInButton;
    EditText etEmail, etPassword;
    Button btnSignIn;
    TextView tvRegistrasi, tvLupaPassword, tv_email;
    FrameLayout parentFrameLayout;
    int id_user, login_level;
    String nama,email,url, avatar, namaUser, emailUser, avatarUser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    GoogleApiClient googleApiClient;
    FirebaseAuth auth;
    FirebaseUser user;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    ApiRequest apiRequest;
    private boolean userExists = false;
    ProgressDialog progressDialog;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);

        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnSignIn = view.findViewById(R.id.btn_sign_in);
        signInButton = view.findViewById(R.id.btn_google_login);
        tvRegistrasi = view.findViewById(R.id.tv_registrasi);
        tvLupaPassword = view.findViewById(R.id.tv_lupa_password);
        tv_email = view.findViewById(R.id.tv_email);
        parentFrameLayout = getActivity().findViewById(R.id.frame_login);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Memproses ...");
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        login_level = getArguments().getInt("login_level");

        if (login_level == LEVEL_KASIR){
            signInButton.setVisibility(View.INVISIBLE);
            tvLupaPassword.setVisibility(View.INVISIBLE);
            tvRegistrasi.setVisibility(View.INVISIBLE);
            tv_email.setText("Username");
            etEmail.setHint("Username");
        }else {
            signInButton.setVisibility(View.VISIBLE);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleApiClient.connect();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProcess();
            }
        });

        tvRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignUpFragment());
            }
        });

        tvLupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new ForgotPassFragment());
            }
        });

    }

    private void updateUI(final boolean isSignedIn) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        if (login_level == LEVEL_ADMIN) {
            int idUser = sharedPreferences.getInt("id_user", 0);
            getActivity().runOnUiThread(new Runnable() {
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
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        editor.putInt("id_cafe", response.body().getIdCafe());
                                        editor.apply();
                                        getActivity().startActivity(intent);
                                        getActivity().finish();
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
                                    Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        progressDialog.dismiss();
                        signInButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }else if(login_level == LEVEL_KASIR){
            Intent intent = new Intent(getActivity(), KasirHomeActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            progressDialog.dismiss();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("RESPON", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e("SUKSES", "display name: " + acct.getDisplayName());
            nama= acct.getDisplayName().toString();
            email = acct.getEmail().toString();
            if (acct.getPhotoUrl() !=null){
                url  = acct.getPhotoUrl().toString();
            }else {
                url = "";
            }
            inputUserRequest(nama, email, url);
        } else {
            progressDialog.dismiss();
            updateUI(false);
        }
    }

    private void inputUserRequest(String nama, String email, String url) {
        Call<Value> inpuUser = apiRequest.inputUserCallback(email, nama, url, "akunGoogle");
        inpuUser.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()){
                    if (response.body().getValue() == 1){
                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        Toast.makeText(getActivity(), "Akun sudah ada", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else if(response.body().getValue() == 0) {
                        id_user = response.body().getIdUser();
                        editor.putInt("id_user", id_user);
                        editor.putString("login_type", "akunGoogle");
                        editor.putInt("type_login", TYPE_LOGIN_LEVEL_ADMIN);
                        editor.commit();
                        updateUI(true);
                    }else if(response.body().getValue() == 2){
                        id_user = response.body().getIdUser();
                        editor.putInt("id_user", id_user);
                        editor.putString("login_type", "akunGoogle");
                        editor.putInt("type_login", TYPE_LOGIN_LEVEL_ADMIN);
                        editor.commit();
                        updateUI(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signIn() {
        progressDialog.show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void loginProcess(){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (login_level == LEVEL_ADMIN) {
            if (etEmail.getText().toString().matches(emailPattern)) {
                if (etPassword.length() > 8) {
                    progressDialog.show();
                    btnSignIn.setEnabled(true);
                    prosesLogin(email, password);
                } else {
                    Toast.makeText(getActivity(), "Password kurang boss", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Username atau Password salah boss", Toast.LENGTH_SHORT).show();
            }
        }else {
            prosesLogin(email, password);
        }

    }

    private void prosesLogin(String email, String password) {
        if (login_level == LEVEL_ADMIN) {
            Call<Value> cekUser = apiRequest.cekUserCallback(email);
            cekUser.enqueue(new Callback<Value>() {
                @Override
                public void onResponse(Call<Value> call, Response<Value> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getValue() == 1) {
                            id_user = response.body().getIdUser();
                            auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            progressDialog.dismiss();
                                            user = auth.getCurrentUser();
                                            if (user != null && user.isEmailVerified()) {
                                                editor.putInt("id_user", id_user);
                                                editor.putInt("type_login", TYPE_LOGIN_LEVEL_ADMIN);
                                                editor.putString("login_type", "akun");
                                                editor.commit();
                                                updateUI(true);
                                            } else {
                                                btnSignIn.setEnabled(true);
                                                Toast.makeText(getActivity(), "Email belum di verifikasi", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    btnSignIn.setEnabled(true);
                                    Toast.makeText(getActivity(), "Passoword Salah", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Email tidak terdaftar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Value> call, Throwable t) {
                    Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else if(login_level == LEVEL_KASIR){
            Call<Value> loginKasir = apiRequest.loginKasirCallback(etEmail.getText().toString(), etPassword.getText().toString());
            loginKasir.enqueue(new Callback<Value>() {
                @Override
                public void onResponse(Call<Value> call, Response<Value> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().getValue() == 1){
                            editor.putInt("id_kasir", response.body().getIdKasir());
                            editor.putInt("id_cafe", response.body().getIdCafe());
                            editor.putInt("type_login", TYPE_LOGIN_LEVEL_KASIR);
                            editor.commit();
                            updateUI(true);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Value> call, Throwable t) {
                    Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }
}