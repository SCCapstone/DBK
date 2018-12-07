package edu.sc.dbkdrymatic;

import android.app.Fragment;
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

import edu.sc.dbkdrymatic.internal.BoostBox;
import edu.sc.dbkdrymatic.internal.Subscriber;

public class BluetoothFragment extends Fragment implements Subscriber<String>, AdapterView.OnItemSelectedListener {

  private BluetoothAdapter bluetoothAdapter;
  View myView;

  public BluetoothFragment() {}
  public BluetoothFragment(BluetoothAdapter adapter) {
    this.bluetoothAdapter = adapter;
  }

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

    final Spinner deviceSpinner = getView().findViewById(R.id.bluetooth_device);
    ArrayAdapter adapter = new ArrayAdapter<BluetoothDevice>(
        this.getActivity(), android.R.layout.simple_spinner_item);
    adapter.addAll(bluetoothAdapter.getBondedDevices());
    deviceSpinner.setAdapter(adapter);
    deviceSpinner.setOnItemSelectedListener(this);
  }

  @Override
  public void publish(String data) {
    TextView dataDisplay = getView().findViewById(R.id.data_display);
    dataDisplay.setText(dataDisplay.getText().toString() + "\n" + data);
  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    final Spinner deviceSpinner = getView().findViewById(R.id.bluetooth_device);
    BoostBox boostBox = new BoostBox((BluetoothDevice) deviceSpinner.getSelectedItem(), this);
    boostBox.listen();
  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {

  }
}