package id.co.myproject.nicepos.view.admin.login;


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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.FragmentTransaction;
import androidx.transition.TransitionManager;
import id.co.myproject.nicepos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPassFragment extends Fragment {


    private EditText etRegisteredEmail;
    private Button btnResetPassword;
    private TextView tvKembali;

    private FrameLayout parentFragment;
    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailIconText;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    public ForgotPassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_pass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etRegisteredEmail = view.findViewById(R.id.et_forgot_password_email);
        btnResetPassword = view.findViewById(R.id.btn_reset_password);
        tvKembali = view.findViewById(R.id.tv_reset_password_go_back);
        parentFragment = getActivity().findViewById(R.id.frame_login);
        emailIconContainer = view.findViewById(R.id.forgot_password_email_container);
        emailIcon = view.findViewById(R.id.forgot_password_email_icon);
        emailIconText = view.findViewById(R.id.forgot_password_email_text);
        progressBar = view.findViewById(R.id.forgot_password_progress);

        firebaseAuth = FirebaseAuth.getInstance();

        tvKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });

        etRegisteredEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIconText.setVisibility(View.GONE);

                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIcon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                btnResetPassword.setEnabled(false);
                sendEmail();
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, CEK_EMAIL, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getInt("value") == 1){
//                                sendEmail();
//                            }else {
//                                Toast.makeText(getActivity(), "Email tidak terdaftar", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }){
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> map = new HashMap<>();
//                        map.put("email", etRegisteredEmail.getText().toString());
//                        return map;
//                    }
//                };
//
//                RequestQueue queue = Volley.newRequestQueue(getActivity());
//                queue.add(stringRequest);


            }
        });

    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(parentFragment.getId(), fragment);
        transaction.commit();
    }

    private void checkInputs() {
        if (TextUtils.isEmpty(etRegisteredEmail.getText())){
            btnResetPassword.setEnabled(false);
        }else {
            btnResetPassword.setEnabled(true);
        }
    }

    private void sendEmail(){
        firebaseAuth.sendPasswordResetEmail(etRegisteredEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            ScaleAnimation scaleAnimation = new ScaleAnimation(1,0,1,0, emailIcon.getWidth()/2, emailIcon.getHeight()/2);
                            scaleAnimation.setDuration(100);
                            scaleAnimation.setInterpolator(new AccelerateInterpolator());
                            scaleAnimation.setRepeatMode(Animation.REVERSE);
                            scaleAnimation.setRepeatCount(1);
                            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    emailIconText.setVisibility(View.VISIBLE);
                                    emailIconText.setText("Email berhasil dikirim! Periksa kotak masuk Anda");
                                    emailIconText.setTextColor(getResources().getColor(R.color.colorDark));
                                    TransitionManager.beginDelayedTransition(emailIconContainer);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                    emailIcon.setImageResource(R.drawable.mail_green);
                                }
                            });
                            emailIcon.startAnimation(scaleAnimation);
                        }else {
                            String error = task.getException().getMessage();
                            btnResetPassword.setEnabled(true);
                            emailIconText.setText(error);
                            emailIconText.setTextColor(getResources().getColor(R.color.colorPrimary));
                            TransitionManager.beginDelayedTransition(emailIconContainer);
                            emailIconText.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
