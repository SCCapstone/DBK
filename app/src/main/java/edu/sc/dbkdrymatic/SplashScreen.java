package edu.sc.dbkdrymatic;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

  public int wait_time= 3000;// time of splash screen in ms

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);
    Thread myThread = new Thread() {
      @Override
      public void run() {
        try {
          sleep(wait_time);
          Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
          startActivity(intent);
          finish();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    myThread.start();
  }
}
