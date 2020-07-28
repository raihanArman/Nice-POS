package id.co.myproject.nicepos.adapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import id.co.myproject.nicepos.view.admin.home.menu.kas.PemasukanFragment;
import id.co.myproject.nicepos.view.admin.home.menu.kas.PengeluaranFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private ArrayList<String> title;

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new PemasukanFragment());
        fragments.add(new PengeluaranFragment());

        title = new ArrayList<>();
        title.add("Pemasukan");
        title.add("Pengeluaran");
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
