package edu.sc.dbkdrymatic.internal;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

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

import static androidx.room.ForeignKey.CASCADE;

/**
 * Representation of a DBK Drymatic Boost Box air heating unit, storing the information exposed
 * by the bluetooth connection to the device.
 */
/*
@Entity(
    tableName = "BoostBox",
    foreignKeys = @ForeignKey(
      entity = SiteInfo.class,
      parentColumns = "id",
      childColumns = "jobId",
      onDelete = CASCADE
    ),
    indices = @Index(value="jobId"))*/
@Entity
@TypeConverters(Converters.class)
public class BoostBox {
  // Bluetooth hardware address of the BoostBox represented by this object.
  @NonNull
  @PrimaryKey
  private String address;

  // Primary key of the Job Site at which this BoostBox is located.
  private int jobId;

  private String name;

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

  public BoostBox(@NonNull String address, String name, int jobId) {
    this.address = address;
    this.name = name;
    this.jobId = jobId;
    this.airflow = false;
    this.running = false;
    this.autoRestart = false;
  }

  @Ignore
  public BoostBox(
      @NonNull String address, String name, Amount<Duration> hours,
      Amount<Temperature> airInTemp, Amount<Temperature> airOutTemp,
      Amount<Temperature> airOutTarget, Amount<Temperature> airOutThreshold,
      boolean airflow, boolean running, boolean autoRestart,
      Amount<ElectricPotential> voltage, Amount<ElectricCurrent> current,
      Amount<Power> power, Amount<Energy> cumulativeEnergy) {
    this.address = address;
    this.name = name;
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
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Amount<Duration> getHours() {
    return this.hours;
  }

  public void setHours(Amount<Duration> hours) {
    this.hours = hours;
  }

  public Amount<ElectricPotential> getVoltage() {
    return voltage;
  }

  public void setVoltage(Amount<ElectricPotential> voltage) {
    this.voltage = voltage;
  }

  public Amount<Energy> getCumulativeEnergy() {
    return cumulativeEnergy;
  }

  public void setCumulativeEnergy(Amount<Energy> cumulativeEnergy) {
    this.cumulativeEnergy = cumulativeEnergy;
  }

  public Amount<Temperature> getAirInTemp() {
    return this.airInTemp;
  }

  public void setAirInTemp(Amount<Temperature> airInTemp) {
    this.airInTemp = airInTemp;
  }

  public Amount<Power> getPower() {
    return this.power;
  }

  public void setPower(Amount<Power> power) {
    this.power = power;
  }

  public Amount<ElectricCurrent> getCurrent() {
    return this.current;
  }

  public void setCurrent(Amount<ElectricCurrent> current) {
    this.current = current;
  }

  public Amount<Temperature> getAirOutTarget() {
    return this.airOutTarget;
  }

  public void setAirOutTarget(Amount<Temperature> airOutTarget) {
    this.airOutTarget = airOutTarget;
  }

  public Amount<Temperature> getAirOutTemp() {
    return this.airOutTemp;
  }

  public void setAirOutTemp(Amount<Temperature> airOutTemp) {
    this.airOutTemp = airOutTemp;
  }

  public Amount<Temperature> getAirOutThreshold() {
    return this.airOutThreshold;
  }

  public void setAirOutThreshold(Amount<Temperature> airOutThreshold) {
    this.airOutThreshold = airOutThreshold;
  }

  public boolean isAirflow() {
    return airflow;
  }

  public void setAirflow(boolean airflow) {
    this.airflow = airflow;
  }

  public boolean isRunning() {
    return running;
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

  public boolean isAutoRestart() {
    return autoRestart;
  }

  public void setAutoRestart(boolean autoRestart) {
    this.autoRestart = autoRestart;
  }

  public int getJobId() {
    return jobId;
  }

  public void setJobId(int jobId) {
    this.jobId = jobId;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static BoostBox getInstance(Job job, BluetoothDevice device) {
    return new BoostBox(device.getAddress(), device.getName(), job.getSiteInfo().getId());
  }

  @Override
  public String toString() {
    if (this.name != null) {
      return this.name;
    }
    return this.address;
  }

  @Override
  public boolean equals(Object other) {
    if (other.getClass() != BoostBox.class) {
      return false;
    }

    return ((BoostBox) other).address.equals(this.address);
  }

  /**
   * Take in a string containing a line of Bluetooth data and update the corresponding field.
   */
  public void parse(String line) {

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
