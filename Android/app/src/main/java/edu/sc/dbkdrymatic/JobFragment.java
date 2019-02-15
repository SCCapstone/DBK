package edu.sc.dbkdrymatic;

import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.database.AppDatabase;

public class JobFragment extends Fragment {
  private View view;
  private DataModel model;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    this.view = inflater.inflate(R.layout.job_layout, container, false);

    AppDatabase appDb = Room.databaseBuilder(
        this.getActivity().getApplicationContext(), AppDatabase.class, "dbk.db").build();
    DataModel.Factory factory = new DataModel.Factory(appDb.siteInfoDao(), appDb.boostBoxDao());
    this.model = ViewModelProviders.of(this.getActivity(), factory).get(DataModel.class);

    return this.view;
  }
}
