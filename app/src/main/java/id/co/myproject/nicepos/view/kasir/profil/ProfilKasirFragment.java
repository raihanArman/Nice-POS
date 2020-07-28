package id.co.myproject.nicepos.view.kasir.profil;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.myproject.nicepos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilKasirFragment extends Fragment {



    public ProfilKasirFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil_kasir, container, false);
    }

}
