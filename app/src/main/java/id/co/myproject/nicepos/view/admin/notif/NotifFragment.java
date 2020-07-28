package id.co.myproject.nicepos.view.admin.notif;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.adapter.NotifAdapter;
import id.co.myproject.nicepos.model.Pesan;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotifFragment extends Fragment {

    ApiRequest apiRequest;
    RecyclerView rv_notif;
    int idCafe;
    NotifAdapter notifAdapter;
    SharedPreferences sharedPreferences;

    public NotifFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notif, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        rv_notif = view.findViewById(R.id.rv_notif);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        idCafe = sharedPreferences.getInt("id_cafe", 0);

        rv_notif.setLayoutManager(new LinearLayoutManager(getActivity()));
        notifAdapter = new NotifAdapter(getActivity());
        rv_notif.setAdapter(notifAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        getPesan();

    }

    private void getPesan() {
        Call<List<Pesan>> getPesanUser = apiRequest.getPesanUserRequest(idCafe);
        getPesanUser.enqueue(new Callback<List<Pesan>>() {
            @Override
            public void onResponse(Call<List<Pesan>> call, Response<List<Pesan>> response) {
                if (response.isSuccessful()){
                    List<Pesan> pesanList = response.body();
                    notifAdapter.setNotificationList(pesanList);
                }
            }

            @Override
            public void onFailure(Call<List<Pesan>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
