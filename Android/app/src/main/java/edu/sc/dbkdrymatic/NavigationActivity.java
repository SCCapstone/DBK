package edu.sc.dbkdrymatic;

import android.app.FragmentManager;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import javax.measure.unit.NonSI;

import edu.sc.dbkdrymatic.internal.AppDatabase;
import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.JobFactory;
import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.SiteInfo;


public class NavigationActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private AppDatabase appDatabase;
  private Job job;
  private Settings settings;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_navigation);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    this.appDatabase = Room.databaseBuilder(
        getApplicationContext(), AppDatabase.class, "site_info").allowMainThreadQueries().build();
    this.job = new JobFactory(this.appDatabase).emptyJob();
    this.settings = new Settings(SiteInfo.CUBIC_FOOT, NonSI.FAHRENHEIT, Country.USA);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.navigation, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    FragmentManager fragmentManager = getFragmentManager();

    if (id == R.id.nav_first_layout) {
      CalculatorFragment cf = new CalculatorFragment(
          this.job.getSiteInfo(), this.settings, appDatabase.siteInfoDao());
      fragmentManager.beginTransaction()
              .replace(R.id.content_frame, cf).commit();
      // Handle the camera action
    } else if (id == R.id.nav_second_layout) {
      fragmentManager.beginTransaction()
              .replace(R.id.content_frame, new BluetoothFragment()).commit();
    } else if (id == R.id.nav_third_layout) {
      SettingsFragment sf = new SettingsFragment(this.settings);
      fragmentManager.beginTransaction()
              .replace(R.id.content_frame, sf).commit();
    }



    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }
}
