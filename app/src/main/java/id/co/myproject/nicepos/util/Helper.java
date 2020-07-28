package id.co.myproject.nicepos.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Helper {
    public static final int TYPE_ADD = 1;
    public static final int TYPE_EDIT = 2;
    public static final int LEVEL_ADMIN = 28;
    public static final int LEVEL_KASIR = 29;
    public static final int TYPE_LOGIN_LEVEL_ADMIN = 62;
    public static final int TYPE_LOGIN_LEVEL_KASIR = 63;
    public static final int FILTER_HARI_INI = 0;
    public static final int FILTER_SEMUA = 1;
    public static final int TYPE_BUNDLE_FROM_ORDER = 31;
    public static final int TYPE_BUNDLE_FROM_TRANSAKSI = 32;
    public static final int GALLERY_REQUEST = 22;
    public static final String LAPORAN_TRANSAKSI = "laporan_transaksi";
    public static final String LAPORAN_PEMASUKAN = "laporan_pemasukan";
    public static final String LAPORAN_PENGELUARAN = "laporan_pengeluaran";

    public static final String DELETE = "Delete";

    public static String rupiahFormat(int harga){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        return kursIndonesia.format(harga);
    }
}
