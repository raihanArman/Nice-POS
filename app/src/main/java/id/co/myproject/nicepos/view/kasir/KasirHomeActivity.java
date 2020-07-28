package id.co.myproject.nicepos.view.kasir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.view.data.DataTransaksiKasirFragment;
import id.co.myproject.nicepos.view.kasir.home.KasirHomeFragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class KasirHomeActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kasir_home);

        frameLayout = findViewById(R.id.frame_kasir_home);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home_nav){
                    setFrament(new KasirHomeFragment());
                }else if (item.getItemId() == R.id.data_nav){
                    setFrament(new DataTransaksiKasirFragment());
                }else if (item.getItemId() == R.id.profil_nav){
//                    setFrament(new ProfilFragment());
                }
                return true;
            }
        });

        setFrament(new KasirHomeFragment());

    }

    private void setFrament(Fragment frament){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(), frament);
        transaction.commit();
    }
}
