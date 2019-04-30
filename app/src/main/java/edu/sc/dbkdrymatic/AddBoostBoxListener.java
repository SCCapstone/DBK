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

public class AddBoostBoxListener implements View.OnClickListener {

  private NavigationActivity activity;
  private BluetoothAdapter btAdapter;

  public AddBoostBoxListener(NavigationActivity activity, BluetoothAdapter adapter) {
    this.activity = activity;
  }

  @Override
  public void onClick(View view) {
    Set<BluetoothDevice> devices = this.btAdapter.getBondedDevices();

    if (devices.size() == 0) {
      Toast
          .makeText(
              this.activity,
              "No paired devices found. Is Bluetooth enabled?",
              Toast.LENGTH_LONG)
          .show();
    }

    LiveData<Set<String>> inUse = this.activity.getDevicesInUse();
    inUse.observe(this.activity, (Set<String> inUseDevices) -> {
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

          this.activity.runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
            AlertDialog boxDialog = builder
                .setTitle(R.string.boost_box_select)
                .setView(deviceSelector)
                .setPositiveButton(R.string.create, (DialogInterface dialog, int i) -> {
                  if (deviceSelector.getSelectedItem() == null) {
                    Toast
                        .makeText(
                            NavigationActivity.this,
                            "Please Select a Bluetooth Device.",
                            Toast.LENGTH_SHORT)
                        .show();
                    return;
                  }

                  System.out.println(deviceSelector.getSelectedItem());
                  BluetoothDevice selection = nameDeviceMap.get(
                      (String) deviceSelector.getSelectedItem());
                  this.activity.selection.addBoostBox(selection);

                  dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (DialogInterface dialog, int i) ->{
                  dialog.cancel();
                }).create();
            boxDialog.show();
          });
        });
  }
}