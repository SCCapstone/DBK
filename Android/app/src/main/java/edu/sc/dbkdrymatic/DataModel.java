package edu.sc.dbkdrymatic;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import javax.measure.unit.NonSI;

import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.SiteInfo;
import edu.sc.dbkdrymatic.internal.database.BoostBoxDao;
import edu.sc.dbkdrymatic.internal.database.SiteInfoDao;

public class DataModel extends ViewModel {

  private SiteInfoDao siDao;
  private BoostBoxDao bbDao;

  private MutableLiveData<Settings> settings;
  private MutableLiveData<Job> selectedJob;

  public DataModel(SiteInfoDao siDao, BoostBoxDao bbDao) {
    this.siDao = siDao;
    this.bbDao = bbDao;
  }

  /**
   * Fetch a monitor of the jobs in the application database.
   */
  public LiveData<List<Job>> getJobs() {
    return this.siDao.getAllJobs();
  }

  /**
   * Fetch the current {@code Job} selected from the navigation menu.
   */
  public LiveData<Job> getSelectedJob() {
    return this.selectedJob;
  }

  public void setSelectedJob(Job job) {
    if (this.selectedJob == null) {
      this.selectedJob = new MutableLiveData<>();
    }
    this.selectedJob.postValue(job);
  }

  /**
   * Fetches a cached {@code Settings} object with the application lifetime.
   *
   * TODO: This should be updated to fetch Settings from {@code SharedPreferences} instead.
   */
  public LiveData<Settings> getSettings() {
    if (this.settings == null) {
      this.settings = new MutableLiveData<>();
      this.settings.postValue(new Settings(SiteInfo.CUBIC_FOOT, NonSI.FAHRENHEIT, Country.USA));
    }

    return this.settings;
  }

  /**
   * Factory for creating {@code DataModel}s with the DAOs they require.
   *
   * <p>This method is used to avoid the {@code DataModel} having to know about the Application
   * Context, helping with separation of concerns.
   */
  public static class Factory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final SiteInfoDao siteInfoDao;
    private final BoostBoxDao boostBoxDao;

    public Factory(SiteInfoDao siteInfoDao, BoostBoxDao boostBoxDao) {
      this.siteInfoDao = siteInfoDao;
      this.boostBoxDao = boostBoxDao;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
      return (T) new DataModel(siteInfoDao, boostBoxDao);
    }
  }
}
