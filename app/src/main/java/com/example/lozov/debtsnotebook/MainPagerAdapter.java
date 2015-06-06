package com.example.lozov.debtsnotebook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by lozov on 26.05.15.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter{
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DebtorsTab();
            case 1:
                return new LendersTab();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "My Debtors";
            case 1:
                return "My Lenders";
        }
        return "N/A";
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
