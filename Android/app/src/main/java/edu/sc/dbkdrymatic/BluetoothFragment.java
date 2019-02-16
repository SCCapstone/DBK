package edu.sc.dbkdrymatic;

import android.support.v4.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class BluetoothFragment extends Fragment implements AdapterView.OnItemSelectedListener {

  private BluetoothAdapter bluetoothAdapter;
  View myView;

  public BluetoothFragment() {}

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    myView = inflater.inflate(R.layout.bluetooth_layout, container, false);
    return myView;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final TextView dataDisplay = getView().findViewById(R.id.data_display);
  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    /*final Spinner deviceSpinner = getView().findViewById(R.id.bluetooth_device);
    BoostBox boostBox = new BoostBox((BluetoothDevice) deviceSpinner.getSelectedItem(), this);
    boostBox.listen();*/
  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {

  }
}