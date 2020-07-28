package id.co.myproject.nicepos.view.admin.home.menu.menu;

import androidx.appcompat.app.AppCompatActivity;
import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.database.TakaranHelper;
import id.co.myproject.nicepos.model.Stok;
import id.co.myproject.nicepos.model.Takaran;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TambahBahanBakuActivity extends AppCompatActivity {

    Spinner sp_takaran;
    EditText et_takaran;
    TextView tv_tambah_bahan;
    Button btn_selesai;
    ListView lv_bahan;
    int idCafe;
    ApiRequest apiRequest;
    List<String> listIdBahanBaku = new ArrayList<>();
    List<String> listNamaBahanBaku = new ArrayList<>();
    int spinnerPosition;
    TakaranHelper takaranHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_bahan_baku);

        sp_takaran = findViewById(R.id.sp_stok);
        et_takaran = findViewById(R.id.et_takaran);
        tv_tambah_bahan = findViewById(R.id.tv_tambah_bahan);
        btn_selesai = findViewById(R.id.btn_selesai);
        lv_bahan = findViewById(R.id.lv_bahan);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        takaranHelper = TakaranHelper.getINSTANCE(this);
        takaranHelper.open();

        idCafe = getIntent().getIntExtra("id_cafe", 0);

        List<String> listStok = new ArrayList<>();
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        Call<List<Stok>> getStok = apiRequest.getStokRequest(idCafe);
        getStok.enqueue(new Callback<List<Stok>>() {
            @Override
            public void onResponse(Call<List<Stok>> call, Response<List<Stok>> response) {
                List<Stok> stokList = response.body();

                for (int i=0; i<stokList.size(); i++){
                    listStok.add(stokList.get(i).getNamaBahanBaku());
                    listIdBahanBaku.add(stokList.get(i).getIdBahanBaku());
                    listNamaBahanBaku.add(stokList.get(i).getNamaBahanBaku());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TambahBahanBakuActivity.this,
                        R.layout.item_spinner, R.id.weekofday, listStok);
                sp_takaran.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Stok>> call, Throwable t) {

            }
        });

        sp_takaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        tv_tambah_bahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertToDatabase();
            }
        });

        btn_selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadDataTakaran();
    }

    private void loadDataTakaran() {
        List<Takaran> takaranList = takaranHelper.getAllCart(idCafe);
        List<String> namabahan = new ArrayList<>();
        for(int i=0; i<takaranList.size(); i++){
            namabahan.add(takaranList.get(i).getNama_bahan_baku()+" sebanyak "+takaranList.get(i).getTakaran());
        }
        ArrayAdapter adapter = new ArrayAdapter(TambahBahanBakuActivity.this,android.R.layout.simple_list_item_1,namabahan);
        lv_bahan.setAdapter(adapter);

    }

    private void insertToDatabase() {
        Takaran takaran = new Takaran();
        takaran.setIdBahanBaku(listIdBahanBaku.get(spinnerPosition));
        takaran.setNama_bahan_baku(listNamaBahanBaku.get(spinnerPosition));
        takaran.setTakaran(et_takaran.getText().toString());
        long insertTakaran = takaranHelper.addToCart(idCafe, takaran);
        if(insertTakaran > 0){
            Toast.makeText(TambahBahanBakuActivity.this, "Berhasil menyimpan", Toast.LENGTH_SHORT).show();
            loadDataTakaran();
        }else {
            Toast.makeText(TambahBahanBakuActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
        }
    }
}
