package id.co.myproject.nicepos.view.admin.login;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.nicepos.R;

import static id.co.myproject.nicepos.util.Helper.LEVEL_ADMIN;
import static id.co.myproject.nicepos.util.Helper.LEVEL_KASIR;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    FrameLayout parentFrameLayout;
    Button btn_kasir, btn_admin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_admin = view.findViewById(R.id.btn_admin);
        btn_kasir = view.findViewById(R.id.btn_kasir);
        parentFrameLayout = getActivity().findViewById(R.id.frame_login);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment signInFragment = new SignInFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("login_level", LEVEL_ADMIN);
                signInFragment.setArguments(bundle);
                setFragment(signInFragment);
            }
        });

        btn_kasir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment signInFragment = new SignInFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("login_level", LEVEL_KASIR);
                signInFragment.setArguments(bundle);
                setFragment(signInFragment);
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(parentFrameLayout.getId(), fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
