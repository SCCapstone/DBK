package edu.sc.dbkdrymatic.internal;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.UUID;

import edu.sc.dbkdrymatic.BluetoothFragment;

public class BoostBox {
  private final String serialUuidString = "00001101-0000-1000-8000-00805F9B34FB";

  private BluetoothDevice device;
  private BluetoothFragment subscriber;

  public BoostBox(BluetoothDevice device, BluetoothFragment subscriber) {
    this.device = device;
    this.subscriber = subscriber;

  }

  public void listen() {
    try {
      UUID serialUuid = UUID.fromString(serialUuidString);
      Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
      BluetoothSocket socket = (BluetoothSocket) m.invoke(device, 1);
      BluetoothFetcher fetcher = new BluetoothFetcher(socket, subscriber);
      fetcher.start();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  private class BluetoothFetcher extends Thread {

    private BluetoothSocket socket;
    private BluetoothFragment subscriber;

    BluetoothFetcher(BluetoothSocket socket, BluetoothFragment subscriber) {
      this.socket = socket;
      this.subscriber = subscriber;
    }

    @Override
    public void run() {
      try {
        socket.connect();
        Scanner scanner = new Scanner(socket.getInputStream());
        scanner.useDelimiter("\n");
        while (true) {
          try {
            final String tmp = scanner.next();
            subscriber.getActivity().runOnUiThread(new Runnable() {
              @Override
              public void run() {
                subscriber.publish(tmp);
              }
            });
          } catch (NullPointerException e) {
            e.printStackTrace();
            break;
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
