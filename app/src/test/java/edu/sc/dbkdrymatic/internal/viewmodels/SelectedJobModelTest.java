package edu.sc.dbkdrymatic.internal.viewmodels;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;

import junit.framework.Assert;

import org.jscience.physics.amount.Amount;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.concurrent.ExecutionException;

import javax.measure.unit.NonSI;

import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Damage;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.SiteInfo;
import edu.sc.dbkdrymatic.internal.database.AppDatabase;
import edu.sc.dbkdrymatic.test.Utils;

@RunWith(RobolectricTestRunner.class)
public class SelectedJobModelTest {
  @Rule
  public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

  private AppDatabase db;
  private SiteInfo siteInfo;
  private LifecycleOwner owner;
  private LifecycleRegistry registry;

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
    this.db.siteInfoDao().insertAll(siteInfo);

    this.owner = Mockito.mock(LifecycleOwner.class);
    this.registry = new LifecycleRegistry(owner);
    Mockito.when(owner.getLifecycle()).thenReturn(this.registry);
  }

  @Test
  public void getSelectedJob_liveDataNotNull() throws ExecutionException, InterruptedException {
    SelectedJobModel model = new SelectedJobModel(this.db.siteInfoDao());
    LiveData<Job> data = model.getSelectedJob();

    Assert.assertNotNull(data);
  }

  @Test
  public void getSelectedJob_jobInitiallyNull() throws ExecutionException, InterruptedException {
    SelectedJobModel model = new SelectedJobModel(this.db.siteInfoDao());
    LiveData<Job> data = model.getSelectedJob();

    Utils.observe(data, this.owner, Mockito.mock(Observer.class));
    Assert.assertNull(data.getValue());
  }

}
