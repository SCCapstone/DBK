package edu.sc.dbkdrymatic.internal.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.List;

import javax.measure.unit.NonSI;

import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.SiteInfo;
import edu.sc.dbkdrymatic.internal.database.SiteInfoDao;

public class DataModel extends ViewModel {

  private SiteInfoDao siDao;

  private MutableLiveData<Job> selectedJob;

  public DataModel(SiteInfoDao siDao) {
    this.siDao = siDao;
  }

  /**
   * Fetch a monitor of the jobs in the application database.
   */
  public LiveData<List<Job>> getJobs() {
    return this.siDao.getAllJobs();
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

    public Factory(SiteInfoDao siteInfoDao) {
      this.siteInfoDao = siteInfoDao;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
      return (T) new DataModel(siteInfoDao);
    }
  }
}
