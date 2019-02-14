package edu.sc.dbkdrymatic.internal.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.jscience.physics.amount.Amount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.measure.unit.NonSI;

import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Damage;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.SiteInfo;

public class AppDatabaseTest {
  SiteInfo siteInfo;

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
  }

  @Test
  public void storageAndRetrieval() {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getTargetContext();
    AppDatabase db = Room.inMemoryDatabaseBuilder(
        appContext, AppDatabase.class).allowMainThreadQueries().build();

    db.siteInfoDao().insertAll(siteInfo);

    LiveData<List<Job>> all = db.siteInfoDao().getAllJobs();
    all.observe(appContext, new Observer<List<Job>>() {
      @Override
      public void onChanged(@Nullable List<Job> jobs) {
        Assert.assertNotNull(jobs);
        Assert.assertEquals(1, jobs.size());
        Assert.assertEquals(siteInfo, jobs.get(0).getSiteInfo());
      }
    });

  }
}
