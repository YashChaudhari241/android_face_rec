package com.example.faceattend;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.faceattend.ui.manleaves.ManageLeavesFragment;

public class TabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    private String[] tabTitles = new String[]{"REQUESTED", "APPROVED"};
    public TabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ManageLeavesFragment homeFragment = new ManageLeavesFragment(false);
                return homeFragment;
            case 1:
                ManageLeavesFragment homeFragment2 = new ManageLeavesFragment(true);
                return homeFragment2;
            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
