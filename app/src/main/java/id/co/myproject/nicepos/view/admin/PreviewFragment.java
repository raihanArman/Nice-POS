package id.co.myproject.nicepos.view.admin;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.feng.fixtablelayout.FixTableLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import id.co.myproject.nicepos.R;
import id.co.myproject.nicepos.adapter.LaporanPemasukanAdapter;
import id.co.myproject.nicepos.adapter.LaporanPengeluarkanAdapter;
import id.co.myproject.nicepos.adapter.LaporanTransaksiAdapter;
import id.co.myproject.nicepos.database.KasHelper;
import id.co.myproject.nicepos.database.OrderHelper;
import id.co.myproject.nicepos.model.Pemasukan;
import id.co.myproject.nicepos.model.Pengeluaran;
import id.co.myproject.nicepos.model.StrukTransaksi;
import id.co.myproject.nicepos.request.ApiRequest;
import id.co.myproject.nicepos.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.nicepos.util.Helper.FILTER_HARI_INI;
import static id.co.myproject.nicepos.util.Helper.FILTER_SEMUA;
import static id.co.myproject.nicepos.util.Helper.LAPORAN_PEMASUKAN;
import static id.co.myproject.nicepos.util.Helper.LAPORAN_PENGELUARAN;
import static id.co.myproject.nicepos.util.Helper.LAPORAN_TRANSAKSI;
import static id.co.myproject.nicepos.util.Helper.rupiahFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewFragment extends Fragment {

    FixTableLayout tb_laporan;
    LinearLayout lv_laporan;
    FloatingActionButton fb_cetak;
    ApiRequest apiRequest;
    SharedPreferences sharedPreferences;
    private Bitmap bitmap;
    int type_filter;
    String type_laporan;
    ImageView iv_back;
    ProgressDialog progressDialog;
    LinearLayout lv_empty;
    OrderHelper orderHelper;
    KasHelper kasHelper;

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public PreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        orderHelper = OrderHelper.getINSTANCE(getActivity());
        orderHelper.open();
        orderHelper.cleanLaporan();
        kasHelper = KasHelper.getINSTANCE(getActivity());
        kasHelper.open();
        kasHelper.cleanKas();

        tb_laporan = view.findViewById(R.id.tb_laporan);
        lv_laporan = view.findViewById(R.id.lv_laporan);
        fb_cetak = view.findViewById(R.id.fb_cetak);
        lv_empty = view.findViewById(R.id.lv_empty);
        iv_back = view.findViewById(R.id.iv_back);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        int idCafe = sharedPreferences.getInt("id_cafe", 0);
        type_filter = getArguments().getInt("type_filter");
        type_laporan = getArguments().getString("type_laporan");

        loadTableMenu(idCafe);


        fb_cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                bitmap = loadBitmapFromView(tb_laporan, tb_laporan.getWidth(), tb_laporan.getHeight());
                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

    }

    private void loadTableMenu(int id_cafe){
        if (type_filter == FILTER_HARI_INI) {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String time1 = " 00:00:00";
            String time2 = " 23:59:00";

            String tanggal1 = date + time1;
            String tanggal2 = date + time2;
            if (type_laporan.equals(LAPORAN_TRANSAKSI)) {
                loadTodayLaporanTransaksi(id_cafe, tanggal1, tanggal2);
            }else if (type_laporan.equals(LAPORAN_PEMASUKAN)){
                loadTodayLaporanPemasukan(id_cafe, tanggal1, tanggal2);
            }else if(type_laporan.equals(LAPORAN_PENGELUARAN)){
                loadTodayLaporanPengeluaran(id_cafe, tanggal1, tanggal2);
            }
        }else if(type_filter == FILTER_SEMUA){
            if (type_laporan.equals(LAPORAN_TRANSAKSI)) {
                loadAllLaporanTransaksi(id_cafe);
            }else if (type_laporan.equals(LAPORAN_PEMASUKAN)){
                loadAllLaporanPemasukan(id_cafe);
            }else if (type_laporan.equals(LAPORAN_PENGELUARAN)){
                loadAllLaporanPengeluaran(id_cafe);
            }
        }
    }

    private void loadAllLaporanPengeluaran(int id_cafe) {
        Call<List<Pengeluaran>> getAllPengeluaran = apiRequest.getPengeluaranAllRequest(id_cafe);
        getAllPengeluaran.enqueue(new Callback<List<Pengeluaran>>() {
            @Override
            public void onResponse(Call<List<Pengeluaran>> call, Response<List<Pengeluaran>> response) {
                List<Pengeluaran> pengeluaranList = response.body();
                setDataLaporanPengeluaran(pengeluaranList);
            }

            @Override
            public void onFailure(Call<List<Pengeluaran>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTodayLaporanPengeluaran(int id_cafe, String tanggal1, String tanggal2) {
        Call<List<Pengeluaran>> getTodayPengeluaran = apiRequest.getPengeluaranTodayRequest(id_cafe, tanggal1, tanggal2);
        getTodayPengeluaran.enqueue(new Callback<List<Pengeluaran>>() {
            @Override
            public void onResponse(Call<List<Pengeluaran>> call, Response<List<Pengeluaran>> response) {
                List<Pengeluaran> pengeluaranList = response.body();
                setDataLaporanPengeluaran(pengeluaranList);
            }

            @Override
            public void onFailure(Call<List<Pengeluaran>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataLaporanPengeluaran(List<Pengeluaran> pengeluaranList) {
        LaporanPengeluarkanAdapter laporanPengeluarkanAdapter = new LaporanPengeluarkanAdapter(pengeluaranList);
        tb_laporan.setAdapter(laporanPengeluarkanAdapter);
        if (pengeluaranList.size() <= 0) {
            lv_empty.setVisibility(View.VISIBLE);
            tb_laporan.setVisibility(View.INVISIBLE);
        } else {
            lv_empty.setVisibility(View.GONE);
            tb_laporan.setVisibility(View.VISIBLE);
        }
        for (Pengeluaran pengeluaran : pengeluaranList) {
            kasHelper.addToPengeluaran(pengeluaran);
            Toast.makeText(getActivity(), ""+pengeluaran.getIdPengeluaran(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAllLaporanPemasukan(int id_cafe) {
        Call<List<Pemasukan>> getAllPemasukan = apiRequest.getPemasukanAllRequest(id_cafe);
        getAllPemasukan.enqueue(new Callback<List<Pemasukan>>() {
            @Override
            public void onResponse(Call<List<Pemasukan>> call, Response<List<Pemasukan>> response) {
                List<Pemasukan> pemasukanList = response.body();
                setDataLaporanPemasukan(pemasukanList);
            }

            @Override
            public void onFailure(Call<List<Pemasukan>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTodayLaporanPemasukan(int id_cafe, String tanggal1, String tanggal2) {
        Call<List<Pemasukan>> getPemasukanTodayRequest = apiRequest.getPemasukanTodayRequest(id_cafe, tanggal1, tanggal2);
        getPemasukanTodayRequest.enqueue(new Callback<List<Pemasukan>>() {
            @Override
            public void onResponse(Call<List<Pemasukan>> call, Response<List<Pemasukan>> response) {
                List<Pemasukan> pemasukanList = response.body();
                setDataLaporanPemasukan(pemasukanList);
            }

            @Override
            public void onFailure(Call<List<Pemasukan>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataLaporanPemasukan(List<Pemasukan> pemasukanList) {
        LaporanPemasukanAdapter laporanPemasukanAdapter = new LaporanPemasukanAdapter(pemasukanList);
        tb_laporan.setAdapter(laporanPemasukanAdapter);
        if (pemasukanList.size() <= 0) {
            lv_empty.setVisibility(View.VISIBLE);
            tb_laporan.setVisibility(View.INVISIBLE);
        } else {
            lv_empty.setVisibility(View.GONE);
            tb_laporan.setVisibility(View.VISIBLE);
        }
        for (Pemasukan pemasukan : pemasukanList) {
            kasHelper.addToPemasukan(pemasukan);
        }
    }

    private void loadAllLaporanTransaksi(int id_cafe) {
        Call<List<StrukTransaksi>> getLaporan = apiRequest.getAllLaporanTransaksi(id_cafe);
        getLaporan.enqueue(new Callback<List<StrukTransaksi>>() {
            @Override
            public void onResponse(Call<List<StrukTransaksi>> call, Response<List<StrukTransaksi>> response) {
                List<StrukTransaksi> strukTransaksiList = response.body();
                setDataLaporanTransaksi(strukTransaksiList);
            }

            @Override
            public void onFailure(Call<List<StrukTransaksi>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTodayLaporanTransaksi(int id_cafe, String tanggal1, String tanggal2) {
        Call<List<StrukTransaksi>> getLaporan = apiRequest.getLaporanTransaksi(id_cafe, tanggal1, tanggal2);
        getLaporan.enqueue(new Callback<List<StrukTransaksi>>() {
            @Override
            public void onResponse(Call<List<StrukTransaksi>> call, Response<List<StrukTransaksi>> response) {
                List<StrukTransaksi> strukTransaksiList = response.body();
                setDataLaporanTransaksi(strukTransaksiList);
            }

            @Override
            public void onFailure(Call<List<StrukTransaksi>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataLaporanTransaksi(List<StrukTransaksi> strukTransaksiList) {
        LaporanTransaksiAdapter laporanTransaksiAdapter = new LaporanTransaksiAdapter(strukTransaksiList);
        tb_laporan.setAdapter(laporanTransaksiAdapter);
        if (strukTransaksiList.size() <= 0) {
            lv_empty.setVisibility(View.VISIBLE);
            tb_laporan.setVisibility(View.INVISIBLE);
        } else {
            lv_empty.setVisibility(View.GONE);
            tb_laporan.setVisibility(View.VISIBLE);
        }
        for (StrukTransaksi strukTransaksi : strukTransaksiList) {
            orderHelper.addToLaporan(strukTransaksi);
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }



    private void createPdf() throws FileNotFoundException, DocumentException {
        if (checkPermissions()){
            String path = "";
            String filename = "";
            if (type_laporan.equals(LAPORAN_TRANSAKSI)) {
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/NicePos/Transaksi";
            }else if (type_laporan.equals(LAPORAN_PEMASUKAN)){
                path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/NicePos/Pemasukan";
            }else if (type_laporan.equals(LAPORAN_PENGELUARAN)){
                path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/NicePos/Pengeluaran";
            }
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdir();

            String tanggal_harini = getArguments().getString("tanggal");

            String random = UUID.randomUUID().toString();
            String code = random.substring(0,3);
            if (type_laporan.equals(LAPORAN_TRANSAKSI)) {
                filename = "Laporan transaksi " + tanggal_harini + "_" + code + ".pdf";
            }else if (type_laporan.equals(LAPORAN_PEMASUKAN)){
                filename = "Laporan pemasukan " + tanggal_harini + "_" + code + ".pdf";
            }else if (type_laporan.equals(LAPORAN_PENGELUARAN)){
                filename = "Laporan pengeluaran " + tanggal_harini + "_" + code + ".pdf";
            }
            File file = new File(dir, filename);
            // write the document content

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            Paragraph p3 = new Paragraph();
            p3.add("Tanggal "+tanggal_harini+"\n\n");
            document.add(p3);

            PdfPTable table;
            if (type_laporan.equals(LAPORAN_TRANSAKSI)) {
                table = new PdfPTable(5);
                table.addCell("Tanggal");
                table.addCell("Kasir");
                table.addCell("Total Harga");
                table.addCell("Total Bayar");
                table.addCell("Kembalian");
                List<StrukTransaksi> strukTransaksiList = orderHelper.getAllLaporan();
                for (StrukTransaksi strukTransaksi : strukTransaksiList) {
                    String tanggal = DateFormat.format("dd MMM yyyy", strukTransaksi.getTanggal()).toString();
                    String kasir = strukTransaksi.getNamaKasir();
                    String harga = rupiahFormat(Integer.parseInt(strukTransaksi.getTotal()));
                    String bayar = rupiahFormat(Integer.parseInt(strukTransaksi.getUangBayar()));
                    String kembali = rupiahFormat(Integer.parseInt(strukTransaksi.getUangKembali()));
                    table.addCell(tanggal);
                    table.addCell(kasir);
                    table.addCell(harga);
                    table.addCell(bayar);
                    table.addCell(kembali);
                }

                document.add(table);
                document.addCreationDate();
                document.close();
            }else if (type_laporan.equals(LAPORAN_PEMASUKAN)){
                table = new PdfPTable(2);
                table.addCell("Tanggal");
                table.addCell("Total Pemasukan");
                List<Pemasukan> pemasukanList = kasHelper.getAllPemasukan();
                for (Pemasukan pemasukan : pemasukanList) {
                    String tanggal = DateFormat.format("dd MMM yyyy", pemasukan.getTanggal()).toString();
                    String total = pemasukan.getTotal();
                    table.addCell(tanggal);
                    table.addCell(total);
                }
                document.add(table);
                document.addCreationDate();
                document.close();
            }else if (type_laporan.equals(LAPORAN_PENGELUARAN)){
                table = new PdfPTable(2);
                table.addCell("Tanggal");
                table.addCell("Total Pengeluaran");
                List<Pengeluaran> pengeluaranList = kasHelper.getAllPengeluaran();
                for (Pengeluaran pengeluaran : pengeluaranList) {
                    String tanggal = DateFormat.format("dd MMM yyyy", pengeluaran.getTanggal()).toString();
                    String total = pengeluaran.getTotal();
                    table.addCell(tanggal);
                    table.addCell(total);
                }
                document.add(table);
                document.addCreationDate();
                document.close();
            }

            openGeneratedPDF(filename);

        }

    }

    private void openGeneratedPDF(String filename){
        progressDialog.dismiss();
        String path = "";
        if (type_laporan.equals(LAPORAN_TRANSAKSI)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/NicePos/Transaksi";
        }else if (type_laporan.equals(LAPORAN_PEMASUKAN)){
            path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/NicePos/Pemasukan";
        }else if (type_laporan.equals(LAPORAN_PENGELUARAN)){
            path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/NicePos/Pengeluaran";
        }
        File dir = new File(path);
        File file = new File(dir, filename);
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(getActivity(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }



    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }
}
