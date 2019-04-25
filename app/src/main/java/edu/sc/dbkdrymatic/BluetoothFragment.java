package edu.sc.dbkdrymatic;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import edu.sc.dbkdrymatic.internal.BoostBox;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.database.AppDatabase;
import edu.sc.dbkdrymatic.internal.viewmodels.SelectedJobModel;

public class BluetoothFragment extends Fragment {

  private RecyclerView recyclerView;
  private RecyclerView.LayoutManager layoutManager;
  private BoostBoxRecyclerAdapter adapter;
  private SelectedJobModel selectedJobModel;

  View view;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    this.view = inflater.inflate(R.layout.bluetooth_layout, container, false);

    // Monitor for changes to the currently selected job and update the view as appropriate.
    AppDatabase appDb = Room.databaseBuilder(
        this.getActivity().getApplicationContext(), AppDatabase.class, "dbk.db").build();
    SelectedJobModel.Factory sjmFactory
        = new SelectedJobModel.Factory(appDb.siteInfoDao(), appDb.boostBoxDao());
    this.selectedJobModel = ViewModelProviders.of(
        this.getActivity(), sjmFactory).get(SelectedJobModel.class);

    this.recyclerView = this.view.findViewById(R.id.bluetooth_recycler);

    this.layoutManager = new LinearLayoutManager(this.getContext());
    this.recyclerView.setLayoutManager(this.layoutManager);
    this.adapter = new BoostBoxRecyclerAdapter(
        this.selectedJobModel.getSelectedJob().getValue().getBoxes());
    this.recyclerView.setAdapter(this.adapter);

    return this.view;
  }

  @Override
  public void onCreate(Bundle savedState) {
    super.onCreate(savedState);
  }
}
