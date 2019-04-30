package edu.sc.dbkdrymatic;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.view.View;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.UUID;

import edu.sc.dbkdrymatic.internal.BoostBox;
import edu.sc.dbkdrymatic.internal.viewmodels.SelectedJobModel;

public class BoostBoxRefreshListener implements View.OnClickListener {
  private static final String serialUuidString = "00001101-0000-1000-8000-00805F9B34FB";

  private BoostBox box;
  private BluetoothAdapter adapter;
  private SelectedJobModel sjm;

  public BoostBoxRefreshListener(BoostBox box, BluetoothAdapter adapter, SelectedJobModel sjm) {
    this.box = box;
    this.adapter = adapter;
    this.sjm = sjm;
  }

  @Override
  public void onClick(View view) {
    System.out.println("Updating " + this.box.getName());
    AsyncTask.execute(() -> {
      try {
        BluetoothDevice device = adapter.getRemoteDevice(box.getAddress());
        UUID serialUuid = UUID.fromString(serialUuidString);
        Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
        BluetoothSocket socket = (BluetoothSocket) m.invoke(device, 1);
        socket.connect();
        Scanner scanner = new Scanner(socket.getInputStream());
        scanner.useDelimiter("(\r\n)");

        while (true) {
          box.parse(scanner.next());
          sjm.updateBoostBox(box);
        }
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      } catch (IOException e) {
        // TODO: Display a toast for failure to connect.
        e.printStackTrace();
      }
    });
  }
}
