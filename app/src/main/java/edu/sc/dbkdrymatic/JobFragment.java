package edu.sc.dbkdrymatic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
