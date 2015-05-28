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
            case 2:
                return new AllUsersTab();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "My Debtors";
            case 1:
                return "My Lenders";
            case 2:
                return "All";
        }
        return "";
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
