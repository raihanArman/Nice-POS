package id.co.myproject.nicepos.view.admin.profil;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import id.co.myproject.nicepos.BuildConfig;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.model.User;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import id.co.myproject.nicepos.view.admin.MainActivity;
import id.co.myproject.nicepos.view.admin.login.LoginActivity;
import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    ImageView ivUser, ivBackground, ivSetting;
    TextView tvUser, tvEmail;
    Button btnLogOut;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int idUser;
    ApiRequest apiRequest;
    String loginType;
    GoogleApiClient googleApiClient;
    public static boolean statusUpdate = false;
    FirebaseAuth auth;
    FirebaseUser user;


    public ProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        idUser = sharedPreferences.getInt("id_user", 0);
        loginType = sharedPreferences.getString("login_type", "");
        ivUser = view.findViewById(R.id.iv_user);
        ivSetting = view.findViewById(R.id.iv_setting);
        ivBackground = view.findViewById(R.id.iv_background);
        tvUser = view.findViewById(R.id.tv_user);
        tvEmail = view.findViewById(R.id.tv_email);
        btnLogOut = view.findViewById(R.id.btn_log_out);

        if (loginType.equals("akun")){
            ivSetting.setVisibility(View.VISIBLE);
            ivSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditProfilFragment editProfilFragment = new EditProfilFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id_user", idUser);
                    editProfilFragment.setArguments(bundle);
                    ((MainActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_home, editProfilFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }else if (loginType.equals("akunGoogle")){
            ivSetting.setVisibility(View.GONE);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleApiClient.connect();

        loadDataUser();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

    }

    private void signOut() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (user != null){
                            auth.signOut();
                        }
                        editor.putInt("id_user", 0);
                        editor.putString("login_type", "");
                        editor.commit();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
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
                    String avatarUser = user.getAvatar();
                    String image = "";
                    String type = user.getType();
                    tvUser.setText(user.getNamaUser());
                    tvEmail.setText(user.getEmailUser());
                    if (type.equals("akun")){
                        if(avatarUser.equals("")){
                            image = BuildConfig.BASE_URL_GAMBAR+"user/avatar.jpg";
                        }else {
                            image = BuildConfig.BASE_URL_GAMBAR+"user/"+avatarUser;
                        }
                    }else if(type.equals("akunGoogle")){
                        if(avatarUser.equals("")){
                            image = BuildConfig.BASE_URL_GAMBAR+"user/avatar.jpg";
                        }else {
                            image = avatarUser;
                        }
                    }
                    Glide.with(getActivity()).load(image).into(ivUser);
                    Glide.with(getActivity()).load(image).transform(new BlurTransformation(25,3)).into(ivBackground);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (loginType.equals("akun")) {
            if (statusUpdate) {
                loadDataUser();
            }
        }
    }
}