package edu.sc.dbkdrymatic.internal;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;

import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Duration;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.ElectricPotential;
import javax.measure.quantity.Energy;
import javax.measure.quantity.Power;
import javax.measure.quantity.Temperature;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.UUID;

import edu.sc.dbkdrymatic.BluetoothFragment;
import edu.sc.dbkdrymatic.internal.database.Converters;

/**
 * Representation of a DBK Drymatic Boost Box air heating unit, storing the information exposed
 * by the bluetooth connection to the device.
 */
@Entity
@TypeConverters(Converters.class)
public class BoostBox {
  private static final String serialUuidString = "00001101-0000-1000-8000-00805F9B34FB";

  // Bluetooth hardware address of the BoostBox represented by this object.
  @NonNull
  @PrimaryKey
  private String address;

  // Primary key of the Job Site at which this BoostBox is located.
  private int jobId;

  // Number of hours the Boost Box has been run since it was last reset.
  @ColumnInfo(name = "hours")
  private Amount<Duration> hours;

  // Temperature of air on the input side of the Boost Box.
  private Amount<Temperature> airInTemp;

  // Temperature of air on the output side of the Boost Box.
  private Amount<Temperature> airOutTemp;

  // Desired temperature of BoostBox output air.
  private Amount<Temperature> airOutTarget;

  // Minimum temperature of BoostBox output air before it will attempt to increase power usage.
  private Amount<Temperature> airOutThreshold;

  // Indicates whether there is sufficient airflow for the BoostBox to activate.
  private boolean airflow;

  // Indicates whether the Boost Box is running.
  private boolean running;

  // Indicates whether the Boost Box will resume previous state when turned on (true) or if it will
  // wake into an inactive state unconditionally (false).
  private boolean autoRestart;

  // AC RMS voltage measured by the device.
  private Amount<ElectricPotential> voltage;

  // Current measured by the device.
  private Amount<ElectricCurrent> current;

  // Instantaneous power consumption by the device.
  private Amount<Power> power;

  // Energy used by the device since it was last reset.
  private Amount<Energy> cumulativeEnergy;

  public BoostBox(
      String address, Amount<Duration> hours,
      Amount<Temperature> airInTemp, Amount<Temperature> airOutTemp,
      Amount<Temperature> airOutTarget, Amount<Temperature> airOutThreshold,
      boolean airflow, boolean running, boolean autoRestart,
      Amount<ElectricPotential> voltage, Amount<ElectricCurrent> current,
      Amount<Power> power, Amount<Energy> cumulativeEnergy) {
    this.address = address;
    this.hours = hours;
    this.airInTemp = airInTemp;
    this.airOutTemp = airOutTemp;
    this.airOutTarget = airOutTarget;
    this.airOutThreshold = airOutThreshold;
    this.airflow = airflow;
    this.running = running;
    this.autoRestart = autoRestart;
    this.voltage = voltage;
    this.current = current;
    this.power = power;
    this.cumulativeEnergy = cumulativeEnergy;
  }

  public String getAddress() {
    return address;
  }

  public Amount<Duration> getHours() {
    return hours;
  }

  public Amount<ElectricPotential> getVoltage() {
    return voltage;
  }

  public Amount<Energy> getCumulativeEnergy() {
    return cumulativeEnergy;
  }

  public Amount<Temperature> getAirInTemp() {
    return airInTemp;
  }

  public Amount<Power> getPower() {
    return power;
  }

  public Amount<ElectricCurrent> getCurrent() {
    return current;
  }

  public Amount<Temperature> getAirOutTarget() {
    return airOutTarget;
  }

  public Amount<Temperature> getAirOutTemp() {
    return airOutTemp;
  }

  public Amount<Temperature> getAirOutThreshold() {
    return airOutThreshold;
  }

  public boolean isAirflow() {
    return airflow;
  }

  public boolean isRunning() {
    return running;
  }

  public boolean isAutoRestart() {
    return autoRestart;
  }

  public int getJobId() {
    return jobId;
  }

  public void setJobId(int jobId) {
    this.jobId = jobId;
  }

  /*** Commented out by hxtk (2019-02-12)
   * This code requires a major rework anyway and blocks changes that are being made to the
   * BoostBox class. It will be uncommented and fixed following resolution of Issue #41.
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
  */

}
