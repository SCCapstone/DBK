package edu.sc.dbkdrymatic;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class JobFragment extends Fragment {
  private View view;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    this.view = inflater.inflate(R.layout.job_layout, container, false);

    ViewPager pager = view.findViewById(R.id.job_pager);
    pager.setAdapter(new TabAdapter(getFragmentManager()));

    return this.view;
  }

  private class TabAdapter extends FragmentPagerAdapter {

    public TabAdapter(FragmentManager manager) {
      super(manager);
    }

    @Override
    public Fragment getItem(int position) {
      if (position == 0) {
        return new CalculatorFragment();
      } else {
        return new BluetoothFragment();
      }
    }

    @Override
    public int getCount() {
      return 2;
    }
  }
}
