package edu.sc.dbkdrymatic;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Set;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import edu.sc.dbkdrymatic.internal.viewmodels.SelectedJobModel;

public class AddBoostBoxListener implements View.OnClickListener, Observer<Set<String>> {

  private NavigationActivity activity;
  private BluetoothAdapter btAdapter;
  private SelectedJobModel selectedJobModel;

  private LiveData<Set<String>> inUse;

  public AddBoostBoxListener(NavigationActivity activity,
                             BluetoothAdapter adapter,
                             SelectedJobModel selectedJobModel) {
    this.activity = activity;
    this.btAdapter = adapter;
    this.selectedJobModel = selectedJobModel;
  }

  @Override
  public void onClick(View view) {
    this.inUse = this.activity.getDevicesInUse();
    inUse.observe(this.activity, this);
  }

  @Override
  public void onChanged(Set<String> inUseDevices) {
    Set<BluetoothDevice> devices = this.btAdapter.getBondedDevices();

    if (devices.size() == 0) {
      Toast
          .makeText(
              this.activity,
              "No paired devices found. Is Bluetooth enabled?",
              Toast.LENGTH_LONG)
          .show();
    }

    final Spinner deviceSelector = new Spinner(
        this.activity, Spinner.MODE_DROPDOWN);
    HashMap<String, BluetoothDevice> nameDeviceMap = new HashMap<>();
    for (BluetoothDevice device: devices) {
      if (inUseDevices.contains(device.getAddress())) {
        continue;
      }
      if (device.getName() != null && !nameDeviceMap.containsKey(device.getName())) {
        nameDeviceMap.put(device.getName(), device);
      } else {
        nameDeviceMap.put(device.toString(), device);
      }
    }

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        this.activity, R.layout.support_simple_spinner_dropdown_item);
    adapter.addAll(nameDeviceMap.keySet());
    deviceSelector.setAdapter(adapter);

    AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
    AlertDialog boxDialog = builder
        .setTitle(R.string.boost_box_select)
        .setView(deviceSelector)
        .setPositiveButton(R.string.create, (DialogInterface dialog, int i) -> {
          if (deviceSelector.getSelectedItem() == null) {
            Toast
                .makeText(
                    this.activity,
                    "Please Select a Bluetooth Device.",
                    Toast.LENGTH_SHORT)
                .show();
            return;
          }

          System.out.println(deviceSelector.getSelectedItem());
          BluetoothDevice selection = nameDeviceMap.get(
              (String) deviceSelector.getSelectedItem());
          this.selectedJobModel.addBoostBox(selection);

          dialog.dismiss();
        })
        .setNegativeButton(R.string.cancel, (DialogInterface dialog, int i) ->{
          dialog.cancel();
        }).create();

    // This is necessary or else the call will trigger itself, because adding a boostbox updates
    // the data being observed.
    this.inUse.removeObserver(this);
    
    boxDialog.show();
  }
}