package id.co.myproject.nicepos.view.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.view.admin.home.AdminHomeFragment;
import id.co.myproject.nicepos.view.admin.profil.ProfilFragment;
import id.co.myproject.nicepos.view.data.DataTransaksiAdminFragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frame_home);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home_nav){
                    setFrament(new AdminHomeFragment());
                }else if (item.getItemId() == R.id.resep_nav){
                    setFrament(new DataTransaksiAdminFragment());
                }else if (item.getItemId() == R.id.profil_nav){
                    setFrament(new ProfilFragment());
                }
                return true;
            }
        });

        setFrament(new AdminHomeFragment());

    }

    private void setFrament(Fragment frament){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(), frament);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
