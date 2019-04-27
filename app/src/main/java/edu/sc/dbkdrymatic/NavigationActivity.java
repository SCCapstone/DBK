package edu.sc.dbkdrymatic;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.sc.dbkdrymatic.internal.BoostBox;
import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.database.AppDatabase;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.viewmodels.DataModel;
import edu.sc.dbkdrymatic.internal.viewmodels.SelectedJobModel;
import edu.sc.dbkdrymatic.internal.viewmodels.SettingsModel;

public class NavigationActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, Observer<List<Job>> {

  private BluetoothAdapter btAdapter;
  private DataModel jobsModel;
  private SelectedJobModel selection;
  private Map<MenuItem, Job> itemJobMap;
  private Settings settings;
  private boolean fabMenuOpened = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    AppDatabase appDb = Room.databaseBuilder(
        getApplicationContext(), AppDatabase.class, "dbk.db").build();

    SharedPreferences preferences = PreferenceManager
        .getDefaultSharedPreferences(this);
    SettingsModel.Factory settingsFactory = new SettingsModel.Factory(preferences);
    SettingsModel settingsModel = ViewModelProviders.of(this, settingsFactory)
        .get(SettingsModel.class);
    settingsModel.getSettings().observe(this, new Observer<Settings>() {
      @Override
      public void onChanged(@Nullable Settings newSettings) {
        settings = newSettings;
      }
    });

    DataModel.Factory jobsFactory = new DataModel.Factory(appDb.siteInfoDao());
    this.jobsModel = ViewModelProviders.of(this, jobsFactory).get(DataModel.class);

    SelectedJobModel.Factory selectionFactory
        = new SelectedJobModel.Factory(appDb.siteInfoDao(), appDb.boostBoxDao());
    this.selection = ViewModelProviders.of(
        this, selectionFactory).get(SelectedJobModel.class);

    setContentView(R.layout.activity_navigation);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    this.jobsModel.getJobs().observe(this, this);

    /** Commented out by hxtk (2019-02-28):
     * Deprecated and takes different arguments than what we supply.
     * Uncomment or replace when we figure out how (Google's documentation us currently lacking).
     */

    /**comment by lanerass (3-12-19)
     * the action bar methods below should no longer be deprecated by using:
     * import.androidx.appcompat.app.ActionBarDrawerToggle;
     *
     * */
    ActionBar actionbar = getSupportActionBar();
    actionbar.setDisplayHomeAsUpEnabled(true);
    actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();
    if(getSupportActionBar() != null) {//if no appbar do not call function
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    // Set up FAB menus.
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new CreateJobFabListener());

    FloatingActionButton cjFab = findViewById(R.id.fab_create_job);
    fab.setOnClickListener(new CreateJobFabListener());

    //TODO: Boost Box FAB handler
    BluetoothManager btManager = (BluetoothManager) this.getSystemService(
        Context.BLUETOOTH_SERVICE);
    this.btAdapter = btManager.getAdapter();

    // Opens About us fragment on start up of the code
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.content_frame, new AboutUsFragment()).commit();
  }

  /**
   * Handle press of the System UI back button.
   *
   * <p>If the navigation drawer is opened, we close the drawer. Otherwise, the system default
   * action is used.
   */
  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else if (this.fabMenuOpened) {
      this.closeFabMenu();
    } else {
      /**
       * alerts user if they want to exit app
       *
       * only used if navigation drawer or fab is not open
       */
      new AlertDialog.Builder(this)
              .setMessage("Would you like to exit the app?")
              .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  NavigationActivity.super.onBackPressed();
                }
              })
              .setNegativeButton("No", null)
              .show();
    }

  }

  /**
   * Populate the {@code AppBar} menu with items from the resource {@code R.menu.navigation}.
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.navigation, menu);

    return true;
  }

  /**
   * Handles the selection of items from the action bar menu.
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement


    return super.onOptionsItemSelected(item);
  }

  /**
   * Handles selection of items from the navigation drawer.
   *
   * <p>If the object selected was an item defined in the menu resource then we switch to that
   * fragment and a dedicated view for that fragment. Otherwise, it is assumed that the object is
   * a {@code Job} from the database. If it is not on record, we emit a {@code Toast} warning the
   * user of the error and do not change the view. If it is a known {@code Job} then we change to
   * the relevant view and pass in that {@code Job}'s ID through the bundle.
   */
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    this.closeFabMenu();
    FragmentManager fragmentManager = getSupportFragmentManager();
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    switch(item.getItemId()) {
      case R.id.nav_about:
        fab.setOnClickListener(new CreateJobFabListener());
        fab.setImageResource(R.drawable.ic_business_center_black_24dp);
        fragmentManager.beginTransaction()
            .replace(R.id.content_frame, new AboutUsFragment()).commit();
        break;
      case R.id.nav_settings:
        fab.setOnClickListener(new CreateJobFabListener());
        fab.setImageResource(R.drawable.ic_business_center_black_24dp);
        fragmentManager.beginTransaction()
            .replace(R.id.content_frame, new SettingsFragment()).commit();
        break;
      default:  // This is the case where they have clicked on a Job.
        // Error check for non-existent job.
        if (!itemJobMap.keySet().contains(item)) {
          Toast.makeText(this, "No such job exists.", Toast.LENGTH_SHORT).show();
          return false;
        }
        // Update the selected `Job`.
        selection.setSelectedJob(itemJobMap.get(item));
        fab.setOnClickListener(new ExpandableMenuFabListener());
        fab.setImageResource(R.drawable.ic_menu_black_24dp);
        fragmentManager.beginTransaction().replace(R.id.content_frame, new JobFragment()).commit();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(item);
    }
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  /**
   * When the list of {@code Job}s in the database changes, this will clear the Jobs section of the
   * navigation drawer and refill it with the up-to-date list of Jobs.
   */

  @Override
  public void onChanged(@Nullable List<Job> jobs) {
    NavigationView navigationView = findViewById(R.id.nav_view);
    Menu menu = navigationView.getMenu();

    // Clear existing list of jobs. This ensures that the jobs are kept in order and that any
    // deleted jobs do not persist in the menu.
    this.itemJobMap = new HashMap<>();
    menu.clear();
    navigationView.inflateMenu(R.menu.activity_navigation_drawer);

    // The variable is marked as nullable, so we have to handle the case where it is null.
    // In normal operations, this path should never execute.
    if (jobs == null) {
      return;
    }

    // Add a menu item with the job name for each `Job` and put it in the map so that it may be
    // referenced by the menu selection.
    for (Job job: jobs) {
      itemJobMap.put(menu.add(job.getSiteInfo().name), job);
    }
  }

  public void toggleFabMenu() {
    if (this.fabMenuOpened) {
      this.closeFabMenu();
    } else {
      this.openFabMenu();
    }
  }

  public void openFabMenu() {
    findViewById(R.id.fab_create_job).setClickable(true);
    findViewById(R.id.fab_create_job).setOnClickListener(new CreateJobFabListener());
    findViewById(R.id.create_job_layout).setVisibility(View.VISIBLE);

    findViewById(R.id.fab_add_box).setClickable(true);
    findViewById(R.id.fab_add_box).setOnClickListener(new AddBoostBoxListener());
    findViewById(R.id.add_box_layout).setVisibility(View.VISIBLE);
    this.fabMenuOpened = true;
  }

  public void closeFabMenu() {
    findViewById(R.id.fab_create_job).setClickable(false);
    findViewById(R.id.create_job_layout).setVisibility(View.INVISIBLE);

    findViewById(R.id.fab_add_box).setClickable(false);
    findViewById(R.id.add_box_layout).setVisibility(View.INVISIBLE);
    this.fabMenuOpened = false;
  }

  /**
   * This is the Floating Action Button's default click listener. When it is being observed by
   * this listener, it will open a dialog for creating a new Job.
   */
  private class CreateJobFabListener implements View.OnClickListener {
    @Override
    public void onClick(View view) {
      final EditText name = new EditText(NavigationActivity.this);
      name.setInputType(InputType.TYPE_CLASS_TEXT);

      AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
      builder
          .setTitle(R.string.new_job_title)
          .setView(name)
          .setPositiveButton(R.string.create, (DialogInterface dialog, int i) -> {
            // TODO: Switch to the job after it is created.
            jobsModel.createWithName(name.getText().toString(), settings);
            dialog.dismiss();
          })
          .setNegativeButton(R.string.cancel, (DialogInterface dialog, int i) ->{
            dialog.cancel();
          }).show();
    }
  }

  /**
   * This is the FloatingActionButton's click listener for the Job view. Within the Job View,
   * clicking the FAB should expose two other FABs: One will open a CreateJob dialog when clicked
   * and the other will open an AddBoostBox dialog which will allow the user to add a BoostBox to
   * the selected Job.
   */
  private class ExpandableMenuFabListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
      NavigationActivity.this.toggleFabMenu();
    }
  }

  private class AddBoostBoxListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
      final Spinner deviceSelector = new Spinner(
          NavigationActivity.this, Spinner.MODE_DROPDOWN);
      Set<BluetoothDevice> devices = NavigationActivity.this.btAdapter.getBondedDevices();

      if (devices.size() == 0) {
        Toast
            .makeText(
                NavigationActivity.this,
                "No paired devices found. Is Bluetooth enabled?",
                Toast.LENGTH_LONG)
            .show();
      }

      HashMap<String, BluetoothDevice> nameDeviceMap = new HashMap<>();
      for (BluetoothDevice device: devices) {
        if (device.getName() != null && !nameDeviceMap.containsKey(device.getName())) {
          nameDeviceMap.put(device.getName(), device);
        } else {
          nameDeviceMap.put(device.toString(), device);
        }
      }

      ArrayAdapter<String> adapter = new ArrayAdapter<String>(
          NavigationActivity.this, R.layout.support_simple_spinner_dropdown_item);
      adapter.addAll(nameDeviceMap.keySet());
      deviceSelector.setAdapter(adapter);

      AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
      builder
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
            NavigationActivity.this.selection.addBoostBox(selection);

            dialog.dismiss();
          })
          .setNegativeButton(R.string.cancel, (DialogInterface dialog, int i) ->{
            dialog.cancel();
          }).show();
    }
  }

}
