package edu.sc.dbkdrymatic;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.database.AppDatabase;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.viewmodels.DataModel;
import edu.sc.dbkdrymatic.internal.viewmodels.SelectedJobModel;
import edu.sc.dbkdrymatic.internal.viewmodels.SettingsModel;

public class NavigationActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, Observer<List<Job>>, View.OnClickListener {

  private DataModel jobsModel;
  private SelectedJobModel selection;
  private Map<MenuItem, Job> itemJobMap;
  private Settings settings;

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

    SelectedJobModel.Factory selectionFactory = new SelectedJobModel.Factory(appDb.siteInfoDao());
    this.selection = ViewModelProviders.of(
        this, selectionFactory).get(SelectedJobModel.class);

    setContentView(R.layout.activity_navigation);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    this.jobsModel.getJobs().observe(this, this);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(this);
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
    } else {
      super.onBackPressed();
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
    if (id == R.id.action_settings) {
      return true;
    }

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
    FragmentManager fragmentManager = getSupportFragmentManager();

    switch(item.getItemId()) {
      case R.id.nav_settings:
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
        fragmentManager.beginTransaction().replace(R.id.content_frame, new JobFragment()).commit();
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

  @Override
  public void onClick(View view) {
    // Handle Floating Action Button
    final EditText name = new EditText(this);
    name.setInputType(InputType.TYPE_CLASS_TEXT);

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder
        .setTitle(R.string.new_job_title)
        .setView(name)
        .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int i) {
            // TODO: Switch to the job after it is created.
            jobsModel.createWithName(name.getText().toString(), settings);
            dialog.dismiss();
          }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
          }
        }).show();
  }
}