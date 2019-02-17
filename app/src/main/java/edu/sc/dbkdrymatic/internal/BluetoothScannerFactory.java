package edu.sc.dbkdrymatic.internal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

public class BluetoothScannerFactory {
  private final String serialUuidString = "00001101-0000-1000-8000-00805F9B34FB";

  private BluetoothAdapter adapter;
  private BluetoothDevice device;
  private BluetoothSocket socket;

  public BluetoothScannerFactory(BluetoothAdapter adapter, BluetoothDevice device) {
    this.adapter = adapter;
    this.device = device;
  }

  public Scanner build() throws IOException {
    UUID serialUuid = UUID.fromString(serialUuidString);
    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(serialUuid);
    socket.connect();

    // TODO: This doesn't guarantee the socket gets closed. Fix before production.
    return new Scanner(socket.getInputStream());
  }
}
