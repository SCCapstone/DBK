package edu.sc.dbkdrymatic.internal;

import java.io.IOException;
import java.util.Scanner;

public class BoostBox {
  private String id;
  private String name;
  private BluetoothScannerFactory streamFactory;

  public BoostBox(String id, String name, BluetoothScannerFactory streamFactory) {
    this.id = id;
    this.name = name;
    this.streamFactory = streamFactory;
  }

  public void update() {
    try {
      Scanner bluetooth = streamFactory.build();
    } catch (IOException e){
      // TODO: Handle exception. It should give a notification similar to if they failed.
      e.printStackTrace();
    }
  }
}
