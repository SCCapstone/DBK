package edu.sc.dbkdrymatic.internal.database;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;

import junit.framework.Assert;

import org.jscience.physics.amount.Amount;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import java.util.List;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import edu.sc.dbkdrymatic.NavigationActivity;
import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Damage;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.SiteInfo;

@RunWith(RobolectricTestRunner.class)
public class AppDatabaseTest {
  public static final double delta = 0.0001;

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  SiteInfo siteInfo;
  AppDatabase db;

  @Before
  public void setUp() {
    this.siteInfo = new SiteInfo(
        Amount.valueOf(12000, SiteInfo.CUBIC_FOOT),
        Amount.valueOf(80, NonSI.FAHRENHEIT),
        Amount.valueOf(105, NonSI.FAHRENHEIT),
        Amount.valueOf(60, NonSI.FAHRENHEIT),
        80,
        Damage.CLASS2,
        Country.USA,
        "Default Site"
    );
    Context appContext = RuntimeEnvironment.application.getApplicationContext();
    this.db = Room.inMemoryDatabaseBuilder(
        appContext, AppDatabase.class).allowMainThreadQueries().build();
  }

  @After
  public void tearDown() {
    this.db.close();
  }

  @Test
  public void storageAndRetrieval() {
    // Context of the app under test.
    NavigationActivity activity = Robolectric.buildActivity(
        NavigationActivity.class).get();


    this.db.siteInfoDao().insertAll(siteInfo);

    LiveData<List<Job>> all = this.db.siteInfoDao().getAllJobs();
    all.observeForever(new Observer<List<Job>>() {
      @Override
      public void onChanged(@Nullable List<Job> jobs) {
        Assert.assertNotNull(jobs);
        Assert.assertEquals(1, jobs.size());

        SiteInfo testInfo = jobs.get(0).getSiteInfo();

        // Test fields
        Assert.assertTrue(siteInfo.equals(testInfo));

        // Test computed properties
        Assert.assertEquals(
            siteInfo.getD2Requirement(),
            testInfo.getD2Requirement(),
            delta);
        Assert.assertEquals(
            siteInfo.getAdjustedEnergy().doubleValue(SI.JOULE),
            testInfo.getAdjustedEnergy().doubleValue(SI.JOULE),
            delta);
        Assert.assertEquals(
            siteInfo.getAdjustedPower().doubleValue(SI.WATT),
            testInfo.getAdjustedPower().doubleValue(SI.WATT),
            delta);
        Assert.assertEquals(
            siteInfo.getBoostBoxRequirement(), testInfo.getBoostBoxRequirement(), 0.00001);
      }
    });
    activity.finish();
  }

}
