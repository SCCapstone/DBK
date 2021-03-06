package edu.sc.dbkdrymatic.internal.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


import junit.framework.Assert;

import org.jscience.physics.amount.Amount;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.measure.unit.NonSI;

import edu.sc.dbkdrymatic.internal.BoostBox;
import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Damage;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.SiteInfo;
import edu.sc.dbkdrymatic.internal.database.AppDatabase;
import edu.sc.dbkdrymatic.internal.database.BoostBoxDao;
import edu.sc.dbkdrymatic.internal.database.SiteInfoDao;
import edu.sc.dbkdrymatic.test.Utils;

@RunWith(RobolectricTestRunner.class)
public class SelectedJobModelTest {
  @Rule
  public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

  private BoostBoxDao bbDao;
  private SiteInfoDao siDao;
  private SiteInfo siteInfo;
  private LifecycleOwner owner;
  private LifecycleRegistry registry;

  @Before
  public void setUp() {
    this.siteInfo = new SiteInfo(
        Amount.valueOf(12000, SiteInfo.CUBIC_FOOT),
        Amount.valueOf(76, NonSI.FAHRENHEIT),
        Amount.valueOf(92, NonSI.FAHRENHEIT),
        Amount.valueOf(100, NonSI.FAHRENHEIT),
        Amount.valueOf(100, NonSI.FAHRENHEIT),
        56,
        Damage.CLASS2,
        Country.USA,
        "Default Site"
    );
    this.siteInfo.setId(1);

    Job job = new Job(new ArrayList<BoostBox>(), this.siteInfo);
    MutableLiveData<Job> jobData = new MutableLiveData<>();
    jobData.setValue(job);

    this.siDao = Mockito.mock(SiteInfoDao.class);
    Mockito.when(this.siDao.getJob(1)).thenReturn(jobData);

    this.owner = Mockito.mock(LifecycleOwner.class);
    this.registry = new LifecycleRegistry(owner);
    Mockito.when(this.owner.getLifecycle()).thenReturn(this.registry);

    this.bbDao = Mockito.mock(BoostBoxDao.class);
  }

  @Test
  public void getSelectedJob_liveDataNotNull() throws ExecutionException, InterruptedException {
    SelectedJobModel model = new SelectedJobModel(this.siDao, this.bbDao);
    LiveData<Job> data = model.getSelectedJob();

    Assert.assertNotNull(data);
  }

  @Test
  public void getSelectedJob_jobInitiallyNull() throws ExecutionException, InterruptedException {
    SelectedJobModel model = new SelectedJobModel(this.siDao, this.bbDao);
    LiveData<Job> data = model.getSelectedJob();

    Utils.observe(data, this.owner, Mockito.mock(Observer.class));
    Assert.assertNull(data.getValue());
  }

  @Test
  public void setJob_setJobFirst() throws ExecutionException, InterruptedException {
    Job job = new Job(new ArrayList<BoostBox>(), this.siteInfo);
    SelectedJobModel model = new SelectedJobModel(this.siDao, this.bbDao);

    model.setSelectedJob(job);
    LiveData<Job> data = model.getSelectedJob();

    Utils.observe(data, this.owner, Mockito.mock(Observer.class));
    Assert.assertNotNull(data.getValue());
    Assert.assertEquals(this.siteInfo, data.getValue().getSiteInfo());
  }

}
