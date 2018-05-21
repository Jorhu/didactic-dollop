package org.git.joribiz.pmm.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationAdapter extends SmartFragmentStatePagerAdapter {
    private final List<Fragment> fragments = new ArrayList<>();

    public BottomNavigationAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    public void setFragment(int position, Fragment fragment) {
        fragments.set(position, fragment);
        notifyDataSetChanged();
    }

    public void removeFragment(int position) {
        fragments.remove(position);
    }
}
